package com.easy.query.core.sharding.route.table;

/**
 * create time 2023/4/25 14:51
 * 文件说明
 *
 * @author xuejiaming
 */
public class BaseTableRouteUnit implements TableRouteUnit{
    private final String dataSource;
    private final String logicTableName;
    private final String actualTableName;
    private final Class<?> entityClass;
    private final int tableIndex;

    public BaseTableRouteUnit(String dataSource, String logicTableName, String actualTableName, Class<?> entityClass,int tableIndex){

        this.dataSource = dataSource;
        this.logicTableName = logicTableName;
        this.actualTableName = actualTableName;
        this.entityClass = entityClass;
        this.tableIndex = tableIndex;
    }

    public String getDataSourceName() {
        return dataSource;
    }

    public String getLogicTableName() {
        return logicTableName;
    }

    public String getActualTableName() {
        return actualTableName;
    }

    public Class<?> getEntityClass() {
        return entityClass;
    }
    public int getTableIndex(){
        return tableIndex;
    }
}
