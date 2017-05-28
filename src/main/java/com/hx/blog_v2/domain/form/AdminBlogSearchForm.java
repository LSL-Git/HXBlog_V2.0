package com.hx.blog_v2.domain.form;

import com.hx.blog_v2.util.BlogConstants;
import com.hx.blog_v2.util.DateUtils;

import java.util.Date;

/**
 * 博客管理的搜索
 *
 * @author Jerry.X.He <970655147@qq.com>
 * @version 1.0
 * @date 5/20/2017 9:42 AM
 */
public class AdminBlogSearchForm {

    private String id;
    private String typeId;
    private String tagId;
    private String keywords;

    public AdminBlogSearchForm() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }
}
