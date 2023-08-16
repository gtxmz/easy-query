package com.easy.query.core.common.tuple;

/**
 * create time 2023/8/15 21:03
 * 文件说明
 *
 * @author xuejiaming
 */
public class Tuple5<T, T1, T2, T3, T4> {
    private final T t;
    private final T1 t1;
    private final T2 t2;
    private final T3 t3;
    private final T4 t4;

    public Tuple5(T t, T1 t1, T2 t2, T3 t3, T4 t4) {
        this.t = t;
        this.t1 = t1;
        this.t2 = t2;
        this.t3 = t3;
        this.t4 = t4;
    }

    public T t() {
        return t;
    }

    public T1 t1() {
        return t1;
    }

    public T2 t2() {
        return t2;
    }

    public T3 t3() {
        return t3;
    }

    public T4 t4() {
        return t4;
    }
}
