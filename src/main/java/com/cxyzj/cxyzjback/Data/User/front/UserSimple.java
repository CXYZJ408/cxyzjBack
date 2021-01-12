package com.cxyzj.cxyzjback.Data.User.front;

import com.cxyzj.cxyzjback.Bean.User.User;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author 夏
 * @Date 17:18 2018/8/29
 */

@EqualsAndHashCode(callSuper = false)
@Data
public class UserSimple extends UserData {

    private String user_id;
    private String nickname;
    private String head_url;
    private int gender;

    public UserSimple(User user) {

        this.user_id = user.getUserId();
        this.nickname = user.getNickname();
        this.head_url = user.getHeadUrl();
        this.gender = user.getGender();

    }

}
