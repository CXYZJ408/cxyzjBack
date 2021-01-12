package com.cxyzj.cxyzjback.Data.Article;

import com.cxyzj.cxyzjback.Bean.Article.ArticleLabel;
import lombok.Data;

/**
 * @Auther: 夏
 * @DATE: 2018/10/9 15:45
 * @Description:
 */
@Data
public class ArticleLabelDetail implements com.cxyzj.cxyzjback.Data.Data {

    private String label_id;
    private String label_name;
    private int quantity;
    private String link;
    private int collections;
    private String introduce;
    private boolean is_select;

    public ArticleLabelDetail(ArticleLabel articleLabel){
        this.label_id = articleLabel.getLabelId();
        this.label_name = articleLabel.getLabelName();
        this.quantity = articleLabel.getQuantity();
        this.link = articleLabel.getLink();
        this.collections = articleLabel.getCollections();
        this.introduce = articleLabel.getIntroduce();
        this.is_select = false;
    }

    @Override
    public String getName() {
        return "label";
    }
}
