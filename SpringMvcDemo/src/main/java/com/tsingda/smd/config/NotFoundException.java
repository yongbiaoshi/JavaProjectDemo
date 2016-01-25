package com.tsingda.smd.config;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

public class NotFoundException extends HttpStatusCodeException {

    /**
     * 
     */
    private static final long serialVersionUID = -2694059721133897710L;

    private NotFoundException(HttpStatus statusCode, String statusText) {
        super(statusCode);
    }

    public static NotFoundException getInstance() {
        return new NotFoundException(HttpStatus.NOT_FOUND, "访问的页面未找到");
    }
}
