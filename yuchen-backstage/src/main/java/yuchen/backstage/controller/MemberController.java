package yuchen.backstage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Administrator on 2016/8/21.
 */
@Controller
@RequestMapping(value = "/member")
public class MemberController {
    @RequestMapping(value = "/add")
    public String addMember(){
        return "member/addmember";
    }
}
