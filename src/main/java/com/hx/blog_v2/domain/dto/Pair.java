package com.hx.blog_v2.domain.dto;

/**
 * Pair
 *
 * @author Jerry.X.He <970655147@qq.com>
 * @version 1.0
 * @date 6/18/2017 7:38 PM
 */
public class Pair<T1, T2> {

    private T1 left;
    private T2 right;

    public Pair(T1 left, T2 right) {
        this.left = left;
        this.right = right;
    }

    public Pair() {
    }

    public T1 getLeft() {
        return left;
    }

    public void setLeft(T1 left) {
        this.left = left;
    }

    public T2 getRight() {
        return right;
    }

    public void setRight(T2 right) {
        this.right = right;
    }
}
