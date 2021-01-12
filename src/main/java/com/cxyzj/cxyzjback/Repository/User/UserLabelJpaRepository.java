package com.cxyzj.cxyzjback.Repository.User;

import com.cxyzj.cxyzjback.Bean.User.UserLabel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

/**
 * @Auther: 夏
 * @DATE: 2018/11/18 13:29
 * @Description:
 */
public interface UserLabelJpaRepository extends JpaRepository<UserLabel, String> {


    boolean existsByUserId(String userId);


    UserLabel findByUserId(String userId);

    @Transactional
    @Modifying
    @Query(value = "update user_label set labels=?1 where user_id=?2", nativeQuery = true)
    void updateLabelsByUserId(String newLabels, String userId);


}
