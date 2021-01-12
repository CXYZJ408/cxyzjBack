package com.cxyzj.cxyzjback.Data.User.front;

import com.cxyzj.cxyzjback.Bean.User.User;
import com.cxyzj.cxyzjback.Catch.RoleList;
import com.cxyzj.cxyzjback.Utils.Utils;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author 夏
 * @Date 09:18 2018/8/21
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class UserDetails extends UserData {

    private String user_id;
    private String nickname;
    private String head_url;
    private String email;
    private String bg_url;
    private long regist_date;
    private String phone;
    private String theme_color;
    private String role_id;
    private String introduce;
    private int gender;
    private int attentions;
    private int fans;
    private int articles;
    private int discussions;
    private int comments;
    private int status_id;

    //TODO 需要添加权限系统
    public UserDetails(User user) {
        Utils utils = new Utils();
        this.user_id = user.getUserId();
        this.nickname = user.getNickname();
        this.head_url = user.getHeadUrl();
        this.bg_url = user.getBgUrl();
        this.regist_date = user.getRegistDate();
        this.theme_color = user.getThemeColor();
        this.introduce = user.getIntroduce();
        this.gender = user.getGender();
        this.attentions = user.getAttentions();
        this.fans = user.getFans();
        this.articles = user.getArticles();
        this.discussions = user.getDiscussions();
        this.comments = user.getComments();
        this.status_id = user.getStatusId();
        this.email = utils.maskEmailPhone(user.getEmail(), false);
        this.phone = utils.maskEmailPhone(user.getPhone(), true);
        this.role_id = RoleList.getRoles().getRole(user.getRoleId());
    }
}
