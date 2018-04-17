package yuchen.backstage.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import yuchen.backstage.annotation.Auth;
import yuchen.backstage.common.JsonResult;
import yuchen.backstage.service.PermissionService;
import yuchen.core.sys.model.PageDataTable;
import yuchen.core.sys.model.PageModel;
import yuchen.core.sys.model.sys.Perm;
import yuchen.core.sys.model.sys.query.PermQuery;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by XR on 2016/8/29.
 */
@Controller
public class PermController extends BaseController {

    @Resource
    private PermissionService permissionService;
    @Auth(rule = "/perm/index")
    @RequestMapping(value = "/perm/index")
    public String permission(Model model, Integer currPage){
        PermQuery query=new PermQuery();
        if (currPage!=null){
            query.setCurrPage(currPage);
        }
//        PageModel<Perm> list= permissionService.queryPageList(query);
//        model.addAttribute("permlist",list);
        return "/perm/index";
    }

    @Auth(rule = "/perm/index")
    @RequestMapping(value = "/perm/ajax")
    @ResponseBody
    public PageDataTable<Perm> ajax(PermQuery query){
        PageDataTable<Perm> pageDataTable = new PageDataTable<>();
        PageModel<Perm> list= permissionService.queryPageList(query);
        pageDataTable.setData(list.getModel());
        pageDataTable.setiTotalRecords(list.getTotalcount());
        pageDataTable.setiTotalDisplayRecords(list.getTotalcount());
        pageDataTable.setPageNo(list.getCurrpage());
        pageDataTable.setiDisplayLength(list.getPagesize());
        return pageDataTable;
    }

    @Auth(rule = "/perm/add")
    @RequestMapping(value = "/perm/add")
    public String add(Model model){
        List<Integer> integerList=new ArrayList<Integer>();
        integerList.add(1);
        integerList.add(2);
        List list= permissionService.queryByType(integerList);
        Perm perm=new Perm();
        model.addAttribute("permlist",list);
        model.addAttribute("perm",perm);
        return "/perm/add";
    }
    @Auth(rule = "/perm/add")
    @ResponseBody
    @RequestMapping(value = "/perm/adding")
    public JsonResult adding(Perm perm){
        if (perm.getParentId()!=null&&perm.getParentId()>0){
            Perm parent= permissionService.queryById(perm.getParentId());
            if (parent!=null){
                int type=parent.getPermType()+1;
                perm.setPermType(type);
            }else {
                perm.setPermType(1);
            }
        }else {
            perm.setParentId((long)0);
            perm.setPermType(1);
        }
        perm.setCreateTime(new Date());
        perm.setUpdateTime(new Date());
        permissionService.insertPerm(perm);
        return jsonResult(1,"新增成功");
    }

    @Auth(rule = "/perm/edit")
    @RequestMapping(value = "/perm/edit")
    public String edit(Model model, @RequestParam(value = "permid",required = false,defaultValue = "0") Long id){
        List<Integer> integerList=new ArrayList<Integer>();
        integerList.add(1);
        integerList.add(2);
        List list= permissionService.queryByType(integerList);
        Perm perm= permissionService.queryById(id);
        model.addAttribute("permlist",list);
        model.addAttribute("perm",perm);
        return "/perm/edit";
    }

    @Auth(rule = "/perm/edit")
    @ResponseBody
    @RequestMapping(value = "/perm/editing")
    public JsonResult editing(Perm perm){
        if (perm.getParentId()!=null&&perm.getParentId()>0){
            Perm parent= permissionService.queryById(perm.getParentId());
            if (parent!=null){
                int type=parent.getPermType()+1;
                perm.setPermType(type);
            }else {
                perm.setPermType(1);
            }
        }else {
            perm.setParentId((long)0);
            perm.setPermType(1);
        }
        if (perm.getId()!=null&&perm.getId()>0){    //修改
            perm.setUpdateTime(new Date());
            permissionService.updatePerm(perm);
            return jsonResult(1,"修改成功");
        }
        return jsonResult(-1,"修改失败");
    }

    @Auth(rule = "/perm/delete")
    @ResponseBody
    @RequestMapping(value = "/perm/delete")
    public JsonResult delete(@RequestParam(value = "permid",defaultValue = "0") Long id){
        if (permissionService.deletePerm(id)>0){
            return jsonResult(1,"删除成功");
        }
        return jsonResult(-1,"删除失败");
    }

    @Auth(rule = "/perm/deleteBatch")
    @ResponseBody
    @RequestMapping(value = "/perm/deleteBatch")
    public JsonResult deleteBatch(@RequestParam(value = "ids") String ids){
        if (StringUtils.isEmpty(ids)){
            return jsonResult(-1,"参数为空");
        }
//        if (memberService.deleteBatch(ids)){
//            return jsonResult(1,"删除成功");
//        }
        return jsonResult(-1,"删除失败");
    }
}
