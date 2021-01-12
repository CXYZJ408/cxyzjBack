package com.cxyzj.cxyzjback.Service.impl.Comment;

import com.cxyzj.cxyzjback.Repository.Article.ArticleJpaRepository;
import com.cxyzj.cxyzjback.Repository.Comment.CommentJpaRepository;
import com.cxyzj.cxyzjback.Repository.Comment.CommentVoteJpaRepository;
import com.cxyzj.cxyzjback.Repository.Comment.ReplyJpaRepository;

import com.cxyzj.cxyzjback.Repository.User.UserJpaRepository;
import com.cxyzj.cxyzjback.Service.Interface.Comment.DiscussionCommentService;

/**
 * @Package com.cxyzj.cxyzjback.Service.impl.Comment
 * @Author Yaser
 * @Date 2018/11/09 13:07
 * @Description:
 */
public class DiscussionCommentServiceImpl extends CommentServiceImpl implements DiscussionCommentService {

    public DiscussionCommentServiceImpl(CommentJpaRepository commentJpaRepository, CommentVoteJpaRepository commentVoteJpaRepository, ReplyJpaRepository replyJpaRepository, ArticleJpaRepository articleJpaRepository, UserJpaRepository userJpaRepository) {
        super(commentJpaRepository, commentVoteJpaRepository, replyJpaRepository, userJpaRepository);
    }
}
