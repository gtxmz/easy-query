package com.easy.query.core.proxy.columns;

import com.easy.query.api.proxy.entity.select.EntityQueryable;
import com.easy.query.core.basic.api.internal.LogicDeletable;
import com.easy.query.core.basic.api.select.Query;
import com.easy.query.core.context.QueryRuntimeContext;
import com.easy.query.core.expression.lambda.SQLExpression1;
import com.easy.query.core.expression.lambda.SQLFuncExpression1;
import com.easy.query.core.expression.parser.core.available.TableAvailable;
import com.easy.query.core.func.SQLFunction;
import com.easy.query.core.proxy.PropColumn;
import com.easy.query.core.proxy.PropTypeColumn;
import com.easy.query.core.proxy.ProxyEntity;
import com.easy.query.core.proxy.SQLSelectAsExpression;
import com.easy.query.core.proxy.available.EntitySQLContextAvailable;
import com.easy.query.core.proxy.columns.impl.EasySQLPredicateQueryable;
import com.easy.query.core.proxy.core.EntitySQLContext;
import com.easy.query.core.proxy.core.ProxyFlatElementEntitySQLContext;
import com.easy.query.core.proxy.extension.functions.ColumnNumberFunctionAvailable;
import com.easy.query.core.proxy.extension.functions.executor.ColumnFunctionCompareComparableAnyChainExpression;
import com.easy.query.core.proxy.extension.functions.executor.ColumnFunctionCompareComparableBooleanChainExpression;
import com.easy.query.core.proxy.extension.functions.executor.ColumnFunctionCompareComparableNumberChainExpression;
import com.easy.query.core.proxy.extension.functions.executor.impl.ColumnFunctionCompareComparableAnyChainExpressionImpl;
import com.easy.query.core.proxy.extension.functions.executor.impl.ColumnFunctionCompareComparableBooleanChainExpressionImpl;
import com.easy.query.core.proxy.extension.functions.executor.impl.ColumnFunctionCompareComparableNumberChainExpressionImpl;
import com.easy.query.core.proxy.impl.SQLColumnIncludeColumn2Impl;
import com.easy.query.core.proxy.impl.SQLPredicateImpl;
import com.easy.query.core.proxy.predicate.aggregate.DSLSQLFunctionAvailable;
import com.easy.query.core.proxy.sql.include.IncludeManyAvailable;
import com.easy.query.core.util.EasyClassUtil;
import com.easy.query.core.util.EasyObjectUtil;

import java.math.BigDecimal;

/**
 * create time 2024/2/11 22:07
 * 文件说明
 *
 * @author xuejiaming
 */
public interface SQLQueryable<T1Proxy extends ProxyEntity<T1Proxy, T1>, T1> extends EntitySQLContextAvailable, PropColumn, LogicDeletable<SQLQueryable<T1Proxy, T1>> {//,ProxyEntity<T1Proxy,T1>

    EntityQueryable<T1Proxy, T1> getQueryable();

    TableAvailable getOriginalTable();

    String getNavValue();

    T1Proxy getProxy();

    @Override
    default String getValue() {
        return getNavValue();
    }

    default SQLPredicateQueryable<T1Proxy, T1> where(SQLExpression1<T1Proxy> whereExpression) {
        getQueryable().where(whereExpression);
        return new EasySQLPredicateQueryable<>(this);
    }

    /**
     * 存在任意一个满足条件
     *
     * @param whereExpression
     */
    default void any(SQLExpression1<T1Proxy> whereExpression) {
        where(whereExpression).any();
    }


    /**
     * 存在任意一个满足条件
     */
    default void any() {
        getEntitySQLContext().accept(new SQLPredicateImpl(f -> f.exists(getQueryable().limit(1))));
    }

    /**
     * 不存在任意一个满足条件
     *
     * @param whereExpression
     */
    default void none(SQLExpression1<T1Proxy> whereExpression) {
        where(whereExpression).none();
    }

    /**
     * 不存在任意一个满足条件
     */
    default void none() {
        getEntitySQLContext().accept(new SQLPredicateImpl(f -> f.none(getQueryable().limit(1))));
    }

    /**
     * 返回boolean表示是否存在任意匹配项
     *
     * @return
     */
    default ColumnFunctionCompareComparableBooleanChainExpression<Boolean> anyValue() {
        Query<?> anyQuery = getQueryable().limit(1).select("1");
        return new ColumnFunctionCompareComparableBooleanChainExpressionImpl<>(this.getEntitySQLContext(), null, null, f -> f.exists(anyQuery), Boolean.class);
    }

    /**
     * 返回boolean表示是否没有任意一项被匹配到
     *
     * @return
     */
    default ColumnFunctionCompareComparableBooleanChainExpression<Boolean> noneValue() {
        Query<?> anyQuery = getQueryable().limit(1).select("1");
        return new ColumnFunctionCompareComparableBooleanChainExpressionImpl<>(this.getEntitySQLContext(), null, null, f -> f.not(f.exists(anyQuery)), Boolean.class);
    }

    default ColumnFunctionCompareComparableNumberChainExpression<Long> count(SQLExpression1<T1Proxy> whereExpression) {
        Query<Long> longQuery = getQueryable().where(whereExpression).selectCount();
        return new ColumnFunctionCompareComparableNumberChainExpressionImpl<>(this.getEntitySQLContext(), null, null, f -> f.subQueryValue(longQuery), Long.class);
    }

    default ColumnFunctionCompareComparableNumberChainExpression<Long> count() {
        return count(x -> {
        });
    }

    default ColumnFunctionCompareComparableNumberChainExpression<Integer> intCount(SQLExpression1<T1Proxy> whereExpression) {
        Query<Integer> longQuery = getQueryable().where(whereExpression).selectCount(Integer.class);
        return new ColumnFunctionCompareComparableNumberChainExpressionImpl<>(this.getEntitySQLContext(), null, null, f -> f.subQueryValue(longQuery), Integer.class);
    }

    default ColumnFunctionCompareComparableNumberChainExpression<Integer> intCount() {
        return intCount(x -> {
        });
    }

    default <TMember extends Number> ColumnFunctionCompareComparableNumberChainExpression<TMember> sum(SQLFuncExpression1<T1Proxy, ColumnNumberFunctionAvailable<TMember>> columnSelector) {
        return sum(columnSelector, false);
    }

    default <TMember extends Number> ColumnFunctionCompareComparableNumberChainExpression<TMember> sum(SQLFuncExpression1<T1Proxy, ColumnNumberFunctionAvailable<TMember>> columnSelector, boolean distinct) {
        Query<TMember> sumQuery = staticSum(getQueryable(), columnSelector, distinct, null);
        return new ColumnFunctionCompareComparableNumberChainExpressionImpl<>(this.getEntitySQLContext(), null, null, f -> f.nullOrDefault(x -> x.subQuery(sumQuery).format(0)), sumQuery.queryClass());
    }

    default <TMember extends Number> ColumnFunctionCompareComparableNumberChainExpression<BigDecimal> sumBigDecimal(SQLFuncExpression1<T1Proxy, ColumnNumberFunctionAvailable<TMember>> columnSelector) {
        return sumBigDecimal(columnSelector, false);
    }

    default <TMember extends Number> ColumnFunctionCompareComparableNumberChainExpression<BigDecimal> sumBigDecimal(SQLFuncExpression1<T1Proxy, ColumnNumberFunctionAvailable<TMember>> columnSelector, boolean distinct) {
        Query<TMember> sumQuery = staticSum(getQueryable(), columnSelector, distinct, BigDecimal.class);
        return new ColumnFunctionCompareComparableNumberChainExpressionImpl<>(this.getEntitySQLContext(), null, null, f -> f.nullOrDefault(x -> x.subQuery(sumQuery).format(0)), BigDecimal.class);
    }

    default <TMember extends Number> ColumnFunctionCompareComparableNumberChainExpression<BigDecimal> avg(SQLFuncExpression1<T1Proxy, ColumnNumberFunctionAvailable<TMember>> columnSelector) {
        return avg(columnSelector, false);
    }

    default <TMember extends Number> ColumnFunctionCompareComparableNumberChainExpression<BigDecimal> avg(SQLFuncExpression1<T1Proxy, ColumnNumberFunctionAvailable<TMember>> columnSelector, boolean distinct) {
        Query<BigDecimal> avgQuery = staticAvg(getQueryable(), columnSelector, distinct);
        return new ColumnFunctionCompareComparableNumberChainExpressionImpl<>(this.getEntitySQLContext(), null, null, f -> f.nullOrDefault(x -> x.subQuery(avgQuery).format(0)), BigDecimal.class);
    }

    default <TMember> ColumnFunctionCompareComparableAnyChainExpression<TMember> max(SQLFuncExpression1<T1Proxy, PropTypeColumn<TMember>> columnSelector) {
        Query<TMember> maxQuery = staticMinOrMax(getQueryable(), columnSelector,true);
        return minOrMax(maxQuery, this.getEntitySQLContext());
    }

    default <TMember> ColumnFunctionCompareComparableAnyChainExpression<TMember> min(SQLFuncExpression1<T1Proxy, PropTypeColumn<TMember>> columnSelector) {
        Query<TMember> minQuery = staticMinOrMax(getQueryable(), columnSelector,false);
        return minOrMax(minQuery, this.getEntitySQLContext());
    }

    default <TMember> ColumnFunctionCompareComparableAnyChainExpression<TMember> select(SQLFuncExpression1<T1Proxy, PropTypeColumn<TMember>> columnSelector){
        Query<TMember> memberQuery = getQueryable().selectColumn(columnSelector);
        return new ColumnFunctionCompareComparableAnyChainExpressionImpl<>(this.getEntitySQLContext(), null, null, f -> f.anySQLFunction("{0}", c -> c.subQuery(memberQuery)));
    }

    static <TMember> ColumnFunctionCompareComparableAnyChainExpression<TMember> minOrMax(Query<TMember> subQuery, EntitySQLContext entitySQLContext) {

        boolean numberType = EasyClassUtil.isNumberType(subQuery.getClass());
        return new ColumnFunctionCompareComparableAnyChainExpressionImpl<>(entitySQLContext, null, null, f -> {
            if (numberType) {
                return f.nullOrDefault(x -> x.subQuery(subQuery).format(0));
            } else {
                return f.subQueryValue(subQuery);
            }
        }, subQuery.queryClass());
    }


    /**
     * 请升级到2.0.24+
     *
     * @param columnProxy
     * @param <TPropertyProxy>
     * @param <TProperty>
     * @return
     */
    @Deprecated
    default <TPropertyProxy extends ProxyEntity<TPropertyProxy, TProperty>, TProperty> Object set(SQLQueryable<TPropertyProxy, TProperty> columnProxy) {
        return set(columnProxy, null);
    }

    /**
     * 请升级到2.0.24+
     *
     * @param columnProxy
     * @param navigateSelectExpression
     * @param <TPropertyProxy>
     * @param <TProperty>
     * @return
     */
    @Deprecated
    default <TPropertyProxy extends ProxyEntity<TPropertyProxy, TProperty>, TProperty> Object set(SQLQueryable<TPropertyProxy, TProperty> columnProxy, SQLFuncExpression1<TPropertyProxy, ProxyEntity<T1Proxy, T1>> navigateSelectExpression) {
        getEntitySQLContext().accept(new SQLColumnIncludeColumn2Impl<>(columnProxy.getOriginalTable(), columnProxy.getNavValue(), getNavValue(), columnProxy.getQueryable().get1Proxy(), navigateSelectExpression));
        return this;
    }

    /**
     * 暂开集合元素
     * 用户返回集合元素
     *
     * @return
     */
    default T1Proxy flatElement() {
        return flatElement(null);
    }

    default T1Proxy flatElement(SQLFuncExpression1<T1Proxy, SQLSelectAsExpression> flatAdapterExpression) {
        QueryRuntimeContext runtimeContext = this.getProxy().getEntitySQLContext().getRuntimeContext();
        T1Proxy tPropertyProxy = getProxy().create(getProxy().getTable(), new ProxyFlatElementEntitySQLContext(this, runtimeContext, flatAdapterExpression));
        tPropertyProxy.setNavValue(getNavValue());
        return tPropertyProxy;
    }
//    default void flatElement(SQLExpression1<T1Proxy> flatFilterExpression) {
//        T1Proxy tPropertyProxy = getProxy().create(getProxy().getTable(), getProxy().getEntitySQLContext());
//        tPropertyProxy.setNavValue(getNavValue());
//
//        FilterContext whereFilterContext = getQueryable().getClientQueryable().getSQLExpressionProvider1().getWhereFilterContext();
//        tPropertyProxy.getEntitySQLContext()._where(whereFilterContext.getFilter(), () -> {
//            flatFilterExpression.apply(tPropertyProxy);
//        });
//        this.any();
//    }


    /**
     * 静态方法封装对sum的处理
     *
     * @param entityQueryable
     * @param columnSelector
     * @param distinct
     * @param propertyType    null那么就使用columnSelector.getPropertyType()
     * @param <TProxy>
     * @param <T>
     * @param <TMember>
     * @return
     */
    static <TProxy extends ProxyEntity<TProxy, T>, T, TMember extends Number> Query<TMember> staticSum(EntityQueryable<TProxy, T> entityQueryable, SQLFuncExpression1<TProxy, ColumnNumberFunctionAvailable<TMember>> columnSelector, boolean distinct, Class<?> propertyType) {
        return entityQueryable.selectColumn(s -> {
            ColumnNumberFunctionAvailable<TMember> apply = columnSelector.apply(s);
            return new ColumnFunctionCompareComparableNumberChainExpressionImpl<>(s.getEntitySQLContext(), s.getTable(), s.getValue(), fx -> {
                return fx.sum(x -> {
                    PropTypeColumn.columnFuncSelector(x, apply);
                }).distinct(distinct);
            }, EasyObjectUtil.nullToDefault(propertyType, apply.getPropertyType()));
        });
    }

    static <TProxy extends ProxyEntity<TProxy, T>, T, TMember extends Number> Query<BigDecimal> staticAvg(EntityQueryable<TProxy, T> entityQueryable, SQLFuncExpression1<TProxy, ColumnNumberFunctionAvailable<TMember>> columnSelector, boolean distinct) {
        return entityQueryable.selectColumn(s -> {
            ColumnNumberFunctionAvailable<TMember> apply = columnSelector.apply(s);
            return new ColumnFunctionCompareComparableNumberChainExpressionImpl<>(s.getEntitySQLContext(), s.getTable(), s.getValue(), fx -> {
                return fx.avg(x -> {
                    PropTypeColumn.columnFuncSelector(x, apply);
                }).distinct(distinct);
            }, BigDecimal.class);
        });
    }

    static <TProxy extends ProxyEntity<TProxy, T>, T, TMember> Query<TMember> staticMinOrMax(EntityQueryable<TProxy, T> entityQueryable, SQLFuncExpression1<TProxy, PropTypeColumn<TMember>> columnSelector, boolean max) {
        return entityQueryable.selectColumn(s -> {
            PropTypeColumn<TMember> apply = columnSelector.apply(s);
            return new ColumnFunctionCompareComparableAnyChainExpressionImpl<>(s.getEntitySQLContext(), s.getTable(), s.getValue(), fx -> {
                if(max){
                    return fx.max(x -> {
                        PropTypeColumn.columnFuncSelector(x, apply);
                    });
                }else{
                    return fx.min(x -> {
                        PropTypeColumn.columnFuncSelector(x, apply);
                    });
                }
            }, apply.getPropertyType());
        });
    }
}
