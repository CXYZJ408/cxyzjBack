package com.cxyzj.cxyzjback.Controller.User.front;

import com.cxyzj.cxyzjback.Service.Interface.User.front.UserListGetService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @Author 夏
 * @Date 15:51 2018/8/30
 */

@RestController
@CrossOrigin
@Slf4j
@RequestMapping(value = "/v1/user")
public class UserListGetController {

    private final UserListGetService userListGetService;

    @Autowired
    public UserListGetController(UserListGetService userListGetService) {
        this.userListGetService = userListGetService;
    }

    @GetMapping(value = "/{user_id}/attention_list/{page_num}")
    @PreAuthorize("hasAnyRole('ROLE_ANONYMITY','ROLE_USER','ROLE_ADMIN','ROLE_ADMINISTRATORS')")
    public String getAttentionList(@PathVariable(name = "user_id") String userId, @PathVariable(name = "page_num") int pageNum) {
        return userListGetService.getAttentionList(userId, pageNum);
    }

    @GetMapping(value = "/{user_id}/fans_list/{page_num}")
    @PreAuthorize("hasAnyRole('ROLE_ANONYMITY','ROLE_USER','ROLE_ADMIN','ROLE_ADMINISTRATORS')")
    public String getFansList(@PathVariable(name = "user_id") String userId, @PathVariable(name = "page_num") int pageNum) {
        return userListGetService.getFansList(userId, pageNum);
    }

    @GetMapping(value = "/{user_id}/article_list")
    @PreAuthorize("hasAnyRole('ROLE_ANONYMITY','ROLE_USER','ROLE_ADMIN','ROLE_ADMINISTRATORS')")
    public String getArticleList(@PathVariable(name = "user_id") String userId) {
        return userListGetService.getArticleList(userId);
    }

    @GetMapping(value = "/{user_id}/comment_list/user_comment/{page_num}")
    @PreAuthorize("hasAnyRole('ROLE_ANONYMITY','ROLE_USER','ROLE_ADMIN','ROLE_ADMINISTRATORS')")
    public String getUserCommentList(@PathVariable(name = "user_id") String userId, @PathVariable(name = "page_num") int pageNum) {
        return userListGetService.getUserCommentList(userId, pageNum);
    }

    @GetMapping(value = "/{user_id}/comment_list/user_reply/{page_num}")
    @PreAuthorize("hasAnyRole('ROLE_ANONYMITY','ROLE_USER','ROLE_ADMIN','ROLE_ADMINISTRATORS')")
    public String getUserReplyList(@PathVariable(name = "user_id") String userId, @PathVariable(name = "page_num") int pageNum) {
        return userListGetService.getUserReplyList(userId, pageNum);
    }

    @GetMapping(value = "/{user_id}/comment_list/other_reply/{page_num}")
    @PreAuthorize("hasAnyRole('ROLE_ANONYMITY','ROLE_USER','ROLE_ADMIN','ROLE_ADMINISTRATORS')")
    public String getOtherToUserReplyList(@PathVariable(name = "user_id") String userId, @PathVariable(name = "page_num") int pageNum) {
        return userListGetService.getOtherToUserReplyList(userId, pageNum);
    }
    @GetMapping(value = "/{user_id}/article_collections/{page_num}")
    @PreAuthorize("hasAnyRole('ROLE_ANONYMITY','ROLE_USER','ROLE_ADMIN','ROLE_ADMINISTRATORS')")
    public String getCollectedArticleList(@PathVariable(name = "user_id") String userId, @PathVariable(name = "page_num") int pageNum) {
        return userListGetService.getCollectedArticleList(userId, pageNum);
    }
}
