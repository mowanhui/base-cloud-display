package com.yamo.cdcommoncore.utils;

import cn.hutool.core.util.StrUtil;
import com.yamo.cdcommoncore.domain.vo.TreeVO;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mowanhui
 * @version 1.0
 * @date 2023/6/6 16:20
 */
@UtilityClass
public class TreeUtils {
    /**
     * 转换为树
     *
     * @param
     * @return
     */
    public static <T extends TreeVO<T>> List<T> toTree(List<T> list) {
        if (list == null || list.size() == 0) {
            return new ArrayList<>();
        }
        List<T> result = new ArrayList<>();
        for (T sub : list) {
            if (StrUtil.isBlank(sub.getTreeParentId())) {
                sort(result,sub);
            } else {
                boolean isHeader = true;
                for (T sub2 : list) {
                    if (sub.getTreeParentId().equals(sub2.getTreeId())) {
                        isHeader = false;
                        break;
                    }
                }
                if (isHeader) {
                    result.add(sub);
                }
            }
            for (T sub2 : list) {
                if (StrUtil.isBlank(sub2.getTreeParentId())) {
                    continue;
                }
                if (sub2.getTreeParentId().equals(sub.getTreeId())) {
                    if (sub.getChildren() == null) {
                        sub.setChildren(new ArrayList<>());
                    }
                    sort(sub.getChildren(), sub2);
                }
            }
        }
        return result;
    }

    public static <T extends TreeVO<T>> void sort(List<T> list, T sub) {
        if (list.size() == 0) {
            list.add(sub);
            return;
        }
        boolean isAdded = false;
        for (int i = 0; i < list.size(); i++) {
            if (sub.getTreeSort() <= list.get(i).getTreeSort()) {
                list.add(i, sub);
                isAdded = true;
                break;
            }
        }
        if (!isAdded) {
            list.add(sub);
        }
    }
}
