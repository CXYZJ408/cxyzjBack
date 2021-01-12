package com.cxyzj.cxyzjback.Repository.Article;

import com.cxyzj.cxyzjback.Bean.Article.Article;
import com.cxyzj.cxyzjback.Bean.User.Attention;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;

/**
 * @Auther: 夏
 * @DATE: 2018/9/12 10:08
 * @Description:
 */
public interface ArticleJpaRepository extends JpaRepository<Article, String> {

    Page<Article> findAllByUserIdAndStatusId(Pageable pageable, String userId, int statusId);

    Article findByArticleId(String articleId);

    List<Article> findAllByUserId(String userId);

    Page<Article> findAllByLabelIdAndStatusId(Pageable pageable, String labelId, int statusId);

    List<Article> findByArticleIdIn(Collection<String> articleIdList);

    @Transactional
    @Modifying
    @Query(value = "update article set comments=?1 where article_id=?2", nativeQuery = true)
    void updateCommentsByArticleId(int comments, String articleId);

    @Transactional
    @Modifying
    @Query(value = "update article set levels=?1, comments=?2 where article_id=?3", nativeQuery = true)
    void updateLevelsAndCommentsByArticleId(int levels, int comments, String articleId);

    @Transactional
    @Modifying
    @Query(value = "update article a set a.views=a.views+1 where article_id=?1", nativeQuery = true)
    void updateViewsByArticleId(String articleId);


    @Transactional
    @Query(value = "select comments from article where article_id=?1", nativeQuery = true)
    int findCommentsByArticleId(String articleId);


    boolean existsByArticleId(String articleId);


    @Transactional
    @Modifying
    @Query(value = "update article a set a.collections=a.collections+1 where article_id=?1", nativeQuery = true)
    void increaseCollectionsByArticleId(String articleId);

    @Transactional
    @Modifying
    @Query(value = "update article a set a.collections=a.collections-1 where article_id=?1", nativeQuery = true)
    void reduceCollectionsByArticleId(String articleId);

    @Transactional
    @Modifying
    @Query(value = "delete from article where article_id=?1", nativeQuery = true)
    void deleteByArticleId(String articleId);

    Page<Article> findAllByStatusId(Pageable pageable, int statusId);
}
