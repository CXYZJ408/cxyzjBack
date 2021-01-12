package com.cxyzj.cxyzjback.Service.impl.User.front;


import com.cxyzj.cxyzjback.Bean.Article.*;
import com.cxyzj.cxyzjback.Bean.User.Attention;
import com.cxyzj.cxyzjback.Bean.User.User;
import com.cxyzj.cxyzjback.Data.Article.ArticleBasic;
import com.cxyzj.cxyzjback.Data.Article.ArticleLabelBasic;
import com.cxyzj.cxyzjback.Data.Comment.*;
import com.cxyzj.cxyzjback.Data.Other.PageData;
import com.cxyzj.cxyzjback.Data.User.front.OtherDetails;
import com.cxyzj.cxyzjback.Data.User.front.UserArticleList;
import com.cxyzj.cxyzjback.Data.User.front.UserSimple;
import com.cxyzj.cxyzjback.Repository.Article.ArticleCollectionJpaRepository;
import com.cxyzj.cxyzjback.Repository.Article.ArticleJpaRepository;
import com.cxyzj.cxyzjback.Repository.Article.ArticleLabelJpaRepository;
import com.cxyzj.cxyzjback.Repository.Article.DraftJpaRepository;
import com.cxyzj.cxyzjback.Repository.Comment.CommentJpaRepository;
import com.cxyzj.cxyzjback.Repository.Comment.CommentVoteJpaRepository;
import com.cxyzj.cxyzjback.Repository.Comment.ReplyJpaRepository;
import com.cxyzj.cxyzjback.Repository.User.UserAttentionJpaRepository;
import com.cxyzj.cxyzjback.Repository.User.UserJpaRepository;
import com.cxyzj.cxyzjback.Service.Interface.User.front.UserListGetService;
import com.cxyzj.cxyzjback.Utils.Constant;
import com.cxyzj.cxyzjback.Utils.ListToMap;
import com.cxyzj.cxyzjback.Utils.Response;
import com.cxyzj.cxyzjback.Utils.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @Author 夏
 * @Date 15:50 2018/8/30
 * @Description: 用户列表信息获取
 * @checked false
 */

@Service
public class UserListGetServiceImpl implements UserListGetService {

    private final UserJpaRepository userJpaRepository;

    private final UserAttentionJpaRepository userAttentionJpaRepository;

    private final ArticleLabelJpaRepository articleLabelJpaRepository;

    private final ArticleJpaRepository articleJpaRepository;

    private final DraftJpaRepository draftJpaRepository;

    private final CommentJpaRepository commentJpaRepository;

    private final ReplyJpaRepository replyJpaRepository;

    private final CommentVoteJpaRepository commentVoteJpaRepository;

    private final ArticleCollectionJpaRepository articleCollectionJpaRepository;

    @Autowired
    public UserListGetServiceImpl(UserJpaRepository userJpaRepository, UserAttentionJpaRepository userAttentionJpaRepository, ArticleLabelJpaRepository articleLabelJpaRepository, ArticleJpaRepository articleJpaRepository, DraftJpaRepository draftJpaRepository, CommentJpaRepository commentJpaRepository, ReplyJpaRepository replyJpaRepository, CommentVoteJpaRepository commentVoteJpaRepository, ArticleCollectionJpaRepository articleCollectionJpaRepository) {
        this.userJpaRepository = userJpaRepository;
        this.userAttentionJpaRepository = userAttentionJpaRepository;
        this.articleLabelJpaRepository = articleLabelJpaRepository;
        this.articleJpaRepository = articleJpaRepository;
        this.draftJpaRepository = draftJpaRepository;
        this.commentJpaRepository = commentJpaRepository;
        this.replyJpaRepository = replyJpaRepository;
        this.commentVoteJpaRepository = commentVoteJpaRepository;
        this.articleCollectionJpaRepository = articleCollectionJpaRepository;
    }

    /**
     * @param userId  用户ID
     * @param pageNum 页码（从0开始）
     * @return 关注列表
     * @checked true
     */
    @Override
    public String getAttentionList(String userId, int pageNum) {
        Response response = new Response();
        Page<Attention> attentionPage = pageSelect(pageNum, Constant.FOCUS, userId);
        PageData pageData = new PageData(attentionPage, pageNum);
        ArrayList<OtherDetails> otherDetailsArrayList = getAttentionsOrFans(attentionPage.iterator());
        response.insert(pageData);
        response.insert("attentions", otherDetailsArrayList);
        return response.sendSuccess();
    }

    /**
     * @param userId  用户ID
     * @param pageNum 页码（从0开始）
     * @return 粉丝列表
     * @checked true
     */
    @Override
    public String getFansList(String userId, int pageNum) {
        Response response = new Response();
        Page<Attention> attentionPage = pageSelect(pageNum, Constant.FOLLOWED, userId);
        PageData pageData = new PageData(attentionPage, pageNum);
        ArrayList<OtherDetails> otherDetailsArrayList = getAttentionsOrFans(attentionPage.iterator());
        response.insert(pageData);
        response.insert("fans", otherDetailsArrayList);
        return response.sendSuccess();
    }


    @Override
    public String getArticleList(String userId) {
        Response response = new Response();
        String userId2 = SecurityContextHolder.getContext().getAuthentication().getName();//获取用户ID
        boolean isAuthor = userId.equals(userId2);
        List<Article> articleList = articleJpaRepository.findAllByUserId(userId);//获取用户所有已发布文章
        List<Draft> draftList = draftJpaRepository.findAllByUserId(userId);//获取用户所有的草稿
        ArrayList<UserArticleList> userArticleList = new ArrayList<>();
        ArrayList<ArticleBasic> articleBasicArrayList = new ArrayList<>();
        if (articleList.size() == 0) {//没有已发布文章
            if (isAuthor) {//是作者本人
                draftList.forEach(draft -> {//草稿只对作者可见
                    ArticleBasic articleBasic = new ArticleBasic(draft);
                    articleBasic.IsAuthor(true);
                    articleBasicArrayList.add(articleBasic);
                });
            }
        } else if (draftList.size() == 0) {//没有草稿
            articleList.forEach(article -> {
                ArticleBasic articleBasic = new ArticleBasic(article);
                articleBasic.IsAuthor(isAuthor);
                boolean collected = articleCollectionJpaRepository.existsByArticleIdAndUserId(article.getArticleId(), userId2);
                articleBasic.set_collected(collected);
                articleBasicArrayList.add(articleBasic);
            });
        } else {//有草稿，也有已发布文章
            TreeMap<String, Integer> articleMap = new TreeMap<>();
            for (int index = 0; index < articleList.size(); index++) {//先处理已发布文章
                Article article = articleList.get(index);
                articleMap.put(article.getArticleId(), index);
                ArticleBasic articleBasic = new ArticleBasic(article);
                boolean collected = articleCollectionJpaRepository.existsByArticleIdAndUserId(article.getArticleId(), userId2);
                articleBasic.set_collected(collected);
                articleBasic.IsAuthor(isAuthor);
                articleBasicArrayList.add(articleBasic);
            }
            if (isAuthor) {//是作者就将草稿信息加入，不是则不加入
                draftList.forEach(draft -> {
                    if (draft.getArticleId() != null) {//绑定了文章，不为空
                        int index = articleMap.get(draft.getArticleId());
                        ArticleBasic article = articleBasicArrayList.get(index);
                        if (article.getUpdate_time() < draft.getUpdateTime()) {//草稿更新的时间晚，将原始的文章替换掉
                            ArticleBasic articleBasic = new ArticleBasic(draft);
                            articleBasic.IsAuthor(true);
                            articleBasicArrayList.set(index, articleBasic);
                        }
                    } else {//未绑定文章
                        ArticleBasic articleBasic = new ArticleBasic(draft);
                        articleBasic.IsAuthor(true);
                        articleBasicArrayList.add(articleBasic);
                    }
                });
            }
        }
        List<ArticleLabel> articleLabelBasics = articleLabelJpaRepository.findAll();//将标签信息从list转为map
        ListToMap<ArticleLabel> articleLabelListToMap = new ListToMap<>();
        Map<String, ArticleLabel> articleLabelMap = articleLabelListToMap.getMap(articleLabelBasics, Constant.LABEL_ID_NAME, ArticleLabel.class);
        articleBasicArrayList.forEach(articleBasic -> {//封装数据
            ArticleLabelBasic articleLabel = new ArticleLabelBasic(articleLabelMap.get(articleBasic.getLabel_id()));
            userArticleList.add(new UserArticleList(articleBasic, articleLabel));
        });
        response.insert("list", userArticleList);
        return response.sendSuccess();
    }

    /**
     * @param userId  要获取的用户id
     * @param pageNum 页码数
     * @return 用户评论列表信息
     */
    @Override
    public String getUserCommentList(String userId, int pageNum) {
        Response response = new Response();
        Page<Comment> commentPage = getComment(pageNum, userId);
        PageData pageData = new PageData(commentPage, pageNum);
        ArrayList<UserCommentList> userCommentList = getUserCommentList(commentPage.iterator());
        response.insert(pageData);
        response.insert("comments", userCommentList);
        return response.sendSuccess();
    }


    @Override
    public String getUserReplyList(String userId, int pageNum) {
        Response response = new Response();
        Page<Reply> replyPage = getReplyByReplier(pageNum, userId);
        PageData pageData = new PageData(replyPage, pageNum);
        ArrayList<UserReplyList> userReplyList = getUserReplyList(replyPage.iterator());
        response.insert(pageData);
        response.insert("replies", userReplyList);
        return response.sendSuccess();
    }

    @Override
    public String getOtherToUserReplyList(String userId, int pageNum) {
        Response response = new Response();
        Page<Reply> replyPage = getReplyByDiscusser(pageNum, userId);
        PageData pageData = new PageData(replyPage, pageNum);
        ArrayList<UserReplyList> userReplyList = getUserReplyList(replyPage.iterator());
        response.insert(pageData);
        response.insert("replies", userReplyList);
        return response.sendSuccess();
    }

    @Override
    public String getCollectedArticleList(String userId, int pageNum) {
        Response response = new Response();
        Sort sort = new Sort(Sort.Direction.DESC, Constant.ARTICLE_ID_NAME);
        Pageable pageable = PageRequest.of(pageNum, Constant.PAGE_ARTICLE, sort);
        String userId2 = SecurityContextHolder.getContext().getAuthentication().getName();//获取用户ID
        ArrayList<UserArticleList> resultList = new ArrayList<>();
        Page<ArticleCollection> articleCollectionPage = articleCollectionJpaRepository.findAllByUserId(pageable, userId);
        if (!articleCollectionPage.isEmpty()) {
            ArrayList<String> articleIdList = new ArrayList<>();
            ArrayList<String> userIdList = new ArrayList<>();

            for (ArticleCollection articleCollection : articleCollectionPage) {
                articleIdList.add(articleCollection.getArticleId());
            }
            List<Article> articleList = articleJpaRepository.findByArticleIdIn(articleIdList);//查找文章信息
            articleList.forEach(article -> userIdList.add(article.getUserId()));
            List<User> userList = userJpaRepository.findByUserIdIn(userIdList);//获取用户信息
            ListToMap<User> userListToMap = new ListToMap<>();
            HashMap<String, User> userHashMap = userListToMap.getMap(userList, Constant.USER_ID_NAME, User.class);
            List<ArticleLabel> articleLabels = articleLabelJpaRepository.findAll();
            ListToMap<ArticleLabel> labelListToMap = new ListToMap<>();
            HashMap<String, ArticleLabel> articleLabelHashMap = labelListToMap.getMap(articleLabels, Constant.LABEL_ID_NAME, ArticleLabel.class);
            articleList.forEach(article -> {
                ArticleBasic articleBasic = new ArticleBasic(article);
                boolean collected = articleCollectionJpaRepository.existsByArticleIdAndUserId(article.getArticleId(), userId2);
                articleBasic.set_collected(collected);
                ArticleLabelBasic articleLabelBasic = new ArticleLabelBasic(articleLabelHashMap.get(article.getLabelId()));
                OtherDetails otherDetails = new OtherDetails(userHashMap.get(article.getUserId()));
                UserArticleList userArticleList = new UserArticleList(articleBasic, articleLabelBasic, otherDetails);
                resultList.add(userArticleList);
            });
        }
        PageData pageData = new PageData(articleCollectionPage, pageNum);//页面数据
        response.insert(pageData);
        response.insert("list", resultList);
        return response.sendSuccess();
    }

    private Page<Comment> getComment(int pageNum, String userId) {
        Sort sort = new Sort(Sort.Direction.DESC, Constant.COMMENT_SORT);
        Pageable pageable = PageRequest.of(pageNum, Constant.PAGE_COMMENT, sort);
        return commentJpaRepository.findAllByDiscusser(pageable, userId);
    }

    private Page<Reply> getReplyByReplier(int pageNum, String userId) {
        Sort sort = new Sort(Sort.Direction.DESC, Constant.COMMENT_SORT);
        Pageable pageable = PageRequest.of(pageNum, Constant.PAGE_COMMENT, sort);
        return replyJpaRepository.findAllByReplier(pageable, userId);
    }


    private Page<Reply> getReplyByDiscusser(int pageNum, String userId) {
        Sort sort = new Sort(Sort.Direction.DESC, Constant.COMMENT_SORT);
        Pageable pageable = PageRequest.of(pageNum, Constant.PAGE_COMMENT, sort);
        return replyJpaRepository.findAllByDiscusser(pageable, userId);
    }

    /**
     * @param pageNum 指定的页数
     * @param status  指定状态
     * @param userId  指定要查询的用户id
     * @return 分页查询结果
     * @checked true
     */
    private Page<Attention> pageSelect(int pageNum, int status, String userId) {
        Sort sort = new Sort(Sort.DEFAULT_DIRECTION, "userId");
        Pageable pageable = PageRequest.of(pageNum, Constant.PAGE_ATTENTION_USER, sort);
        return userAttentionJpaRepository.findAllByUserIdAndStatusOrUserIdAndStatus(pageable, userId, status, userId, Constant.EACH);//互相关注也包含在内
    }

    /**
     * @param attentionIterator 要处理的用户的迭代器
     * @return 处理后的用户信息
     * @checked true
     */
    private ArrayList<OtherDetails> getAttentionsOrFans(Iterator<Attention> attentionIterator) {
        HashMap<String, Integer> userIdMap = new HashMap<>();
        ArrayList<String> userIdList = new ArrayList<>();
        while (attentionIterator.hasNext()) {
            Attention attention = attentionIterator.next();
            userIdMap.put(attention.getTargetUser(), attention.getStatus());
            userIdList.add(attention.getTargetUser());
        }
        ArrayList<OtherDetails> otherDetailsArrayList = new ArrayList<>();
        List<User> userList = userJpaRepository.findAllById(userIdList);
        for (User user : userList) {
            int status = userIdMap.get(user.getUserId());
            boolean isFollow = status == 203 || status == 201;
            otherDetailsArrayList.add(new OtherDetails(user, isFollow));
        }
        return otherDetailsArrayList;
    }

    /**
     * @param CommentIterator 获取评论列表迭代器
     * @return 评论列表信息
     */
    private ArrayList<UserCommentList> getUserCommentList(Iterator<Comment> CommentIterator) {
        ArrayList<Comment> comments = new ArrayList<>();//评论列表信息
        ArrayList<UserComment> userComments = new ArrayList<>();//用户评论信息
        HashSet<String> targetIdSet = new HashSet<>();//目标对象id列表
        HashSet<String> userIdSet = new HashSet<>();//用户id列表
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        while (CommentIterator.hasNext()) {//迭代评论信息
            Comment comment = CommentIterator.next();
            comments.add(comment);
            userComments.add(new UserComment(comment));
            userIdSet.add(comment.getDiscusser());
            targetIdSet.add(comment.getTargetId());
        }
        //目前只实现文章的用户评论列表获取
        ListToMap<Article> articleListToMap = new ListToMap<>();
        List<Article> articleList = articleJpaRepository.findAllById(targetIdSet);
        HashMap<String, Article> articleHashSet = articleListToMap.getMap(articleList, Constant.ARTICLE_ID_NAME, Article.class);//映射文章信息
        ListToMap<User> userListToMap = new ListToMap<>();
        List<User> userList = userJpaRepository.findAllById(userIdSet);
        HashMap<String, User> userHashMap = userListToMap.getMap(userList, Constant.USER_ID_NAME, User.class);//映射用户信息
        boolean existVote = commentVoteJpaRepository.existsByUserId(userId);
        ArrayList<UserCommentList> resultList = new ArrayList<>();
        for (int i = 0; i < comments.size(); i++) {//封装数据
            Comment comment = comments.get(i);
            User user = userHashMap.get(comment.getDiscusser());//获取映射的用户信息
            Article article = articleHashSet.get(comment.getTargetId());//获取映射的文章信息
            UserComment userComment = userComments.get(i);
            if (userId.equals(comment.getDiscusser())) {//判断是否是作者
                userComment.isAuthor(true);//是作者
            } else if (existVote) {
                //不是作者
                CommentVote commentVote = commentVoteJpaRepository.findByUserIdAndCommentReplyId(userId, comment.getCommentId());//读取投票信息
                if (commentVote != null) {
                    switch (commentVote.getStatus()) {
                        case Constant.SUPPORT:
                            userComment.set_support(true);
                            break;
                        case Constant.OBJECT:
                            userComment.set_obj(true);
                            break;
                    }
                }
            }
            userComment.setTarget_id(comment.getTargetId());
            userComment.setComment_title(article.getTitle());
            userComment.setModel(comment.getMode());
            UserSimple userSimple = new UserSimple(user);
            UserCommentList commentList = new UserCommentList(userComment, userSimple);
            resultList.add(commentList);
        }
        return resultList;
    }

    /**
     * @param replyIterator 获取回复列表迭代器
     * @return 回复列表信息
     */
    private ArrayList<UserReplyList> getUserReplyList(Iterator<Reply> replyIterator) {
        ArrayList<Reply> replies = new ArrayList<>();//评论列表信息
        ArrayList<UserReply> userReplies = new ArrayList<>();//用户评论信息
        HashSet<String> targetIdSet = new HashSet<>();//目标对象id列表
        HashSet<String> userIdSet = new HashSet<>();//用户id列表
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        while (replyIterator.hasNext()) {//迭代评论信息
            Reply reply = replyIterator.next();
            replies.add(reply);
            userReplies.add(new UserReply(reply));
            userIdSet.add(reply.getDiscusser());
            userIdSet.add(reply.getReplier());
            targetIdSet.add(reply.getTargetId());
        }
        //目前只实现文章的用户回复列表获取
        ListToMap<Article> articleListToMap = new ListToMap<>();
        List<Article> articleList = articleJpaRepository.findAllById(targetIdSet);
        HashMap<String, Article> articleHashSet = articleListToMap.getMap(articleList, Constant.ARTICLE_ID_NAME, Article.class);//映射文章信息
        ListToMap<User> userListToMap = new ListToMap<>();
        List<User> userList = userJpaRepository.findAllById(userIdSet);
        HashMap<String, User> userHashMap = userListToMap.getMap(userList, Constant.USER_ID_NAME, User.class);//映射用户信息
        boolean existVote = commentVoteJpaRepository.existsByUserId(userId);
        ArrayList<UserReplyList> resultList = new ArrayList<>();
        for (int i = 0; i < replies.size(); i++) {//封装数据
            Reply reply = replies.get(i);
            User discusser = userHashMap.get(reply.getDiscusser());//获取映射的用户信息,评论者
            User replier = userHashMap.get(reply.getReplier());//获取映射的用户信息,评论回复者
            Article article = articleHashSet.get(reply.getTargetId());//获取映射的文章信息
            UserReply userReply = userReplies.get(i);
            if (userId.equals(reply.getReplier())) {//判断是否是作者
                userReply.isAuthor(true);//是作者
            } else if (existVote) {
                //不是作者
                CommentVote commentVote = commentVoteJpaRepository.findByUserIdAndCommentReplyId(userId, reply.getReplyId());//读取投票信息
                if (commentVote != null) {
                    switch (commentVote.getStatus()) {
                        case Constant.SUPPORT:
                            userReply.set_support(true);
                            break;
                        case Constant.OBJECT:
                            userReply.set_obj(true);
                            break;
                    }
                }
            }
            userReply.setTarget_id(reply.getTargetId());
            userReply.setComment_title(article.getTitle());
            userReply.setModel(reply.getMode());
            UserSimple discusserSimple = new UserSimple(discusser);
            UserSimple replierSimple = new UserSimple(replier);
            UserReplyList commentList = new UserReplyList(userReply, discusserSimple, replierSimple);
            resultList.add(commentList);
        }
        return resultList;
    }
}
