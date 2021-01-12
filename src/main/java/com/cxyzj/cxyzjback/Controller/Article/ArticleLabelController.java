package com.cxyzj.cxyzjback.Controller.Article;

import com.cxyzj.cxyzjback.Service.Interface.Article.ArticleLabelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @Auther: 夏
 * @DATE: 2018/10/9 15:16
 * @Description:
 */

@RestController
@CrossOrigin
@Slf4j
@RequestMapping(value = "/v1/article/label")
public class ArticleLabelController {

    private final ArticleLabelService articleLabelService;

    @Autowired
    public ArticleLabelController(ArticleLabelService articleLabelService) {
        this.articleLabelService = articleLabelService;
    }

    /**
     * @Description 获取 文章标签的详细列表信息
     */
    @GetMapping(value = "/list/details")
    @PreAuthorize("hasAnyRole('ROLE_ANONYMITY','ROLE_USER','ROLE_ADMIN','ROLE_ADMINISTRATORS')")
    public String getLabelListDetails() {
        return articleLabelService.labelListDetails();
    }

    /**
     * @Description 获取 用户文章标签的简单列表信息
     */
    @GetMapping(value = "/user/list/simple")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN','ROLE_ADMINISTRATORS')")
    public String getUserLabelListSimple() {
        return articleLabelService.userLabelListSimple();
    }

    /**
     * @Description 添加用户文章标签
     */
    @PostMapping(value = "/user/{label_id}")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN','ROLE_ADMINISTRATORS')")
    public String addLabel(@PathVariable(name = "label_id") String labelId) {
        return articleLabelService.addLabel(labelId);
    }

    /**
     * @Description 删除指定用户label
     */
    @DeleteMapping(value = "/user/{label_id}")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN','ROLE_ADMINISTRATORS')")
    public String deleteUserLabel(@PathVariable(name = "label_id") String labelId) {
        return articleLabelService.deleteUserLabel(labelId);
    }

    /**
     * @Description 根据指定标签获取文章列表
     */
    @GetMapping(value = "/{label_id}/{page_num}/{type}")
    @PreAuthorize("hasAnyRole('ROLE_ANONYMITY','ROLE_USER','ROLE_ADMIN','ROLE_ADMINISTRATORS')")
    public String getArticleListByLabel(@PathVariable(name = "label_id") String labelId,
                                        @PathVariable(name = "page_num") int pageNum,
                                        @PathVariable(name = "type") String type) {
        return articleLabelService.getArticleByLabel(labelId, pageNum, type);
    }

    /**
     * @Description 获取指定标签的详细信息
     */
    @GetMapping(value = "/{label_id}")
    @PreAuthorize("hasAnyRole('ROLE_ANONYMITY','ROLE_USER','ROLE_ADMIN','ROLE_ADMINISTRATORS')")
    public String getArticleLabelDetails(@PathVariable(name = "label_id") String labelId) {
        return articleLabelService.getLabelDetailsByLabelId(labelId);
    }

    /**
     *
     * @Description 获取所有标签的简单列表信息
     */
    @GetMapping(value = "/list/simple")
    @PreAuthorize("hasAnyRole('ROLE_ANONYMITY','ROLE_USER','ROLE_ADMIN','ROLE_ADMINISTRATORS')")
    public String getLabelListSimple() {
        return articleLabelService.labelListSimple();
    }

    /**
     * @Description 获取用户文章标签列表的详细信息
     */
    @GetMapping(value = "/user/list/details")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN','ROLE_ADMINISTRATORS')")
    public String getUserLabelListDetail() {
        return articleLabelService.userLabelListDetail();
    }


}
