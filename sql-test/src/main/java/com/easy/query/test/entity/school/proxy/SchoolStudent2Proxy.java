package com.easy.query.test.entity.school.proxy;

import com.easy.query.core.expression.parser.core.available.TableAvailable;
import com.easy.query.core.proxy.AbstractProxyEntity;
import com.easy.query.core.proxy.SQLColumn;
import com.easy.query.core.proxy.SQLSelectAsExpression;
import com.easy.query.core.proxy.fetcher.AbstractFetcher;
import com.easy.query.core.proxy.core.EntitySQLContext;
import com.easy.query.test.entity.school.SchoolStudent2;
import com.easy.query.core.proxy.columns.types.SQLStringTypeColumn;
import com.easy.query.core.proxy.columns.SQLNavigateColumn;
import com.easy.query.core.proxy.columns.types.SQLAnyTypeColumn;

/**
 * this file automatically generated by easy-query, don't modify it
 * 当前文件是easy-query自动生成的请不要随意修改
 * 如果出现属性冲突请使用@ProxyProperty进行重命名
 *
 * @author easy-query
 */
public class SchoolStudent2Proxy extends AbstractProxyEntity<SchoolStudent2Proxy, SchoolStudent2> {

    private static final Class<SchoolStudent2> entityClass = SchoolStudent2.class;

    public static final SchoolStudent2Proxy TABLE = createTable().createEmpty();

    public static SchoolStudent2Proxy createTable() {
        return new SchoolStudent2Proxy();
    }

    public SchoolStudent2Proxy() {
    }

    /**
     * {@link SchoolStudent2#getId}
     */
    public SQLStringTypeColumn<SchoolStudent2Proxy> id() {
        return getStringTypeColumn("id");
    }

    /**
     * {@link SchoolStudent2#getClassId}
     */
    public SQLStringTypeColumn<SchoolStudent2Proxy> classId() {
        return getStringTypeColumn("classId");
    }

    /**
     * {@link SchoolStudent2#getName}
     */
    public SQLStringTypeColumn<SchoolStudent2Proxy> name() {
        return getStringTypeColumn("name");
    }

    /**
     * private Integer age;
     * {@link SchoolStudent2#getSchoolClass}
     */
    public com.easy.query.test.entity.school.proxy.SchoolClassProxy schoolClass() {
        return getNavigate("schoolClass", new com.easy.query.test.entity.school.proxy.SchoolClassProxy());
    }


    @Override
    public Class<SchoolStudent2> getEntityClass() {
        return entityClass;
    }


    /**
     * 数据库列的简单获取
     *
     * @return
     */
    public SchoolStudent2ProxyFetcher FETCHER = new SchoolStudent2ProxyFetcher(this, null, SQLSelectAsExpression.empty);


    public static class SchoolStudent2ProxyFetcher extends AbstractFetcher<SchoolStudent2Proxy, SchoolStudent2, SchoolStudent2ProxyFetcher> {

        public SchoolStudent2ProxyFetcher(SchoolStudent2Proxy proxy, SchoolStudent2ProxyFetcher prev, SQLSelectAsExpression sqlSelectAsExpression) {
            super(proxy, prev, sqlSelectAsExpression);
        }


        /**
         * {@link SchoolStudent2#getId}
         */
        public SchoolStudent2ProxyFetcher id() {
            return add(getProxy().id());
        }

        /**
         * {@link SchoolStudent2#getClassId}
         */
        public SchoolStudent2ProxyFetcher classId() {
            return add(getProxy().classId());
        }

        /**
         * {@link SchoolStudent2#getName}
         */
        public SchoolStudent2ProxyFetcher name() {
            return add(getProxy().name());
        }


        @Override
        protected SchoolStudent2ProxyFetcher createFetcher(SchoolStudent2Proxy cp, AbstractFetcher<SchoolStudent2Proxy, SchoolStudent2, SchoolStudent2ProxyFetcher> prev, SQLSelectAsExpression sqlSelectExpression) {
            return new SchoolStudent2ProxyFetcher(cp, this, sqlSelectExpression);
        }
    }

}
