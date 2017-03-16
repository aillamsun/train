package com.train.controller;

import com.google.common.collect.Maps;
import com.train.model.Test;
import com.train.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by sungang on 2017/3/15.
 */
@RestController
@RequestMapping("test")
public class TestController {

    @Autowired
    private TestService testService;
    @Autowired
    private MessageSource messageSource;

    @Autowired
    RestTemplate restTemplate;

    @RequestMapping("")
    public String test(Model model){
        List<Test> tests = testService.selectAll();
        model.addAttribute("tests",tests);
        return "test/index";
    }


    @RequestMapping("json")
    @ResponseBody
    public Map<String,Object> json(){
        List<Test> tests = testService.selectAll();
        Map<String,Object> r = Maps.newHashMap();
        r.put("tests",tests);
        Locale locale = LocaleContextHolder.getLocale();
        r.put("testMessage",messageSource.getMessage("welcome",null,locale));
        return r;
    }


    @RequestMapping("rest")
    @ResponseBody
    public String rest(){
        String url = "https://www.baidu.com";
        String json = restTemplate.getForEntity(url, String.class).getBody();
        return json;
    }

}
