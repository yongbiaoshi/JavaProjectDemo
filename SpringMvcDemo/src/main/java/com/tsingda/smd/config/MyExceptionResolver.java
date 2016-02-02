package com.tsingda.smd.config;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import com.tsingda.smd.common.JsonResponseData;
import com.tsingda.smd.util.AjaxUtil;

public class MyExceptionResolver extends SimpleMappingExceptionResolver {

    private Properties exceptionStatusMapping;
    private Class<?>[] excludedExceptions;
    private Integer defaultStatusCode;
    private MappingJackson2HttpMessageConverter jsonMessageConverter;

    @Override
    protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
            Exception ex) {
        logger.error("异常信息：", ex);
        JsonResponseData data = new JsonResponseData(false, "服务器错误，错误信息：" + ex.getMessage(), null);
        if(ex instanceof MaxUploadSizeExceededException){
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            responseExceptionToJson(data, response);
            return null;
        }
        if(isResponseBodyHandlerMethod(handler) || AjaxUtil.isAjaxRequest(request)){
            Integer statusCode = determineStatus(ex, request);
            if (statusCode != null) {
                response.setStatus(statusCode);
            }
            responseExceptionToJson(data, response);
            return new ModelAndView();
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

    public void setJsonMessageConverter(MappingJackson2HttpMessageConverter jsonMessageConverter) {
        this.jsonMessageConverter = jsonMessageConverter;
    }
    
    private void responseExceptionToJson(JsonResponseData data, HttpServletResponse response){
        try {
            jsonMessageConverter.write(data, MediaType.APPLICATION_JSON_UTF8, new ServletServerHttpResponse(response));
        } catch (HttpMessageNotWritableException e) {
            logger.error("返回Json错误信息失败，错误信息：", e);
        } catch (IOException e) {
            logger.error("返回Json错误信息失败，错误信息：", e);
        }
    }

}
