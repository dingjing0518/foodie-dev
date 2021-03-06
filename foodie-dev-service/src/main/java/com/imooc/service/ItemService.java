package com.imooc.service;

import com.imooc.pojo.Items;
import com.imooc.pojo.ItemsImg;
import com.imooc.pojo.ItemsParam;
import com.imooc.pojo.ItemsSpec;
import com.imooc.pojo.vo.CommentsLevelCountsVO;
import com.imooc.pojo.vo.ShopCartVO;
import com.imooc.utils.PagedGridResult;

import java.util.List;

public interface ItemService {

    /**
     * 根据商品ID查询详情
     *
     * @param itemId 商品ID
     * @return 商品实体
     */
    Items queryItemById(String itemId);

    /**
     * 根据商品ID查询商品图片列表
     *
     * @param itemId 商品ID
     * @return 图片列表
     */
    List<ItemsImg> queryItemImgList(String itemId);

    /**
     * 根据商品ID查询商品规格
     *
     * @param itemId 商品ID
     * @return 商品规格列表
     */
    List<ItemsSpec> queryItemsSpecList(String itemId);

    /**
     * 根据商品ID查询商品参数
     *
     * @param itemId 商品ID
     * @return 商品参数
     */
    ItemsParam queryItemsParam(String itemId);

    /**
     * 根据商品id查询商品的评价等级数量
     *
     * @param itemId 商品ID
     * @return 评价等级数量
     */
    CommentsLevelCountsVO queryCommentsCounts(String itemId);

    /**
     * 根据商品id查询商品的评价(分页)
     *
     * @param itemId   商品ID
     * @param level    评价等级
     * @param page     当前页
     * @param pageSize 分页数量
     * @return 商品的评价
     */
    PagedGridResult queryPagedComments(String itemId, Integer level, Integer page, Integer pageSize);

    /**
     * 商品搜索列表
     *
     * @param keywords 关键词
     * @param sort     排序
     * @param page     当前页
     * @param pageSize 分页数量
     * @return 商品列表
     */
    PagedGridResult searchItems(String keywords, String sort, Integer page, Integer pageSize);

    /**
     * 根据分类Id搜索商品列表
     *
     * @param catId    三级分类Id
     * @param sort     排序
     * @param page     当前页
     * @param pageSize 分页数量
     * @return 商品列表
     */
    PagedGridResult searchItems(Integer catId, String sort, Integer page, Integer pageSize);

    /**
     * 根据规格ids查询最新的购物车重的商品数据
     *
     * @param specIds ids
     * @return shopCartVo
     */
    List<ShopCartVO> queryItemsBySpecIds(String specIds);

    /**
     * 根据规格id获取商品数据
     *
     * @param specId 规格id
     * @return 商品数据
     */
    ItemsSpec queryItemsBySpecId(String specId);

    /**
     * 根据商品id获取商品主图url
     *
     * @param itemId 商品id
     * @return 商品主图url
     */
    String queryItemMainImgById(String itemId);

    /**
     * 扣减商品规格的库存
     *
     * @param specId    规格Id
     * @param buyCounts 购买数量
     */
    void decreaseItemSpecStock(String specId, int buyCounts);
}
