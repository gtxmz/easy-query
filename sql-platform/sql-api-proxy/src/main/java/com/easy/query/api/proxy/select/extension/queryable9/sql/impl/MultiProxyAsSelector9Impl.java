package com.easy.query.api.proxy.select.extension.queryable9.sql.impl;

import com.easy.query.api.proxy.select.extension.queryable9.sql.MultiProxyAsSelector9;
import com.easy.query.api.proxy.sql.ProxyAsSelector;
import com.easy.query.core.expression.builder.AsSelector;
import com.easy.query.core.expression.builder.core.SQLNative;
import com.easy.query.core.proxy.ProxyEntity;
import com.easy.query.core.util.EasyObjectUtil;

/**
 * create time 2023/9/10 11:01
 * 文件说明
 *
 * @author xuejiaming
 */
public class MultiProxyAsSelector9Impl<T1Proxy extends ProxyEntity<T1Proxy, T1>, T1,
        T2Proxy extends ProxyEntity<T2Proxy, T2>, T2,
        T3Proxy extends ProxyEntity<T3Proxy, T3>, T3,
        T4Proxy extends ProxyEntity<T4Proxy, T4>, T4,
        T5Proxy extends ProxyEntity<T5Proxy, T5>, T5,
        T6Proxy extends ProxyEntity<T6Proxy, T6>, T6,
        T7Proxy extends ProxyEntity<T7Proxy, T7>, T7,
        T8Proxy extends ProxyEntity<T8Proxy, T8>, T8,
        T9Proxy extends ProxyEntity<T9Proxy, T9>, T9,
        TRProxy extends ProxyEntity<TRProxy, TR>, TR> implements MultiProxyAsSelector9<T1Proxy, T2Proxy, T3Proxy, T4Proxy, T5Proxy, T6Proxy, T7Proxy, T8Proxy, T9Proxy, TRProxy, TR> {


    private final AsSelector asSelector;
    private final T1Proxy t;
    private final T2Proxy t1;
    private final T3Proxy t2;
    private final T4Proxy t3;
    private final T5Proxy t4;
    private final T6Proxy t5;
    private final T7Proxy t6;
    private final T8Proxy t7;
    private final T9Proxy t8;
    private final TRProxy trProxy;

    public MultiProxyAsSelector9Impl(AsSelector asSelector, T1Proxy t, T2Proxy t1, T3Proxy t2, T4Proxy t3, T5Proxy t4, T6Proxy t5, T7Proxy t6, T8Proxy t7, T9Proxy t8, TRProxy trProxy) {
        this.asSelector = asSelector;
        this.t = t;
        this.t1 = t1;
        this.t2 = t2;
        this.t3 = t3;
        this.t4 = t4;
        this.t5 = t5;
        this.t6 = t6;
        this.t7 = t7;
        this.t8 = t8;
        this.trProxy = trProxy;
    }
    @Override
    public T1Proxy t() {
        return t;
    }

    @Override
    public T2Proxy t1() {
        return t1;
    }

    @Override
    public T3Proxy t2() {
        return t2;
    }

    @Override
    public T4Proxy t3() {
        return t3;
    }

    @Override
    public T5Proxy t4() {
        return t4;
    }

    @Override
    public T6Proxy t5() {
        return t5;
    }

    @Override
    public T7Proxy t6() {
        return t6;
    }

    @Override
    public T8Proxy t7() {
        return t7;
    }

    @Override
    public T9Proxy t8() {
        return t8;
    }

    @Override
    public TRProxy tr() {
        return trProxy;
    }

    @Override
    public AsSelector getAsSelector() {
        return asSelector;
    }

    @Override
    public <T> SQLNative<T> getSQLNative() {
        return EasyObjectUtil.typeCastNullable(asSelector);
    }

    @Override
    public ProxyAsSelector<TRProxy, TR> castChain() {
        return this;
    }
}
