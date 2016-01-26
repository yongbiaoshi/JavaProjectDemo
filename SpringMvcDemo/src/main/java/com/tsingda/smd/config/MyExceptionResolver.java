package com.tsingda.smd.config;

import java.util.Enumeration;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import com.tsingda.smd.util.AjaxUtil;

public class MyExceptionResolver extends SimpleMappingExceptionResolver {

    private Properties exceptionStatusMapping;
    private Class<?>[] excludedExceptions;
    private Integer defaultStatusCode;

    @Override
    protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
            Exception ex) {
        logger.error("异常信息：", ex);
        if (AjaxUtil.isAjaxRequest(request)) {
            Integer statusCode = determineStatus(ex, request);
            if (statusCode != null) {
                response.setStatus(statusCode);
            }
            return new ModelAndView();
        } else {
            return super.doResolveException(request, response, handler, ex);
        }
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

}
