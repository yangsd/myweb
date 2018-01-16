package com.yiyi.auth.shiro;

import org.apache.shiro.session.ExpiredSessionException;
import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.session.mgt.DelegatingSession;
import org.apache.shiro.session.mgt.SessionContext;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.web.servlet.Cookie;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.WebSessionKey;
import org.apache.shiro.web.session.mgt.WebSessionManager;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;

/**
 * Web Session Manager
 * Created by sdyang on 2017/7/15.
 */
public class MyWebSessionManager extends DefaultSessionManager implements WebSessionManager {
    private static final Logger logger = LoggerFactory.getLogger(MyWebSessionManager.class);
    private Cookie sessionIdCookie;
    private boolean sessionIdCookieEnabled;
    private boolean sessionIdUrlRewritingEnabled;

    public MyWebSessionManager() {
        SimpleCookie cookie = new SimpleCookie("JSESSIONID");
        cookie.setHttpOnly(true);
        this.sessionIdCookie = cookie;
        this.sessionIdCookieEnabled = true;
        this.sessionIdUrlRewritingEnabled = true;
    }

    public Cookie getSessionIdCookie() {
        return this.sessionIdCookie;
    }

    public void setSessionIdCookie(Cookie sessionIdCookie) {
        this.sessionIdCookie = sessionIdCookie;
    }

    public boolean isSessionIdCookieEnabled() {
        return this.sessionIdCookieEnabled;
    }

    public void setSessionIdCookieEnabled(boolean sessionIdCookieEnabled) {
        this.sessionIdCookieEnabled = sessionIdCookieEnabled;
    }

    public boolean isSessionIdUrlRewritingEnabled() {
        return this.sessionIdUrlRewritingEnabled;
    }

    public void setSessionIdUrlRewritingEnabled(boolean sessionIdUrlRewritingEnabled) {
        this.sessionIdUrlRewritingEnabled = sessionIdUrlRewritingEnabled;
    }

    private void storeSessionId(Serializable currentId, HttpServletRequest request, HttpServletResponse response) {
        if(currentId == null) {
            String template1 = "sessionId cannot be null when persisting for subsequent requests.";
            throw new IllegalArgumentException(template1);
        } else {
            Cookie template = this.getSessionIdCookie();
            SimpleCookie cookie = new SimpleCookie(template);
            String idString = currentId.toString();
            cookie.setValue(idString);
            cookie.saveTo(request, response);
            logger.trace("Set session ID cookie for session with id {}", idString);
        }
    }



    private void removeSessionIdCookie(HttpServletRequest request, HttpServletResponse response) {
        this.getSessionIdCookie().removeFrom(request, response);
    }

    private String getSessionIdCookieValue(ServletRequest request, ServletResponse response) {
        if(!this.isSessionIdCookieEnabled()) {
            logger.debug("Session ID cookie is disabled - session id will not be acquired from a request cookie.");
            return null;
        } else if(!(request instanceof HttpServletRequest)) {
            logger.debug("Current request is not an HttpServletRequest - cannot get session ID cookie.  Returning null.");
            return null;
        } else {
            HttpServletRequest httpRequest = (HttpServletRequest)request;
            return this.getSessionIdCookie().readValue(httpRequest, WebUtils.toHttp(response));
        }
    }

    private Serializable getReferencedSessionId(ServletRequest request, ServletResponse response) {
        String id = this.getSessionIdCookieValue(request, response);
        if(id != null) {
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE, "cookie");
        } else {
            id = this.getUriPathSegmentParamValue(request, "JSESSIONID");
            if(id == null) {
                String name = this.getSessionIdName();
                id = request.getParameter(name);
                if(id == null) {
                    id = request.getParameter(name.toLowerCase());
                }
            }

            if(id != null) {
                request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE, "url");
            }
        }

        if(id != null) {
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID, id);
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID, Boolean.TRUE);
        }

        request.setAttribute(ShiroHttpServletRequest.SESSION_ID_URL_REWRITING_ENABLED, Boolean.valueOf(this.isSessionIdUrlRewritingEnabled()));
        return id;
    }

    private String getUriPathSegmentParamValue(ServletRequest servletRequest, String paramName) {
        if(!(servletRequest instanceof HttpServletRequest)) {
            return null;
        } else {
            HttpServletRequest request = (HttpServletRequest)servletRequest;
            String uri = request.getRequestURI();
            if(uri == null) {
                return null;
            } else {
                int queryStartIndex = uri.indexOf(63);
                if(queryStartIndex >= 0) {
                    uri = uri.substring(0, queryStartIndex);
                }

                int index = uri.indexOf(59);
                if(index < 0) {
                    return null;
                } else {
                    String TOKEN = paramName + "=";
                    uri = uri.substring(index + 1);
                    index = uri.lastIndexOf(TOKEN);
                    if(index < 0) {
                        return null;
                    } else {
                        uri = uri.substring(index + TOKEN.length());
                        index = uri.indexOf(59);
                        if(index >= 0) {
                            uri = uri.substring(0, index);
                        }

                        return uri;
                    }
                }
            }
        }
    }

    private String getSessionIdName() {
        String name = this.sessionIdCookie != null?this.sessionIdCookie.getName():null;
        if(name == null) {
            name = "JSESSIONID";
        }

        return name;
    }

    protected Session createExposedSession(Session session, SessionContext context) {
        if(!WebUtils.isWeb(context)) {
            return super.createExposedSession(session, context);
        } else {
            ServletRequest request = WebUtils.getRequest(context);
            ServletResponse response = WebUtils.getResponse(context);
            WebSessionKey key = new WebSessionKey(session.getId(), request, response);
            return new DelegatingSession(this, key);
        }
    }

    protected Session createExposedSession(Session session, SessionKey key) {
        if(!WebUtils.isWeb(key)) {
            return super.createExposedSession(session, key);
        } else {
            ServletRequest request = WebUtils.getRequest(key);
            ServletResponse response = WebUtils.getResponse(key);
            WebSessionKey sessionKey = new WebSessionKey(session.getId(), request, response);
            return new DelegatingSession(this, sessionKey);
        }
    }

    protected void onStart(Session session, SessionContext context) {
        super.onStart(session, context);
        if(!WebUtils.isHttp(context)) {
            logger.debug("SessionContext argument is not HTTP compatible or does not have an HTTP request/response pair. No session ID cookie will be set.");
        } else {
            HttpServletRequest request = WebUtils.getHttpRequest(context);
            HttpServletResponse response = WebUtils.getHttpResponse(context);
            if(this.isSessionIdCookieEnabled()) {
                Serializable sessionId = session.getId();
                this.storeSessionId(sessionId, request, response);
            } else {
                logger.debug("Session ID cookie is disabled.  No cookie has been set for new session with id {}", session.getId());
            }

            request.removeAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE);
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_IS_NEW, Boolean.TRUE);
        }
    }

    public Serializable getSessionId(SessionKey key) {
        Serializable id = super.getSessionId(key);
        if(id == null && WebUtils.isWeb(key)) {
            ServletRequest request = WebUtils.getRequest(key);
            ServletResponse response = WebUtils.getResponse(key);
            id = this.getSessionId(request, response);
        }

        return id;
    }

    protected Serializable getSessionId(ServletRequest request, ServletResponse response) {
        return this.getReferencedSessionId(request, response);
    }

    protected void onExpiration(Session s, ExpiredSessionException ese, SessionKey key) {
        super.onExpiration(s, ese, key);
        this.onInvalidation(key);
    }

    protected void onInvalidation(Session session, InvalidSessionException ise, SessionKey key) {
        super.onInvalidation(session, ise, key);
        this.onInvalidation(key);
    }

    private void onInvalidation(SessionKey key) {
        ServletRequest request = WebUtils.getRequest(key);
        if(request != null) {
            request.removeAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID);
        }

        if(WebUtils.isHttp(key)) {
            logger.debug("Referenced session was invalid.  Removing session ID cookie.");
            this.removeSessionIdCookie(WebUtils.getHttpRequest(key), WebUtils.getHttpResponse(key));
        } else {
            logger.debug("SessionKey argument is not HTTP compatible or does not have an HTTP request/response pair. Session ID cookie will not be removed due to invalidated session.");
        }

    }

    protected void onStop(Session session, SessionKey key) {
        super.onStop(session, key);
        if(WebUtils.isHttp(key)) {
            HttpServletRequest request = WebUtils.getHttpRequest(key);
            HttpServletResponse response = WebUtils.getHttpResponse(key);
            logger.debug("Session has been stopped (subject logout or explicit stop).  Removing session ID cookie.");
            this.removeSessionIdCookie(request, response);
        } else {
            logger.debug("SessionKey argument is not HTTP compatible or does not have an HTTP request/response pair. Session ID cookie will not be removed due to stopped session.");
        }

    }

    public boolean isServletContainerSessions() {
        return false;
    }
}
