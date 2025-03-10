package com.easy.query.core.expression.segment.impl;

import com.easy.query.core.basic.jdbc.parameter.ToSQLContext;
import com.easy.query.core.expression.parser.core.available.TableAvailable;
import com.easy.query.core.expression.segment.ColumnSegment;
import com.easy.query.core.expression.sql.builder.ExpressionContext;
import com.easy.query.core.metadata.ColumnMetadata;
import com.easy.query.core.util.EasySQLExpressionUtil;

/**
 * create time 2023/6/16 23:22
 * 文件说明
 *
 * @author xuejiaming
 */
public class AnonymousColumnSegmentImpl extends ColumnSegmentImpl {
    private final TableAvailable table;
    private final ExpressionContext expressionContext;

    public AnonymousColumnSegmentImpl(TableAvailable table, ExpressionContext expressionContext, String alias) {
        super(null, alias);
        this.table = table;
        this.expressionContext = expressionContext;
    }
    @Override
    public TableAvailable getTable() {
        return table;
    }


    @Override
    public ColumnMetadata getColumnMetadata() {
        return null;
    }

    @Override
    public String getPropertyName() {
        return null;
    }

    @Override
    public String toSQL(ToSQLContext toSQLContext) {
        String sqlColumnSegment = EasySQLExpressionUtil.getSQLOwnerColumn(expressionContext.getRuntimeContext(), table, alias, toSQLContext);
        String alias = getAlias();
        if (alias != null) {
            return sqlColumnSegment +
                    " AS " + EasySQLExpressionUtil.getQuoteName(expressionContext.getRuntimeContext(), alias);
        }
        return sqlColumnSegment;
    }

    @Override
    public ColumnSegment cloneSQLColumnSegment() {
        return new AnonymousColumnSegmentImpl(table, expressionContext, alias);
    }
}
