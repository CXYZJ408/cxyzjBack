package com.cxyzj.cxyzjback.Data.Article;

import com.cxyzj.cxyzjback.Bean.Article.Article;
import com.cxyzj.cxyzjback.Bean.Article.Draft;
import com.cxyzj.cxyzjback.Data.Data;
import com.cxyzj.cxyzjback.Utils.Constant;

/**
 * @Package com.cxyzj.cxyzjback.Data.Article
 * @Author Yaser
 * @Date 2018/10/30 17:54
 * @Description:
 */
@lombok.Data
public class UserArticle implements Data {

    private String article_id;
    private String title;
    private long update_time;
    private String text;
    private int status_id;

    public UserArticle(Article article) {
        this.article_id = article.getArticleId();
        this.title = article.getTitle();
        this.update_time = article.getUpdateTime();
        this.text = article.getText();
        this.status_id = article.getStatusId();
    }

    public UserArticle(Draft draft) {
        if(draft.getArticleId()==null){
            this.article_id = draft.getDraftId();
        }else{
            this.article_id = draft.getArticleId();
        }
        this.title = draft.getTitle();
        this.update_time = draft.getUpdateTime();
        this.text = draft.getText();
        this.status_id = Constant.DRAFT;
    }

    @Override
    public String getName() {
        return "article";
    }
}
