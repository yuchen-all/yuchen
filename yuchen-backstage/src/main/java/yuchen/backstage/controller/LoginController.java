package yuchen.backstage.controller;

import org.apache.commons.lang.StringUtils;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import yuchen.backstage.common.AuthUser;
import yuchen.backstage.common.Constants;
import yuchen.backstage.service.MemberService;
import yuchen.backstage.service.PermissionService;
import yuchen.common.utility.MD5Utility;
import yuchen.core.sys.model.sys.Member;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Set;

/**
 * Created by Administrator on 2016/8/21.
 */
@Controller
public class LoginController extends BaseController {
    @Resource
    private MemberService memberService;

    @Resource
    private PermissionService permissionService;

    @Resource
    private ThreadPoolTaskExecutor taskExecutor;

    @RequestMapping(value = "/login",method = {RequestMethod.GET})
    public String login(String msgcode,Model model){
        if ("-1".equals(msgcode)){
            model.addAttribute("msg","请输入用户名密码");
        }
        if ("-2".equals(msgcode)){
            model.addAttribute("msg","用户名密码错误");
        }
        if ("-3".equals(msgcode)){
            model.addAttribute("msg","用户名密码错误");
        }
        if ("-4".equals(msgcode)){
            model.addAttribute("msg","用户已禁用");
        }
        return "login";
    }

    @RequestMapping(value = "/httplogin",method = {RequestMethod.POST})
    public String httplogin(Model model, @RequestParam(value = "username") String username, @RequestParam(value = "password") String password,
                            HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView modelAndView=new ModelAndView();
        if (StringUtils.isEmpty(username)||StringUtils.isEmpty(password)){
            model.addAttribute("msgcode","-1");
            return "redirect:/login";
        }
        Member member= memberService.queryByUsername(username);
        if (member==null){
            model.addAttribute("msgcode","-2");
            return "redirect:/login";
        }
        if (!MD5Utility.toMD5(password).equals(member.getPassword())){
            model.addAttribute("msgcode","-3");
            return "redirect:/login";
        }
        if (member.getStatus()==-1){
            model.addAttribute("msgcode","-4");
            return "redirect:/login";
        }
        if (MD5Utility.toMD5(password).equals(member.getPassword())){
//            String userinfo="";
//            userinfo+=""+member.getId()+","+member.getPhone()+","+member.getUserName()+"";
//            setCookie(response,userinfo);
            AuthUser authUser=new AuthUser();
            authUser.setId(member.getId());
            authUser.setUserName(member.getUserName());
            authUser.setDisplayName(member.getDisplayName());
            Set<String> perms= permissionService.queryUrlsByMemberId(member.getId());
            StringBuilder sp=new StringBuilder();
            for (String perm:perms) {
                sp.append(perm+",");
            }
            request.getSession().setAttribute(Constants.SESSION_USER_KEY,authUser);
            request.getSession().setAttribute(Constants.SESSION_USER_PERM_KEY,sp.toString());
            return "redirect:/";
        }
        return "redirect:/login";
    }

    @RequestMapping(value = "/loginout")
    public RedirectView loginout(HttpSession httpSession){
        httpSession.removeAttribute(Constants.SESSION_USER_KEY);
        httpSession.removeAttribute(Constants.SESSION_USER_PERM_KEY);
        return new RedirectView("/login", true);
    }
}
