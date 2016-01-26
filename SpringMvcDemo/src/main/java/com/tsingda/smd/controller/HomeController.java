package com.tsingda.smd.controller;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.groups.Default;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tsingda.smd.config.NotFoundException;
import com.tsingda.smd.model.User;
import com.tsingda.smd.model.ValidatorGroups;

/**
 * 一些基本的验证请求
 * @author Administrator
 *
 */
@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);
		
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);
		
		model.addAttribute("serverTime", formattedDate );
		
		return "home";
	}
	
	/**
	 * 拦截所有没有对应Controller的请求
	 * @param request HttpServletRequest
	 * @throws Exception
	 */
	@RequestMapping(value = "*/**")
    public void notFoundRequestMapping(HttpServletRequest request) throws NotFoundException {
	    logger.error("页面不存在，URI：{}", request.getRequestURI());
	    throw NotFoundException.getInstance();
    }
	
	/**
	 * 测试Json格式返回值
	 * @param locale locale
	 * @param model model
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
        throw new NullPointerException("测试异常");
    }
	
	@RequestMapping(value = "/str", method = RequestMethod.GET)
	public @ResponseBody String str(Locale locale, Model model) {
	    logger.info("Welcome home! The client locale is {}.", locale);
	    return "我想吃早饭……";
	}
	
	@RequestMapping(value = "/u")
    public @ResponseBody Object u(@Validated({Default.class, ValidatorGroups.UserAdd.class}) User user, BindingResult result) {
	    if(result.hasErrors()){
	        return result.getFieldErrors();
	    }
        return user;
    }
}
