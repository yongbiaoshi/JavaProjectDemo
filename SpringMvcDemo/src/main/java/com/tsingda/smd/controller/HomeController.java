package com.tsingda.smd.controller;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tsingda.smd.model.User;

/**
 * Handles requests for the application home page.
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
	
	@RequestMapping(value = "/str", method = RequestMethod.GET)
    public @ResponseBody String str(Locale locale, Model model) {
        logger.info("Welcome home! The client locale is {}.", locale);
        return "我想吃早饭……";
    }
	
	@RequestMapping(value = "/u")
    public @ResponseBody String u(User user) {
        return "我想吃早饭……";
    }
}
