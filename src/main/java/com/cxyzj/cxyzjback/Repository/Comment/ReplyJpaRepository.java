package com.cxyzj.cxyzjback.Repository.Comment;

import com.cxyzj.cxyzjback.Bean.Article.Reply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: 夏
 * @DATE: 2018/9/6 15:15
 * @Description:
 */
public interface ReplyJpaRepository extends JpaRepository<Reply, String> {


    boolean existsByReplyId(String replyId);

    boolean existsByCommentId(String commentId);

    Page<Reply> findAllByReplier(Pageable pageable, String replier);

    Page<Reply> findAllByDiscusser(Pageable pageable, String discusser);


    Page<Reply> findAllByCommentId(Pageable pageable, String commentId);


    @Transactional
    @Modifying
    @Query(value = "delete from reply where target_id=?1", nativeQuery = true)
    void deleteByTargetId(String targetId);

    @Transactional
    @Modifying
    void deleteByCommentId(String commentId);


    int countByCommentId(String commentId);

    @Transactional
    @Modifying
    @Query(value = "delete from reply where reply_id=?1", nativeQuery = true)
    void deleteByReplyId(String replyId);


    @Transactional
    @Query(value = "select support from reply where reply_id=?1", nativeQuery = true)
    int findSupportByReplyId(String replyId);

    @Transactional
    @Modifying
    @Query(value = "update reply set support=?1 where reply_id=?2", nativeQuery = true)
    void updateReplySupport(int support, String replyId);

    @Transactional
    @Query(value = "select object from reply where reply_id=?1", nativeQuery = true)
    int findObjectByReplyId(String replyId);

    @Transactional
    @Modifying
    @Query(value = "update reply set object=?1 where reply_id=?2", nativeQuery = true)
    void updateReplyObject(int object, String replyId);


}
