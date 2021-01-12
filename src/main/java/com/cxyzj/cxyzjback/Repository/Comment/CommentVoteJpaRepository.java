package com.cxyzj.cxyzjback.Repository.Comment;

import com.cxyzj.cxyzjback.Bean.Article.CommentVote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;

/**
 * @Auther: 夏
 * @DATE: 2018/9/6 15:14
 * @Description:
 */
public interface CommentVoteJpaRepository extends JpaRepository<CommentVote, String> {

    @Transactional
    @Modifying
    void deleteByCommentReplyIdAndUserId(String commentReplyId, String userId);

    boolean existsByUserIdAndCommentReplyId(String userId, String commentReplyId);

    boolean existsByUserId(String userId);

    boolean existsByTargetId(String targetId);

    @Transactional
    @Modifying
    void deleteByTargetId(String targetId);


    CommentVote findByUserIdAndCommentReplyId(String userId, String commentReplyId);
}
