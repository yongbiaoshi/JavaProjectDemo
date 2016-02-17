/**   
 * @Title: AppError.java 
 * @Package com.tsingda.smd.common 
 * @Description: TODO
 * @author Administrator
 * @date 2016年2月17日 上午10:15:43 
 * @version V1.0   
 */
package com.tsingda.smd.message;

/**
 * @ClassName: AppError
 * @Description: 错误信息定义
 * @author Administrator
 * @date 2016年2月17日 上午10:15:43
 * 
 */
public enum AppError {

    ERROR_MSG_1(1, "没有权限"), ERROR_MSG_5(5, "密码错误"), ERROR_MSG_6(6, "用户名或者密码错误");
    
    private int code;
    private String msg;

    private AppError(int code, String msg) {

    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
