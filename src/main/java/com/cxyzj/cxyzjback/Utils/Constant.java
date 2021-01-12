package com.cxyzj.cxyzjback.Utils;

/**
 * @Package com.cxyzj.cxyzjback.Utils
 * @Author Yaser
 * @Date 2018/09/24 19:54
 * @Description: 常量表
 */
public final class Constant {
    public static final int PAGE_ATTENTION_USER = 9; //一页的用户数量
    public static final int PAGE_ARTICLE = 5; //一页的文章数量
    public static final int PAGE_COMMENT = 5; //一页的评论的数量
    public static final int PAGE_REPLY = 5; //一页的回复数量
    public static final int PAGE_USER_ARTICLE = 5; //用户的一页的文章数量
    public static final String NONE = "-1";//不存在
    public static final String NEWS = "0000000000";//新文章
    public static final String MODEL_ARTICLE = "articles";//评论类型：文章
    public static final String MODEL_DISCUSSION = "discussions";//评论类型：讨论
    public static final String ARTICLE_ID_NAME = "articleId";

    public static final String ARTICLE_VIEWS = "views";
    public static final String ARTICLE_UPDATETIME = "updateTime";

    public static final String USER_ID_NAME = "userId";
    public static final String LABEL_ID_NAME = "labelId";
    public static final String COMMENT_ID_NAME = "commentId";
    public static final String COMMENT_SORT = "createTime";

    public static final String REPLY_SORT = "createTime";
    public static final String LIST = "list";
    public static final int HOT_COMMENT = 10;
    //数据库status表的信息
    public static final int OBJECT = 0;//反对
    public static final int SUPPORT = 1;//支持
    public static final int DRAFT = 100;//草稿
    public static final int PUBLISH = 101;//已发布
    public static final int CHECKING = 102;//审核中
    public static final int BANNED = 103;//冻结
    public static final int NORMAL = 104;//启用
    public static final int FOCUS = 201;//关注
    public static final int FOLLOWED = 202;//被关注
    public static final int EACH = 203;//互相关注
}
