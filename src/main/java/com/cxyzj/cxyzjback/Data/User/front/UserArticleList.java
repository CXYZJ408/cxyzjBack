package com.cxyzj.cxyzjback.Data.User.front;

import com.cxyzj.cxyzjback.Data.Article.ArticleBasic;
import com.cxyzj.cxyzjback.Data.Article.ArticleLabelBasic;
import com.cxyzj.cxyzjback.Data.Data;

/**
 * @Package com.cxyzj.cxyzjback.Data.User.front
 * @Author Yaser
 * @Date 2018/12/26 14:00
 * @Description:
 */
public class UserArticleList implements Data {
    private ArticleBasic article;
    private ArticleLabelBasic label;
    private OtherDetails user;

    public UserArticleList(ArticleBasic article, ArticleLabelBasic label) {
        this.article = article;
        this.label = label;
    }

    public UserArticleList(ArticleBasic article, ArticleLabelBasic label, OtherDetails user) {
        this.article = article;
        this.label = label;
        this.user = user;
    }

    @Override
    public String getName() {
        return "list";
    }
}
