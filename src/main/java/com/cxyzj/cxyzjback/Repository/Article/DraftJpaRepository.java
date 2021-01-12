package com.cxyzj.cxyzjback.Repository.Article;

import com.cxyzj.cxyzjback.Bean.Article.Draft;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface DraftJpaRepository extends JpaRepository<Draft, String> {
    List<Draft> findAllByUserId(String userId);

    boolean existsByArticleId(String articleId);

    boolean existsByDraftId(String draftId);

    boolean existsByArticleIdOrDraftId(String articleId, String draftId);

    @Transactional
    @Modifying
    @Query(value = "update article_draft set title=?1, update_time=?2,label_id=?3,text=?4 where article_id=?5", nativeQuery = true)
    void updateDraftByArticleId(String title, long updateTime, String labelId, String text, String articleId);

    @Transactional
    @Modifying
    @Query(value = "update article_draft set title=?1, update_time=?2,label_id=?3,text=?4,article_id=?5 where draft_id=?6", nativeQuery = true)
    void updateDraftByDraftId(String title, long updateTime, String labelId, String text, String articleId, String draftId);

    @Transactional
    @Modifying
    void deleteByArticleId(String articleId);

    @Transactional
    @Modifying
    void deleteByArticleIdOrDraftId(String articleId,String draftId);

    Draft findByArticleId(String articleId);

    Draft findByArticleIdOrDraftId(String articleId,String draftId);


    @Transactional
    @Modifying
    @Query(value = "insert into article_draft (label_id, text, title, update_time, user_id, article_id) values (?1, ?2, ?3, ?4, ?5, ?6)", nativeQuery = true)
    void insertIntoDraft(String labelId, String text, String title, long updateTime, String user_id, String article_id);
}
