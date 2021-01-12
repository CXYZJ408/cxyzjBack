package com.cxyzj.cxyzjback.Controller.User.back;


import com.cxyzj.cxyzjback.Service.Interface.User.back.UsersBackService;
import com.cxyzj.cxyzjback.Utils.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@Slf4j
@RequestMapping(value = "/v1/admin/users")
public class UsersBackController {
    private final UsersBackService usersBackService;

    @Autowired
    public UsersBackController(UsersBackService usersBackService) {
        this.usersBackService = usersBackService;
    }

    @PostMapping("/test")
    public String alertIsAdmin(@RequestParam(required = false, defaultValue = Constant.NONE, value = "nickname") String nickname,
                               @RequestParam(required = false, defaultValue = Constant.NONE, value = "user_id") String userId,
                               @RequestParam(required = false, defaultValue = Constant.NONE, value = "gender") String gender) {


        return usersBackService.test(userId, nickname, gender);
    }

}
