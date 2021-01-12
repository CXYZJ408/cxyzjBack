package com.cxyzj.cxyzjback.Service.Interface.Comment;

/**
 * @Auther: 夏
 * @DATE: 2018/9/6 15:24
 * @Description:
 */
public interface CommentService {
    String publishComment(String text, String targetId);

    String getCommentList(String targetId, int pageNum);

    String publishReply(String commentId, String text, String discusserId, String targetId);

    String getReplyList(String commentId, int pageNum, String targetId);

    int commentReplyDel(String commentId, String replyId);

    String support(String commentId, String replyId, String targetId) throws NoSuchFieldException;

    String object(String commentId, String replyId, String targetId) throws NoSuchFieldException;

    String supportDel(String commentId, String replyId, String targetId) throws NoSuchFieldException;

    String objectDel(String commentId, String replyId, String targetId) throws NoSuchFieldException;

}
