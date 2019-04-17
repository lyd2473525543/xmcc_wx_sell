package com.xmcc.dao;

import java.util.List;

/**
 * 执行批量操作
 * @param <T>
 */
public interface BatchDao<T> {
    void batchInsert(List<T> list);
}
