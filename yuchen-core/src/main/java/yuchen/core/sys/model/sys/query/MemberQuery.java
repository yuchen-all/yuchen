package yuchen.core.sys.model.sys.query;


import yuchen.core.sys.model.QueryPageModel;

/**
 * Created by alan.zheng on 2017/1/19.
 */
public class MemberQuery extends QueryPageModel {
    private Long id;

    private String username;

    private String mobile;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
