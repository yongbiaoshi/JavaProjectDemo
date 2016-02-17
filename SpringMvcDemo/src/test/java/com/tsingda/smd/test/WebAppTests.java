package com.tsingda.smd.test;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;

import com.tsingda.smd.config.AppConfig;
import com.tsingda.smd.config.MvcConfig;

@ContextConfiguration(classes = { AppConfig.class, MvcConfig.class })
@WebAppConfiguration
@ActiveProfiles({ "dev", "integration" })
public class WebAppTests {

    public static void main(String[] args) {

    }
}
