package com.easy.query.core.sharding.merge.result.impl;
import com.easy.query.core.exception.EasyQuerySQLException;
import com.easy.query.core.expression.segment.AggregationColumnSegment;
import com.easy.query.core.expression.segment.SqlSegment;
import com.easy.query.core.sharding.merge.context.StreamMergeContext;
import com.easy.query.core.sharding.merge.result.StreamResultSet;
import com.easy.query.core.sharding.merge.result.aggregation.AggregationUnitFactory;
import com.easy.query.core.sharding.merge.segment.PropertyGroup;
import com.easy.query.core.util.EasyCollectionUtil;
import com.easy.query.core.util.ClassUtil;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * create time 2023/5/2 23:45
 * 文件说明
 *
 * @author xuejiaming
 */
public final class EasyInMemoryGroupByOrderStreamMergeResultSet extends AbstractInMemoryStreamMergeResultSet {
    public EasyInMemoryGroupByOrderStreamMergeResultSet(StreamMergeContext streamMergeContext, List<StreamResultSet> streamResultSets) throws SQLException {
        super(streamMergeContext, streamResultSets);

    }

    @Override
    protected List<MemoryResultSetRow> init(StreamMergeContext streamMergeContext, List<StreamResultSet> streamResultSets) throws SQLException {
        Map<GroupValue, MemoryResultSetRow> dataMap = new LinkedHashMap<>(1024);
       Map<GroupValue, List<AggregateValue>> aggregationMap = new LinkedHashMap<>(1024);

        for (StreamResultSet resultSet : streamResultSets) {
            try(StreamResultSet streamResultSet=resultSet) {
                while(streamResultSet.next()){
                    GroupValue groupValue = new GroupValue(streamMergeContext, streamResultSet);
                    initForFirstGroupByValue(streamMergeContext,streamResultSet,groupValue,dataMap,aggregationMap);
                    aggregate(streamResultSet,groupValue,aggregationMap);
                }

            } catch (Exception e) {
                throw new SQLException(e);
            }
        }
        setAggregationValueToMemoryRow(streamMergeContext,dataMap,aggregationMap);

        List<Boolean> valueCaseSensitive = EasyCollectionUtil.isEmpty(streamResultSets) ? Collections.emptyList() : getValueCaseSensitive();
        return getMemoryResultSetRows(streamMergeContext,dataMap,valueCaseSensitive);

    }
    private List<MemoryResultSetRow> getMemoryResultSetRows(StreamMergeContext streamMergeContext,Map<GroupValue, MemoryResultSetRow> dataMap,List<Boolean> valueCaseSensitive){
        ArrayList<MemoryResultSetRow> resultSetRows = new ArrayList<>(dataMap.values());
        resultSetRows.sort(new GroupByRowComparator(streamMergeContext,valueCaseSensitive));
        return resultSetRows;
    }

    public List<Boolean> getValueCaseSensitive() throws SQLException {
        ArrayList<Boolean> result = new ArrayList<>(columnCount);
        for (int i = 0; i < columnCount; i++) {
            result.add(metaData.isCaseSensitive(i+1));
        }
        return result;
    }

    private void initForFirstGroupByValue(StreamMergeContext streamMergeContext,StreamResultSet streamResultSet,GroupValue groupValue,
                                          Map<GroupValue, MemoryResultSetRow> dataMap,Map<GroupValue, List<AggregateValue>> aggregationMap) throws SQLException {
        if(!dataMap.containsKey(groupValue)){
            dataMap.put(groupValue,new MemoryResultSetRow(streamResultSet,columnCount));
        }
        if(!aggregationMap.containsKey(groupValue)){
            List<AggregateValue> aggregationUnitValues = createAggregationUnitValues();
            aggregationMap.put(groupValue,aggregationUnitValues);
        }
    }
    private void aggregate(StreamResultSet streamResultSet,GroupValue groupValue,
                           Map<GroupValue, List<AggregateValue>> aggregationMap) throws SQLException {
        List<AggregateValue> aggregateValues = aggregationMap.get(groupValue);
        for (AggregateValue aggregateValue : aggregateValues) {
            Comparable<?> value = getAggregationValue(streamResultSet,aggregateValue.getColumnIndex());
            aggregateValue.getAggregationUnit().merge(Collections.singletonList(value));
        }
    }

    private Comparable<?> getAggregationValue(StreamResultSet streamResultSet,int columnIndex) throws SQLException {
        Object result = streamResultSet.getObject(columnIndex + 1);
        if (null == result || result instanceof Comparable) {
            return (Comparable<?>) result;
        }
        throw new EasyQuerySQLException("aggregation value must implements comparable");
    }
    private void setAggregationValueToMemoryRow(StreamMergeContext streamMergeContext,Map<GroupValue, MemoryResultSetRow> dataMap,Map<GroupValue, List<AggregateValue>> aggregationMap) throws SQLException {
        for (Map.Entry<GroupValue, MemoryResultSetRow> dataKv : dataMap.entrySet()) {
            List<AggregateValue> aggregateValues = aggregationMap.get(dataKv.getKey());
            for (AggregateValue aggregateValue : aggregateValues) {
                dataKv.getValue().setValue(aggregateValue.getColumnIndex()+1,aggregateValue.getAggregationUnit().getResult());
            }
        }
    }
    private List<AggregateValue> createAggregationUnitValues() {
        List<SqlSegment> sqlSegments = streamMergeContext.getSelectColumns().getSqlSegments();
        ArrayList<AggregateValue> aggregationUnits = new ArrayList<>(columnCount);
        for (SqlSegment sqlSegment : sqlSegments) {
        }
        for (int i = 0; i < sqlSegments.size(); i++) {
            SqlSegment sqlSegment = sqlSegments.get(i);
            if(sqlSegment instanceof AggregationColumnSegment){
                AggregationColumnSegment aggregationColumnSegment = (AggregationColumnSegment) sqlSegment;
                aggregationUnits.add(new AggregateValue(i, AggregationUnitFactory.create(aggregationColumnSegment.getAggregationType())));
            }
        }
        return aggregationUnits;
    }
}
