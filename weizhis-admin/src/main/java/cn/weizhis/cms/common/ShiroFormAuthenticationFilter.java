package cn.weizhis.cms.common;

import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Auther: minliang
 * @Date: 2018/10/23 14:58
 * @Description:
 */
public class ShiroFormAuthenticationFilter extends FormAuthenticationFilter {
    Logger logger = LoggerFactory.getLogger(ShiroFormAuthenticationFilter.class);
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        if (isLoginRequest(request, response)) {
            if (isLoginSubmission(request, response)) {
                if (logger.isTraceEnabled()) {
                    logger.trace("Login submission detected.  Attempting to execute login.");
                }
                return executeLogin(request, response);
            } else {
                if (logger.isTraceEnabled()) {
                    logger.trace("Login page view.");
                }
                //allow them to see the login page ;)
                return true;
            }
        } else {
            HttpServletRequest req = (HttpServletRequest)request;
            HttpServletResponse resp = (HttpServletResponse) response;
            if(req.getMethod().equals(RequestMethod.OPTIONS.name())) {
                resp.setStatus(HttpStatus.OK.value());
                return true;
            }

            if (logger.isTraceEnabled()) {
                logger.trace("Attempting to access a path which requires authentication.  Forwarding to the " +
                        "Authentication url [" + getLoginUrl() + "]");
            }
            //前端Ajax请求时requestHeader里面带一些参数，用于判断是否是前端的请求
//            String token= req.getHeader("token");
//            if (token!= null || req.getHeader("wkcheck") != null) {
//                //前端Ajax请求，则不会重定向
//                resp.setHeader("Access-Control-Allow-Origin",  req.getHeader("Origin"));
//                resp.setHeader("Access-Control-Allow-Credentials", "true");
//                resp.setContentType("application/json; charset=utf-8");
//                resp.setCharacterEncoding("UTF-8");
//                PrintWriter out = resp.getWriter();
//                InvokeResult result = new InvokeResult();
//                result.failure("登录失效");
//                out.println(result);
//                out.flush();
//                out.close();
//            } else {
                saveRequestAndRedirectToLogin(request, response);
//            }
            return false;
        }
    }
}
