package yuchen.backstage.annotation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by XR on 2016/8/29.
 */
public class AuthExceptionHandler implements HandlerExceptionResolver {
    private static Logger logger = LoggerFactory.getLogger(AuthExceptionHandler.class);
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {

        String requestheader= httpServletRequest.getHeader("X-Requested-With");
        if (e instanceof LoginException){
            if (requestheader!=null){
                return new ModelAndView("redirect:/nologin");
            }
            return new ModelAndView("redirect:/login");
        }
        if (e instanceof AuthException){
            if (requestheader!=null){
                return new ModelAndView("redirect:/noauthorized/ajax");
            }
            return new ModelAndView("redirect:/noauthorized");
        }
        logger.error("统一异常",e);
        return new ModelAndView("redirect:/404");
    }
}
