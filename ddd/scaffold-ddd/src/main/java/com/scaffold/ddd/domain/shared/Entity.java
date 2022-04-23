package com.scaffold.ddd.domain.shared;

/**
 * @author hui.zhang
 * @date 2022年04月23日 09:13
 */
public interface Entity<T> {

    boolean sameIdentityAs(T other);
}
