package com.easy.query.core.expression.sql.expression.impl;

import com.easy.query.core.context.QueryRuntimeContext;
import com.easy.query.core.basic.jdbc.parameter.SQLParameterCollector;
import com.easy.query.core.enums.MultiTableTypeEnum;
import com.easy.query.core.expression.parser.core.available.TableAvailable;
import com.easy.query.core.expression.sql.expression.AnonymousEntityTableSQLExpression;
import com.easy.query.core.expression.sql.expression.EntityQuerySQLExpression;
import com.easy.query.core.util.EasySQLExpressionUtil;

/**
 * create time 2023/4/23 16:30
 * 文件说明
 *
 * @author xuejiaming
 */
public class AnonymousEntityTableSQLExpressionImpl extends TableSQLExpressionImpl implements AnonymousEntityTableSQLExpression {
    private final EntityQuerySQLExpression easyQuerySQLExpression;

    public AnonymousEntityTableSQLExpressionImpl(TableAvailable entityTable, MultiTableTypeEnum multiTableType, EntityQuerySQLExpression easyQuerySQLExpression, QueryRuntimeContext runtimeContext) {
        super(entityTable, multiTableType, runtimeContext);
        this.easyQuerySQLExpression = easyQuerySQLExpression;
    }


    @Override
    public String getTableName() {
        if (tableNameAs != null) {
            return tableNameAs.apply(getAlias());
        }
        return getAlias();
    }

    @Override
    public String toSQL(SQLParameterCollector sqlParameterCollector) {
        EasySQLExpressionUtil.expressionInvokeRoot(sqlParameterCollector);

        StringBuilder sql = new StringBuilder();

        sql.append(getSelectTableSource()).append("(").append(easyQuerySQLExpression.toSQL(sqlParameterCollector)).append(")");
        String tableName = getTableName();
        if (tableName != null) {
            sql.append(" ").append(tableName);
        }
        return sql.toString();
    }

}
