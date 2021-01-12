package com.cxyzj.cxyzjback.Repository.Article;

import com.cxyzj.cxyzjback.Bean.Article.ArticleCollection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

/**
 * @Auther: 夏
 * @DATE: 2018/9/18 08:41
 * @Description:
 */
public interface ArticleCollectionJpaRepository extends JpaRepository<ArticleCollection, String> {


    boolean existsByArticleIdAndUserId(String articleId, String userId);

    Page<ArticleCollection> findAllByUserId(Pageable pageable, String userId);

    boolean existsByUserId(String userId);

    @Transactional
    @Modifying
    @Query(value = "delete from article_collection where article_id=?1 and user_id=?2", nativeQuery = true)
    void deleteByArticleIdAndUserId(String articleId, String userId);

    @Transactional
    @Modifying
    @Query(value = "delete from article_collection where article_id=?1", nativeQuery = true)
    void deleteByArticleId(String articleId);

}
