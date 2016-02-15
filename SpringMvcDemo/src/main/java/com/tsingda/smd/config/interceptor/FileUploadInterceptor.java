package com.tsingda.smd.config.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.servlet.ServletRequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.tsingda.smd.util.JsonUtil;

public class FileUploadInterceptor implements HandlerInterceptor {
    
    private final Logger logger = LoggerFactory.getLogger(FileUploadInterceptor.class);
    
    private long maxSize;
    
    public long getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(long maxSize) {
        this.maxSize = maxSize;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        logger.debug("请求：{} 参数：{}", request.getRequestURI(), JsonUtil.stringify(request.getParameterMap()));
        
        if (request != null && ServletFileUpload.isMultipartContent(request)) {
            logger.debug("-------------FileUploadInterceptor-----------------");
            ServletRequestContext ctx = new ServletRequestContext(request);
            long requestSize = ctx.contentLength();
            logger.debug("MultipartContent, requestSize:{}, maxSize:{}, handler:{}", requestSize, this.maxSize, handler);
            if (requestSize > maxSize) {
                throw new MaxUploadSizeExceededException(maxSize);
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {

    }

}
