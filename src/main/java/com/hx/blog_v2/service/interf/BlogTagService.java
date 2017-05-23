package com.hx.blog_v2.service.interf;

import com.hx.blog_v2.domain.form.BlogTagAddForm;
import com.hx.blog_v2.domain.form.BlogTagUpdateForm;
import com.hx.blog_v2.domain.po.BlogTagPO;
import com.hx.blog_v2.domain.vo.BlogTagVO;
import com.hx.common.interf.common.Page;
import com.hx.common.interf.common.Result;
import com.hx.common.result.SimplePage;

/**
 * BlogService
 *
 * @author Jerry.X.He <970655147@qq.com>
 * @version 1.0
 * @date 5/20/2017 11:48 AM
 */
public interface BlogTagService extends BaseService<BlogTagPO> {

    /**
     * 增加一个 BlogTag
     *
     * @return result
     * @author Jerry.X.He
     * @date 5/20/2017 6:21 PM
     * @since 1.0
     */
    Result add(BlogTagAddForm params);

    /**
     * 获取所有的 BlogTag
     *
     * @return result
     * @author Jerry.X.He
     * @date 5/20/2017 6:21 PM
     * @since 1.0
     */
    Result list();

    /**
     * 更新一个 BlogTag
     *
     * @return result
     * @author Jerry.X.He
     * @date 5/20/2017 6:21 PM
     * @since 1.0
     */
    Result update(BlogTagUpdateForm params);

    /**
     * 移除一个 BlogTag
     *
     * @return result
     * @author Jerry.X.He
     * @date 5/20/2017 6:21 PM
     * @since 1.0
     */
    Result remove(BlogTagUpdateForm params);


}