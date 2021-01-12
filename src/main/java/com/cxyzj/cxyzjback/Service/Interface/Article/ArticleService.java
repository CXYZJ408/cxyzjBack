package com.cxyzj.cxyzjback.Service.Interface.Article;

/**
 * @Auther: 夏
 * @DATE: 2018/9/12 10:10
 * @Description:
 */
public interface ArticleService {
    String publishArticle(String title, String text, String labelId, String articleSum, String thumbnail, String articleId, String userId);

    String articleDetails(String articleId);

    String collect(String articleId);

    String collectDel(String articleId);

    String articleDel(String articleId, String userId);

    String draftUpdateBatch(String drafts);

    String getArticleList(String labelId, int pageNum);

    String getUserArticleList();

    String getUserArticle(String articleId);
}
