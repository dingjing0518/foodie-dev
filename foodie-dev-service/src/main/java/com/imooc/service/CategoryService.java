package com.imooc.service;

import com.imooc.pojo.Category;
import com.imooc.pojo.vo.CategoryVO;

import java.util.List;

public interface CategoryService {
    /**
     * 查询所有一级分类列表
     * @param type 类别
     * @return 一级分类列表
     */
    public List<Category> queryAllRootLevelCat(Integer type);

    /**
     * 根据一级分类id查询子类信息
     * @param rootCatId
     * @return
     */
    public List<CategoryVO> getSubCatList(Integer rootCatId);
}
