package com.cxyzj.cxyzjback.Data.Comment;

import com.cxyzj.cxyzjback.Data.User.front.UserSimple;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: 夏
 * @DATE: 2018/10/9 10:19
 * @Description:
 */

@Data
public class CommentList implements com.cxyzj.cxyzjback.Data.Data {

    private CommentBasic comment;
    private UserSimple discusser;
    private List<ReplyList> children;

    public CommentList(CommentBasic comment, UserSimple discusser, List<ReplyList> children) {
        this.comment = comment;
        this.discusser = discusser;
        this.children = children;
    }

    public CommentList(CommentBasic comment, UserSimple discusser) {
        this.comment = comment;
        this.discusser = discusser;
        this.children = new ArrayList<>();
    }

    @Override
    public String getName() {
        return "list";
    }
}
