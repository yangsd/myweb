package com.yiyi.auth.service;

import com.yiyi.auth.mapper.ResourceMapper;
import com.yiyi.auth.mapper.RoleResourceRelMapper;
import com.yiyi.auth.mapper.UserMapper;
import com.yiyi.auth.mapper.UserRoleRelMapper;
import com.yiyi.auth.model.Resource;
import com.yiyi.auth.model.User;
import com.yiyi.exception.MyException;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author sdyang
 * @create 2018-01-11 10:29
 **/
@Service
public class ResourceService {

    private static Logger logger = LoggerFactory.getLogger(ResourceService.class);

    @Autowired
    private ResourceMapper resourceMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserRoleRelMapper userRoleRelMapper;
    @Autowired
    private RoleResourceRelMapper roleResourceRelMapper;

    public void save(Resource resource) {
        if(resource.getId() == null){
            resourceMapper.insert(resource);
        }else{
            resourceMapper.updateById(resource);
        }
    }

    public void delete(Long id) {
        resourceMapper.deleteById(id);
    }

    public Resource findOne(Long id) {
        return resourceMapper.selectById(id);
    }

    public List<Resource> findAll() {
        return (List<Resource>) resourceMapper.selectAll();
    }


    /**
     * 查找菜单资源
     * @param resourceIds
     * @return
     */
    public List<Resource> findMenu(List<Long> resourceIds) {
        List<Resource> resources = new ArrayList<Resource>();
        for (Long resourceId : resourceIds) {
            Resource resource = findOne(resourceId);
            if (resource != null && resource.getType() == Resource.ResourceType.menu) {
                resources.add(resource);
            }
        }
        return resources;
    }


    public String getMenuByLoginId(String loginid)  {

        StringBuffer menu = new StringBuffer();

        User user = userMapper.findByLoginId(loginid);// 当前用户

        List<Long> roleIds = userRoleRelMapper.findByUserId(user.getId());

        if (roleIds == null || roleIds.size()==0) {
            throw new MyException("用户没有分配角色！");
        }

        //查询用户所拥有的资源

        List<Resource> urls = null;
        List<Long> resourceids = new ArrayList<Long>();
        List<Long> roleids = userRoleRelMapper.findByUserId(user.getId());
        for(Long id:roleids) {
            resourceids.addAll(roleResourceRelMapper.findByRoleId(id));
        }

        urls = findMenu(resourceids);

        if (CollectionUtils.isEmpty(urls)) {
            return null;
        }

        Map<Long, List<Resource>> urlMap = new HashMap<Long, List<Resource>>();
        for (Resource url : urls) {
            // 如果Map里已经包含了key，则增加url
            if (urlMap.containsKey(url.getParent_id())) {
                List<Resource> l = urlMap.get(url.getParent_id());
                l.add(url);
                urlMap.put(url.getParent_id(), l);
            } else {
                // 否则新增一个list,新增一个key
                List<Resource> l = new ArrayList<Resource>();
                l.add(url);
                urlMap.put(url.getParent_id(), l);
            }
        }
        menu.append("<div id=\"sidebar-menu\" class=\"main_menu_side hidden-print main_menu\">");
        menu.append("<div class=\"menu_section\">");
        menu.append("<h3>&nbsp;&nbsp;&nbsp;&nbsp;</h3>");
        menu.append("<ul class=\"nav side-menu\">");


        // key为0为所有的父菜单
        List<Resource> parentUrl = urlMap.get(Resource.parentCode);
        if (parentUrl != null && parentUrl.size() > 0) {
            for (Resource p : parentUrl) {
                menu.append("<li><a><i class=\"fa ").append(p.getIcon()).append(" \"></i> ").append(p.getName()).append(" <span class=\"fa fa-chevron-down\"></span></a>");
                menu.append("<ul class=\"nav child_menu\">");
                if (urlMap.containsKey(p.getId())) {
                    // 子菜单
                    this.getSubMenu(p.getId(), urlMap, menu);
                }
                menu.append(" </ul>");
                menu.append("</li>");
            }
        }
        menu.append("</ul>");
        menu.append("</div>");
        menu.append("</div>");
        return menu.toString();
    }

    /**
     * 组织子菜单
     *
     * @param pk_url
     * @param urlMap
     * @param menu
     * @author sdyang
     * @date 2015年8月15日 下午12:15:08
     */
    private void getSubMenu(Long pk_url, Map<Long, List<Resource>> urlMap,
                            StringBuffer menu) {

        List<Resource> urlList = urlMap.get(pk_url);
        for (Resource url : urlList) {
            if (urlMap.containsKey(url.getId())) {
                getSubMenu(url.getId(), urlMap, menu);
            } else {
                if (url.getUrl() != null) {
                    menu.append("<li><a href=\"").append(url.getUrl()).append("\">").append(url.getName()).append("</a></li>");
                }
            }
        }
    }

}
