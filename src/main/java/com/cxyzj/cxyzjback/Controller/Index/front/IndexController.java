package com.cxyzj.cxyzjback.Controller.Index.front;

/**
 * @Package com.cxyzj.cxyzjback.Controller.Index.front
 * @Author Yaser
 * @Date 2018/11/29 22:01
 * @Description:
 */

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@Slf4j
@RequestMapping(value = "/v1/index")
public class IndexController {
    @GetMapping(value = "/img")
    public String getImg() {
        return "/img/Other/bg1.jpg";
    }
}
