package com.imooc.service;

import com.imooc.pojo.bo.UserBO;
import com.imooc.pojo.Users;

public interface UserService {
    /**
     * 判断用户名是否存在
     */
    public boolean queryUsernameIsExist(String username);

    /**
     * 创建用户
     */
    public Users createUser(UserBO userBO);


    /**
     * 用于登录
     */
    public Users queryUserForLogin(String username, String password);
}
