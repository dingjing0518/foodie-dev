package com.imooc.service;

import com.imooc.pojo.Category;

import java.util.List;

public interface CategoryService {
    /**
     * 查询所有一级分类列表
     * @param type 类别
     * @return 一级分类列表
     */
    public List<Category> queryAllRootLevelCat(Integer type);
}
