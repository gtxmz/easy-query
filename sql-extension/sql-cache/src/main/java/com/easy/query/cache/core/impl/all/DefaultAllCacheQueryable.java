package com.easy.query.cache.core.impl.all;

import com.easy.query.cache.core.CacheAllEntity;
import com.easy.query.cache.core.EasyCacheIndex;
import com.easy.query.cache.core.EasyCacheStorageOption;
import com.easy.query.cache.core.Pair;
import com.easy.query.cache.core.base.CachePredicate;
import com.easy.query.cache.core.impl.AbstractSingleCacheQueryable;
import com.easy.query.cache.core.queryable.AllCacheQueryable;
import com.easy.query.core.api.pagination.DefaultPageResult;
import com.easy.query.core.api.pagination.EasyPageResult;
import com.easy.query.core.expression.lambda.SQLExpression1;
import com.easy.query.core.expression.parser.core.base.ColumnAsSelector;
import com.easy.query.core.util.EasyCollectionUtil;
import com.easy.query.core.util.EasyStringUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * create time 2023/5/16 12:00
 * 文件说明
 *
 * @author xuejiaming
 */
public class DefaultAllCacheQueryable<TEntity extends CacheAllEntity> extends AbstractSingleCacheQueryable<TEntity> implements AllCacheQueryable<TEntity> {
    public DefaultAllCacheQueryable(EasyCacheStorageOption easyCacheStorageOption, Class<TEntity> entityClass) {
        super(easyCacheStorageOption, entityClass);
    }

    @Override
    public List<TEntity> getIn(Collection<String> ids) {
        if (EasyCollectionUtil.isEmpty(ids)) {
            return new ArrayList<>(0);
        }
        List<Pair<String, TEntity>> caches = doGet(ids);

        Set<String> idSet = new HashSet<>(ids);
        Stream<TEntity> select = caches.stream().filter(o -> idSet.contains(o.getObject1()) && o.getObject2() != null)
                .map(o -> o.getObject2());
        return filterResult(select).collect(Collectors.toList());
    }


    @Override
    public boolean any(String id) {
        Set<String> indexs = doGetIndex();
        if (indexs.isEmpty()) {
            return false;
        }
        if (hasFilter()) {
            List<TEntity> in = getIn(indexs);
            return EasyCollectionUtil.isNotEmpty(in);
        }
        return true;
    }

    @Override
    public List<TEntity> getAll() {
        List<Pair<String, TEntity>> caches = doGet(Collections.emptyList());
        Stream<TEntity> select = caches.stream().filter(o -> o.getObject2() != null).map(o -> o.getObject2());
        return filterResult(select).collect(Collectors.toList());
    }

    @Override
    public List<String> getAllIndex() {
        Set<String> indexs = doGetIndex();
        return new ArrayList<>(indexs);
    }

    @Override
    public int count() {
        Set<String> indexs = doGetIndex();
        return indexs.size();
    }

    @Override
    public EasyPageResult<TEntity> getPage(int pageIndex, int pageSize) {
        Set<String> indexs = doGetIndex();
        if (indexs.isEmpty()) {
            return new DefaultPageResult<>(0,new ArrayList<>(0));
        }
        int take = pageSize <= 0 ? 1 : pageSize;
        int index = pageIndex <= 0 ? 1 : pageIndex;
        int skip = (index - 1) * take;
        List<Pair<String, TEntity>> caches = getCacheByIds(indexs);
        Stream<TEntity> select = caches.stream().filter(o -> o.getObject2() != null)
                .map(o -> o.getObject2());
        List<TEntity> tCacheItems = filterResult(select).skip(skip).limit(take).collect(Collectors.toList());
        return new DefaultPageResult<>(indexs.size(),tCacheItems);
    }

    protected List<Pair<String, TEntity>> doGet(Collection<String> ids) {
        Set<String> indexs = doGetIndex();
        if (indexs.isEmpty()) {
            return Collections.emptyList();
        }
        Set<String> findIds = filterIdWhichIsQuery(indexs, ids);
        if (findIds.size() > 0) {
            return getCacheByIds(findIds);
        }
        return Collections.emptyList();

    }

    private Set<String> filterIdWhichIsQuery(Set<String> indexs, Collection<String> ids) {
        if (EasyCollectionUtil.isEmpty(ids)) {
            return indexs;
        }
        return ids.stream().filter(o-> EasyStringUtil.isNotBlank(o)&&indexs.contains(o)).collect(Collectors.toSet());
    }

    protected Set<String> doGetIndex() {
        Set<String> fields = new HashSet<>();
        fields.add("INDEX");
        List<Pair<String, EasyCacheIndex>> cache = easyRedisManager.cache(EasyCacheIndex.class, getEntityKey(), fields, easyCacheOption.getTimeoutMillisSeconds(), easyCacheOption.getValueNullTimeoutMillisSeconds(), ids -> {
            return getIndex(getCacheAllIndex());
        });
        EasyCacheIndex v = cache.get(0).getObject2();
        return (v == null || v.getIndex() == null) ? new HashSet<>() : v.getIndex();
    }

    private List<Pair<String, TEntity>> getCacheByIds(Set<String> ids) {
        return easyRedisManager.cache(entityClass, getEntityKey(), ids, easyCacheOption.getTimeoutMillisSeconds(), easyCacheOption.getValueNullTimeoutMillisSeconds(), otherIds -> {
            return toKeyAndEntity(getEntities(otherIds));
        });
    }

    /**
     * 获取全部索引 mapper.where(o->o.eq(tenantID))
     *
     * @return
     */
    protected List<String> getCacheAllIndex() {
        return getCacheAllIndex0();
    }

    protected List<String> getCacheAllIndex0() {
//        return new LinkedList<>();
        SQLExpression1<ColumnAsSelector<TEntity, String>> idProperty = x -> x.column(getIdProperty());
        return easyQueryClient.queryable(entityClass)
                .asNoTracking()
                .select(String.class, idProperty)
                .toList();
    }

    private List<Pair<String, EasyCacheIndex>> getIndex(Collection<String> indexs) {
        List<Pair<String, EasyCacheIndex>> ret = new ArrayList<>(1);
        EasyCacheIndex easyCacheIndex = new EasyCacheIndex();
        easyCacheIndex.setIndex(new HashSet<>(indexs));
        ret.add(new Pair<>(easyCacheOption.getCacheIndex(), easyCacheIndex));
        return ret;
    }


    protected List<Pair<String, TEntity>> toKeyAndEntity(List<TEntity> entities) {
        return entities.stream().map(this::getKeyAndEntity).collect(Collectors.toList());
    }

    @Override
    public AllCacheQueryable<TEntity> filter(boolean condition, CachePredicate<TEntity> predicate) {
        if (condition) {
            addFilter(predicate);
        }
        return this;
    }
}
