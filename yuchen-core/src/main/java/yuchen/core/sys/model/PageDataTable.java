package yuchen.core.sys.model;

import java.util.List;

/**
 * Created by alan.zheng on 2018/4/17.
 */
public class PageDataTable<T> extends BaseModel {
    private List<T> data;

    private Integer iTotalRecords;

    private Integer iTotalDisplayRecords;
    /**
     * 页码
     */
    private Integer pageNo;
    /**
     * 页条数
     */
    private Integer iDisplayLength;

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public Integer getiTotalRecords() {
        return iTotalRecords;
    }

    public void setiTotalRecords(Integer iTotalRecords) {
        this.iTotalRecords = iTotalRecords;
    }

    public Integer getiTotalDisplayRecords() {
        return iTotalDisplayRecords;
    }

    public void setiTotalDisplayRecords(Integer iTotalDisplayRecords) {
        this.iTotalDisplayRecords = iTotalDisplayRecords;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getiDisplayLength() {
        return iDisplayLength;
    }

    public void setiDisplayLength(Integer iDisplayLength) {
        this.iDisplayLength = iDisplayLength;
    }
}
