package com.cxyzj.cxyzjback.Data.Comment;

import com.cxyzj.cxyzjback.Bean.Article.Comment;
import lombok.Data;

/**
 * @Auther: 夏
 * @DATE: 2018/9/6 16:14
 * @Description:
 */

@Data
public class CommentBasic implements com.cxyzj.cxyzjback.Data.Data {

    private String comment_id;
    private String text;
    private long create_time;
    private int support;
    private boolean allow_vote;
    private boolean allow_delete;
    private boolean is_support;
    private boolean is_author;
    private boolean is_obj;
    private int level;
    private int children;

    public CommentBasic(Comment comment) {
        this.comment_id = comment.getCommentId();
        this.text = comment.getText();
        this.create_time = comment.getCreateTime();
        this.support = comment.getSupport();
        this.allow_vote = true;
        this.is_obj = false;
        this.is_support = false;
        this.allow_delete = false;
        this.is_author = false;
        this.level = comment.getLevel();
        this.children = comment.getChildren();
    }

    public void isAuthor(boolean state) {
        this.allow_delete = state;
        this.allow_vote = !state;
        this.is_author = state;
    }

    @Override
    public String getName() {
        return "comment";
    }
}
