package com.cxyzj.cxyzjback.Controller.Comment;

import com.cxyzj.cxyzjback.Service.Interface.Comment.CommentService;
import com.cxyzj.cxyzjback.Utils.Response;
import com.cxyzj.cxyzjback.Utils.Status;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @Package com.cxyzj.cxyzjback.Controller.Comment
 * @Author Yaser
 * @Date 2018/11/09 10:28
 * @Description: 评论系统的基础服务
 */
@RestController
@CrossOrigin
@Slf4j
@RequestMapping(value = "/v1/comment")
public class CommentController {
    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    /**
     * @Description 支持评论
     */
    @PostMapping(value = "/support")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN','ROLE_ADMINISTRATORS')")
    public String support(@RequestParam(required = false, name = "comment_id") String commentId,
                          @RequestParam(required = false, name = "reply_id") String replyId,
                          @RequestParam(name = "target_id") String targetId) {
        try {
            return commentService.support(commentId, replyId, targetId);
        } catch (NoSuchFieldException e) {
            return new Response().sendFailure(Status.NO_SUCH_FIELD, "comment_id字段与reply_id字段不可以同时为空");
        }
    }

    /**
     * @Description 反对评论
     */
    @PostMapping(value = "/object")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN','ROLE_ADMINISTRATORS')")
    public String object(@RequestParam(required = false, name = "comment_id") String commentId,
                         @RequestParam(required = false, name = "reply_id") String replyId, @RequestParam(name = "target_id") String targetId) {
        try {
            return commentService.object(commentId, replyId, targetId);
        } catch (NoSuchFieldException e) {
            return new Response().sendFailure(Status.NO_SUCH_FIELD, "comment_id字段与reply_id字段不可以同时为空");
        }
    }

    /**
     * @Description 取消支持评论
     */
    @DeleteMapping(value = "/support")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN','ROLE_ADMINISTRATORS')")
    public String supportDel(@RequestParam(required = false, name = "comment_id") String commentId,
                             @RequestParam(required = false, name = "reply_id") String replyId,
                             @RequestParam(name = "target_id") String targetId) {
        try {
            return commentService.supportDel(commentId, replyId, targetId);
        } catch (NoSuchFieldException e) {
            return new Response().sendFailure(Status.NO_SUCH_FIELD, "comment_id字段与reply_id字段不可以同时为空");
        }
    }

    /**
     * @Description 取消反对评论
     */
    @DeleteMapping(value = "/object")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN','ROLE_ADMINISTRATORS')")
    public String objectDel(@RequestParam(required = false, name = "comment_id") String commentId,
                            @RequestParam(required = false, name = "reply_id") String replyId,
                            @RequestParam(name = "target_id") String targetId) {
        try {
            return commentService.objectDel(commentId, replyId, targetId);
        } catch (NoSuchFieldException e) {
            return new Response().sendFailure(Status.NO_SUCH_FIELD, "comment_id字段与reply_id字段不可以同时为空");
        }
    }

}
