package com.yiyi.auth.shiro;

import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.servlet.ShiroHttpServletResponse;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author sdyang
 * @date 2017/2/12 17:34.
 */
public class MyShiroHttpServletResponse extends ShiroHttpServletResponse {

//    private static final String DEFAULT_SESSION_ID_PARAMETER_NAME = "JSESSIONID";

    public MyShiroHttpServletResponse(HttpServletResponse wrapped, ServletContext context,
                                      ShiroHttpServletRequest request) {
        super(wrapped, context, request);
    }

    @Override
    public String encodeRedirectURL(String url) {
        return url;
    }

    @Override
    protected String toEncoded(String url, String sessionId) {
        if ((url == null) || (sessionId == null))
            return (url);
        String path = url;
        String query = "";
        String anchor = "";
        int question = url.indexOf('?');
        if (question >= 0) {
            path = url.substring(0, question);
            query = url.substring(question);
        }
        int pound = path.indexOf('#');
        if (pound >= 0) {
            anchor = path.substring(pound);
            path = path.substring(0, pound);
        }
        StringBuilder sb = new StringBuilder(path);
        //重写toEncoded方法，注释掉这几行代码就不会再生成JESSIONID了。
//        if (sb.length() > 0) { // session id param can't be first.
//            sb.append(";");
//            sb.append(DEFAULT_SESSION_ID_PARAMETER_NAME);
//            sb.append("=");
//            sb.append(sessionId);
//        }
//        sb.append(anchor);
//        sb.append(query);
        return (sb.toString());
    }
}