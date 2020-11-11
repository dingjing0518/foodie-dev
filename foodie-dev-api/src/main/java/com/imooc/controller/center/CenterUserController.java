package com.imooc.controller.center;

import com.imooc.controller.BaseController;
import com.imooc.pojo.Users;
import com.imooc.pojo.bo.center.CenterUserBO;
import com.imooc.resource.FileUpload;
import com.imooc.service.center.CenterUserService;
import com.imooc.utils.CookieUtils;
import com.imooc.utils.DateUtil;
import com.imooc.utils.IMOOCJSONResult;
import com.imooc.utils.JsonUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(value = "用户信息接口", tags = {"用户信息相关接口"})
@RestController
@RequestMapping("userInfo")
public class CenterUserController extends BaseController {
    @Autowired
    private FileUpload fileUpload;
    @Autowired
    private CenterUserService centerUserService;

    @ApiOperation(value = "修改用户信息", notes = "修改用户信息", httpMethod = "POST")
    @PostMapping("/update")
    public IMOOCJSONResult userInfo(
            @ApiParam(name = "userId", value = "用户Id", required = true)
            @RequestParam String userId,
            @RequestBody @Valid CenterUserBO centerUserBO,
            BindingResult bindingResult,
            HttpServletRequest request,
            HttpServletResponse response) {
        //判断bindingResult是否保存错误的验证信息，如果有，则直接return
        if (bindingResult.hasErrors()) {
            Map<String, String> errorsMap = getErrors(bindingResult);
            return IMOOCJSONResult.errorMap(errorsMap);
        }
        Users result = centerUserService.updateInfo(userId, centerUserBO);
        result = setNullProperty(result);
        CookieUtils.setCookie(request, response, "user", JsonUtils.objectToJson(result), true);
        return IMOOCJSONResult.ok();
    }

    @ApiOperation(value = "用户头像修改", notes = "用户头像修改", httpMethod = "POST")
    @PostMapping("/uploadFace")
    public IMOOCJSONResult uploadFace(
            @ApiParam(name = "userId", value = "用户Id", required = true)
            @RequestParam String userId,
            @ApiParam(name = "file", value = "用户头像", required = true)
                    MultipartFile file,
            HttpServletRequest request,
            HttpServletResponse response) {
        //定义头像保存的地址
        String fileSpace = fileUpload.getImageUserFaceLocation();
        //在路径上为每一个用户增加一个userId，用于区分不同用户上传
        String uploadPathPrefix = File.separator + userId;
        //开始文件上传
        if (file != null) {
            FileOutputStream fileOutputStream = null;
            try {
                //获得文件上传的文件名称
                String fileName = file.getOriginalFilename();
                if (StringUtils.isNotBlank(fileName)) {
                    String[] fileNameArr = fileName.split("\\.");
                    String suffix = fileNameArr[fileNameArr.length - 1];
                    //校验文件格式------png,jpg,jpeg
                    if (!suffix.equalsIgnoreCase("png")
                            && !suffix.equalsIgnoreCase("jpg")
                            && !suffix.equalsIgnoreCase("jpeg")) {
                        return IMOOCJSONResult.errorMsg("图片格式不正确!");
                    }
                    //文件名重组 覆盖式上传，增量式：额外拼接当前时间
                    String newFileName = "face-" + userId + "." + suffix;
                    //上传头像的最终保存的位置
                    String finalFacePath = fileSpace + uploadPathPrefix + File.separator + newFileName;

                    uploadPathPrefix += ("/" + newFileName);
                    File outFile = new File(finalFacePath);
                    if (outFile.getParentFile() != null) {
                        //创建文件夹
                        outFile.getParentFile().mkdirs();
                    }
                    fileOutputStream = new FileOutputStream(outFile);
                    InputStream inputStream = file.getInputStream();
                    IOUtils.copy(inputStream, fileOutputStream);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fileOutputStream != null) {
                        fileOutputStream.flush();
                        fileOutputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            return IMOOCJSONResult.errorMsg("文件不能为空！");
        }
        String imageServerUrl = fileUpload.getImageServerUrl();
        String finalUserFaceUrl = imageServerUrl + uploadPathPrefix + "?t=" + DateUtil.getCurrentDateString(DateUtil.DATE_PATTERN);
        Users userResult = centerUserService.updateUserFace(userId, finalUserFaceUrl);

        userResult = setNullProperty(userResult);
        CookieUtils.setCookie(request, response, "user", JsonUtils.objectToJson(userResult), true);
        return IMOOCJSONResult.ok();
    }

    private Map<String, String> getErrors(BindingResult bindingResult) {
        Map<String, String> map = new HashMap<>();
        List<FieldError> list = bindingResult.getFieldErrors();
        for (FieldError fieldError : list) {
            //发生验证错误所对应的某一个属性
            String errorField = fieldError.getField();
            //验证错误的信息
            String errorDefaultMessage = fieldError.getDefaultMessage();
            map.put(errorField, errorDefaultMessage);
        }
        return map;
    }

    private Users setNullProperty(Users users) {
        users.setPassword(null);
        users.setMobile(null);
        users.setEmail(null);
        users.setCreatedTime(null);
        users.setUpdatedTime(null);
        users.setBirthday(null);
        return users;
    }
}
