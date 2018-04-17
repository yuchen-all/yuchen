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
import yuchen.backstage.service.RoleService;
import yuchen.core.sys.dto.RolePermDto;
import yuchen.core.sys.model.PageModel;
import yuchen.core.sys.model.sys.Role;
import yuchen.core.sys.model.sys.query.RoleQuery;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;

/**
 * Created by XR on 2016/8/29.
 */
@Controller
public class RoleController extends BaseController {

    @Resource
    private PermissionService permissionService;

    @Resource
    private RoleService roleService;
    @Auth(rule = "/role/index")
    @RequestMapping(value = "/role/index")
    public String index(Model model, Integer currPage){
        RoleQuery query=new RoleQuery();
        PageModel<Role> list=roleService.queryPageList(query);
        model.addAttribute("rolelist",list);
        return "/role/index";
    }
    @Auth(rule = "/role/add")
    @RequestMapping(value = "/role/add")
    public String add(Model model){
        List list=permissionService.queryPermByLevel();
        RolePermDto rolePermDto=new RolePermDto();
        model.addAttribute("permlist",list);
        model.addAttribute("role",rolePermDto);
        return "/role/add";
    }
    @Auth(rule = "/role/add")
    @ResponseBody
    @RequestMapping(value = "/role/adding")
    public JsonResult adding(Role role, @RequestParam(value = "permids") String ids){
        role.setCreateTime(new Date());
        role.setUpdateTime(new Date());
        if (roleService.insertRole(role,ids)){
            return jsonResult(1,"新增成功");
        }
        return jsonResult(-1,"新增失败");
    }

    @Auth(rule = "/role/edit")
    @RequestMapping(value = "/role/edit")
    public String edit(Model model, @RequestParam(value = "roleid",defaultValue = "0",required = false) Long id) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        List list=permissionService.queryPermByLevel();
        RolePermDto rolePermDto=roleService.queryDtoById(id);
        model.addAttribute("permlist",list);
        model.addAttribute("role",rolePermDto);
        return "/role/edit";
    }

    @Auth(rule = "/role/edit")
    @ResponseBody
    @RequestMapping(value = "/role/editing")
    public JsonResult editing(Role role, @RequestParam(value = "permids") String ids){
        if (role.getId()!=null&&role.getId()>0){   //修改
            try {
                if (roleService.updateRole(role,ids)){
                    return jsonResult(1,"修改成功");
                }
            } catch (Exception e) {
                return jsonResult(-1,"修改失败");
            }
        }
        return jsonResult(-1,"修改失败");
    }

    @Auth(rule = "/role/delete")
    @ResponseBody
    @RequestMapping(value = "/role/delete")
    public JsonResult delete(@RequestParam(value = "roleid") Long id){
        if (id>0){
            if (roleService.deleteRole(id)>0){
                return jsonResult(1,"删除成功");
            }
        }
        return jsonResult(-1,"删除失败");
    }

    @Auth(rule = "/role/resetadmin")
    @ResponseBody
    @RequestMapping(value = "/role/resetadmin")
    public JsonResult resetAdmin(){
        if (roleService.resetadmin()){
            return jsonResult(1,"重置成功");
        }
        return jsonResult(-1,"重置失败");
    }

    @Auth(rule = "/role/deleteBatch")
    @ResponseBody
    @RequestMapping(value = "/role/deleteBatch")
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
