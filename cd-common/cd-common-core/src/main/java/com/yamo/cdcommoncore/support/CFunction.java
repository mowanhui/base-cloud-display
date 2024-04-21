package com.yamo.cdcommoncore.support;

import java.io.Serializable;
import java.util.function.Function;

/**
 * @author mowanhui
 * @version 1.0
 * @date 2023/8/23 17:15
 */
public interface CFunction <T, R> extends Function<T, R>, Serializable {
}
