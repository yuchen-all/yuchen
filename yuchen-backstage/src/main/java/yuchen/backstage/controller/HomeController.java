package yuchen.backstage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import yuchen.backstage.annotation.Auth;
import yuchen.backstage.common.AuthUser;
import yuchen.backstage.common.JsonResult;
import yuchen.backstage.service.MemberService;
import yuchen.core.sys.dto.AuthPerm;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by Administrator on 2016/8/20.
 */
@Controller
public class HomeController extends BaseController {
    @Resource
    private MemberService memberService;
    @Auth(rule = "/")
    @RequestMapping(value = "/")
    public String index(HttpSession httpSession, Model model){
        AuthUser authUser=getAuthUser(httpSession);
        List<AuthPerm> list= memberService.queryAuthPerm(authUser.getId());
        model.addAttribute("perms",list);
        model.addAttribute("user",authUser);
        return "index";
    }

    @RequestMapping(value = "/welcome",method = {RequestMethod.GET})
    public String welcome(){
        return "welcome";
    }

    @RequestMapping(value = "/404")
    public String error(){
        return "404";
    }

    @RequestMapping(value = "/noauthorized")
    public String noauthorized(){
        return "noauthorized";
    }

    @ResponseBody
    @RequestMapping(value = "/noauthorized/ajax")
    public JsonResult noauthorizedajax(){
        return jsonResult(-1,"没有权限");
    }

    @ResponseBody
    @RequestMapping(value = "/nologin")
    public JsonResult nologin(){
        return jsonResult(-1,"请登录");
    }
}
