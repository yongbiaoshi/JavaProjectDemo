package com.tsingda.smd.util;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Ajax请求工具类
 * 
 * @author Administrator
 *
 */
public class AjaxUtil {

    /**
     * 构造函数（私有）
     */
    private AjaxUtil() {
    }

    /**
     * Ajax请求头
     */
    private static final String AJAX_REQUEST_HEAD = "X-Requested-With";

    /**
     * Ajax请求头内容
     */
    private static final String AJAX_REQUEST_TYPE = "XMLHttpRequest";

    /**
     * 自定义session过期statusCode
     */
    private static final Integer CUSTUM_SESSION_TIME_OUT_CODE = 911;

    /**
     * 判断是否是Ajax请求
     * 
     * @param request
     *            请求对象
     * @return 是否是Ajax请求
     */
    public static boolean isAjaxRequest(HttpServletRequest request) {
        String head = request.getHeader(AJAX_REQUEST_HEAD);
        return head != null && AJAX_REQUEST_TYPE.equals(head);
    }

    /**
     * 发送session过期response
     * 
     * @param response
     *            response
     * @throws IOException
     *             异常
     */
    public static void sendAjaxSessionTimeout(HttpServletResponse response) throws IOException {
        // 返回 头 更改，发送自定义错误码
        response.setHeader("sessionstatus", "timeout");
        response.sendError(CUSTUM_SESSION_TIME_OUT_CODE, "session timeout.");
    }
}
