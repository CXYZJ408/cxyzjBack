package com.cxyzj.cxyzjback.Utils;

import com.cxyzj.cxyzjback.Bean.Article.Article;
import com.cxyzj.cxyzjback.Data.Article.ArticleLabelDetail;

import java.util.List;

/**
 * @Auther: 夏
 * @DATE: 2018/11/21 18:07
 * @Description 排序方法
 */
public class ListSort {

    /**
     * @Description 根据时间的排序方法
     */
    public void listTimeSort(List<Article> list) {
        list.sort((o1, o2) -> {
            try {
                long dt1 = o1.getUpdateTime();
                long dt2 = o2.getUpdateTime();
                return Long.compare(dt1, dt2);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return 0;
        });
    }

    /**
     * @Description 根据热度的排序方法
     */
    public void listHotSort(List<Article> list) {
        list.sort((o1, o2) -> {
            try {
                int views1 = o1.getViews();
                int views2 = o2.getViews();
                return Integer.compare(views2, views1);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return 0;
        });
    }

    /**
     * @Description 根据label的quantity从大到小排序
     */
    public void listLabelDeltailQuantity(List<ArticleLabelDetail> list) {
        list.sort((o1, o2) -> {
            try {
                int quantity1 = o1.getQuantity();
                int quantity2 = o2.getQuantity();
                return Integer.compare(quantity2, quantity1);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return 0;
        });
    }
}
