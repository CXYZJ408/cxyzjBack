package com.cxyzj.cxyzjback.Data.Article;

import com.cxyzj.cxyzjback.Bean.Article.Article;
import com.cxyzj.cxyzjback.Bean.Article.Draft;
import com.cxyzj.cxyzjback.Data.Data;
import com.cxyzj.cxyzjback.Utils.Constant;

/**
 * @Package com.cxyzj.cxyzjback.Data.Article
 * @Author Yaser
 * @Date 2018/10/30 16:07
 * @Description:
 */
@lombok.Data
public class UserArticleListSimple implements Data {
    private String article_id;
    private String title;
    private long update_time;
    private int status_id;

    public UserArticleListSimple(Article article) {
        this.article_id = article.getArticleId();
        this.title = article.getTitle();
        this.update_time = article.getUpdateTime();
        this.status_id = article.getStatusId();
    }
    public UserArticleListSimple(Draft draft) {
        if(draft.getArticleId()==null){
            this.article_id = draft.getDraftId();
        }else{
            this.article_id = draft.getArticleId();
        }
        this.title = draft.getTitle();
        this.update_time = draft.getUpdateTime();
        this.status_id = Constant.DRAFT;
    }
    @Override
    public String getName() {
        return "list";
    }
}
