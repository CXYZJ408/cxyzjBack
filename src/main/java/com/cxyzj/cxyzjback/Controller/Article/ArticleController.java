package com.cxyzj.cxyzjback.Controller.Article;


import com.cxyzj.cxyzjback.Service.Interface.Article.ArticleService;
import com.cxyzj.cxyzjback.Utils.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@Slf4j
@RequestMapping(value = "/v1/article")
public class ArticleController {

    private final ArticleService articleService;

    @Autowired
    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    // TODO: 2018/12/17 0017  将草稿的主键改为draft_id
    @PostMapping(value = "/publish")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN','ROLE_ADMINISTRATORS')  and principal.username.equals(#userId)")
    public String publishArticle(@RequestParam(required = false) String title, @RequestParam(required = false) String text, @RequestParam(name = "label_id", required = false) String labelId,
                                 @RequestParam(name = "article_sum", required = false) String articleSum, @RequestParam(required = false) String thumbnail,
                                 @RequestParam(name = "article_id") String articleId, @RequestParam(name = "user_id") String userId) {
        return articleService.publishArticle(title, text, labelId, articleSum, thumbnail, articleId, userId);
    }

    @GetMapping(value = "/user_article_list")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN','ROLE_ADMINISTRATORS')  and principal.username.equals(#userId)")
    public String userArticleList(@RequestParam(name = "user_id") String userId) {

        return articleService.getUserArticleList();
    }

    @GetMapping(value = "/user_articles/{article_id}")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN','ROLE_ADMINISTRATORS')  and principal.username.equals(#userId)")
    public String userArticleList(@PathVariable(name = "article_id") String articleId, @RequestParam(name = "user_id") String userId) {
        return articleService.getUserArticle(articleId);
    }

    @GetMapping(value = "/{article_id}")
    @PreAuthorize("hasAnyRole('ROLE_ANONYMITY','ROLE_USER','ROLE_ADMIN','ROLE_ADMINISTRATORS')")
    public String articleDetails(@PathVariable(name = "article_id") String articleId) {
        return articleService.articleDetails(articleId);
    }

    @PutMapping(value = "/collect/{article_id}")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN','ROLE_ADMINISTRATORS')")
    public String collect(@PathVariable(name = "article_id") String articleId) {
        return articleService.collect(articleId);
    }

    @DeleteMapping(value = "/collect/{article_id}")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN','ROLE_ADMINISTRATORS')")
    public String collectDel(@PathVariable(name = "article_id") String articleId) {
        return articleService.collectDel(articleId);
    }

    @DeleteMapping(value = "/{article_id}")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN','ROLE_ADMINISTRATORS')  and principal.username.equals(#userId)")
    public String articleDel(@PathVariable(name = "article_id") String articleId, @RequestParam(name = "user_id") String userId) {
        return articleService.articleDel(articleId, userId);
    }

    /**
     * @Description 批量更新草稿
     */
    @PostMapping(value = "/drafts/update_batch")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN','ROLE_ADMINISTRATORS')  and principal.username.equals(#userId)")
    public String draftUpdateBatch(@RequestParam(name = "draftList") String draftList, @RequestParam(name = "user_id") String userId) {
        return articleService.draftUpdateBatch(draftList);
    }

    /**
     * @Description 获取 文章列表(主页)
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ANONYMITY','ROLE_USER','ROLE_ADMIN','ROLE_ADMINISTRATORS')")
    public String getArticleList(@RequestParam(name = "label_id", required = false, defaultValue = Constant.NONE) String labelId,
                                 @RequestParam(name = "page_num") int pageNum) {
        return articleService.getArticleList(labelId, pageNum);
    }


}
