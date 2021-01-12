package com.cxyzj.cxyzjback.Data.Comment;

import com.cxyzj.cxyzjback.Bean.Article.Reply;
import lombok.Data;

/**
 * @Auther: 夏
 * @DATE: 2018/9/7 10:11
 * @Description:
 */
@Data
public class ReplyBasic implements com.cxyzj.cxyzjback.Data.Data {

    private String comment_id;
    private String reply_id;
    private String text;
    private String discusser_id;
    private String discusser_nickname;
    private long create_time;
    private int support;
    private boolean allow_vote;
    private boolean is_author;
    private boolean is_support;
    private boolean is_obj;
    private boolean allow_delete;

    public ReplyBasic(Reply reply, String discusserNickname) {
        this.comment_id = reply.getCommentId();
        this.reply_id = reply.getReplyId();
        this.text = reply.getText();
        this.create_time = reply.getCreateTime();
        this.support = reply.getSupport();
        this.discusser_id = reply.getDiscusser();
        this.discusser_nickname = discusserNickname;
        this.allow_vote = true;
        this.is_support = false;
        this.is_author = false;
        this.is_obj = false;
        this.allow_delete = false;
    }

    public void isAuthor(boolean state) {
        this.allow_delete = state;
        this.allow_vote = !state;
        this.is_author = state;
    }

    @Override
    public String getName() {
        return "reply";
    }
}
