package com.hx.blog_v2.domain.form.blog;

import com.hx.blog_v2.domain.BaseForm;
import com.hx.blog_v2.util.CacheConstants;
import com.hx.log.str.StringUtils;

import java.util.Arrays;
import java.util.List;

/**
 * 当前层回复的搜索
 *
 * @author Jerry.X.He <970655147@qq.com>
 * @version 1.0
 * @date 5/20/2017 9:42 AM
 */
public class FloorCommentListSearchForm extends BaseForm {

    private String blogId;
    private String floorId;

    public FloorCommentListSearchForm() {
    }

    public String getBlogId() {
        return blogId;
    }

    public void setBlogId(String blogId) {
        this.blogId = blogId;
    }

    public String getFloorId() {
        return floorId;
    }

    public void setFloorId(String floorId) {
        this.floorId = floorId;
    }

    @Override
    public String generateCacheKey() {
        List<String> list = Arrays.asList(blogId, floorId);
        return StringUtils.join(list, CacheConstants.CACHE_LOCAL_SEP);
    }
}

