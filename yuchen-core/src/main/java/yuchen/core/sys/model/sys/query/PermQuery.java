package yuchen.core.sys.model.sys.query;


import yuchen.core.sys.model.QueryPageModel;

/**
 * Created by alan.zheng on 2017/1/19.
 */
public class PermQuery extends QueryPageModel {
    private String permUrl;

    public String getPermUrl() {
        return permUrl;
    }

    public void setPermUrl(String permUrl) {
        this.permUrl = permUrl;
    }
}
