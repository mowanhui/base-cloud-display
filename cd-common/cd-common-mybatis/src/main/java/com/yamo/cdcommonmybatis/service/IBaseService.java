package com.yamo.cdcommonmybatis.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yamo.cdcommoncore.exception.BizException;

/**
 * @author mowanhui
 * @version 1.0
 * @date 2023/8/22 10:42
 */
public interface IBaseService<T> extends IService<T> {
    /**
     * 根据记录Id获取记录信息
     *
     * @param name  id字段名称
     * @param value id字段值
     * @return
     */
    default T getByName(String name, String value) {
        this.notExistByName(name, value);
        QueryWrapper<T> wrapper = new QueryWrapper<>();
        wrapper.eq(name, value)
                .eq("is_deleted", "0");
        return this.getOne(wrapper);
    }

    default boolean isExistByName(String name, String value) {
        QueryWrapper<T> wrapper = new QueryWrapper<>();
        wrapper.eq(name, value)
                .eq("is_deleted", "0");
        int num = this.count(wrapper);
        return num > 0;
    }

    /**
     * 校验存在记录
     *
     * @param name
     * @param value
     */
    default void existByName(String name, String value) {
        existByName(name, value, "记录已存在！");
    }

    /**
     * 校验存在记录
     *
     * @param name
     * @param value
     */
    default void existByName(String name, String value, String errorMsg) {
        if (this.isExistByName(name, value)) {
            throw new BizException(errorMsg);
        }
    }

    /**
     * 校验不存在记录
     *
     * @param name
     * @param value
     */
    default void notExistByName(String name, String value) {
        notExistByName(name, value, "记录不存在！");
    }

    /**
     * 校验不存在记录
     *
     * @param name
     * @param value
     */
    default void notExistByName(String name, String value, String errorMsg) {
        if (!this.isExistByName(name, value)) {
            throw new BizException(errorMsg);
        }
    }
}
