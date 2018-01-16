package com.yiyi.auth.shiro;

import org.apache.shiro.web.filter.mgt.FilterChainManager;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;

import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sdyang on 2017/7/17.
 */
public class MyPathMatchingFilterChainResolver extends PathMatchingFilterChainResolver {
    private MyFilterChainManager customDefaultFilterChainManager;
    public void setCustomDefaultFilterChainManager(
            MyFilterChainManager customDefaultFilterChainManager) {
        this.customDefaultFilterChainManager = customDefaultFilterChainManager;
        setFilterChainManager(customDefaultFilterChainManager);
    }

    public FilterChain getChain(ServletRequest request, ServletResponse response, FilterChain originalChain) {
        FilterChainManager filterChainManager = getFilterChainManager();
        if (!filterChainManager.hasChains()) {
            return null;
        }
        String requestURI = getPathWithinApplication(request);
        List<String> chainNames = new ArrayList<String>();
        for (String pathPattern : filterChainManager.getChainNames()) {
            if (pathMatches(pathPattern, requestURI)) {
                chainNames.add(pathPattern);
            }
        }
        if(chainNames.size() == 0) {
            return null;
        }
        return customDefaultFilterChainManager.proxy(originalChain, chainNames);
    }
}