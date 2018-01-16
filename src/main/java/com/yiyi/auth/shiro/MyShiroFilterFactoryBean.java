package com.yiyi.auth.shiro;


import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.mgt.FilterChainManager;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.mgt.WebSecurityManager;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.apache.shiro.mgt.SecurityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanInitializationException;

/**
 * @author sdyang
 * @date 2017/2/12 17:33.
 */
public class MyShiroFilterFactoryBean extends ShiroFilterFactoryBean {

    private static final Logger logger = LoggerFactory.getLogger(MyShiroFilterFactoryBean.class);

    @Override
    public Class getObjectType() {
        return MyShiroFilterFactoryBean.class;
    }

    @Override
    protected AbstractShiroFilter createInstance() throws Exception {
        logger.debug("Creating Shiro Filter instance.");
        SecurityManager securityManager = getSecurityManager();
        String manager1;
        if(securityManager == null) {
            manager1 = "SecurityManager property must be set.";
            throw new BeanInitializationException(manager1);
        } else if(!(securityManager instanceof WebSecurityManager)) {
            manager1 = "The security manager does not implement the WebSecurityManager interface.";
            throw new BeanInitializationException(manager1);
        } else {
            FilterChainManager manager = this.createFilterChainManager();
            PathMatchingFilterChainResolver chainResolver = new PathMatchingFilterChainResolver();
            chainResolver.setFilterChainManager(manager);
            return new MyShiroFilter((WebSecurityManager)securityManager, chainResolver);
        }
    }

}
