package com.cxyzj.cxyzjback.Data.Comment;

import com.cxyzj.cxyzjback.Bean.Article.Reply;
import lombok.Data;

/**
 * @Package com.cxyzj.cxyzjback.Data.Comment
 * @Author Yaser
 * @Date 2018/12/27 11:57
 * @Description:
 */
@Data
public class UserReply {
    private String comment_id;
    private String reply_id;
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

    public UserReply(Reply reply) {
        this.comment_id = reply.getCommentId();
        this.reply_id = reply.getReplyId();
        this.target_id = reply.getTargetId();
        this.model = reply.getMode();
        this.text = reply.getText();
        this.create_time = reply.getCreateTime();
        this.support = reply.getSupport();
        this.allow_vote = true;
        this.allow_delete = false;
        this.is_support = false;
        this.is_author = false;
        this.is_obj = false;
    }

    public void isAuthor(boolean state) {
        this.allow_delete = state;
        this.allow_vote = !state;
        this.is_author = state;
    }
}
