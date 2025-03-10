package com.easy.query.core.func.def.impl;

import com.easy.query.core.expression.parser.core.available.TableAvailable;
import com.easy.query.core.func.column.ColumnExpression;
import com.easy.query.core.func.def.AbstractExpressionSQLFunction;
import com.easy.query.core.util.EasyClassUtil;

import java.util.List;

/**
 * create time 2023/12/21 11:58
 * 文件说明
 *
 * @author xuejiaming
 */
public class CastSQLFunction extends AbstractExpressionSQLFunction {
    private final List<ColumnExpression> columnExpressions;
    private final Class<?> targetClass;

    public CastSQLFunction(List<ColumnExpression> columnExpressions, Class<?> targetClass) {

        this.columnExpressions = columnExpressions;
        this.targetClass = targetClass;
    }

    @Override
    public String sqlSegment(TableAvailable defaultTable) {
        String targetClassName = EasyClassUtil.getFullName(targetClass);
        switch (targetClassName){
            case "boolean":
            case "java.lang.Boolean": return " CAST({0} AS SIGNED)";
            case "char": return "SUBSTR(CAST({0} AS CHAR), 1, 1)";
            case "java.sql.Time":
            case "java.time.Time": return "CAST({0} AS TIME)";
            case "java.sql.Date":
            case "java.time.LocalDate": return "CAST({0} AS DATE)";
            case "java.sql.Timestamp":
            case "java.util.Date":
            case "java.time.LocalDateTime": return "CAST({0} AS DATETIME)";
            case "java.math.BigDecimal": return "CAST({0} AS DECIMAL(36,18))";
            case "double":
            case "float":
            case "java.lang.Float":
            case "java.lang.Double": return "CAST({0} AS DECIMAL(32,16))";
            case "byte":
            case "short":
            case "int":
            case "long":
            case "java.lang.Byte":
            case "java.lang.Short":
            case "java.lang.Integer":
            case "java.lang.Long": return "CAST({0} AS SIGNED)";
            case "java.util.UUID": return "SUBSTR(CAST({0} AS CHAR), 1, 36)";
            case "java.lang.String": return "CAST({0} AS CHAR)";
        }
        throw new UnsupportedOperationException("不支持当前转换函数:"+targetClassName);
    }

    @Override
    public int paramMarks() {
        return columnExpressions.size();
    }

    @Override
    protected List<ColumnExpression> getColumnExpressions() {
        return columnExpressions;
    }

}
