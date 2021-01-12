package com.cxyzj.cxyzjback.Data.Comment;


import com.cxyzj.cxyzjback.Data.User.front.UserSimple;
import lombok.Data;

/**
 * @Auther: 夏
 * @DATE: 2018/10/9 09:48
 * @Description:
 */

@Data
public class ReplyList implements com.cxyzj.cxyzjback.Data.Data {

    private ReplyBasic reply;
    private UserSimple replier;

    public ReplyList(ReplyBasic reply, UserSimple replier) {
        this.reply = reply;
        this.replier = replier;
    }

    @Override
    public String getName() {
        return "children";
    }

}
