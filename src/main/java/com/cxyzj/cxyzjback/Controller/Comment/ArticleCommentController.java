package com.cxyzj.cxyzjback.Controller.Comment;

import com.cxyzj.cxyzjback.Service.Interface.Comment.ArticleCommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @Auther: 夏
 * @DATE: 2018/9/6 15:17
 * @Description: 评论系统文章服务
 */

@RestController
@CrossOrigin
@Slf4j
@RequestMapping(value = "/v1/comment")
public class ArticleCommentController {

    private final ArticleCommentService articleComment;

    @Autowired
    public ArticleCommentController(ArticleCommentService articleComment) {
        this.articleComment = articleComment;
    }

    /**
     * @Description 发表文章评论
     */
    @PostMapping(value = "/article/publish")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN','ROLE_ADMINISTRATORS')")
    public String publishArticleComment(@RequestParam String text, @RequestParam(name = "article_id") String articleId) {
        return articleComment.publishComment(text, articleId);
    }

    /**
     * @Description 获取文章评论列表
     */
    @GetMapping(value = "/article/{article_id}/{page_num}")
    @PreAuthorize("hasAnyRole('ROLE_ANONYMITY','ROLE_USER','ROLE_ADMIN','ROLE_ADMINISTRATORS')")
    public String getCommentList(@PathVariable(name = "article_id") String articleId,
                                 @PathVariable(name = "page_num") int pageNum) {
        return articleComment.getCommentList(articleId, pageNum);
    }

    /**
     * @Description 获取文章热门评论列表
     */
    @GetMapping(value = "/article/{article_id}/hot")
    @PreAuthorize("hasAnyRole('ROLE_ANONYMITY','ROLE_USER','ROLE_ADMIN','ROLE_ADMINISTRATORS')")
    public String getHotCommentList(@PathVariable(name = "article_id") String articleId) {
        return articleComment.getHotCommentList(articleId);
    }

    /**
     * @Description 回复文章评论
     */
    @PostMapping(value = "/reply/article/publish")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN','ROLE_ADMINISTRATORS')")
    public String reply(@RequestParam(name = "comment_id") String commentId, @RequestParam String text,
                        @RequestParam(name = "discusser_id") String discusserId,
                        @RequestParam(name = "article_id") String articleId) {
        return articleComment.publishReply(commentId, text, discusserId, articleId);
    }

    /**
     * @Description 获取文章回复列表
     */
    @GetMapping(value = "/reply/article/{article_id}/{comment_id}/{page_num}")
    @PreAuthorize("hasAnyRole('ROLE_ANONYMITY','ROLE_USER','ROLE_ADMIN','ROLE_ADMINISTRATORS')")
    public String replyList(@PathVariable(name = "comment_id") String commentId,
                            @PathVariable(name = "page_num") int pageNum, @PathVariable(name = "article_id") String articleId) {
        return articleComment.getReplyList(commentId, pageNum, articleId);
    }

    /**
     * @Description 删除评论
     */
    @DeleteMapping(value = "/article")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN','ROLE_ADMINISTRATORS')")
    public String commentDel(@RequestParam(name = "comment_id") String commentId,
                             @RequestParam(required = false, name = "reply_id") String replyId,
                             @RequestParam(name = "article_id") String articleId) {
        return articleComment.articleCommentReplyDel(commentId, replyId, articleId);
    }
}
