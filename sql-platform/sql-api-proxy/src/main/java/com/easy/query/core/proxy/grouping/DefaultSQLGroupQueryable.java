package com.easy.query.core.proxy.grouping;

import com.easy.query.api.proxy.extension.casewhen.CaseWhenEntityBuilder;
import com.easy.query.core.expression.lambda.SQLActionExpression;
import com.easy.query.core.expression.lambda.SQLExpression1;
import com.easy.query.core.expression.lambda.SQLFuncExpression1;
import com.easy.query.core.func.SQLFunction;
import com.easy.query.core.proxy.PropTypeColumn;
import com.easy.query.core.proxy.core.EntitySQLContext;
import com.easy.query.core.proxy.extension.functions.ColumnNumberFunctionAvailable;
import com.easy.query.core.proxy.extension.functions.executor.ColumnFunctionCompareComparableAnyChainExpression;
import com.easy.query.core.proxy.extension.functions.executor.ColumnFunctionCompareComparableNumberChainExpression;
import com.easy.query.core.proxy.extension.functions.executor.ColumnFunctionCompareComparableStringChainExpression;
import com.easy.query.core.proxy.extension.functions.executor.impl.ColumnFunctionCompareComparableAnyChainExpressionImpl;
import com.easy.query.core.proxy.extension.functions.executor.impl.ColumnFunctionCompareComparableNumberChainExpressionImpl;
import com.easy.query.core.proxy.extension.functions.executor.impl.ColumnFunctionCompareComparableStringChainExpressionImpl;
import com.easy.query.core.util.EasyObjectUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * create time 2025/3/5 21:20
 * 文件说明
 *
 * @author xuejiaming
 */
public class DefaultSQLGroupQueryable<TProxy> implements SQLGroupQueryable<TProxy> {
    private final SQLExpression1<TProxy> predicate;
    private final TProxy groupTable;
    private final EntitySQLContext entitySQLContext;
    private boolean distinct = false;

    public DefaultSQLGroupQueryable(TProxy groupTable, EntitySQLContext entitySQLContext, SQLExpression1<TProxy> predicate) {
        this.groupTable = groupTable;
        this.entitySQLContext = entitySQLContext;
        this.predicate = predicate;
    }

    @Override
    public SQLGroupQueryable<TProxy> distinct(boolean useDistinct) {
        this.distinct = useDistinct;
        return this;
    }

    @Override
    public <T extends Long> ColumnFunctionCompareComparableNumberChainExpression<T> count() {
        if (predicate == null) {
            return new ColumnFunctionCompareComparableNumberChainExpressionImpl<>(this.getEntitySQLContext(), null, null, fx -> {
                return fx.count(o -> {
                }).distinct(distinct);
            }, Long.class);
        } else {
            ColumnFunctionCompareComparableAnyChainExpression<Long> preColumn = new CaseWhenEntityBuilder(this.getEntitySQLContext()).caseWhen(() -> predicate.apply(groupTable)).then(1).elseEnd(null, Long.class);
            return new ColumnFunctionCompareComparableNumberChainExpressionImpl<>(this.getEntitySQLContext(), preColumn.getTable(), preColumn.getValue(), fx -> {
                SQLFunction sqlFunction = preColumn.func().apply(fx);
                return fx.count(sqlFunction).distinct(distinct);
            }, Long.class);
        }
    }

    @Override
    public <T extends Integer> ColumnFunctionCompareComparableNumberChainExpression<T> intCount() {
        ColumnFunctionCompareComparableNumberChainExpression<Long> count = count();
        count._setPropertyType(Integer.class);
        return EasyObjectUtil.typeCastNullable(count);
    }

    @Override
    public <TMember extends Number> ColumnFunctionCompareComparableNumberChainExpression<TMember> sum(SQLFuncExpression1<TProxy, ColumnNumberFunctionAvailable<TMember>> columnSelector) {
        ColumnNumberFunctionAvailable<TMember> column = columnSelector.apply(groupTable);
        PropTypeColumn<?> preColumn = predicate == null ? column : new CaseWhenEntityBuilder(this.getEntitySQLContext()).caseWhen(() -> predicate.apply(groupTable)).then(column).elseEnd(0, column.getPropertyType());
        return new ColumnFunctionCompareComparableNumberChainExpressionImpl<>(this.getEntitySQLContext(), preColumn.getTable(), preColumn.getValue(), fx -> {
            return fx.sum(x -> {
                PropTypeColumn.columnFuncSelector(x, preColumn);
            }).distinct(distinct);
        }, column.getPropertyType());
    }

    @Override
    public <TMember extends Number> ColumnFunctionCompareComparableNumberChainExpression<BigDecimal> sumBigDecimal(SQLFuncExpression1<TProxy, ColumnNumberFunctionAvailable<TMember>> columnSelector) {
        ColumnFunctionCompareComparableNumberChainExpression<TMember> sum = sum(columnSelector);
        sum._setPropertyType(BigDecimal.class);
        return EasyObjectUtil.typeCastNullable(sum);
    }

    @Override
    public <TMember extends Number> ColumnFunctionCompareComparableNumberChainExpression<Integer> sumInt(SQLFuncExpression1<TProxy, ColumnNumberFunctionAvailable<TMember>> columnSelector) {
        ColumnFunctionCompareComparableNumberChainExpression<TMember> sum = sum(columnSelector);
        sum._setPropertyType(Integer.class);
        return EasyObjectUtil.typeCastNullable(sum);
    }

    @Override
    public <TMember extends Number> ColumnFunctionCompareComparableNumberChainExpression<Long> sumLong(SQLFuncExpression1<TProxy, ColumnNumberFunctionAvailable<TMember>> columnSelector) {
        ColumnFunctionCompareComparableNumberChainExpression<TMember> sum = sum(columnSelector);
        sum._setPropertyType(Long.class);
        return EasyObjectUtil.typeCastNullable(sum);
    }

    @Override
    public <TMember extends Number> ColumnFunctionCompareComparableNumberChainExpression<BigDecimal> avg(SQLFuncExpression1<TProxy, ColumnNumberFunctionAvailable<TMember>> columnSelector) {
        ColumnNumberFunctionAvailable<TMember> column = columnSelector.apply(groupTable);
        PropTypeColumn<?> preColumn = predicate == null ? column : new CaseWhenEntityBuilder(this.getEntitySQLContext()).caseWhen(() -> predicate.apply(groupTable)).then(column).elseEnd(null, column.getPropertyType());
        return new ColumnFunctionCompareComparableNumberChainExpressionImpl<>(this.getEntitySQLContext(), preColumn.getTable(), preColumn.getValue(), fx -> {
            return fx.avg(x -> {
                PropTypeColumn.columnFuncSelector(x, preColumn);
            }).distinct(distinct);
        }, BigDecimal.class);
    }

    @Override
    public <TMember> ColumnFunctionCompareComparableAnyChainExpression<TMember> max(SQLFuncExpression1<TProxy, PropTypeColumn<TMember>> columnSelector) {
        PropTypeColumn<TMember> column = columnSelector.apply(groupTable);
        PropTypeColumn<?> preColumn = predicate == null ? column : new CaseWhenEntityBuilder(this.getEntitySQLContext()).caseWhen(() -> predicate.apply(groupTable)).then(column).elseEnd(null, column.getPropertyType());
        return new ColumnFunctionCompareComparableAnyChainExpressionImpl<>(this.getEntitySQLContext(), preColumn.getTable(), preColumn.getValue(), fx -> {
            return fx.max(x -> {
                PropTypeColumn.columnFuncSelector(x, preColumn);
            });
        }, column.getPropertyType());
    }

    @Override
    public <TMember> ColumnFunctionCompareComparableAnyChainExpression<TMember> min(SQLFuncExpression1<TProxy, PropTypeColumn<TMember>> columnSelector) {
        PropTypeColumn<TMember> column = columnSelector.apply(groupTable);
        PropTypeColumn<?> preColumn = predicate == null ? column : new CaseWhenEntityBuilder(this.getEntitySQLContext()).caseWhen(() -> predicate.apply(groupTable)).then(column).elseEnd(null, column.getPropertyType());
        return new ColumnFunctionCompareComparableAnyChainExpressionImpl<>(this.getEntitySQLContext(), preColumn.getTable(), preColumn.getValue(), fx -> {
            return fx.min(x -> {
                PropTypeColumn.columnFuncSelector(x, preColumn);
            });
        }, column.getPropertyType());
    }

    @Override
    public <TMember> ColumnFunctionCompareComparableStringChainExpression<String> join(SQLFuncExpression1<TProxy, PropTypeColumn<TMember>> columnSelector, String delimiter) {
        PropTypeColumn<TMember> column = columnSelector.apply(groupTable);
        PropTypeColumn<?> preColumn = predicate == null ? column : new CaseWhenEntityBuilder(this.getEntitySQLContext()).caseWhen(() -> predicate.apply(groupTable)).then(column).elseEnd(null, column.getPropertyType());
        return new ColumnFunctionCompareComparableStringChainExpressionImpl<>(this.getEntitySQLContext(), preColumn.getTable(), preColumn.getValue(), fx -> {
            return fx.join(x -> {
                x.value(delimiter);
                PropTypeColumn.columnFuncSelector(x, preColumn);
            }, distinct);
        }, column.getPropertyType());
    }

    @Override
    public EntitySQLContext getEntitySQLContext() {
        return entitySQLContext;
    }
}
