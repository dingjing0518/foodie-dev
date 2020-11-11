package com.imooc.exception;

import com.imooc.utils.IMOOCJSONResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

/**
 * 文件上传异常处理类
 */
@RestControllerAdvice               //#1
public class CustomExceptionHandler {

    @ExceptionHandler(MaxUploadSizeExceededException.class)  //#2
    public IMOOCJSONResult handlerMaxUploadFile(MaxUploadSizeExceededException ex) {
        return IMOOCJSONResult.errorMsg("文件上传大小不能超过500k，请压缩图片或者降低图片质量再上传！");
    }

}
