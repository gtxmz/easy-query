package com.easy.query.test.doc.entity.proxy;

import com.easy.query.core.proxy.AbstractProxyEntity;
import com.easy.query.core.proxy.SQLColumn;
import com.easy.query.core.proxy.SQLSelectAsExpression;
import com.easy.query.core.proxy.columns.SQLDateTimeColumn;
import com.easy.query.core.proxy.columns.SQLStringColumn;
import com.easy.query.core.proxy.fetcher.AbstractFetcher;
import com.easy.query.test.doc.entity.SysUser;

import java.time.LocalDateTime;

/**
 * this file automatically generated by easy-query, don't modify it
 * 当前文件是easy-query自动生成的请不要随意修改
 *
 * @author xuejiaming
 */
public class SysUserProxy extends AbstractProxyEntity < SysUserProxy, SysUser > {

    private static final Class < SysUser > entityClass = SysUser .class;

    public static SysUserProxy createTable () {
        return new SysUserProxy ();
    }

    public SysUserProxy () {
    }

    /**
     * {@link SysUser#getId}
     */
    public SQLStringColumn < SysUserProxy, java.lang.String> id(){
    return getStringColumn("id",String.class);
}

    /**
     * {@link SysUser#getName}
     */
    public SQLStringColumn< SysUserProxy, String> name(){
    return getStringColumn("name",String.class);
}

    /**
     * {@link SysUser#getAccount}
     */
    public SQLColumn < SysUserProxy, java.lang.String> account(){
    return get("account");
}

    /**
     * {@link SysUser#getDepartName}
     */
    public SQLColumn < SysUserProxy, java.lang.String> departName(){
    return get("departName");
}

    /**
     * {@link SysUser#getPhone}
     */
    public SQLColumn < SysUserProxy, java.lang.String> phone(){
    return get("phone");
}

    /**
     * {@link SysUser#getCreateTime}
     */
    public SQLDateTimeColumn< SysUserProxy, LocalDateTime> createTime(){
    return getDateTimeColumn("createTime",LocalDateTime.class);
}


    @Override
    public Class < SysUser > getEntityClass () {
        return entityClass;
    }


    /**
     * 数据库列的简单获取
     * @return
     */
    public SysUserProxyFetcher FETCHER = new SysUserProxyFetcher (this, null, SQLSelectAsExpression.empty);


    public static class SysUserProxyFetcher extends AbstractFetcher<SysUserProxy, SysUser, SysUserProxyFetcher> {

        public SysUserProxyFetcher (SysUserProxy proxy, SysUserProxyFetcher prev, SQLSelectAsExpression sqlSelectAsExpression) {
        super(proxy, prev, sqlSelectAsExpression);
    }


        /**
         * {@link SysUser#getId}
         */
        public SysUserProxyFetcher id() {
            return add(getProxy().id());
        }

        /**
         * {@link SysUser#getName}
         */
        public SysUserProxyFetcher name() {
            return add(getProxy().name());
        }

        /**
         * {@link SysUser#getAccount}
         */
        public SysUserProxyFetcher account() {
            return add(getProxy().account());
        }

        /**
         * {@link SysUser#getDepartName}
         */
        public SysUserProxyFetcher departName() {
            return add(getProxy().departName());
        }

        /**
         * {@link SysUser#getPhone}
         */
        public SysUserProxyFetcher phone() {
            return add(getProxy().phone());
        }

        /**
         * {@link SysUser#getCreateTime}
         */
        public SysUserProxyFetcher createTime() {
            return add(getProxy().createTime());
        }


        @Override
        protected SysUserProxyFetcher createFetcher(
            SysUserProxy cp,
            AbstractFetcher<SysUserProxy, SysUser, SysUserProxyFetcher> prev,
            SQLSelectAsExpression sqlSelectExpression
        ) {
            return new SysUserProxyFetcher (cp, this, sqlSelectExpression);
        }
    }

}
