package com.yamo.cdcommoncore.domain.pojo;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author mowanhui
 * @version 1.0
 * @date 2023/6/6 14:27
 */
@Data
public class SortSub implements Comparable<SortSub>{
    private String id;
    private Integer sort;

    @Override
    public int compareTo(@NotNull SortSub o) {
        return this.sort.compareTo(o.getSort());
    }
}
