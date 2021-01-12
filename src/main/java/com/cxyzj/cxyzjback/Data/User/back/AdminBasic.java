package com.cxyzj.cxyzjback.Data.User.back;

import com.cxyzj.cxyzjback.Bean.User.User;
import com.cxyzj.cxyzjback.Data.User.front.UserData;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Auther: 夏
 * @DATE: 2018/9/29 10:26
 * @Description:
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class AdminBasic extends UserData {

    private String user_id;
    private String nickname;
    private String head_url;
    private int role;

    public AdminBasic(User user) {
        this.user_id = user.getUserId();
        this.nickname = user.getNickname();
        this.head_url = user.getHeadUrl();
        this.role = user.getRoleId();
    }

}
