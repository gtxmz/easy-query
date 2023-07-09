package com.easy.query.core.enums;

/**
 * create time 2023/3/25 09:01
 * 文件说明
 *
 * @author xuejiaming
 */
public enum EasyBehaviorEnum {
    /**
     * 使用逻辑删除
     */
    LOGIC_DELETE(1),
    /**
     * 使用拦截器
     */
    USE_INTERCEPTOR(1<<1),
    /**
     * 使用数据追踪查询
     */
    USE_TRACKING(1<<2),
    /**
     * 查询的时候忽略大列
     */
    QUERY_LARGE_COLUMN(1<<3),
    ON_DUPLICATE_KEY_IGNORE(1<<4),
    ON_DUPLICATE_KEY_UPDATE(1<<5),
    EXECUTE_BATCH(1<<6),
    EXECUTE_NO_BATCH(1<<7);

    private final int code;

    EasyBehaviorEnum(int code){

        this.code = code;
    }

    public int getCode() {
        return code;
    }

}
