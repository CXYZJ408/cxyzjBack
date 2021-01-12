package com.cxyzj.cxyzjback.Service.impl.Article;

import com.cxyzj.cxyzjback.Bean.Article.Article;
import com.cxyzj.cxyzjback.Bean.Article.ArticleLabel;
import com.cxyzj.cxyzjback.Bean.User.User;
import com.cxyzj.cxyzjback.Bean.User.UserLabel;
import com.cxyzj.cxyzjback.Data.Article.ArticleBasic;
import com.cxyzj.cxyzjback.Data.Article.ArticleLabelBasic;
import com.cxyzj.cxyzjback.Data.Article.ArticleLabelDetail;
import com.cxyzj.cxyzjback.Data.Article.ArticleList;
import com.cxyzj.cxyzjback.Data.Other.PageData;
import com.cxyzj.cxyzjback.Data.User.front.UserSimple;
import com.cxyzj.cxyzjback.Repository.Article.ArticleJpaRepository;
import com.cxyzj.cxyzjback.Repository.Article.ArticleLabelJpaRepository;
import com.cxyzj.cxyzjback.Repository.User.UserJpaRepository;
import com.cxyzj.cxyzjback.Repository.User.UserLabelJpaRepository;
import com.cxyzj.cxyzjback.Service.Interface.Article.ArticleLabelService;
import com.cxyzj.cxyzjback.Utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Auther: 夏
 * @DATE: 2018/10/9 15:14
 * @Description: 文章标签系统的API
 * @checked false
 */

@Slf4j
@Service
public class ArticleLabelServiceImpl implements ArticleLabelService {

    private final ArticleLabelJpaRepository articleLabelJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final UserLabelJpaRepository userLabelJpaRepository;
    private final ArticleJpaRepository articleJpaRepository;
    private static final String HOT_LIST = "hot";
    private static final String NEW_LIST = "new";
    private static final String LABEL = "label";
    private static final String COLLECTIONS = "collections";

    @Autowired
    public ArticleLabelServiceImpl(ArticleLabelJpaRepository articleLabelJpaRepository,
                                   UserJpaRepository userJpaRepository,
                                   UserLabelJpaRepository userLabelJpaRepository,
                                   ArticleJpaRepository articleJpaRepository) {
        this.articleLabelJpaRepository = articleLabelJpaRepository;
        this.userJpaRepository = userJpaRepository;
        this.userLabelJpaRepository = userLabelJpaRepository;
        this.articleJpaRepository = articleJpaRepository;
    }

    private ListSort listSort = new ListSort();

    /**
     * @return
     * @Description 获取 文章标签列表详情
     * @checked true
     */
    @Override
    public String labelListDetails() {
        Response response = new Response();
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        String labels = "";//获取用户label信息
        List<String> userLabels;
        if (userLabelJpaRepository.existsByUserId(userId)) {
            labels = userLabelJpaRepository.findByUserId(userId).getLabels();//获取用户label信息
        }
        userLabels = readUserLabels(labels);

        ArrayList<ArticleLabelDetail> articleLabelDetails = new ArrayList<>();
        List<ArticleLabel> articleLabels = articleLabelJpaRepository.findAll();//获取所有label信息
        articleLabels.stream().filter(articleLabel -> !articleLabel.getLabelId().equals("-1")).//排除掉id为-1的label
                sorted(Comparator.comparing(ArticleLabel::getQuantity).reversed()).//对结果按照quantity从大到小排序
                forEach(articleLabel -> {//遍历labels
            ArticleLabelDetail articleLabelDetail = new ArticleLabelDetail(articleLabel);//封装数据
            if (userLabels.contains(articleLabel.getLabelId())) {//如果当前用户选择过该标签
                articleLabelDetail.set_select(true);
            }
            articleLabelDetails.add(articleLabelDetail);
        });
        response.insert(LABEL, articleLabelDetails);
        return response.sendSuccess();
    }

    /**
     * @Description 获取 文章标签列表简单信息
     * @checked true
     */
    @Override
    public String labelListSimple() {
        Response response = new Response();

        ArrayList<ArticleLabelBasic> articleLabelBasics = new ArrayList<>();
        List<ArticleLabel> articleLabels = articleLabelJpaRepository.findAll();
        articleLabels.stream().filter(articleLabel -> !articleLabel.getLabelId().equals("-1")).//排除掉id为-1的label
                sorted(Comparator.comparing(ArticleLabel::getQuantity).reversed())//对结果按照quantity从大到小排序
                .forEach(articleLabel -> {//遍历并封装数据
                    ArticleLabelBasic articleLabelBasic = new ArticleLabelBasic(articleLabel);
                    articleLabelBasics.add(articleLabelBasic);
                });
        response.insert(LABEL, articleLabelBasics);
        return response.sendSuccess();
    }

    /**
     * @param labelId 用户要添加的labelID
     * @Description 添加文章标签
     * @checked true
     */
    @Override
    public String addLabel(String labelId) {
        Response response = new Response();

        if (!articleLabelJpaRepository.existsByLabelId(labelId)) {
            //不存在该标签
            return response.sendFailure(Status.LABEL_NOT_EXIST, "标签不存在！");
        }
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        UserLabel userLabel = new UserLabel();
        //判断User_label表里是否存在该用户的记录
        if (!userLabelJpaRepository.existsByUserId(userId)) {
            //如果不存在
            //存在该标签
            userLabel.setUserId(userId);
            userLabel.setLabels(labelId);//添加信息
            userLabelJpaRepository.save(userLabel);
            articleLabelJpaRepository.updateCollectionsByLabelId(1, labelId);
            //返回label的collections值
            ArticleLabel articleLabel = articleLabelJpaRepository.findByLabelId(labelId);
            response.insert(COLLECTIONS, articleLabel.getCollections());
            return response.sendSuccess();
        } else {
            //如果存在该记录
            //查询到该用户的labels值
            String labels = userLabelJpaRepository.findByUserId(userId).getLabels();
            if (!labels.contains(labelId)) {
                //重新拼接字段值
                String newLabels = labels + "," + labelId;
                //更新labels字段
                userLabelJpaRepository.updateLabelsByUserId(newLabels, userId);
                articleLabelJpaRepository.updateCollectionsByLabelId(1, labelId);
                //返回label的collections值
                ArticleLabel articleLabel = articleLabelJpaRepository.findByLabelId(labelId);
                response.insert(COLLECTIONS, articleLabel.getCollections());
                return response.sendSuccess();
            } else {
                return response.sendFailure(Status.LABEL_HAS_SELECT, "标签已被选择");
            }
        }
    }

    /**
     * @param labelId 要删除的用户标签id
     * @Description 删除用户文章标签
     * @checked true
     */
    @Override
    public String deleteUserLabel(String labelId) {
        Response response = new Response();

        if (!articleLabelJpaRepository.existsByLabelId(labelId)) {
            //不存在该标签
            return response.sendFailure(Status.LABEL_NOT_EXIST, "标签不存在！");
        }
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        if (userLabelJpaRepository.existsByUserId(userId)) {
            String labels = userLabelJpaRepository.findByUserId(userId).getLabels();
            if (labels.contains(labelId)) {
                //存在标签
                labels = Arrays.stream(labels.split(",")).filter(x -> !labelId.equals(x)).collect(Collectors.joining(","));
                userLabelJpaRepository.updateLabelsByUserId(labels, userId);
                articleLabelJpaRepository.updateCollectionsByLabelId(-1, labelId);
                ArticleLabel articleLabel = articleLabelJpaRepository.findByLabelId(labelId);
                response.insert(COLLECTIONS, articleLabel.getCollections());
                return response.sendSuccess();
            } else {
                return response.sendFailure(Status.LABEL_NOT_EXIST, "标签不存在");
            }
        }
        return response.sendFailure(Status.NO_USER_LABEL, "用户无自定义标签");

    }

    /**
     * @param labelId 要获取的文章列表的标签
     * @param pageNum 页码
     * @param type    类型，包含hot（热门）与news（最新）
     * @return 文章列表
     * @Description 根据文章标签查询文章列表
     * @checked true
     */
    @Override
    public String getArticleByLabel(String labelId, int pageNum, String type) {
        Response response = new Response();

        if (!articleLabelJpaRepository.existsByLabelId(labelId)) {
            //不存在该标签
            return response.sendFailure(Status.LABEL_NOT_EXIST, "标签不存在！");
        }
        if (!type.equals(HOT_LIST) && !type.equals(NEW_LIST)) {
            return response.sendFailure(Status.WRONG_TYPE, "类型错误");
        }
        Page<Article> articlePage = articlePage(pageNum, labelId, type);
        PageData pageData = new PageData(articlePage, pageNum);
        response.insert(Constant.LIST, getArticleList(articlePage.iterator()));
        response.insert(pageData);
        return response.sendSuccess();
    }

    /**
     * @Description 获取用户文章标签列表的简单信息
     * @checked true
     */
    @Override
    public String userLabelListSimple() {
        Response response = new Response();

        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        if (userLabelJpaRepository.existsByUserId(userId)) {
            //如果存在该用户的标签信息
            String labels = userLabelJpaRepository.findByUserId(userId).getLabels();
            List<String> labelList = readUserLabels(labels);//读取用户标签列表
            List<ArticleLabelBasic> labelBasics = new ArrayList<>();//返回的标签数据列表
            labelList.forEach(labelId -> {
                ArticleLabel articleLabel = articleLabelJpaRepository.findByLabelId(labelId);
                ArticleLabelBasic articleLabelBasic = new ArticleLabelBasic(articleLabel);
                labelBasics.add(articleLabelBasic);
            });
            response.insert(LABEL, labelBasics);
            return response.sendSuccess();
        }
        return response.sendFailure(Status.NO_USER_LABEL, "用户无自定义标签");
    }

    /**
     * @Description 获取 用户文章标签列表的详细信息
     * @checked true
     */
    @Override
    public String userLabelListDetail() {
        Response response = new Response();

        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        if (userLabelJpaRepository.existsByUserId(userId)) {
            //如果存在该用户的标签信息
            String labels = userLabelJpaRepository.findByUserId(userId).getLabels();
            List<String> labelList = readUserLabels(labels);//读取标签列表信息
            List<ArticleLabelDetail> labelDetails = new ArrayList<>();
            labelList.forEach(labelId -> {
                ArticleLabel articleLabel = articleLabelJpaRepository.findByLabelId(labelId);
                ArticleLabelDetail articleLabelDetail = new ArticleLabelDetail(articleLabel);//封装数据
                articleLabelDetail.set_select(true);
                labelDetails.add(articleLabelDetail);
            });
            listSort.listLabelDeltailQuantity(labelDetails);//排序操作
            response.insert(LABEL, labelDetails);
            return response.sendSuccess();
        }
        return response.sendFailure(Status.NO_USER_LABEL, "用户无自定义标签");
    }

    /**
     * @param labelId 要获取的lableId
     * @Description: 根据所给id读取相关标签信息
     */
    @Override
    public String getLabelDetailsByLabelId(String labelId) {
        Response response = new Response();

        if (!articleLabelJpaRepository.existsByLabelId(labelId)) {
            //不存在该标签
            return response.sendFailure(Status.LABEL_NOT_EXIST, "标签不存在！");
        }
        ArticleLabel articleLabel = articleLabelJpaRepository.findByLabelId(labelId);//获取标签信息
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        String labels = "";//获取用户label信息
        List<String> userLabels;
        if (userLabelJpaRepository.existsByUserId(userId)) {
            labels = userLabelJpaRepository.findByUserId(userId).getLabels();//获取用户label信息
        }
        userLabels = readUserLabels(labels);
        ArticleLabelDetail articleLabelDetail = new ArticleLabelDetail(articleLabel);
        if (userLabels.contains(labelId)) {
            articleLabelDetail.set_select(true);
        }
        response.insert(articleLabelDetail);
        return response.sendSuccess();
    }

    /**
     * @param articleIterator 文章迭代器
     * @return 文章列表信息
     * @checked true
     */
    private ArrayList<ArticleList> getArticleList(Iterator<Article> articleIterator) {
        ArrayList<Article> articles = new ArrayList<>();//文章列表
        ArrayList<ArticleBasic> articleBasics = new ArrayList<>();//返回给前端的文章列表
        ArrayList<String> userIdList = new ArrayList<>();//文章id列表（用于查询label）
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        while (articleIterator.hasNext()) {
            Article article = articleIterator.next();//获取一篇文章
            articles.add(article);
            ArticleBasic articleBasic = new ArticleBasic(article);//封装文章数据
            articleBasic.IsAuthor(userId.equals(article.getUserId()));//设置是否为作者
            articleBasics.add(articleBasic);
            userIdList.add(article.getUserId());//读取文章标签id
        }

        List<User> userList;
        HashMap<String, User> userMap;
        userList = userJpaRepository.findAllById(userIdList);
        ListToMap<User> userListToMap = new ListToMap<>();
        userMap = userListToMap.getMap(userList, Constant.USER_ID_NAME, User.class);//userId映射

        ArrayList<ArticleList> resultList = new ArrayList<>();//返回的列表信息
        for (int i = 0; i < articles.size(); i++) {
            Article article = articles.get(i);
            ArticleList articleList;
            UserSimple userSimple = new UserSimple(userMap.get(article.getUserId()));
            articleList = new ArticleList(articleBasics.get(i), userSimple);//封装数据
            resultList.add(articleList);
        }
        return resultList;
    }

    private List<String> readUserLabels(String userLabels) {
        if (userLabels.length() == 0) {
            return new ArrayList<>();
        }
        return Arrays.stream(userLabels.split(",")).collect(Collectors.toList());
    }

    private Page<Article> articlePage(int pageNum, String labelId, String type) {
        Sort sort = null;
        if (type.equals(HOT_LIST)) {
            sort = new Sort(Sort.Direction.DESC, Constant.ARTICLE_VIEWS);//排序方式，按照文章浏览量进行排序（从大到小）
        } else if (type.equals(NEW_LIST)) {
            sort = new Sort(Sort.Direction.DESC, Constant.ARTICLE_UPDATETIME);//排序方式，按照文章更新时间进行排序（从大到小）
        }
        Pageable pageable = PageRequest.of(pageNum, Constant.PAGE_ARTICLE, sort);//设置分页信息，参数为：页码数，一次获取的个数，排序方式
        return articleJpaRepository.findAllByLabelIdAndStatusId(pageable, labelId, Constant.PUBLISH);


    }
}
