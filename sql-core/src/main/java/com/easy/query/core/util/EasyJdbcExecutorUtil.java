package com.easy.query.core.util;

import com.easy.query.core.basic.jdbc.conn.EasyConnection;
import com.easy.query.core.basic.jdbc.executor.ExecutorContext;
import com.easy.query.core.basic.jdbc.executor.internal.merge.result.StreamResultSet;
import com.easy.query.core.basic.jdbc.executor.internal.merge.result.impl.EasyShardingStreamResultSet;
import com.easy.query.core.basic.jdbc.executor.internal.merge.result.impl.EasyStreamResultSet;
import com.easy.query.core.basic.jdbc.parameter.BeanSQLParameter;
import com.easy.query.core.basic.jdbc.parameter.ConstSQLParameter;
import com.easy.query.core.basic.jdbc.parameter.EasyConstSQLParameter;
import com.easy.query.core.basic.jdbc.parameter.SQLParameter;
import com.easy.query.core.basic.jdbc.types.EasyParameter;
import com.easy.query.core.basic.jdbc.types.JdbcTypeHandlerManager;
import com.easy.query.core.basic.jdbc.types.handler.JdbcTypeHandler;
import com.easy.query.core.context.QueryRuntimeContext;
import com.easy.query.core.exception.EasyQueryException;
import com.easy.query.core.exception.EasyQuerySQLStatementException;
import com.easy.query.core.expression.lambda.PropertySetterCaller;
import com.easy.query.core.logging.Log;
import com.easy.query.core.logging.LogFactory;
import com.easy.query.core.metadata.ColumnMetadata;
import com.easy.query.core.metadata.EntityMetadata;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * create time 2023/4/21 17:01
 * 文件说明
 *
 * @author xuejiaming
 */
public class EasyJdbcExecutorUtil {

    private static final int BATCH_GROUP_COUNT = 1000;
    private static final Log log = LogFactory.getLog(EasyJdbcExecutorUtil.class);

    private static void printShardingSQLFormat(final StringBuilder printSQL, final EasyConnection easyConnection) {
        printSQL.append(Thread.currentThread().getName());
        printSQL.append(", name:");
        printSQL.append(easyConnection.getDataSourceName());
        printSQL.append(", ");
    }

    private static void printReplicaSQLFormat(final StringBuilder printSQL, final EasyConnection easyConnection) {
        printSQL.append("strategy:");
        printSQL.append(easyConnection.getConnectionStrategy().getName());
        printSQL.append(", ");
    }

    private static void logSQL(final boolean printSql, final String sql, final EasyConnection easyConnection, final boolean shardingPrint, final boolean replicaPrint) {
        if (printSql) {
            StringBuilder printSQL = new StringBuilder();
            printSQL.append("==> ");
            if (shardingPrint) {
                printShardingSQLFormat(printSQL, easyConnection);
            }
            if (replicaPrint) {
                printReplicaSQLFormat(printSQL, easyConnection);
            }
            printSQL.append("Preparing: ");
            printSQL.append(sql);
            log.info(printSQL.toString());
        }
    }

    private static void logParameter(boolean printSql, List<SQLParameter> parameters, final EasyConnection easyConnection, final boolean shardingPrint, final boolean replicaPrint) {
        if (printSql) {
            StringBuilder printSQL = new StringBuilder();
            printSQL.append("==> ");
            if (shardingPrint) {
                printShardingSQLFormat(printSQL, easyConnection);
            }
            if (replicaPrint) {
                printReplicaSQLFormat(printSQL, easyConnection);
            }
            printSQL.append("Parameters: ");
            printSQL.append(EasySQLUtil.sqlParameterToString(parameters));
            log.info(printSQL.toString());
        }
    }

    private static void logResult(boolean printSql, long total, final EasyConnection easyConnection, final boolean shardingPrint, final boolean replicaPrint) {
        if (printSql) {
            StringBuilder printSQL = new StringBuilder();
            printSQL.append("<== ");
            if (shardingPrint) {
                printShardingSQLFormat(printSQL, easyConnection);
            }
            if (replicaPrint) {
                printReplicaSQLFormat(printSQL, easyConnection);
            }
            printSQL.append("Total: ");
            printSQL.append(total);
            log.info(printSQL.toString());
        }
    }

    private static void logResult(boolean printSql, int total, final EasyConnection easyConnection, final boolean shardingPrint, final boolean replicaPrint) {
        if (printSql) {
            logResult(true, (long) total, easyConnection, shardingPrint, replicaPrint);
        }
    }

    private static void logUse(boolean printSql, long start, long end, final EasyConnection easyConnection, final boolean shardingPrint, final boolean replicaPrint) {
        if (printSql) {
            StringBuilder printSQL = new StringBuilder();
            printSQL.append("<== ");
            if (shardingPrint) {
                printShardingSQLFormat(printSQL, easyConnection);
            }
            if (replicaPrint) {
                printReplicaSQLFormat(printSQL, easyConnection);
            }
            printSQL.append("Time Elapsed: ");
            printSQL.append(end - start);
            printSQL.append("(ms)");
            log.info(printSQL.toString());
        }
    }

    public static <T> List<SQLParameter> extractParameters(ExecutorContext executorContext, T entity, List<SQLParameter> sqlParameters,boolean printSql,EasyConnection easyConnection,boolean shardingPrint,boolean replicaPrint) {
        if (EasyCollectionUtil.isNotEmpty(sqlParameters)) {

            List<SQLParameter> params = new ArrayList<>(sqlParameters.size());
            for (SQLParameter sqlParameter : sqlParameters) {
                if (sqlParameter instanceof ConstSQLParameter) {
                    Object value = executorContext.toValue(sqlParameter, sqlParameter.getValue());
                    params.add(new EasyConstSQLParameter(sqlParameter.getTableOrNull(), sqlParameter.getPropertyNameOrNull(), value));
                } else if (sqlParameter instanceof BeanSQLParameter) {
                    BeanSQLParameter beanSQLParameter = (BeanSQLParameter) sqlParameter;
                    beanSQLParameter.setBean(entity);
                    Object value = executorContext.toValue(beanSQLParameter, beanSQLParameter.getValue());
                    params.add(new EasyConstSQLParameter(beanSQLParameter.getTableOrNull(), beanSQLParameter.getPropertyNameOrNull(), value));
                } else {
                    throw new EasyQueryException("current sql parameter:[" + EasyClassUtil.getSimpleName(sqlParameter.getClass()) + "],property name:[" + sqlParameter.getPropertyNameOrNull() + "] is not implements BeanSQLParameter or ConstSQLParameter");
                }
            }

            if (printSql) {
                logParameter(true, params, easyConnection, shardingPrint, replicaPrint);
            }
            return params;
        }
        return Collections.emptyList();
    }

    public static StreamResultSet query(ExecutorContext executorContext, EasyConnection easyConnection, String sql, List<SQLParameter> sqlParameters) throws SQLException {
        return query(executorContext, easyConnection, sql, sqlParameters, false, false);
    }

    public static StreamResultSet query(ExecutorContext executorContext, EasyConnection easyConnection, String sql, List<SQLParameter> sqlParameters, boolean shardingPrint, boolean replicaPrint) throws SQLException {
        boolean printSql = executorContext.getEasyQueryOption().isPrintSql();
        logSQL(printSql, sql, easyConnection, shardingPrint, replicaPrint);
        QueryRuntimeContext runtimeContext = executorContext.getRuntimeContext();
        JdbcTypeHandlerManager easyJdbcTypeHandler = runtimeContext.getJdbcTypeHandlerManager();

        PreparedStatement ps = null;
        ResultSet rs = null;
        List<SQLParameter> parameters = extractParameters(executorContext, null, sqlParameters,printSql,easyConnection,shardingPrint,replicaPrint);
        StreamResultSet sr = null;
        try {
            ps = createPreparedStatement(easyConnection.getConnection(), sql, parameters, easyJdbcTypeHandler);

            long start = printSql ? System.currentTimeMillis() : 0L;
            rs = ps.executeQuery();
            long end = printSql ? System.currentTimeMillis() : 0L;
            if (printSql) {
                logUse(true, start, end, easyConnection, shardingPrint, replicaPrint);
            }
            //如果是分片查询那么需要提前next
            if (shardingPrint) {
                boolean next = rs.next();
                sr = new EasyShardingStreamResultSet(rs, ps, next);
            } else {
                sr = new EasyStreamResultSet(rs, ps);
            }

        } catch (SQLException e) {
            log.error(sql, e);
            throw new EasyQuerySQLStatementException(sql, e);
        }
        return sr;
    }

    public static <T> int insert(ExecutorContext executorContext, EasyConnection easyConnection, String sql, List<T> entities, List<SQLParameter> sqlParameters, boolean fillAutoIncrement, boolean shardingPrint, boolean replicaPrint) throws SQLException {
        boolean printSql = executorContext.getEasyQueryOption().isPrintSql();
        logSQL(printSql, sql, easyConnection, shardingPrint, replicaPrint);
        QueryRuntimeContext runtimeContext = executorContext.getRuntimeContext();
        JdbcTypeHandlerManager easyJdbcTypeHandler = runtimeContext.getJdbcTypeHandlerManager();
        Class<?> entityClass = entities.get(0).getClass();
        EntityMetadata entityMetadata = runtimeContext.getEntityMetadataManager().getEntityMetadata(entityClass);
        List<String> incrementColumns = fillAutoIncrement ? entityMetadata.getIncrementColumns() : null;
        PreparedStatement ps = null;
        int r = 0;
        try {
            int batchSize = 0;
            for (T entity : entities) {
                batchSize++;
                List<SQLParameter> parameters = extractParameters(executorContext, entity, sqlParameters,printSql,easyConnection,shardingPrint,replicaPrint);

                if (ps == null) {
                    ps = createPreparedStatement(easyConnection.getConnection(), sql, parameters, easyJdbcTypeHandler, incrementColumns);
                } else {
                    setPreparedStatement(ps, parameters, easyJdbcTypeHandler);
                }
                ps.addBatch();
                if ((batchSize % BATCH_GROUP_COUNT) == 0) {
                    int[] ints = ps.executeBatch();
//                    r += ints.length;
                    r += EasyCollectionUtil.sum(ints);
                    ps.clearBatch();
                }
            }
            if ((batchSize % BATCH_GROUP_COUNT) != 0) {
                int[] ints = ps.executeBatch();
//                r += ints.length;
                r += EasyCollectionUtil.sum(ints);
                ps.clearBatch();
            }
            logResult(printSql, r, easyConnection, shardingPrint, replicaPrint);
            //如果需要自动填充并且存在自动填充列
            if (fillAutoIncrement && EasyCollectionUtil.isNotEmpty(incrementColumns)) {
                assert ps != null;
                ResultSet keysSet = ps.getGeneratedKeys();
                int index = 0;
                ColumnMetadata[] columnMetadatas = new ColumnMetadata[incrementColumns.size()];
                while (keysSet.next()) {
                    T entity = entities.get(index);
                    for (int i = 0; i < incrementColumns.size(); i++) {
                        ColumnMetadata columnMetadata = columnMetadatas[i];
                        if (columnMetadata == null) {
                            String columnName = incrementColumns.get(i);
                            String propertyName = entityMetadata.getPropertyNameNotNull(columnName);
                            columnMetadata = entityMetadata.getColumnNotNull(propertyName);
                            columnMetadatas[i] = columnMetadata;
                        }

                        Object value = keysSet.getObject(i + 1);
                        Object newValue = EasyClassUtil.convertValueToRequiredType(value, columnMetadata.getPropertyType());
                        PropertySetterCaller<Object> beanSetter = columnMetadata.getSetterCaller();
                        beanSetter.call(entity, newValue);
//                        Method setter = getSetter(property, entityClass);
//                        callSetter(entity,setter, property, newValue);
                    }
                    index++;
                }
            }
        } catch (SQLException e) {
            log.error(sql, e);
            throw new EasyQuerySQLStatementException(sql, e);
        } finally {
            clear(ps);
        }
        return r;

    }

    private static void clear(PreparedStatement ps) {
        try {
            if (ps != null) {
                ps.close();
            }
        } catch (SQLException ignored) {
        }
    }

    public static <T> int executeRows(ExecutorContext executorContext, EasyConnection easyConnection, String sql, List<T> entities, List<SQLParameter> sqlParameters, boolean shardingPrint, boolean replicaPrint) throws SQLException {
        boolean printSql = executorContext.getEasyQueryOption().isPrintSql();
        logSQL(printSql, sql, easyConnection, shardingPrint, replicaPrint);
        QueryRuntimeContext runtimeContext = executorContext.getRuntimeContext();
        JdbcTypeHandlerManager easyJdbcTypeHandlerManager = runtimeContext.getJdbcTypeHandlerManager();
        PreparedStatement ps = null;
        int r = 0;
        try {
            int batchSize = 0;
            for (T entity : entities) {
                batchSize++;
                List<SQLParameter> parameters = extractParameters(executorContext, entity, sqlParameters,printSql,easyConnection,shardingPrint,replicaPrint);

                if (ps == null) {
                    ps = createPreparedStatement(easyConnection.getConnection(), sql, parameters, easyJdbcTypeHandlerManager);
                } else {
                    setPreparedStatement(ps, parameters, easyJdbcTypeHandlerManager);
                }
                ps.addBatch();
                if ((batchSize % BATCH_GROUP_COUNT) == 0) {
                    int[] ints = ps.executeBatch();
                    r += EasyCollectionUtil.sum(ints);
                    ps.clearBatch();
                }
            }
            if ((batchSize % BATCH_GROUP_COUNT) != 0) {
                int[] ints = ps.executeBatch();
                r += EasyCollectionUtil.sum(ints);
                ps.clearBatch();
            }
            logResult(printSql, r, easyConnection, shardingPrint, replicaPrint);
        } catch (SQLException e) {
            log.error(sql, e);
            throw new EasyQuerySQLStatementException(sql, e);
        } finally {
            clear(ps);
        }
        return r;
    }

    public static <T> int executeRows(ExecutorContext executorContext, EasyConnection easyConnection, String sql, List<SQLParameter> sqlParameters, boolean shardingPrint, boolean replicaPrint) throws SQLException {
        boolean printSql = executorContext.getEasyQueryOption().isPrintSql();
        logSQL(printSql, sql, easyConnection, shardingPrint, replicaPrint);
        QueryRuntimeContext runtimeContext = executorContext.getRuntimeContext();
        JdbcTypeHandlerManager easyJdbcTypeHandlerManager = runtimeContext.getJdbcTypeHandlerManager();
        PreparedStatement ps = null;
        int r = 0;

        List<SQLParameter> parameters = extractParameters(executorContext, null, sqlParameters,printSql,easyConnection,shardingPrint,replicaPrint);

        try {
            ps = createPreparedStatement(easyConnection.getConnection(), sql, parameters, easyJdbcTypeHandlerManager);
            r = ps.executeUpdate();
            logResult(printSql, r, easyConnection, shardingPrint, replicaPrint);
        } catch (SQLException e) {
            log.error(sql, e);
            throw new EasyQuerySQLStatementException(sql, e);
        } finally {
            clear(ps);
        }
        return r;
    }


    private static PreparedStatement createPreparedStatement(Connection connection, String sql, List<SQLParameter> sqlParameters, JdbcTypeHandlerManager easyJdbcTypeHandlerManager) throws SQLException {
        return createPreparedStatement(connection, sql, sqlParameters, easyJdbcTypeHandlerManager, null);
    }

    private static PreparedStatement createPreparedStatement(Connection connection, String sql, List<SQLParameter> sqlParameters, JdbcTypeHandlerManager easyJdbcTypeHandlerManager, List<String> incrementColumns) throws SQLException {
        PreparedStatement preparedStatement = EasyCollectionUtil.isEmpty(incrementColumns) ? connection.prepareStatement(sql) : connection.prepareStatement(sql, incrementColumns.toArray(new String[0]));
        return setPreparedStatement(preparedStatement, sqlParameters, easyJdbcTypeHandlerManager);
    }

    private static PreparedStatement setPreparedStatement(PreparedStatement preparedStatement, List<SQLParameter> sqlParameters, JdbcTypeHandlerManager easyJdbcTypeHandlerManager) throws SQLException {
        if (EasyCollectionUtil.isNotEmpty(sqlParameters)) {

            EasyParameter easyParameter = new EasyParameter(preparedStatement, sqlParameters);
            int paramSize = sqlParameters.size();
            for (int i = 0; i < paramSize; i++) {
                easyParameter.setIndex(i);
                JdbcTypeHandler handler = easyJdbcTypeHandlerManager.getHandler(easyParameter.getValueType());
                handler.setParameter(easyParameter);
            }
        }
        return preparedStatement;
    }


}
