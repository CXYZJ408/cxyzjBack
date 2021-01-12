package com.cxyzj.cxyzjback.Data.Comment;

import com.cxyzj.cxyzjback.Data.Data;
import com.cxyzj.cxyzjback.Data.User.front.UserSimple;

/**
 * @Package com.cxyzj.cxyzjback.Data.Comment
 * @Author Yaser
 * @Date 2018/12/27 12:00
 * @Description:
 */
public class UserReplyList implements Data {
    private UserReply userReply;
    private UserSimple discusser;
    private UserSimple replier;

    public UserReplyList(UserReply userReply, UserSimple discusser, UserSimple replier) {
        this.userReply = userReply;
        this.discusser = discusser;
        this.replier = replier;
    }

    @Override
    public String getName() {
        return "list";
    }
}
