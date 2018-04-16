package yuchen.backstage.annotation;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import yuchen.backstage.common.AuthUser;
import yuchen.backstage.common.Constants;
import yuchen.backstage.common.PermissionUtility;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by XR on 2016/8/29.
 */
@Aspect
@Component
public class AuthService {
    @Pointcut("@annotation(annotation.Auth)")
    public void methodPointcut(){

    }

    @Before("@annotation(auth)")
    public void before(Auth auth) throws Exception {
        ServletRequestAttributes requestAttributes= (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request= requestAttributes.getRequest();
        AuthUser authUser=(AuthUser) request.getSession().getAttribute(Constants.SESSION_USER_KEY);
        if (authUser==null){
            throw new LoginException("未登陆");
        }
        String permstr=(String) request.getSession().getAttribute(Constants.SESSION_USER_PERM_KEY);
        PermissionUtility.changePerms(permstr);
        String[] perms=permstr.split(",");
        boolean isauth=false;
        if (auth.rule().equals("/")){
            isauth=true;
        }else {
            for (String perm:perms) {
                if (auth.rule().equals(perm)){
                    isauth=true;
                    break;
                }
            }
        }
        if (!isauth){
            throw new AuthException("没有权限");
        }
    }



//    @Around(value = "@annotation(auth)")
//    public Object aroundMethod(ProceedingJoinPoint proceedingJoinPoint,Auth auth) throws Throwable {
//        ServletRequestAttributes requestAttributes= (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//        HttpServletRequest request= requestAttributes.getRequest();
//        AuthUser authUser=(AuthUser) request.getSession().getAttribute(Constants.SESSION_USER_KEY);
//        if (authUser==null){
//            throw new LoginException("未登陆");
//        }
//        String permstr=(String) request.getSession().getAttribute(Constants.SESSION_USER_PERM_KEY);
//        String[] perms=permstr.split(",");
//        for (String perm:perms) {
//            if (auth.rule()==perm){
//                return proceedingJoinPoint.proceed();
//            }
//        }
//        throw new AuthException("没有权限");
//    }
}
