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

    /**
     * 修改用户地址
     *
     * @param addressBO 用户地址BO
     */
    void updateUserAddress(AddressBO addressBO);

    /**
     * 删除用户地址
     *
     * @param userId    用户Id
     * @param addressId 地址Id
     */
    void deleteUserAddress(String userId, String addressId);

    /**
     * 修改默认地址
     *
     * @param userId    用户Id
     * @param addressId 地址Id
     */
    void updateUserAddressToBeDefault(String userId, String addressId);

    /**
     *
     * @param userId 用户Id
     * @param addressId 地址Id
     * @return 用户的地址
     */
    UserAddress queryUserAddress(String userId, String addressId);
}
