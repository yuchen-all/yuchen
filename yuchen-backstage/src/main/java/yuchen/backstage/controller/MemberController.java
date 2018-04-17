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
import yuchen.core.sys.model.PageDataTable;
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
public class MemberController extends BaseController {
    @Resource
    private MemberService memberService;

    @Resource
    private RoleService roleService;

    @Auth(rule = "/member/index")
    @RequestMapping(value = "/member/index")
    public String index(Model model,MemberQuery query, HttpSession httpSession) {
//        PageModel<MemberDto> list= memberService.queryPageList(query);
//        model.addAttribute("memberlist",list);
//        model.addAttribute("query",query);
        model.addAttribute("user",getAuthUser(httpSession));
        return "/member/index";
    }

    @Auth(rule = "/member/index")
    @RequestMapping(value = "/member/ajax")
    @ResponseBody
    public PageDataTable<MemberDto> ajax(MemberQuery query) {
        PageModel<MemberDto> list= memberService.queryPageList(query);
        PageDataTable<MemberDto> pageDataTable = new PageDataTable<>();
        pageDataTable.setData(list.getModel());
        pageDataTable.setiTotalRecords(list.getTotalcount());
        pageDataTable.setiTotalDisplayRecords(list.getTotalcount());
        pageDataTable.setPageNo(list.getCurrpage());
        pageDataTable.setiDisplayLength(list.getPagesize());
        return pageDataTable;
    }

    @Auth(rule = "/member/add")
    @RequestMapping(value = "/member/add")
    public String add(Model model, @RequestParam(value = "memberid",required = false,defaultValue = "0") Long id) {
        MemberDto member=new MemberDto();
        List list= roleService.queryList();
        if (id>0){
            member=memberService.queryDtoById(id);
            model.addAttribute("rolelist",list);
            model.addAttribute("member",member);
            return "/member/edit";
        }
        model.addAttribute("rolelist",list);
        model.addAttribute("member",member);
        return "/member/add";
    }
    @Auth(rule ="/member/add" )
    @ResponseBody
    @RequestMapping(value = "/member/adding")
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

    @Auth(rule = "/member/edit")
    @RequestMapping(value = "/member/edit")
    public String edit(Model model, @RequestParam(value = "memberId") Long memberId){
        MemberDto member = memberService.queryDtoById(memberId);
        List<Role> roleList = roleService.queryList();
        model.addAttribute("member",member);
        model.addAttribute("rolelist",roleList);
        return "/member/edit";
    }

    @Auth(rule = "/member/start" )
    @ResponseBody
    @RequestMapping(value = "/member/start")
    public JsonResult start(@RequestParam(value = "memberid") Long id){
        if (memberService.updateStatus(id,(short) 1)>0){
            return jsonResult(1,"启用成功");
        }
        return jsonResult(-1,"启用失败");
    }
    @Auth(rule = "/member/stop")
    @ResponseBody
    @RequestMapping(value = "/member/stop")
    public JsonResult stop(@RequestParam(value = "memberid") Long id){
        if (memberService.updateStatus(id,(short)-1)>0){
            return jsonResult(1,"禁用成功");
        }
        return jsonResult(-1,"禁用失败");
    }

    @Auth(rule = "/member/delete")
    @ResponseBody
    @RequestMapping(value = "/member/delete")
    public JsonResult delete(@RequestParam(value = "memberid") Long id){
        if (id>0){
            if (memberService.deleteMember(id)>0){
                return jsonResult(1,"删除成功");
            }
        }
        return jsonResult(-1,"删除失败");
    }

    @Auth(rule = "/member/deleteBatch")
    @ResponseBody
    @RequestMapping(value = "/member/deleteBatch")
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
