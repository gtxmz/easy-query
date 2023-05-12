package com.easy.query.core.bootstrapper;

import com.easy.query.core.abstraction.DefaultEasyQueryRuntimeContext;
import com.easy.query.core.abstraction.EasyQueryRuntimeContext;
import com.easy.query.core.abstraction.EasySqlApiFactory;
import com.easy.query.core.api.client.DefaultEasyQuery;
import com.easy.query.core.api.client.EasyQuery;
import com.easy.query.core.api.def.DefaultEasySqlApiFactory;
import com.easy.query.core.basic.jdbc.con.DefaultConnectionManager;
import com.easy.query.core.basic.jdbc.con.DefaultEasyConnectionFactory;
import com.easy.query.core.basic.jdbc.con.DefaultEasyDataSourceConnectionFactory;
import com.easy.query.core.basic.jdbc.con.EasyConnectionFactory;
import com.easy.query.core.basic.jdbc.con.EasyConnectionManager;
import com.easy.query.core.basic.jdbc.con.EasyDataSourceConnectionFactory;
import com.easy.query.core.basic.jdbc.executor.DefaultEntityExpressionExecutor;
import com.easy.query.core.basic.jdbc.executor.EntityExpressionExecutor;
import com.easy.query.core.basic.jdbc.types.EasyJdbcTypeHandlerManager;
import com.easy.query.core.basic.jdbc.types.JdbcTypeHandlerManager;
import com.easy.query.core.basic.pagination.DefaultEasyPageResultProvider;
import com.easy.query.core.basic.pagination.EasyPageResultProvider;
import com.easy.query.core.basic.plugin.track.DefaultTrackManager;
import com.easy.query.core.basic.plugin.track.TrackManager;
import com.easy.query.core.basic.thread.DefaultEasyShardingExecutorService;
import com.easy.query.core.basic.thread.EasyShardingExecutorService;
import com.easy.query.core.configuration.EasyQueryOptionBuilder;
import com.easy.query.core.inject.ServiceCollection;
import com.easy.query.core.inject.ServiceProvider;
import com.easy.query.core.inject.impl.ServiceCollectionImpl;
import com.easy.query.core.configuration.dialect.Dialect;
import com.easy.query.core.configuration.nameconversion.NameConversion;
import com.easy.query.core.configuration.dialect.DefaultDialect;
import com.easy.query.core.configuration.nameconversion.impl.UnderlinedNameConversion;
import com.easy.query.core.configuration.EasyQueryConfiguration;
import com.easy.query.core.configuration.EasyQueryOption;
import com.easy.query.core.expression.executor.parser.DefaultEasyPrepareParser;
import com.easy.query.core.expression.executor.parser.EasyPrepareParser;
import com.easy.query.core.expression.executor.query.DefaultExecutionContextFactory;
import com.easy.query.core.expression.executor.query.ExecutionContextFactory;
import com.easy.query.core.expression.parser.factory.DefaultEasyQueryLambdaFactory;
import com.easy.query.core.expression.parser.factory.EasyQueryLambdaFactory;
import com.easy.query.core.expression.sql.builder.factory.DefaultEasyExpressionBuilderFactory;
import com.easy.query.core.expression.sql.builder.factory.EasyExpressionBuilderFactory;
import com.easy.query.core.expression.sql.expression.factory.DefaultEasyExpressionFactory;
import com.easy.query.core.expression.sql.expression.factory.EasyExpressionFactory;
import com.easy.query.core.metadata.DefaultEntityMetadataManager;
import com.easy.query.core.metadata.EntityMetadataManager;
import com.easy.query.core.sharding.DefaultEasyQueryDataSource;
import com.easy.query.core.sharding.EasyQueryDataSource;
import com.easy.query.core.sharding.comparer.JavaLanguageShardingComparer;
import com.easy.query.core.sharding.comparer.ShardingComparer;
import com.easy.query.core.sharding.rewrite.DefaultRewriteContextFactory;
import com.easy.query.core.sharding.rewrite.RewriteContextFactory;
import com.easy.query.core.sharding.route.DefaultRouteContextFactory;
import com.easy.query.core.sharding.route.RouteContextFactory;
import com.easy.query.core.sharding.route.manager.DataSourceRouteManager;
import com.easy.query.core.sharding.route.manager.impl.DefaultDataSourceRouteManager;
import com.easy.query.core.sharding.route.manager.impl.DefaultTableRouteManager;
import com.easy.query.core.sharding.route.manager.TableRouteManager;
import com.easy.query.core.sharding.route.datasource.engine.DataSourceRouteEngine;
import com.easy.query.core.sharding.route.datasource.engine.DefaultDataSourceRouteEngine;
import com.easy.query.core.sharding.route.table.engine.DefaultTableRouteEngine;
import com.easy.query.core.sharding.route.table.engine.TableRouteEngine;

import javax.sql.DataSource;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * create time 2023/4/26 20:31
 * 文件说明
 *
 * @author xuejiaming
 */
public class EasyQueryBuilderConfiguration {
    protected DataSource dataSource;
    protected final EasyQueryOptionBuilder easyQueryOptionBuilder=new EasyQueryOptionBuilder();
    private final ServiceCollection serviceCollection = new ServiceCollectionImpl();

    public EasyQueryBuilderConfiguration() {
        defaultConfiguration();
    }

    private void defaultConfiguration() {
    replaceServiceFactory(EasyQueryDataSource.class, sp -> new DefaultEasyQueryDataSource("ds0", sp.getService(DataSource.class)))
                .replaceService(Dialect.class, DefaultDialect.class)
                .replaceService(NameConversion.class, UnderlinedNameConversion.class)
                .replaceService(EasyQueryConfiguration.class)
                .replaceService(EntityMetadataManager.class, DefaultEntityMetadataManager.class)
                .replaceService(EasyQueryLambdaFactory.class, DefaultEasyQueryLambdaFactory.class)
                .replaceService(EasyExpressionBuilderFactory.class, DefaultEasyExpressionBuilderFactory.class)
                .replaceService(EasySqlApiFactory.class, DefaultEasySqlApiFactory.class)
                .replaceService(TrackManager.class, DefaultTrackManager.class)
                .replaceService(EasyPageResultProvider.class, DefaultEasyPageResultProvider.class)
                .replaceService(EasyPrepareParser.class, DefaultEasyPrepareParser.class)
                .replaceService(EasyConnectionManager.class, DefaultConnectionManager.class)
                .replaceService(DataSourceRouteManager.class, DefaultDataSourceRouteManager.class)
                .replaceService(DataSourceRouteEngine.class, DefaultDataSourceRouteEngine.class)
                .replaceService(TableRouteManager.class, DefaultTableRouteManager.class)
                .replaceService(TableRouteEngine.class, DefaultTableRouteEngine.class)
                .replaceService(RouteContextFactory.class, DefaultRouteContextFactory.class)
                .replaceService(RewriteContextFactory.class, DefaultRewriteContextFactory.class)
                .replaceService(ExecutionContextFactory.class, DefaultExecutionContextFactory.class)
                .replaceService(EntityExpressionExecutor.class, DefaultEntityExpressionExecutor.class)
                .replaceService(EasyShardingExecutorService.class, DefaultEasyShardingExecutorService.class)
                .replaceService(EasyExpressionFactory.class, DefaultEasyExpressionFactory.class)
                .replaceService(ShardingComparer.class, JavaLanguageShardingComparer.class)
                .replaceService(JdbcTypeHandlerManager.class, EasyJdbcTypeHandlerManager.class)
                .replaceService(EasyQueryRuntimeContext.class, DefaultEasyQueryRuntimeContext.class)
                .replaceService(EasyDataSourceConnectionFactory.class, DefaultEasyDataSourceConnectionFactory.class)
                .replaceService(EasyConnectionFactory.class, DefaultEasyConnectionFactory.class)
                .replaceService(EasyQuery.class, DefaultEasyQuery.class);
    }

    public EasyQueryBuilderConfiguration setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        return this;
    }

    /**
     * 添加服务如果已经存在则替换
     * @param implementType 依赖注入当前实例类型
     * @return 当前服务集合
     * @param <TImplement> 实现类型
     */
    public <TImplement> EasyQueryBuilderConfiguration replaceService(Class<TImplement> implementType) {
        serviceCollection.addService(implementType);
        return this;
    }

    /**
     * 添加服务如果已经存在则替换
     * @param implementInstance 依赖注入当前实例
     * @return 当前服务集合
     * @param <TImplement> 实现类型
     */
    public <TImplement> EasyQueryBuilderConfiguration replaceService(TImplement implementInstance) {
        serviceCollection.addService(implementInstance);
        return this;
    }

    /**
     * 添加服务如果已经存在则替换
     * @param serviceType 依赖注入的接口
     * @param implementType 依赖注入的实现类
     * @return 当前服务集合
     * @param <TService> 接口类型
     * @param <TImplement> 实现类型
     */
    public <TService, TImplement extends TService> EasyQueryBuilderConfiguration replaceService(Class<TService> serviceType, Class<TImplement> implementType) {
        serviceCollection.addService(serviceType, implementType);
        return this;
    }

    /**
     * 添加服务如果已经存在则替换
     * @param serviceType 依赖注入的接口
     * @param implementInstance 依赖注入的实现
     * @return 当前服务集合
     * @param <TService> 接口类型
     * @param <TImplement> 实现类型
     */
    public <TService, TImplement extends TService> EasyQueryBuilderConfiguration replaceService(Class<TService> serviceType, TImplement implementInstance) {
        serviceCollection.addService(serviceType, implementInstance);
        return this;
    }

    /**
     * 添加服务如果已经存在则替换
     * @param serviceType 依赖注入的接口
     * @param implementFactory 依赖注入的实现工厂
     * @return 当前服务集合
     * @param <TService> 接口类型
     * @param <TImplement> 实现类型
     */
    public <TService, TImplement extends TService> EasyQueryBuilderConfiguration replaceServiceFactory(Class<TService> serviceType, Function<ServiceProvider, TImplement> implementFactory) {
        serviceCollection.addServiceFactory(serviceType, implementFactory);
        return this;
    }

    public EasyQueryBuilderConfiguration useDatabaseConfigure(DatabaseConfiguration databaseConfiguration){
        databaseConfiguration.configure(serviceCollection);
        return this;
    }
    public EasyQueryBuilderConfiguration optionConfigure(Consumer<EasyQueryOptionBuilder> configure){
        configure.accept(this.easyQueryOptionBuilder);
        return this;
    }

    /**
     * 创建对应的查询器
     * @return EasyQuery实例
     */
    public EasyQuery build() {
        if (this.dataSource == null) {
            throw new IllegalArgumentException("data source null");
        }
        replaceService(DataSource.class, this.dataSource);
        EasyQueryOption easyQueryOption = easyQueryOptionBuilder.build();
        replaceService(easyQueryOption);
        ServiceProvider serviceProvider = serviceCollection.build();
        return serviceProvider.getService(EasyQuery.class);
    }

}
