package com.tsingda.smd.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.validation.groups.Default;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.tsingda.smd.model.User;
import com.tsingda.smd.model.ValidatorGroups;

/**
 * 一些基本的验证请求
 * 
 * @author Administrator
 *
 */
@Controller
public class HomeController {

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
    @Autowired
    private Environment env;

    @Value(value = "#{appProperties['app.download.file.path']}")
    private String dowloadPath;

    /**
     * Simply selects the home view to render by returning its name.
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String home(Locale locale, Model model) {
        logger.info("Welcome home! The client locale is {}.", locale);

        Date date = new Date();
        DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);

        String formattedDate = dateFormat.format(date);

        model.addAttribute("serverTime", formattedDate);

        return "home";
    }

    /**
     * 测试Json格式返回值
     * 
     * @param locale
     *            locale
     * @param model
     *            model
     * @return a map for test
     */
    @RequestMapping(value = "/json", method = RequestMethod.GET)
    public @ResponseBody Map<String, Object> json(Locale locale, Model model) {
        logger.info("Welcome home! The client locale is {}.", locale);

        Date date = new Date();

        Map<String, Object> r = new HashMap<String, Object>();
        r.put("current", date);
        r.put("locale", locale);
        r.put("msg", "我饿了");
        return r;
    }

    @RequestMapping(value = "/exception")
    public void exception(Locale locale, Model model) {
        System.out.println(1 / 0);
        throw new NullPointerException("测试异常");
    }

    @RequestMapping(value = "/str", method = RequestMethod.GET)
    public @ResponseBody String str(Locale locale, Model model) {
        logger.info("Welcome home! The client locale is {}.", locale);
        return "我想吃早饭……";
    }

    @RequestMapping(value = "/u")
    public @ResponseBody Object u(@Validated({ Default.class, ValidatorGroups.UserAdd.class }) User user,
            BindingResult result) {
        // System.out.println(1/0);
        if (result.hasErrors()) {
            return result.getFieldErrors();
        }
        return user;
    }

    @RequestMapping(value = "/ajax")
    public String ajax() {
        return "ajax";
    }

    @RequestMapping(value = "/file")
    public StreamingResponseBody file(HttpServletResponse response) throws IOException {
        File f = new File(dowloadPath + "test.png");
        if (f == null || !f.exists() || f.isDirectory()) {
            throw new FileNotFoundException("文件未找到：" + f.getAbsolutePath());
        }
        configFileResponseHeaders(response, f.getName());
        StreamingResponseBody streaming = new StreamingResponseBody() {
            @Override
            public void writeTo(OutputStream outputStream) throws IOException {
                byte[] b = FileUtils.readFileToByteArray(f);
                outputStream.write(b);
                outputStream.flush();
                outputStream.close();
            }
        };
        return streaming;
    }

    private void configFileResponseHeaders(HttpServletResponse response, String fileName) {
        String attachmentHeaderValue = "form-data; name=\"attachment\"; filename=\"" + fileName + "\"";
        response.addHeader(HttpHeaders.CONTENT_DISPOSITION, attachmentHeaderValue);
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
    }
}
