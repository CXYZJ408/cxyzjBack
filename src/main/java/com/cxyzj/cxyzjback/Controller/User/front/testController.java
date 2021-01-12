package com.cxyzj.cxyzjback.Controller.User.front;

import com.cxyzj.cxyzjback.Service.Interface.Other.Test;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author Yaser
 * @Date 4/19/2019 9:16 PM
 * @Description:
 */
@RestController
@RequestMapping(value = "/test")
@Slf4j
public class testController {
    private final Test test;

    @Autowired
    public testController(Test test) {
        this.test = test;
    }

    @PostMapping(value = "/t1")
    public String Test(String name, String password, @RequestParam(required = false, defaultValue = "16", value = "my_age") String myAge) {
        log.info(myAge);
        return test.login(name, password);
    }
}
