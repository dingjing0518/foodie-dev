package com.imooc.service;

import com.imooc.pojo.UserAddress;
import com.imooc.pojo.bo.AddressBO;

import java.util.List;

public interface AddressService {
    /**
     * 根据用户Id查询用户的地址列表
     *
     * @param userId 用户Id
     * @return 用户的地址列表
     */
    List<UserAddress> queryAll(String userId);

    /**
     * 新增用户地址
     *
     * @param addressBO 用户地址BO
     */
    void addNewUserAddress(AddressBO addressBO);
}
