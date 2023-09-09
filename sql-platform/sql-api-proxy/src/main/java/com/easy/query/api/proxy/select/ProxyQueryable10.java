package com.easy.query.api.proxy.select;

import com.easy.query.api.proxy.select.extension.queryable10.ProxyAggregatable10;
import com.easy.query.api.proxy.select.extension.queryable10.ProxyFilterable10;
import com.easy.query.api.proxy.select.extension.queryable10.ProxyGroupable10;
import com.easy.query.api.proxy.select.extension.queryable10.ProxyHavingable10;
import com.easy.query.api.proxy.select.extension.queryable10.ProxyOrderable10;
import com.easy.query.api.proxy.select.extension.queryable10.ProxySelectable10;
import com.easy.query.api.proxy.select.extension.queryable10.override.OverrideProxyQueryable10;
import com.easy.query.core.proxy.ProxyEntity;

/**
 * create time 2023/6/23 15:26
 * 文件说明
 *
 * @author xuejiaming
 */
public interface ProxyQueryable10<T1Proxy extends ProxyEntity<T1Proxy, T1>,
        T1, T2Proxy extends ProxyEntity<T2Proxy, T2>, T2,
        T3Proxy extends ProxyEntity<T3Proxy, T3>, T3,
        T4Proxy extends ProxyEntity<T4Proxy, T4>, T4,
        T5Proxy extends ProxyEntity<T5Proxy, T5>, T5,
        T6Proxy extends ProxyEntity<T6Proxy, T6>, T6,
        T7Proxy extends ProxyEntity<T7Proxy, T7>, T7,
        T8Proxy extends ProxyEntity<T8Proxy, T8>, T8,
        T9Proxy extends ProxyEntity<T9Proxy, T9>, T9,
        T10Proxy extends ProxyEntity<T10Proxy, T10>, T10>
        extends OverrideProxyQueryable10<T1Proxy,T1,T2Proxy, T2,T3Proxy, T3,T4Proxy, T4,T5Proxy, T5,T6Proxy, T6,T7Proxy, T7,T8Proxy, T8,T9Proxy, T9,T10Proxy, T10>,
        ProxyFilterable10<T1Proxy,T1,T2Proxy, T2,T3Proxy, T3,T4Proxy, T4,T5Proxy, T5,T6Proxy, T6,T7Proxy, T7,T8Proxy, T8,T9Proxy, T9,T10Proxy, T10>,
        ProxySelectable10<T1Proxy,T1,T2Proxy, T2,T3Proxy, T3,T4Proxy, T4,T5Proxy, T5,T6Proxy, T6,T7Proxy, T7,T8Proxy, T8,T9Proxy, T9,T10Proxy, T10>,
        ProxyAggregatable10<T1Proxy,T1,T2Proxy, T2,T3Proxy, T3,T4Proxy, T4,T5Proxy, T5,T6Proxy, T6,T7Proxy, T7,T8Proxy, T8,T9Proxy, T9,T10Proxy, T10>,
        ProxyGroupable10<T1Proxy,T1,T2Proxy, T2,T3Proxy, T3,T4Proxy, T4,T5Proxy, T5,T6Proxy, T6,T7Proxy, T7,T8Proxy, T8,T9Proxy, T9,T10Proxy, T10>,
        ProxyHavingable10<T1Proxy,T1,T2Proxy, T2,T3Proxy, T3,T4Proxy, T4,T5Proxy, T5,T6Proxy, T6,T7Proxy, T7,T8Proxy, T8,T9Proxy, T9,T10Proxy, T10>,
        ProxyOrderable10<T1Proxy,T1,T2Proxy, T2,T3Proxy, T3,T4Proxy, T4,T5Proxy, T5,T6Proxy, T6,T7Proxy, T7,T8Proxy, T8,T9Proxy, T9,T10Proxy, T10> {
}
