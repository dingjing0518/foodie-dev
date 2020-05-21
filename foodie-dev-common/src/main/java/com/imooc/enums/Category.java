package com.imooc.enums;

/**
 * @Desc: 分类 枚举
 */
public enum Category {
    ROOT_LEVEL(1, "一级分类"),
    SECOND_LEVEL(2, "二级分类"),
    THREE_LEVEL(3, "三级分类");
    public final Integer type;
    public final String value;

    Category(Integer type, String value) {
        this.type = type;
        this.value = value;
    }
}
