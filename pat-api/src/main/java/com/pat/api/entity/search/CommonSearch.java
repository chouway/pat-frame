package com.pat.api.entity.search;

import lombok.Data;
import lombok.experimental.Accessors;
import org.beetl.sql.core.query.LambdaQuery;

import java.io.Serializable;
import java.util.List;
import java.util.function.Function;

/**
 * CommonSearch
 *
 * @author chouway
 * @date 2021.05.19
 */
@Data
@Accessors(chain = true)
public class CommonSearch<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private long pageNumber = 1;

    private long pageSize = 32;
    /**
     * 查询结果列
     */
    private LambdaQuery.Property<T,?>[] columns;


    /**
     * 查询条件
     */
    private T  entity;

    public CommonSearch<T> setColumns(LambdaQuery.Property<T,?>... columns){
        this.columns = columns;
        return this;
    }
}
