package com.cxyzj.cxyzjback.Data.Article;

import com.cxyzj.cxyzjback.Bean.Article.Article;
import com.cxyzj.cxyzjback.Bean.Article.Draft;
import com.cxyzj.cxyzjback.Utils.Constant;
import lombok.Data;

/**
 * @Auther: 夏
 * @DATE: 2018/9/17 16:58
 * @Description:
 */

@Data
public class ArticleBasic implements com.cxyzj.cxyzjback.Data.Data {

    private String article_id;
    private String title;
    private String article_sum;
    private long update_time;
    private int views;
    private int comments;
    private int collections;
    private String thumbnail;
    private String text;
    private boolean is_collected;
    private boolean allow_delete;
    private boolean allow_edit;
    private int status_id;
    private boolean is_author;
    private String label_id;

    public ArticleBasic(Article article) {
        this.article_id = article.getArticleId();
        this.title = article.getTitle();
        this.article_sum = article.getArticleSum();
        this.update_time = article.getUpdateTime();
        this.views = article.getViews();
        this.comments = article.getComments();
        this.collections = article.getCollections();
        this.thumbnail = article.getThumbnail();
        this.text = article.getText();
        this.is_collected = false;
        this.allow_delete = false;
        this.allow_edit = false;
        this.status_id = article.getStatusId();
        this.is_author = false;
        this.label_id = article.getLabelId();
    }

    public ArticleBasic(Draft draft) {//将草稿转化为文章
        if (draft.getArticleId() == null) {
            this.article_id = draft.getDraftId();
        } else {
            this.article_id = draft.getArticleId();
        }
        this.title = draft.getTitle();
        this.article_sum = draft.getText();
        this.update_time = draft.getUpdateTime();
        this.views = 0;
        this.comments = 0;
        this.collections = 0;
        this.thumbnail = "";
        this.text = draft.getText();
        this.is_collected = false;
        this.allow_delete = false;
        this.allow_edit = false;
        this.status_id = Constant.DRAFT;
        this.is_author = false;
        this.label_id = draft.getLabelId();
    }

    @Override
    public String getName() {
        return "article";
    }

    public void IsAuthor(boolean IsAuthor) {
        this.allow_delete = IsAuthor;
        this.allow_edit = IsAuthor;
        this.is_author = IsAuthor;
    }
}
