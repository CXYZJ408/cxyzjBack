package com.cxyzj.cxyzjback.Service.Interface.Article;

/**
 * @Auther: 夏
 * @DATE: 2018/10/9 15:15
 * @Description:
 */
public interface ArticleLabelService {
    String labelListDetails();

    String labelListSimple();

    String addLabel(String labelId);

    String getArticleByLabel(String labelId, int pageNum, String type);

    String deleteUserLabel(String labelId);

    String userLabelListSimple();

    String userLabelListDetail();

    String getLabelDetailsByLabelId(String labelId);
}
