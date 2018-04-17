package yuchen.backstage.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import yuchen.backstage.annotation.Auth;
import yuchen.backstage.common.JsonResult;
import yuchen.backstage.service.MemberService;
import yuchen.backstage.service.RoleService;
import yuchen.common.utility.MD5Utility;
import yuchen.core.sys.dto.MemberDto;
import yuchen.core.sys.model.PageModel;
import yuchen.core.sys.model.sys.Member;
import yuchen.core.sys.model.sys.Role;
import yuchen.core.sys.model.sys.query.MemberQuery;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/8/21.
 */
@Controller
public class AdminController extends BaseController {
    @Resource
    private MemberService memberService;

    @Resource
    private RoleService roleService;

    @Auth(rule = "/admin/index")
    @RequestMapping(value = "/admin/index")
    public String index(Model model, HttpSession httpSession) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        MemberQuery query=new MemberQuery();
        PageModel<MemberDto> list= memberService.queryPageList(query);
        model.addAttribute("memberlist",list);
        model.addAttribute("user",getAuthUser(httpSession));
        return "/admin/index";
    }
    @Auth(rule = "/admin/add")
    @RequestMapping(value = "/admin/add")
    public String add(Model model, @RequestParam(value = "memberid",required = false,defaultValue = "0") Long id) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        MemberDto member=new MemberDto();
        List list= roleService.queryList();
        if (id>0){
            member=memberService.queryDtoById(id);
            model.addAttribute("rolelist",list);
            model.addAttribute("member",member);
            return "/admin/edit";
        }
        model.addAttribute("rolelist",list);
        model.addAttribute("member",member);
        return "/admin/add";
    }
    @Auth(rule ="/admin/add" )
    @ResponseBody
    @RequestMapping(value = "/admin/adding")
    public JsonResult adding(Member member, @RequestParam(value = "roleids") String ids){
        if (member.getId()!=null&&member.getId()>0){
            Member oldMember = memberService.queryById(member.getId());
            oldMember.setUserName(member.getUserName());
            oldMember.setDisplayName(member.getDisplayName());
            oldMember.setSex(member.getSex());
            oldMember.setMobile(member.getMobile());
            oldMember.setRemark(member.getRemark());
            if (memberService.updateMember(oldMember,ids)){
                return jsonResult(1,"修改成功");
            }
            return jsonResult(-1,"修改失败");
        }
        member.setStatus((short)1);
        member.setCreateTime(new Date());
        member.setPassword(MD5Utility.toMD5(member.getPassword()));
        memberService.insertMember(member,ids);
        return jsonResult(1,"新增成功");
    }

    @Auth(rule = "/admin/edit")
    @RequestMapping(value = "/admin/edit")
    public String edit(Model model, @RequestParam(value = "memberId") Long memberId){
        MemberDto member = memberService.queryDtoById(memberId);
        List<Role> roleList = roleService.queryList();
        model.addAttribute("member",member);
        model.addAttribute("rolelist",roleList);
        return "/admin/edit";
    }

    @Auth(rule = "/admin/start" )
    @ResponseBody
    @RequestMapping(value = "/admin/start")
    public JsonResult start(@RequestParam(value = "memberid") Long id){
        if (memberService.updateStatus(id,(short) 1)>0){
            return jsonResult(1,"启用成功");
        }
        return jsonResult(-1,"启用失败");
    }
    @Auth(rule = "/admin/stop")
    @ResponseBody
    @RequestMapping(value = "/admin/stop")
    public JsonResult stop(@RequestParam(value = "memberid") Long id){
        if (memberService.updateStatus(id,(short)-1)>0){
            return jsonResult(1,"禁用成功");
        }
        return jsonResult(-1,"禁用失败");
    }

    @Auth(rule = "/admin/delete")
    @ResponseBody
    @RequestMapping(value = "/admin/delete")
    public JsonResult delete(@RequestParam(value = "memberid") Long id){
        if (id>0){
            if (memberService.deleteMember(id)>0){
                return jsonResult(1,"删除成功");
            }
        }
        return jsonResult(-1,"删除失败");
    }

    @Auth(rule = "/admin/deleteBatch")
    @ResponseBody
    @RequestMapping(value = "/admin/deleteBatch")
    public JsonResult deleteBatch(@RequestParam(value = "ids") String ids){
        if (StringUtils.isEmpty(ids)){
            return jsonResult(-1,"参数为空");
        }
        if (memberService.deleteBatch(ids)){
            return jsonResult(1,"删除成功");
        }
        return jsonResult(-1,"删除失败");
    }

}
