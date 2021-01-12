package com.cxyzj.cxyzjback.Service.impl.Article;

import com.cxyzj.cxyzjback.Bean.Article.*;
import com.cxyzj.cxyzjback.Bean.User.User;
import com.cxyzj.cxyzjback.Data.Article.*;
import com.cxyzj.cxyzjback.Data.Other.PageData;
import com.cxyzj.cxyzjback.Data.User.front.OtherDetails;
import com.cxyzj.cxyzjback.Data.User.front.UserSimple;
import com.cxyzj.cxyzjback.Data.User.front.UserBasic;
import com.cxyzj.cxyzjback.Repository.Article.*;
import com.cxyzj.cxyzjback.Repository.Comment.CommentJpaRepository;
import com.cxyzj.cxyzjback.Repository.Comment.CommentVoteJpaRepository;
import com.cxyzj.cxyzjback.Repository.Comment.ReplyJpaRepository;
import com.cxyzj.cxyzjback.Repository.User.UserAttentionJpaRepository;
import com.cxyzj.cxyzjback.Repository.User.UserJpaRepository;
import com.cxyzj.cxyzjback.Service.Interface.Article.ArticleService;
import com.cxyzj.cxyzjback.Utils.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.*;

/**
 * @Auther: 夏
 * @DATE: 2018/9/12 10:10
 * @Description: 文章操作与文章信息获取的API（已检查过）
 * @checked true
 */
@Service
@Slf4j
public class ArticleServiceImpl implements ArticleService {

    private final ArticleJpaRepository articleJpaRepository;

    private final ArticleLabelJpaRepository articleLabelJpaRepository;

    private final UserJpaRepository userJpaRepository;

    private final ArticleCollectionJpaRepository articleCollectionJpaRepository;

    private final ReplyJpaRepository replyJpaRepository;

    private final CommentJpaRepository commentJpaRepository;

    private final CommentVoteJpaRepository commentVoteJpaRepository;

    private final UserAttentionJpaRepository userAttentionJpaRepository;

    private final DraftJpaRepository draftJpaRepository;

    @Autowired
    public ArticleServiceImpl(ArticleJpaRepository articleJpaRepository, ArticleLabelJpaRepository articleLabelJpaRepository, UserJpaRepository userJpaRepository, ArticleCollectionJpaRepository articleCollectionJpaRepository, ReplyJpaRepository replyJpaRepository, CommentJpaRepository commentJpaRepository, CommentVoteJpaRepository commentVoteJpaRepository, UserAttentionJpaRepository userAttentionJpaRepository, DraftJpaRepository draftJpaRepository) {
        this.articleJpaRepository = articleJpaRepository;
        this.articleLabelJpaRepository = articleLabelJpaRepository;
        this.userJpaRepository = userJpaRepository;
        this.articleCollectionJpaRepository = articleCollectionJpaRepository;
        this.replyJpaRepository = replyJpaRepository;
        this.commentJpaRepository = commentJpaRepository;
        this.commentVoteJpaRepository = commentVoteJpaRepository;
        this.userAttentionJpaRepository = userAttentionJpaRepository;
        this.draftJpaRepository = draftJpaRepository;
    }


    /**
     * @param title      文章标题
     * @param text       文章内容
     * @param labelId    文章类型
     * @param articleSum 文章概要
     * @param thumbnail  缩略图
     * @param articleId  文章状态
     * @param userId     用户id
     * @return 文章ID
     * @checked true
     */
    @Override
    public String publishArticle(String title, String text, String labelId, String articleSum,
                                 String thumbnail, String articleId, String userId) {
        Response response = new Response();
        Article article = new Article();
        if (!articleId.equals(Constant.NEWS)) {
            //上传的不是新的文章
            article.setArticleId(articleId);
            if (articleJpaRepository.existsByArticleId(articleId)) {
                Article articleJudge = articleJpaRepository.findByArticleId(articleId);//判断作者是否正确
                if (!articleJudge.getUserId().equals(userId)) {
                    response.sendFailure(Status.WRONG_AUTHOR, "作者信息有误");
                }
            }
            //旧文更新
            if (draftJpaRepository.existsByArticleIdOrDraftId(articleId, articleId)) {
                //草稿箱存在老版本
                draftJpaRepository.deleteByArticleIdOrDraftId(articleId, articleId);//删除
            }
        }
        article.setTitle(title);
        article.setText(text);
        article.setLabelId(labelId);
        article.setArticleSum(articleSum);
        article.setThumbnail(thumbnail);
        article.setUserId(userId);
        article.setStatusId(Constant.PUBLISH);
        article.setUpdateTime(System.currentTimeMillis());
        articleLabelJpaRepository.updateQuantityByLabelId(1, labelId);//label下的文章数+1
        article = articleJpaRepository.save(article);
        userJpaRepository.increaseArticlesByUserId(1, userId);//文章数+1
        response.insert("article_id", article.getArticleId());
        return response.sendSuccess();
    }

    /**
     * @param articleId 文章ID
     * @return 文章数据
     * @checked true
     * @Description: 访问文章获取文章详细数据
     * 以后可进行优化点：对于每一篇未缓存处理的文章，在初次访问可以做一个有时间限制(例如3分钟)的缓存，
     * 之后，每有一个人访问，这个时间就增加一点（例如增加1分钟），
     * 如果没有人访问则时间到了之后缓存自动删除
     */
    @Override
    public String articleDetails(String articleId) {
        Response response = new Response();
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();//获取访问用户id
        articleJpaRepository.updateViewsByArticleId(articleId);//更新文章访问次数
        Article article = articleJpaRepository.findByArticleId(articleId);//获取文章
        ArticleBasic articleBasic = new ArticleBasic(article);
        User articleUser = userJpaRepository.findByUserId(article.getUserId());//获取文章作者信息
        ArticleLabel articleLabel = articleLabelJpaRepository.findByLabelId(article.getLabelId());//获取文章标签信息

        if (userId.equals(article.getUserId())) {
            //是作者
            articleBasic.IsAuthor(true);
            response.insert(new UserBasic(articleUser));
        } else {
            //不是作者
            articleBasic.IsAuthor(false);
            if (articleCollectionJpaRepository.existsByArticleIdAndUserId(articleId, userId)) {//判断用户是否收藏了该文章
                articleBasic.set_collected(true);
            }
            boolean status = false;
            if (userAttentionJpaRepository.existsByUserIdAndTargetUser(userId, articleUser.getUserId())) {//判断是否关注
                status = userAttentionJpaRepository.findStatusByUserIdAndTargetUser(userId, articleUser.getUserId()) == Constant.FOCUS ||
                        userAttentionJpaRepository.findStatusByUserIdAndTargetUser(userId, articleUser.getUserId()) == Constant.EACH;
            }
            response.insert(new OtherDetails(articleUser, status));
        }
        response.insert("article", articleBasic);
        response.insert("label", new ArticleLabelBasic(articleLabel));
        return response.sendSuccess();
    }

    /**
     * @param articleId 文章ID
     * @return 是否收藏成功
     * @checked true
     */
    @Override
    public String collect(String articleId) {

        Response response = new Response();
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!articleCollectionJpaRepository.existsByArticleIdAndUserId(articleId, userId)) {
            //未收藏
            ArticleCollection articleCollection = new ArticleCollection();
            articleCollection.setArticleId(articleId);
            articleCollection.setUserId(userId);
            articleCollectionJpaRepository.save(articleCollection);
            articleJpaRepository.increaseCollectionsByArticleId(articleId);
            Article article = articleJpaRepository.findByArticleId(articleId);
            response.insert("collections", article.getCollections());
            return response.sendSuccess();
        } else {
            return response.sendFailure(Status.ARTICLE_HAS_COLLECTED, "该文章已被收藏过了！");
        }
    }

    /**
     * @param articleId 文章id
     * @return 是否取消收藏成功
     * @checked true
     */
    @Override
    public String collectDel(String articleId) {
        Response response = new Response();
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        if (articleCollectionJpaRepository.existsByArticleIdAndUserId(articleId, userId)) {
            articleCollectionJpaRepository.deleteByArticleIdAndUserId(articleId, userId);
            articleJpaRepository.reduceCollectionsByArticleId(articleId);
            Article article = articleJpaRepository.findByArticleId(articleId);
            response.insert("collections", article.getCollections());
            return response.sendSuccess();
        } else {
            return response.sendFailure(Status.ARTICLE_NOT_COLLECTED, "该文章未被收藏！");
        }
    }

    /**
     * @param articleId 文章ID
     * @param userId    用户ID
     * @return 是否删除成功
     * @checked true
     */
    @Override
    public String articleDel(String articleId, String userId) {
        Response response = new Response();
        boolean hasDeleted = false;
        if (draftJpaRepository.existsByArticleIdOrDraftId(articleId, articleId)) {
            //文章存在于草稿箱中
            Draft draft = draftJpaRepository.findByArticleIdOrDraftId(articleId, articleId);
            if (!draft.getUserId().equals(userId)) {
                return response.sendFailure(Status.WRONG_AUTHOR, "作者信息有误");
            }
            draftJpaRepository.deleteByArticleIdOrDraftId(articleId, articleId);
            hasDeleted = true;
        }
        if (articleJpaRepository.existsByArticleId(articleId)) {
            Article article = articleJpaRepository.findByArticleId(articleId);
            if (!article.getUserId().equals(userId)) {
                return response.sendFailure(Status.WRONG_AUTHOR, "作者信息有误");
            }
            //存在文章
            String labelId = article.getLabelId();
            commentVoteJpaRepository.deleteByTargetId(articleId);//删除文章所有的选票
            replyJpaRepository.deleteByTargetId(articleId);//删除文章所有的回复
            commentJpaRepository.deleteByTargetId(articleId);//删除文章所有的评论
            //评论与回复的删除顺序需要注意，因为回复表中有评论的外键
            articleCollectionJpaRepository.deleteByArticleId(articleId);//删除文章收藏
            articleJpaRepository.deleteByArticleId(articleId);//删除文章
            userJpaRepository.deleteArticlesByUserId(1, userId);//将用户的文章数-1
            articleLabelJpaRepository.updateQuantityByLabelId(-1, labelId);//将对应标签下的文章数-1
            hasDeleted = true;
        }
        if (hasDeleted) {
            return response.sendSuccess();
        } else {
            return response.sendFailure(Status.ARTICLE_NOT_EXIST, "文章不存在！");
        }
    }

    /**
     * @param draftList 待解析的json格式数据
     * @return 更新的文章的id
     * 注：前端需要id的情况只有一种，即上传的文章只有一篇，如果是多篇上传，不会需要id
     */
    @Override
    public String draftUpdateBatch(String draftList) {
        Response response = new Response();
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Draft>>() {
        }.getType();
        ArrayList<Draft> draftArrayList = gson.fromJson(draftList, type);//解析数据
        String articleId = "";//返回给前端的文章id
        ArrayList<Draft> waitToSave = new ArrayList<>();
        for (Draft draft : draftArrayList) {//遍历上传的草稿集
            if (draft.getArticleId().equals(Constant.NEWS)) {//新文章
                draft.setArticleId(null);
                waitToSave.add(draft);
            } else {
                if (articleJpaRepository.existsByArticleId(draft.getArticleId())) {//判断article表中是否有
                    Article article = articleJpaRepository.findByArticleId(draft.getArticleId());
                    if (article.getUserId().equals(draft.getUserId())) {//验证用户身份
                        if (draftJpaRepository.existsByArticleId(draft.getArticleId())) {//判断文章是否已绑定
                            draftJpaRepository.updateDraftByArticleId(draft.getTitle(), draft.getUpdateTime(), draft.getLabelId(), draft.getText(), draft.getArticleId());
                            articleId = draft.getArticleId();
                        } else {//没有绑定，则进行绑定
                            waitToSave.add(draft);
                        }
                    } else {
                        return response.sendFailure(Status.WRONG_AUTHOR, "作者信息有误");
                    }
                } else {//article表中没有
                    if (draftJpaRepository.existsByDraftId(draft.getArticleId())) {//判断是否是draft表中的数据
                        Draft draft1 = draftJpaRepository.findByArticleIdOrDraftId(draft.getArticleId(), draft.getArticleId());
                        if (draft1.getUserId().equals(draft.getUserId())) {//验证用户身份
                            draftJpaRepository.updateDraftByDraftId(draft.getTitle(), draft.getUpdateTime(), draft.getLabelId(), draft.getText(), null, draft.getArticleId());
                            articleId = draft.getArticleId();
                        } else {
                            return response.sendFailure(Status.WRONG_AUTHOR, "作者信息有误");
                        }
                    } else {//不存在该文章
                        return response.sendFailure(Status.ARTICLE_NOT_EXIST, "文章不存在！");
                    }
                }
            }
        }

        if (waitToSave.size() > 0) {
            Draft draft = draftJpaRepository.saveAll(waitToSave).get(0);//批量更新
            if (draft.getArticleId() == null) {
                articleId = draft.getDraftId();
            } else {
                articleId = draft.getArticleId();
            }
        }
        response.insert("article_id", articleId);
        return response.sendSuccess();
    }


    @Override
    public String getArticleList(String labelId, int pageNum) {
        Response response = new Response();
        Page<Article> articlePage = articlePage(pageNum, Constant.PUBLISH, Constant.NONE, labelId);
        PageData pageData = new PageData(articlePage, pageNum);
        response.insert("list", getArticleList(articlePage.iterator(), true));
        response.insert(pageData);
        return response.sendSuccess();
    }

    @Override
    public String getUserArticleList() {
        Response response = new Response();
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();//获取用户ID
        List<Article> articleList = articleJpaRepository.findAllByUserId(userId);
        List<Draft> draftList = draftJpaRepository.findAllByUserId(userId);
        Map<String, Integer> articleMap = new HashMap<>();
        ArrayList<UserArticleListSimple> userArticleListSimples = new ArrayList<>();
        for (int i = 0; i < articleList.size(); i++) {//做映射，将用户article表中的文章作为主键，articleList中的次序作为值
            Article article = articleList.get(i);
            userArticleListSimples.add(new UserArticleListSimple(article));
            articleMap.put(article.getArticleId(), i);
        }
        for (Draft draft : draftList) {
            if (draft.getArticleId() != null) {//该草稿有绑定的文章
                //存在两个版本
                int index = articleMap.get(draft.getArticleId());
                Article article = articleList.get(index);
                if (article.getUpdateTime() < draft.getUpdateTime()) {//草稿更新的时间晚，将原始的文章替换掉
                    userArticleListSimples.set(index, new UserArticleListSimple(draft));
                }
            } else {
                //只存在draft
                userArticleListSimples.add(new UserArticleListSimple(draft));
            }
        }
        response.insert("article_list", userArticleListSimples);
        return response.sendSuccess();
    }

    @Override
    public String getUserArticle(String articleId) {
        Response response = new Response();
        if (draftJpaRepository.existsByArticleIdOrDraftId(articleId, articleId)) {
            Draft draft = draftJpaRepository.findByArticleIdOrDraftId(articleId, articleId);
            ArticleLabel articleLabel;
            if (articleJpaRepository.existsByArticleId(articleId)) {
                //同时存在
                Article article = articleJpaRepository.findByArticleId(articleId);
                if (draft.getUpdateTime() > article.getUpdateTime()) {//草稿的时间更新
                    response.insert(new UserArticle(draft));
                    articleLabel = articleLabelJpaRepository.findByLabelId(draft.getLabelId());
                } else {//article表中的时间更新
                    response.insert(new UserArticle(article));
                    articleLabel = articleLabelJpaRepository.findByLabelId(article.getLabelId());
                }
            } else {
                //仅存在于草稿箱
                response.insert(new UserArticle(draft));
                articleLabel = articleLabelJpaRepository.findByLabelId(draft.getLabelId());
            }
            response.insert(new ArticleLabelBasic(articleLabel));
        } else {
            //草稿箱中不存在
            if (articleJpaRepository.existsByArticleId(articleId)) {
                //仅存在于已发布的文章中
                Article article = articleJpaRepository.findByArticleId(articleId);
                ArticleLabel articleLabel = articleLabelJpaRepository.findByLabelId(article.getLabelId());
                response.insert(new ArticleLabelBasic(articleLabel));
                response.insert(new UserArticle(article));
            } else {
                return response.sendFailure(Status.ARTICLE_NOT_EXIST, "文章不存在");
            }
        }
        return response.sendSuccess();
    }

    /**
     * @param pageNum  页码（从0开始）
     * @param statusId 文章状态
     * @param userId   用户ID
     * @param labelId  文章标签ID
     * @return 文章查询结果信息
     * @checked true
     * @Description: 查询指定的文章列表，其中页码与文章状态为必选信息，其它为可选项，但userId与labelId不会同时存在
     */
    public Page<Article> articlePage(int pageNum, int statusId, String userId, String labelId) {
        Sort sort = new Sort(Sort.DEFAULT_DIRECTION, Constant.ARTICLE_ID_NAME);//排序方式，按照文章id进行默认排序（从小到大）
        Pageable pageable = PageRequest.of(pageNum, Constant.PAGE_ARTICLE, sort);//设置分页信息，参数为：页码数，一次获取的个数，排序方式
        if (!labelId.equals(Constant.NONE)) {
            //labelId存在
            return articleJpaRepository.findAllByLabelIdAndStatusId(pageable, labelId, statusId);
        } else {
            //labelId不存在
            if (!userId.equals(Constant.NONE)) {
                //userId存在
                return articleJpaRepository.findAllByUserIdAndStatusId(pageable, userId, statusId);
            } else {
                //都不存在
                return articleJpaRepository.findAllByStatusId(pageable, statusId);
            }
        }

    }

    /**
     * @param articleIterator 文章迭代器
     * @param needUser        是否需要用户
     * @return 文章列表信息
     * @checked true
     */
    public ArrayList<ArticleList> getArticleList(Iterator<Article> articleIterator, boolean needUser) {
        ArrayList<Article> articles = new ArrayList<>();//文章列表
        ArrayList<ArticleBasic> articleBasics = new ArrayList<>();//返回给前端的文章列表
        ArrayList<String> labelIdList = new ArrayList<>();//文章id列表（用于查询label）
        ArrayList<String> userIdList = new ArrayList<>();//用户id列表（用于查询user）
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        while (articleIterator.hasNext()) {//读取文章数据
            Article article = articleIterator.next();//获取一篇文章
            articles.add(article);
            ArticleBasic articleBasic = new ArticleBasic(article);//封装文章数据
            articleBasic.IsAuthor(userId.equals(article.getUserId()));//设置是否为作者
            articleBasic.set_collected(articleCollectionJpaRepository.existsByArticleIdAndUserId(article.getArticleId(), userId));
            articleBasics.add(articleBasic);
            labelIdList.add(article.getLabelId());//读取文章标签id
            if (needUser) {
                userIdList.add(article.getUserId());//读取文章作者
            }
        }
        //因为jpa在批量查询数据的时候，对于id重复的数据会自动合并，所以需要做一个映射处理
        List<ArticleLabel> labelList = articleLabelJpaRepository.findAllById(labelIdList);//获取标签列表
        ListToMap<ArticleLabel> labelListToMap = new ListToMap<>();
        HashMap<String, ArticleLabel> articleLabelMap = labelListToMap.getMap(labelList, Constant.LABEL_ID_NAME, ArticleLabel.class);//标签映射map
        List<User> userList;
        HashMap<String, User> userMap = null;//用户映射map
        if (needUser) {
            userList = userJpaRepository.findAllById(userIdList);//获取用户列表
            ListToMap<User> userListToMap = new ListToMap<>();
            userMap = userListToMap.getMap(userList, Constant.USER_ID_NAME, User.class);//用户映射map
        }
        ArrayList<ArticleList> resultList = new ArrayList<>();//返回的列表信息
        for (int i = 0; i < articles.size(); i++) {
            Article article = articles.get(i);
            ArticleList articleList;
            ArticleLabelBasic articleLabelBasic = new ArticleLabelBasic(articleLabelMap.get(article.getLabelId()));
            if (needUser) {
                User user = userMap.get(article.getUserId());//获取文章用户
                UserSimple userSimple = new UserSimple(user);//封装用户数据，
                articleList = new ArticleList(articleBasics.get(i), articleLabelBasic, userSimple);//封装数据
            } else {
                articleList = new ArticleList(articleBasics.get(i), articleLabelBasic);//封装数据
            }
            resultList.add(articleList);
        }
        return resultList;
    }

}
