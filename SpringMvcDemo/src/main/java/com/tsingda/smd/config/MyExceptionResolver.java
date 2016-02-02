package com.tsingda.smd.config;

import java.util.Enumeration;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.tsingda.smd.util.AjaxUtil;

public class MyExceptionResolver extends SimpleMappingExceptionResolver {

    private Properties exceptionStatusMapping;
    private Class<?>[] excludedExceptions;
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

    public boolean isResponseBodyHandlerMethod(Object handler) {
        return handler != null && handler instanceof HandlerMethod
                && ((HandlerMethod) handler).getMethodAnnotation(ResponseBody.class) != null;
    }

    public void setExceptionCodeMapping(Properties exceptionCodeMapping) {
        this.exceptionStatusMapping = exceptionCodeMapping;
    }

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
