package com.cxyzj.cxyzjback.Data.Article;

import com.cxyzj.cxyzjback.Data.User.front.UserSimple;
import lombok.Data;

/**
 * @Package com.cxyzj.cxyzjback.Data.Article
 * @Author Yaser
 * @Date 2018/10/05 19:50
 * @Description:
 */
@Data
public class ArticleList implements com.cxyzj.cxyzjback.Data.Data {

    private String article_id;//用于标识一个文章（前端列表渲染时需要该参数）
    private ArticleBasic article;
    private ArticleLabelBasic label;
    private UserSimple user;

    public ArticleList(ArticleBasic article, ArticleLabelBasic label, UserSimple user) {
        this.article_id = article.getArticle_id();
        this.article = article;
        this.label = label;
        this.user = user;
    }

    public ArticleList(ArticleBasic article, ArticleLabelBasic label) {
        this.article_id = article.getArticle_id();
        this.article = article;
        this.label = label;
    }

    public ArticleList(ArticleBasic article, UserSimple userSimple) {
        this.article_id = article.getArticle_id();
        this.article = article;
        this.user = userSimple;
    }

    @Override
    public String getName() {
        return "list";
    }
}
