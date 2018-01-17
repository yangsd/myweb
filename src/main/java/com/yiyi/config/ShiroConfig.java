package com.yiyi.config;


import com.yiyi.auth.shiro.KickoutSessionControlFilter;
import com.yiyi.auth.shiro.RetryLimitHashedCredentialsMatcher;
import com.yiyi.auth.shiro.UserRealm;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.session.mgt.eis.JavaUuidSessionIdGenerator;
import org.apache.shiro.session.mgt.quartz.QuartzSessionValidationScheduler;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.config.MethodInvokingFactoryBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author sdyang
 * @date 2018-01-16 14:20:51
 */
@Configuration
public class ShiroConfig {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private Map<String, Filter> filters = new HashMap<String, Filter>();

    private static Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();

    @Bean
    public CacheManager getShiroCacheManager(EhCacheManagerFactoryBean ehcache) {
        EhCacheManager ehCacheManager = new EhCacheManager();
        ehCacheManager.setCacheManager(ehcache.getObject());
        return ehCacheManager;
    }

    /**
     * 过滤请求的url
     *
     * @return
     */
    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean filterRegistration = new FilterRegistrationBean();
        filterRegistration.setFilter(new DelegatingFilterProxy("shiroFilter"));
        // 该值缺省为false,表示生命周期由SpringApplicationContext管理,设置为true则表示由ServletContainer管理
        filterRegistration.addInitParameter("targetFilterLifecycle", "true");
        filterRegistration.setEnabled(true);
        filterRegistration.addUrlPatterns("/*");
        return filterRegistration;
    }

    /**
     * 登录次数限制
     *
     * @return
     */
    @Bean(name = "credentialsMatcher")
    public RetryLimitHashedCredentialsMatcher getCredentialsMatcher(CacheManager shiroCacheManager) {
        //使用redis作为缓存
        RetryLimitHashedCredentialsMatcher credentialsMatcher = new RetryLimitHashedCredentialsMatcher(shiroCacheManager);
        credentialsMatcher.setHashAlgorithmName("md5");
        credentialsMatcher.setHashIterations(2);
        credentialsMatcher.setStoredCredentialsHexEncoded(true);
        return credentialsMatcher;

    }


    @Bean
    public DefaultWebSessionManager getSessionManager() {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setGlobalSessionTimeout(1800000);
        sessionManager.setDeleteInvalidSessions(true);
        sessionManager.setSessionValidationSchedulerEnabled(false);
//        sessionManager.setSessionValidationScheduler(getSessionValidationScheduler());
        sessionManager.setSessionDAO(getSessionDAO());
        sessionManager.setSessionIdCookieEnabled(false);
//        sessionManager.setSessionIdCookie(getSimpleCookie());
        return sessionManager;
    }

    @Bean
    public JavaUuidSessionIdGenerator getSessionIdGenerator() {
        return new JavaUuidSessionIdGenerator();
    }

    @Bean
    public QuartzSessionValidationScheduler getSessionValidationScheduler() {
        QuartzSessionValidationScheduler sessionValidationScheduler = new QuartzSessionValidationScheduler();
        sessionValidationScheduler.setSessionValidationInterval(1800000);
        sessionValidationScheduler.setSessionManager(getSessionManager());
        return sessionValidationScheduler;
    }

    @Bean
    public EnterpriseCacheSessionDAO getSessionDAO() {
        EnterpriseCacheSessionDAO sessionDAO = new EnterpriseCacheSessionDAO();
        sessionDAO.setActiveSessionsCacheName("shiro-activeSessionCache");
        sessionDAO.setSessionIdGenerator(getSessionIdGenerator());
        return sessionDAO;
    }


    @Bean
    public SimpleCookie getSimpleCookie() {
        SimpleCookie cookie = new SimpleCookie("sid");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(-1);
        return cookie;
    }

    /**
     * 控制并发人数
     *
     * @return
     */
    @Bean
    public KickoutSessionControlFilter getKickoutSessionControlFilter(CacheManager cacheManager,DefaultWebSessionManager sessionManager) {

        KickoutSessionControlFilter kickoutSessionControlFilter = new KickoutSessionControlFilter();
        kickoutSessionControlFilter.setCacheManager(cacheManager);
        kickoutSessionControlFilter.setSessionManager(sessionManager);

        //是否踢出后来登录的，默认是false；即后者登录的用户踢出前者登录的用户
        kickoutSessionControlFilter.setKickoutAfter(false);

        kickoutSessionControlFilter.setMaxSession(1);
        kickoutSessionControlFilter.setKickoutUrl("/kickout");
        return kickoutSessionControlFilter;
    }

    /**
     * 域，Shiro从Realm获取安全数据（如用户、角色、权限）
     *
     * @return
     */
    @Bean(name = "userRealm")
    public UserRealm getUserRealm(RetryLimitHashedCredentialsMatcher credentialsMatcher) {
        UserRealm userRealm = new UserRealm();
        userRealm.setCredentialsMatcher(credentialsMatcher);
        userRealm.setCachingEnabled(true);//是否使用缓存
        return userRealm;
    }


    /**
     * Shiro生命周期处理器
     *
     * @return
     */
    @Bean(name = "lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }


    @Bean
    public DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator daap = new DefaultAdvisorAutoProxyCreator();
        daap.setProxyTargetClass(true);
        return daap;
    }

    /**
     * 安全管理器
     */
    @Bean(name = "securityManager")
    public DefaultWebSecurityManager getSecurityManager(DefaultWebSessionManager sessionManager,UserRealm userRealm,CacheManager shiroCacheManager) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();

        //会话管理
        securityManager.setSessionManager(sessionManager);
        securityManager.setRealm(userRealm);
        securityManager.setCacheManager(shiroCacheManager);

        return securityManager;
    }

    @Bean
    public MethodInvokingFactoryBean getMethodInvokingFactoryBean(DefaultWebSecurityManager securityManager) {
        MethodInvokingFactoryBean methodInvoking = new MethodInvokingFactoryBean();
        methodInvoking.setStaticMethod("org.apache.shiro.SecurityUtils.setSecurityManager");
        methodInvoking.setArguments(new DefaultWebSecurityManager[]{securityManager});
        return methodInvoking;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor getAuthorizationAttributeSourceAdvisor(DefaultWebSecurityManager securityManager,CacheManager shiroCacheManager) {
        AuthorizationAttributeSourceAdvisor aasa = new AuthorizationAttributeSourceAdvisor();
        aasa.setSecurityManager(securityManager);
        return new AuthorizationAttributeSourceAdvisor();
    }

    /**
     * 基于Form表单的身份验证过滤器
     *
     * @return
     */
    @Bean(name = "formAuthenticationFilter")
    public FormAuthenticationFilter getFormAuthenticationFilter() {
        FormAuthenticationFilter formFilter = new FormAuthenticationFilter();
        formFilter.setUsernameParam("loginid");
        formFilter.setPasswordParam("password");
        formFilter.setLoginUrl("/login");
        return formFilter;
    }

//     <bean id="shiroFilter" class="org.apache.shiro.spring.web.ShiroFilterFactoryBean">
//        <property name="securityManager" ref="securityManager"/>
//        <property name="loginUrl" value="/login"/>
//        <property name="filters">
//            <util:map>
//                <entry key="authc" value-ref="formAuthenticationFilter"/>
//                <entry key="sysUser" value-ref="sysUserFilter"/>
//                <entry key="kickout" value-ref="kickoutSessionControlFilter"/>
//            </util:map>
//        </property>
//        <property name="filterChainDefinitions">
//            <value>
//                /login = authc
//                /logout = logout
//                /authenticated = authc
//    /** = kickout,user,sysUser
//     </value>
//     </property>
//     </bean>

    /**
     * shiro权限过滤
     *
     * @return
     */
    @Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(DefaultWebSecurityManager securityManager,KickoutSessionControlFilter kickoutSessionControlFilter) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        shiroFilterFactoryBean.setLoginUrl("/login");
        shiroFilterFactoryBean.setSuccessUrl("/index");

        filters.put("authc", getFormAuthenticationFilter());
        filters.put("kickout", kickoutSessionControlFilter);

        shiroFilterFactoryBean.setFilters(filters);

        //authc需要验证，anon不需要验证

        filterChainDefinitionMap.put("/static/**", "anon");
        filterChainDefinitionMap.put("/login", "anon");//登录
        filterChainDefinitionMap.put("/logout", "logout");//退出

        filterChainDefinitionMap.put("/index", "authc");//首页
        filterChainDefinitionMap.put("/menu", "authc");//菜单

        filterChainDefinitionMap.put("/**", "kickout");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);

        return shiroFilterFactoryBean;
    }

    @Bean
    public ExceptionHandlerExceptionResolver getExceptionHandler() {
        ExceptionHandlerExceptionResolver eh = new ExceptionHandlerExceptionResolver();
        return eh;
    }
}
