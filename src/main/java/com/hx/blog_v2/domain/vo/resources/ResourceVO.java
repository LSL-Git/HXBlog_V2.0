package com.hx.blog_v2.domain.vo.resources;

import com.hx.blog_v2.domain.BaseVO;
import com.hx.blog_v2.util.BlogConstants;
import com.hx.blog_v2.util.DateUtils;
import com.hx.log.alogrithm.tree.interf.TreeIdExtractor;

import java.util.Date;

/**
 * ResourceVO
 *
 * @author Jerry.X.He <970655147@qq.com>
 * @version 1.0
 * @date 5/22/2017 8:03 PM
 */
public class ResourceVO extends BaseVO implements TreeIdExtractor<ResourceVO, String> {

    private String id;
    private String name;
    private String iconClass;
    private String url;
    private int sort;
    private String parentId;
    private String createdAt;
    private String updatedAt;
    private int enable;

    public ResourceVO(String name, String iconClass, String url, String parentId, int enable) {
        this();
        this.name = name;
        this.iconClass = iconClass;
        this.url = url;
        this.parentId = parentId;
        this.enable = enable;
    }

    public ResourceVO() {
        Date now = new Date();
        createdAt = DateUtils.format(now, BlogConstants.FORMAT_YYYY_MM_DD_HH_MM_SS);
        updatedAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIconClass() {
        return iconClass;
    }

    public void setIconClass(String iconClass) {
        this.iconClass = iconClass;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getEnable() {
        return enable;
    }

    public void setEnable(int enable) {
        this.enable = enable;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public String parentId() {
        return parentId;
    }

    @Override
    public boolean equals(Object obj) {
        if(! (obj instanceof ResourceVO)) {
            return false;
        }

        return id.equals(((ResourceVO) obj).getId());
    }
}
