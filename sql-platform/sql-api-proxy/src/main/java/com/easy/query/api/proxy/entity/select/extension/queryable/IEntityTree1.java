package com.easy.query.api.proxy.entity.select.extension.queryable;

import com.easy.query.api.proxy.entity.select.EntityQueryable;
import com.easy.query.core.expression.lambda.SQLExpression1;
import com.easy.query.core.expression.lambda.SQLFuncExpression1;
import com.easy.query.core.expression.parser.core.base.tree.TreeCTEConfigurer;
import com.easy.query.core.proxy.ProxyEntity;
import com.easy.query.core.proxy.SQLColumn;

/**
 * create time 2024/1/13 21:06
 * 文件说明
 *
 * @author xuejiaming
 */
public interface IEntityTree1<T1Proxy extends ProxyEntity<T1Proxy, T1>, T1> {

    default EntityQueryable<T1Proxy, T1> asTreeCTE() {
        return asTreeCTE(o -> {
        });
    }

    EntityQueryable<T1Proxy, T1> asTreeCTE(SQLExpression1<TreeCTEConfigurer> treeExpression);
}
