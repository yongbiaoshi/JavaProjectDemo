package com.tsingda.smd.config;

import java.util.Enumeration;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.tsingda.smd.util.AjaxUtil;

/**
 * 自定义异常处理 继承{@link SimpleMappingExceptionResolver} ，并添加了Json类型的返回值 当请求为ajax请求或者
 * handler 有@responsebody 注解时，出现异常，则返回Json类型的Response
 * 
 * @ClassName: MyExceptionResolver
 * @Description: 自定义异常统一处理
 * @author Administrator
 * @date 2016年2月3日 上午9:19:02
 * @see SimpleMappingExceptionResolver
 */
public class MyExceptionResolver extends SimpleMappingExceptionResolver {

    /**
     * 异常与status{@link HttpStatus}的对应状态
     */
    private Properties exceptionStatusMapping;

    /**
     * 不做处理的异常类型
     */
    private Class<?>[] excludedExceptions;

    /**
     * 默认Status，一般设置为500{@link HttpStatus#INTERNAL_SERVER_ERROR}
     */
    private Integer defaultStatusCode;

    @Override
    protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
            Exception ex) {
        logger.error("异常信息：", ex);
        if (ex instanceof MaxUploadSizeExceededException) {
            if (AjaxUtil.isAjaxRequest(request) || isResponseBodyHandlerMethod(handler)) {
                return exceptionToJsonView(ex);
            }
        } else if (AjaxUtil.isAjaxRequest(request) || isResponseBodyHandlerMethod(handler)) {
            Integer statusCode = determineStatus(ex, request);
            if (statusCode != null) {
                response.setStatus(statusCode);
            }
            return exceptionToJsonView(ex);
        }
        return super.doResolveException(request, response, handler, ex);

    }

    /**
     * 判断handler是否有{@link ResponseBody}注解
     * 
     * @param handler
     *            handler
     * @return true or false
     */
    public boolean isResponseBodyHandlerMethod(Object handler) {
        return handler != null && handler instanceof HandlerMethod
                && ((HandlerMethod) handler).getMethodAnnotation(ResponseBody.class) != null;
    }

    public void setExceptionCodeMapping(Properties exceptionCodeMapping) {
        this.exceptionStatusMapping = exceptionCodeMapping;
    }

    /**
     * 根据异常判断Status
     *
     * @see SimpleMappingExceptionResolver#determineStatusCode
     * @param ex
     *            异常
     * @param request
     *            http request
     * @return http status code
     */
    protected Integer determineStatus(Exception ex, HttpServletRequest request) {
        if (ex == null) {
            return null;
        }
        if (this.excludedExceptions != null) {
            for (Class<?> excludedEx : this.excludedExceptions) {
                if (excludedEx.equals(ex.getClass())) {
                    return null;
                }
            }
        }
        if (this.exceptionStatusMapping != null) {
            return findMatchingStatusCode(this.exceptionStatusMapping, ex);
        } else {
            return this.defaultStatusCode;
        }
    }

    protected Integer findMatchingStatusCode(Properties exceptionStatusMapping, Exception ex) {
        Integer statusCode = null;
        String dominantMapping = null;
        int deepest = Integer.MAX_VALUE;
        for (Enumeration<?> names = exceptionStatusMapping.propertyNames(); names.hasMoreElements();) {
            String exceptionMapping = (String) names.nextElement();
            int depth = getDepth(exceptionMapping, ex);
            if (depth >= 0
                    && (depth < deepest || (depth == deepest && dominantMapping != null && exceptionMapping.length() > dominantMapping
                            .length()))) {
                deepest = depth;
                dominantMapping = exceptionMapping;
                statusCode = (int) exceptionStatusMapping.get(exceptionMapping);
            }
        }
        if (statusCode != null && logger.isDebugEnabled()) {
            logger.debug("Resolving to statusCode '" + statusCode + "' for exception of type ["
                    + ex.getClass().getName() + "], based on exception mapping [" + dominantMapping + "]");
        }
        return statusCode;
    }

    private ModelAndView exceptionToJsonView(Exception ex) {
        ModelAndView mv = new ModelAndView();
        mv.setView(new MappingJackson2JsonView());
        mv.addObject("success", false);
        mv.addObject("msg", "服务器错误，错误信息：" + ex.getMessage());
        return mv;
    }

}
