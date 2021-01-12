package com.cxyzj.cxyzjback.Data.Comment;

import com.cxyzj.cxyzjback.Bean.Article.Comment;
import lombok.Data;

/**
 * @Package com.cxyzj.cxyzjback.Data.Comment
 * @Author Yaser
 * @Date 2018/12/27 10:58
 * @Description:
 */
@Data
public class UserComment {
    private String comment_id;
    private String target_id;
    private String model;
    private String comment_title;
    private String text;
    private long create_time;
    private int support;
    private boolean allow_vote;
    private boolean allow_delete;
    private boolean is_support;
    private boolean is_author;
    private boolean is_obj;
    private int children;

    public UserComment(Comment comment) {
        this.comment_id = comment.getCommentId();
        this.target_id = comment.getTargetId();
        this.model = comment.getMode();
        this.text = comment.getText();
        this.create_time = comment.getCreateTime();
        this.support = comment.getSupport();
        this.allow_vote = true;
        this.allow_delete = false;
        this.is_support = false;
        this.is_author = false;
        this.is_obj = false;
        this.children = comment.getChildren();
    }

    public void isAuthor(boolean state) {
        this.allow_delete = state;
        this.allow_vote = !state;
        this.is_author = state;
    }
}
