package com.imooc.pojo.bo.center;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;
import java.util.Date;

@ApiModel(value = "用户对象", description = "从客户端，由用户传入的数据对象")
public class CenterUserBO {
    @ApiModelProperty(value = "用户名", name = "username", example = "imooc", required = false)
    private String username;

    @ApiModelProperty(value = "密码", name = "password", example = "123456", required = false)
    private String password;

    @ApiModelProperty(value = "确认密码", name = "confirmPassword", example = "和密码一致", required = false)
    private String confirmPassword;

    @NotBlank(message = "用户昵称字段不能为空")
    @Length(max = 12, message = "用户昵称不能超过12")
    @ApiModelProperty(value = "昵称", name = "昵称", example = "猪猪侠", required = true)
    private String nickname;

    @Length(max = 12, message = "用户真是姓名字段超过12")
    @ApiModelProperty(value = "真实名字", name = "真实名字", example = "猪超强", required = false)
    private String realname;

    @Pattern(regexp = "^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1}))+\\d{8})$", message = "手机号码格式错误")
    @ApiModelProperty(value = "手机电话", name = "手机电话", example = "13000000000", required = false)
    private String mobile;

    @Email
    @ApiModelProperty(value = "邮箱", name = "邮箱", example = "zhuchaoqiang@qq.com", required = false)
    private String email;

    @Min(value = 0, message = "性别选择不正确")
    @Max(value = 2, message = "性别选择不正确")
    @ApiModelProperty(value = "性别", name = "性别", example = "0：女 1：男 2：保密", required = false)
    private Integer sex;

    @ApiModelProperty(value = "生日", name = "生日", example = "1900-01-01", required = false)
    private Date birthday;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }
}
