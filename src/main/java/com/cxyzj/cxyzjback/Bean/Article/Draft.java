package com.cxyzj.cxyzjback.Bean.Article;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * @Package com.cxyzj.cxyzjback.Bean.Article
 * @Author Yaser
 * @Date 2018/10/30 15:54
 * @Description:
 */
@Entity
@Data
@Table(name = "article_draft")
public class Draft {
    @Id
    @GeneratedValue(generator = "SnowflakeIdGenerator")//自定义ID生成器
    @GenericGenerator(name = "SnowflakeIdGenerator", strategy = "com.cxyzj.cxyzjback.Utils.SnowflakeIdGenerator")
    @Column(name = "draft_id")
    private String draftId;

    @Column(name = "article_id")
    private String articleId;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "title")
    private String title;

    @Column(name = "update_time")
    private long updateTime;

    @Column(name = "label_id")
    private String labelId;

    @Column(name = "text")
    private String text;

}
