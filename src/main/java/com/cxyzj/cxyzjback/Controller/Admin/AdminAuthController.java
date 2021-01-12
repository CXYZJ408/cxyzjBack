package com.cxyzj.cxyzjback.Controller.Admin;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.*;

/**
 * @Auther: 夏
 * @DATE: 2018/9/29 10:17
 * @Description:
 */

@RestController
@CrossOrigin
@Slf4j
@RequestMapping("/v1/admin/auth")
public class AdminAuthController {

    @PostMapping(value = "/login")
    public String adminLogin(@RequestParam(required = false) String email, @RequestParam(required = false) String phone, @RequestParam String password){
        return null;
    }

}
