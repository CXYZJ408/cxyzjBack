package com.cxyzj.cxyzjback.Data.Comment;

import com.cxyzj.cxyzjback.Data.Data;
import com.cxyzj.cxyzjback.Data.User.front.UserSimple;

/**
 * @Package com.cxyzj.cxyzjback.Data.Comment
 * @Author Yaser
 * @Date 2018/12/27 11:08
 * @Description:
 */
public class UserCommentList implements Data {
    private UserComment userComment;
    private UserSimple discusser;

    public UserCommentList(UserComment userComment, UserSimple discusser) {
        this.userComment = userComment;
        this.discusser = discusser;
    }

    @Override
    public String getName() {
        return "list";
    }
}
