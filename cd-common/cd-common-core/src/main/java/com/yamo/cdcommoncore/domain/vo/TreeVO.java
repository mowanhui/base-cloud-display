package com.yamo.cdcommoncore.domain.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author mowanhui
 * @version 1.0
 * @date 2023/6/12 16:04
 */
@Data
public class TreeVO<T> implements Serializable {
    @JsonIgnore
    private String treeParentId;
    @JsonIgnore
    private String treeId;
    @JsonIgnore
    private Long treeSort;
    private List<T> children;
}
