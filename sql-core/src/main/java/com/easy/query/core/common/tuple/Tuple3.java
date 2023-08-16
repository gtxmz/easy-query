package com.easy.query.core.common.tuple;

/**
 * create time 2023/7/2 21:37
 * 文件说明
 *
 * @author xuejiaming
 */
public class Tuple3<T, T1, T2> {
    private final T t;
    private final T1 t1;
    private final T2 t2;

    public Tuple3(T t, T1 t1, T2 t2){

        this.t = t;
        this.t1 = t1;
        this.t2 = t2;
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
}
