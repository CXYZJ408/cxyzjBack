package com.cxyzj.cxyzjback.Service.Interface.Comment;

public interface ArticleCommentService extends CommentService {
    String getHotCommentList(String articleId);

    String articleCommentReplyDel(String commentId, String replyId, String articleId);
}
