package com.cxyzj.cxyzjback.Service.impl.Comment;

import com.cxyzj.cxyzjback.Repository.Comment.CommentJpaRepository;
import com.cxyzj.cxyzjback.Repository.Comment.CommentVoteJpaRepository;
import com.cxyzj.cxyzjback.Repository.Comment.ReplyJpaRepository;
import com.cxyzj.cxyzjback.Repository.User.UserJpaRepository;
import com.cxyzj.cxyzjback.Service.Interface.Comment.CommentService;
import com.cxyzj.cxyzjback.Utils.Constant;
import com.cxyzj.cxyzjback.Utils.Response;
import com.cxyzj.cxyzjback.Bean.Article.*;
import com.cxyzj.cxyzjback.Utils.Status;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;

import com.cxyzj.cxyzjback.Bean.User.User;
import com.cxyzj.cxyzjback.Data.Comment.CommentBasic;
import com.cxyzj.cxyzjback.Data.Comment.CommentList;
import com.cxyzj.cxyzjback.Data.Comment.ReplyBasic;
import com.cxyzj.cxyzjback.Data.Comment.ReplyList;
import com.cxyzj.cxyzjback.Data.Other.PageData;
import com.cxyzj.cxyzjback.Data.User.front.UserSimple;

import com.cxyzj.cxyzjback.Utils.ListToMap;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.*;


/**
 * @Package com.cxyzj.cxyzjback.Service.impl.Comment
 * @Author Yaser
 * @Date 2018/11/09 10:54
 * @Description:
 */
@Slf4j
public class CommentServiceImpl implements CommentService {
    private final CommentJpaRepository commentJpaRepository;

    private final CommentVoteJpaRepository commentVoteJpaRepository;

    private final ReplyJpaRepository replyJpaRepository;

    private final UserJpaRepository userJpaRepository;
    private static final String SUPPORT = "support";
    private static final String OBJECT = "object";
    private static final String LIST = "list";

    @Autowired
    public CommentServiceImpl(CommentJpaRepository commentJpaRepository, CommentVoteJpaRepository commentVoteJpaRepository, ReplyJpaRepository replyJpaRepository, UserJpaRepository userJpaRepository) {
        this.commentJpaRepository = commentJpaRepository;
        this.commentVoteJpaRepository = commentVoteJpaRepository;
        this.replyJpaRepository = replyJpaRepository;
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    public int commentReplyDel(String commentId, String replyId) {
        //如果存在replyId则删除回复，否则删除评论，不管删除哪个commentId必定有
        Comment comment = commentJpaRepository.findByCommentId(commentId);
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        int commentsNeedToDel = 1;
        if (replyId != null) {
            //删除回复
            if (replyJpaRepository.existsByReplyId(replyId)) {
                comment.setChildren(comment.getChildren() - 1);
                commentJpaRepository.save(comment);
                replyJpaRepository.deleteByReplyId(replyId);
                //删除commentVote
                commentVoteJpaRepository.deleteByCommentReplyIdAndUserId(replyId, userId);
            } else {
                return -Status.COMMENT_HAS_DELETE;
            }
        } else {
            //删除comment
            if (!commentJpaRepository.existsByCommentId(commentId)) {
                return -Status.COMMENT_HAS_DELETE;
            }
            if (replyJpaRepository.existsByCommentId(commentId)) {
                //如果存在reply，则删除reply
                commentsNeedToDel += replyJpaRepository.countByCommentId(commentId);//加上需要删除的回复的数量
                replyJpaRepository.deleteByCommentId(commentId);
            }
            //删除commentVote
            commentVoteJpaRepository.deleteByCommentReplyIdAndUserId(commentId, userId);
            commentJpaRepository.deleteByCommentId(commentId);//删除评论

        }
        return commentsNeedToDel;
    }

    @Override
    public String support(String commentId, String replyId, String targetId) throws NoSuchFieldException {
        return commentSupportObject(Constant.SUPPORT, commentId, replyId, targetId);
    }

    @Override
    public String object(String commentId, String replyId, String targetId) throws NoSuchFieldException {
        return commentSupportObject(Constant.OBJECT, commentId, replyId, targetId);
    }

    @Override
    public String supportDel(String commentId, String replyId, String targetId) throws NoSuchFieldException {
        return commentSupportObjectDel(Constant.SUPPORT, commentId, replyId);
    }

    @Override
    public String objectDel(String commentId, String replyId, String targetId) throws NoSuchFieldException {
        return commentSupportObjectDel(Constant.OBJECT, commentId, replyId);

    }


    @Override
    public String getCommentList(String targetId, int pageNum) {
        log.info("getUserCommentList");
        Response response = new Response();
        Page<Comment> commentPage = getComment(pageNum, targetId);
        PageData pageData = new PageData(commentPage, pageNum);
        response.insert(LIST, getCommentList(commentPage.iterator(), targetId));
        response.insert(pageData);
        return response.sendSuccess();
    }

    @Override
    public String getReplyList(String commentId, int pageNum, String targetId) {

        Response response = new Response();
        Page<Reply> replyPage = getReply(pageNum, commentId);
        PageData pageData = new PageData(replyPage, pageNum);
        response.insert(LIST, getReplyList(replyPage.iterator(),targetId));
        response.insert(pageData);
        return response.sendSuccess();
    }

    private String commentSupportObjectDel(int state, String commentId, String replyId) throws NoSuchFieldException {
        Response response = new Response();
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        String commentReplyId = commentId == null ? replyId : commentId;
        if (!commentVoteJpaRepository.existsByUserIdAndCommentReplyId(userId, commentReplyId)) {
            return response.sendFailure(Status.USER_NOT_SUPPORT_OR_OBJECT, "未支持或反对该评论");
        }
        if (commentId != null) {
            if (commentJpaRepository.existsByCommentId(commentId)) {//存在该评论
                commentVoteJpaRepository.deleteByCommentReplyIdAndUserId(commentId, userId);//删除投票信息
                if (state == Constant.SUPPORT) {//支持
                    //更新投票信息
                    int support = commentJpaRepository.findSupportByCommentId(commentId) - 1;
                    commentJpaRepository.updateCommentSupport(support, commentId);
                    response.insert(SUPPORT, support);
                } else {//反对
                    // 更新投票信息
                    int object = commentJpaRepository.findObjectByCommentId(commentId) - 1;
                    commentJpaRepository.updateCommentObject(object, commentId);
                    response.insert(OBJECT, object);
                }
                return response.sendSuccess();
            }

        } else if (replyId != null) {
            if (replyJpaRepository.existsByReplyId(replyId)) {//存在该回复
                commentVoteJpaRepository.deleteByCommentReplyIdAndUserId(replyId, userId);//删除投票信息
                if (state == Constant.SUPPORT) {//支持
                    //更新投票信息
                    int support = replyJpaRepository.findSupportByReplyId(replyId) - 1;
                    replyJpaRepository.updateReplySupport(support, replyId);
                    response.insert(SUPPORT, support);
                } else {//反对
                    //更新投票信息
                    int object = replyJpaRepository.findObjectByReplyId(replyId) - 1;
                    replyJpaRepository.updateReplyObject(object, replyId);
                    response.insert(OBJECT, object);
                }
                return response.sendSuccess();
            }
        } else {
            throw new NoSuchFieldException("commentId字段与replyId字段不可同时为空！");
        }
        return response.sendFailure(Status.COMMENT_HAS_DELETE, "评论已删除或不存在");
    }

    private String commentSupportObject(int state, String commentId, String replyId, String targetId) throws NoSuchFieldException {
        Response response = new Response();
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        CommentVote commentVote = new CommentVote();
        String commentReplyId = commentId == null ? replyId : commentId;
        if (commentVoteJpaRepository.existsByUserIdAndCommentReplyId(userId, commentReplyId)) {//如果已经支持或反对过
            return response.sendFailure(Status.USER_HAS_SUPPORT_OR_OBJECT, "用户已支持或反对过该评论了");
        }
        if (commentId != null) {
            if (commentJpaRepository.existsByCommentId(commentId)) {
                //评论存在
                commentVote.setUserId(userId);
                commentVote.setCommentReplyId(commentId);
                commentVote.setTargetId(targetId);
                commentVote.setStatus(state);
                commentVoteJpaRepository.save(commentVote);
                //comment表中的support或object数+1
                if (state == Constant.SUPPORT) {
                    int support = commentJpaRepository.findSupportByCommentId(commentId) + 1;
                    commentJpaRepository.updateCommentSupport(support, commentId);
                    response.insert(SUPPORT, support);
                } else {
                    int object = commentJpaRepository.findObjectByCommentId(commentId) + 1;
                    commentJpaRepository.updateCommentObject(object, commentId);
                    response.insert(OBJECT, object);
                }
                return response.sendSuccess();
            }
        } else if (replyId != null) {
            if (replyJpaRepository.existsByReplyId(replyId)) {
                commentVote.setUserId(userId);
                commentVote.setTargetId(targetId);
                commentVote.setCommentReplyId(replyId);
                commentVote.setStatus(state);
                commentVoteJpaRepository.save(commentVote);
                //reply表中的support或object数+1
                if (state == Constant.SUPPORT) {
                    int support = replyJpaRepository.findSupportByReplyId(replyId) + 1;
                    replyJpaRepository.updateReplySupport(support, replyId);
                    response.insert(SUPPORT, support);
                } else {
                    int object = replyJpaRepository.findObjectByReplyId(replyId) + 1;
                    replyJpaRepository.updateReplyObject(object, replyId);
                    response.insert(OBJECT, object);
                }
                return response.sendSuccess();
            }
        } else {
            throw new NoSuchFieldException("commentId字段与replyId字段不可同时为空！");
        }
        return response.sendFailure(Status.COMMENT_HAS_DELETE, "评论已删除或不存在");
    }

    private Page<Comment> getComment(int pageNum, String targetId) {
        Sort sort = new Sort(Sort.Direction.DESC, Constant.COMMENT_SORT);
        Pageable pageable = PageRequest.of(pageNum, Constant.PAGE_COMMENT, sort);
        return commentJpaRepository.findAllByTargetId(pageable, targetId);
    }

    private Page<Reply> getReply(int pageNum, String commentId) {
        Sort sort = new Sort(Sort.DEFAULT_DIRECTION, Constant.REPLY_SORT);
        Pageable pageable = PageRequest.of(pageNum, Constant.PAGE_REPLY, sort);
        return replyJpaRepository.findAllByCommentId(pageable, commentId);
    }

    /**
     * @param ReplyIterator 获取回复列表迭代器
     * @return 回复列表信息
     */
    private ArrayList<ReplyList> getReplyList(Iterator<Reply> ReplyIterator, String targetId) {
        ArrayList<Reply> replies = new ArrayList<>();
        ArrayList<ReplyBasic> replyBasics = new ArrayList<>();//回复列表
        ArrayList<String> userIdList = new ArrayList<>();//用户id列表
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        boolean existVote = commentVoteJpaRepository.existsByTargetId(targetId);
        while (ReplyIterator.hasNext()) {
            Reply reply = ReplyIterator.next();
            replies.add(reply);
            User discusser = userJpaRepository.findByUserId(reply.getDiscusser());//获取被评论用户的信息
            ReplyBasic replyBasic = new ReplyBasic(reply, discusser.getNickname());
            if (userId.equals(reply.getReplier())) {//判断是否是作者
                replyBasic.isAuthor(true);//是作者
            } else if(existVote){
                //不是作者
                CommentVote commentVote = commentVoteJpaRepository.findByUserIdAndCommentReplyId(userId, reply.getReplyId());//判断是否支持过该评论
                if (commentVote != null) {
                    switch (commentVote.getStatus()) {
                        case Constant.SUPPORT:
                            replyBasic.set_support(true);
                            break;
                        case Constant.OBJECT:
                            replyBasic.set_obj(true);
                            break;
                    }
                }
            }
            replyBasics.add(replyBasic);
            userIdList.add(reply.getReplier());//读取回复用户
        }
        ListToMap<User> userListToMap = new ListToMap<>();
        List<User> userList = userJpaRepository.findAllById(userIdList);
        HashMap<String, User> userHashMap = userListToMap.getMap(userList, Constant.USER_ID_NAME, User.class);

        ArrayList<ReplyList> resultList = new ArrayList<>();
        for (int i = 0; i < replies.size(); i++) {
            //封装列表数据
            Reply reply = replies.get(i);
            User user = userHashMap.get(reply.getReplier());//获取回复用户
            UserSimple userSimple = new UserSimple(user);//封装用户数据
            ReplyBasic replyBasic = replyBasics.get(i);
            ReplyList replyList = new ReplyList(replyBasic, userSimple);//封装数据
            resultList.add(replyList);
        }
        return resultList;
    }

    /**
     * @param CommentIterator 获取评论列表迭代器
     * @return 评论列表信息
     */
    ArrayList<CommentList> getCommentList(Iterator<Comment> CommentIterator, String targetId) {
        log.info("---------getUserCommentList-------");
        ArrayList<Comment> comments = new ArrayList<>();
        ArrayList<CommentBasic> commentBasics = new ArrayList<>();//评论信息
        HashSet<String> userIdList = new HashSet<>();//用户id列表
        ArrayList<String> commentIdList = new ArrayList<>();//回复列表获取
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        boolean existVote = commentVoteJpaRepository.existsByTargetId(targetId);
        while (CommentIterator.hasNext()) {
            Comment comment = CommentIterator.next();
            comments.add(comment);
            CommentBasic commentBasic = new CommentBasic(comment);
            if (userId.equals(comment.getDiscusser())) {//判断是否是作者
                commentBasic.isAuthor(true);//是作者
            } else if (existVote) {
                //不是作者
                CommentVote commentVote = commentVoteJpaRepository.findByUserIdAndCommentReplyId(userId, comment.getCommentId());
                if (commentVote != null) {
                    switch (commentVote.getStatus()) {
                        case Constant.SUPPORT:
                            commentBasic.set_support(true);
                            break;
                        case Constant.OBJECT:
                            commentBasic.set_obj(true);
                            break;
                    }
                }
            }
            commentBasics.add(commentBasic);
            userIdList.add(comment.getDiscusser());
            if (comment.getChildren() > 0) {
                commentIdList.add(comment.getCommentId());
            }
        }
        ListToMap<User> userListToMap = new ListToMap<>();
        log.info("---------getUserList-------");
        List<User> userList = userJpaRepository.findAllById(userIdList);
        HashMap<String, User> userHashMap = userListToMap.getMap(userList, Constant.USER_ID_NAME, User.class);

        HashMap<String, ArrayList<ReplyList>> replyListHashMap = new HashMap<>();
        for (String commentId : commentIdList) {//获取每一个评论下5条回复
            Page<Reply> replyPage = getReply(0, commentId);//获取分页信息
            ArrayList<ReplyList> replyList = getReplyList(replyPage.iterator(),targetId);//获取列表
            replyListHashMap.put(commentId, replyList);//进行映射操作
        }

        ArrayList<CommentList> resultList = new ArrayList<>();
        for (int i = 0; i < comments.size(); i++) {//封装数据
            Comment comment = comments.get(i);
            User user = userHashMap.get(comment.getDiscusser());
            CommentBasic commentBasic = commentBasics.get(i);
            UserSimple userSimple = new UserSimple(user);
            ArrayList<ReplyList> replyList = replyListHashMap.get(comment.getCommentId());
            CommentList commentList = new CommentList(commentBasic, userSimple, replyList);
            resultList.add(commentList);
        }

        return resultList;
    }


    @Override
    public String publishComment(String text, String targetId) {
        //让子类实现
        return null;
    }

    @Override
    public String publishReply(String commentId, String text, String discusserId, String targetId) {
        //让子类实现
        return null;
    }
}
