package com.imooc.service.center;

import com.imooc.pojo.Users;
import com.imooc.pojo.bo.center.CenterUserBO;

public interface CenterUserService {

    /**
     * 根据用户Id查询用户信息
     */
    Users queryUserInfo(String userId);

    /**
     * 修改用户信息
     */
    Users updateInfo(String userId, CenterUserBO centerUserBO);

    /**
     * 用户头像更新
     */
    Users updateUserFace(String userId, String faceUrl);
}
