package com.easy.query.core.expression.sql.builder.internal;

import com.easy.query.core.abstraction.EasyQueryRuntimeContext;
import com.easy.query.core.enums.EasyBehaviorEnum;
import com.easy.query.core.expression.sql.builder.EntityExpressionBuilder;
import com.easy.query.core.expression.sql.builder.EntityTableExpressionBuilder;
import com.easy.query.core.expression.sql.builder.ExpressionContext;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 文件说明
 * @Date: 2023/3/6 08:58
 * @author xuejiaming
 */
public abstract class AbstractEntityExpressionBuilder implements EntityExpressionBuilder {
    protected final ExpressionContext sqlExpressionContext;
    protected final List<EntityTableExpressionBuilder> tables;

    public AbstractEntityExpressionBuilder(ExpressionContext sqlExpressionContext){
        this.sqlExpressionContext = sqlExpressionContext;
        this.tables = new ArrayList<>();
    }

    @Override
    public ExpressionContext getExpressionContext() {
        return sqlExpressionContext;
    }

    @Override
    public EasyQueryRuntimeContext getRuntimeContext() {
        return sqlExpressionContext.getRuntimeContext();
    }

    @Override
    public void addSQLEntityTableExpression(EntityTableExpressionBuilder tableExpression) {
        tables.add(tableExpression);
    }

    @Override
    public List<EntityTableExpressionBuilder> getTables() {
        return tables;
    }
    @Override
    public void setLogicDelete(boolean logicDelete) {
        if(logicDelete){
            sqlExpressionContext.getBehavior().addBehavior(EasyBehaviorEnum.LOGIC_DELETE);
        }else{
            sqlExpressionContext.getBehavior().removeBehavior(EasyBehaviorEnum.LOGIC_DELETE);
        }
    }
}
