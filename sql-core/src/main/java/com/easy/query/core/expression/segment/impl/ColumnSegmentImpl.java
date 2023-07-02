package com.easy.query.core.expression.segment.impl;

import com.easy.query.core.context.QueryRuntimeContext;
import com.easy.query.core.basic.jdbc.parameter.ToSQLContext;
import com.easy.query.core.expression.parser.core.available.TableAvailable;
import com.easy.query.core.expression.segment.ColumnSegment;
import com.easy.query.core.util.EasySQLExpressionUtil;

/**
 * @FileName: ColumnSegment.java
 * @Description: 文件说明
 * @Date: 2023/2/13 15:18
 * @author xuejiaming
 */
public class ColumnSegmentImpl implements ColumnSegment {


    protected final TableAvailable table;


    protected final String propertyName;
    protected final QueryRuntimeContext runtimeContext;
    protected String alias;

    public ColumnSegmentImpl(TableAvailable table, String propertyName, QueryRuntimeContext runtimeContext){
        this(table,propertyName,runtimeContext,null);
    }
    public ColumnSegmentImpl(TableAvailable table, String propertyName, QueryRuntimeContext runtimeContext, String alias){
        this.table = table;
        this.propertyName = propertyName;
        this.runtimeContext = runtimeContext;
        this.alias = alias;
    }

    @Override
    public TableAvailable getTable() {
        return table;
    }

    @Override
    public String getPropertyName() {
        return propertyName;
    }

    @Override
    public String getAlias() {
        return alias;
    }

    @Override
    public String toSQL(ToSQLContext toSQLContext) {
        String sqlColumnSegment = EasySQLExpressionUtil.getSQLOwnerColumnByProperty(runtimeContext,table,propertyName,toSQLContext);
        if(alias==null){
            return sqlColumnSegment;
        }
        return sqlColumnSegment +" AS " + EasySQLExpressionUtil.getQuoteName(runtimeContext, alias);
    }

    @Override
    public ColumnSegment cloneSQLColumnSegment() {
        return new ColumnSegmentImpl(table,propertyName, runtimeContext,alias);
    }

}
