package yuchen.backstage.common;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;

/**
 * Created by alan.zheng on 2018/4/16.
 */
public class Authority {
    public boolean isPermission(String perm){
        String strings=PermissionUtility.getPerms();
        String[] perms=strings.split(",");
        boolean isauth=false;
        for (String permstr:perms) {
            if (permstr.equals(perm)){
                isauth=true;
                break;
            }
        }
        return isauth;
    }

    public String permissionList(){
        String strings=PermissionUtility.getPerms();
        return strings;
    }

    public AuthUser currentUser(){
        HttpSession session = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getRequest().getSession();
        AuthUser authUser = (AuthUser) session.getAttribute(Constants.SESSION_USER_KEY);
        return authUser;
    }

}
