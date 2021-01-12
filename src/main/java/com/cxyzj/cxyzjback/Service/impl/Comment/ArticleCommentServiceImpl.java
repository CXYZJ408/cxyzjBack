package com.cxyzj.cxyzjback.Service.impl.Comment;

import com.cxyzj.cxyzjback.Bean.Article.*;
import com.cxyzj.cxyzjback.Bean.User.User;
import com.cxyzj.cxyzjback.Data.Comment.CommentBasic;
import com.cxyzj.cxyzjback.Data.Comment.CommentList;
import com.cxyzj.cxyzjback.Data.Comment.ReplyBasic;
import com.cxyzj.cxyzjback.Data.Comment.ReplyList;
import com.cxyzj.cxyzjback.Data.User.front.UserSimple;
import com.cxyzj.cxyzjback.Repository.Article.*;
import com.cxyzj.cxyzjback.Repository.Comment.CommentJpaRepository;
import com.cxyzj.cxyzjback.Repository.Comment.CommentVoteJpaRepository;
import com.cxyzj.cxyzjback.Repository.Comment.ReplyJpaRepository;
import com.cxyzj.cxyzjback.Repository.User.UserJpaRepository;
import com.cxyzj.cxyzjback.Service.Interface.Comment.ArticleCommentService;
import com.cxyzj.cxyzjback.Utils.Constant;
import com.cxyzj.cxyzjback.Utils.Response;
import com.cxyzj.cxyzjback.Utils.Status;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @Auther: 夏
 * @DATE: 2018/9/6 15:23
 * @Description: 文章评论系统API
 * @checked false
 */

@Service
@Slf4j
public class ArticleCommentServiceImpl extends CommentServiceImpl implements ArticleCommentService {

    private final CommentJpaRepository commentJpaRepository;


    private final ReplyJpaRepository replyJpaRepository;

    private final UserJpaRepository userJpaRepository;

    private final ArticleJpaRepository articleJpaRepository;
    private static final String LIST = "list";
    private static final String HOT_LIST = "hot_list";

    public ArticleCommentServiceImpl(CommentJpaRepository commentJpaRepository, CommentVoteJpaRepository commentVoteJpaRepository, ReplyJpaRepository replyJpaRepository, ArticleJpaRepository articleJpaRepository, UserJpaRepository userJpaRepository) {
        super(commentJpaRepository, commentVoteJpaRepository, replyJpaRepository, userJpaRepository);
        this.commentJpaRepository = commentJpaRepository;
        this.replyJpaRepository = replyJpaRepository;
        this.userJpaRepository = userJpaRepository;
        this.articleJpaRepository = articleJpaRepository;
    }

    @Override
    public String articleCommentReplyDel(String commentId, String replyId, String articleId) {
        //更新文章信息
        Response response = new Response();
        int commentsNeedToDel = super.commentReplyDel(commentId, replyId);
        if (commentsNeedToDel == -Status.COMMENT_HAS_DELETE) {
            response.sendFailure(Status.COMMENT_HAS_DELETE, "评论不存在或已被删除");
        }
        Article article = articleJpaRepository.findByArticleId(articleId);
        int comments = article.getComments();
        int newComments = comments - commentsNeedToDel;//删除后的评论数量
        articleJpaRepository.updateCommentsByArticleId(newComments, article.getArticleId());
        response.insert("comments", newComments);
        return response.sendSuccess();
    }

    /**
     * @param text      评论
     * @param articleId 被评论的文章ID
     * @return 评论信息
     */
    @Override
    public String publishComment(String text, String articleId) {
        Response response = new Response();
        Comment comment = new Comment();
        long createTime = System.currentTimeMillis();
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        //设置评论信息
        comment.setDiscusser(userId);
        comment.setText(text);
        comment.setCreateTime(createTime);
        comment.setTargetId(articleId);
        comment.setMode(Constant.MODEL_ARTICLE);
        Article article = articleJpaRepository.findByArticleId(articleId);
        comment.setLevel(article.getLevels() + 1);
        commentJpaRepository.save(comment);

        //修改文章评论数，修改用户的评论数
        User user = userJpaRepository.findByUserId(userId);
        userJpaRepository.updateCommentsByUserId(user.getComments() + 1, userId);
        articleJpaRepository.updateLevelsAndCommentsByArticleId(article.getLevels() + 1, article.getComments() + 1, articleId);

        //返回评论信息
        UserSimple userSimple = new UserSimple(user);
        CommentBasic commentBasic = new CommentBasic(comment);
        commentBasic.isAuthor(true);
        CommentList commentList = new CommentList(commentBasic, userSimple);//封装成list数据发送

        response.insert(commentList);
        return response.sendSuccess();
    }


    @Override
    public String getHotCommentList(String articleId) {
        log.info("getHotCommentList");
        Response response = new Response();
        List<Comment> hotCommentList = commentJpaRepository.findAllBySupportIsGreaterThan(Constant.HOT_COMMENT);
        if (hotCommentList.size() == 0) {
            response.insert(HOT_LIST, false);
            log.info("getHotCommentList----" + response);
        } else {
            response.insert(HOT_LIST, super.getCommentList(hotCommentList.iterator(),articleId));
            log.info("getHotCommentList-----" + response);
        }
        return response.sendSuccess();
    }

    @Override
    public String publishReply(String commentId, String text, String discusserId, String articleId) {

        Response response = new Response();
        Reply reply = new Reply();
        if (commentJpaRepository.existsByCommentId(commentId)) {
            String userId = SecurityContextHolder.getContext().getAuthentication().getName();
            //存储回复信息
            long createTime = System.currentTimeMillis();
            reply.setCommentId(commentId);
            reply.setText(text);
            reply.setCreateTime(createTime);
            reply.setReplier(userId);
            reply.setDiscusser(discusserId);
            reply.setTargetId(articleId);
            reply.setMode(Constant.MODEL_ARTICLE);
            replyJpaRepository.save(reply);

            //处理文章，用户，评论的信息
            User user = userJpaRepository.findByUserId(userId);
            User discusser = userJpaRepository.findByUserId(discusserId);
            int children = commentJpaRepository.findChildrenByCommentId(commentId) + 1;
            int comments = articleJpaRepository.findCommentsByArticleId(articleId) + 1;
            commentJpaRepository.updateChildrenByCommentId(children, commentId);
            articleJpaRepository.updateCommentsByArticleId(comments, articleId);
            userJpaRepository.updateCommentsByUserId(user.getComments() + 1, userId);

            //发给前端的数据
            ReplyBasic replyBasic = new ReplyBasic(reply,discusser.getNickname());
            UserSimple userSimple = new UserSimple(user);
            replyBasic.isAuthor(true);
            ReplyList replyList = new ReplyList(replyBasic, userSimple);//封装成list数据发送

            response.insert(LIST, replyList);

            return response.sendSuccess();
        } else {
            return response.sendFailure(Status.COMMENT_HAS_DELETE, "评论已删除");
        }
    }
}
