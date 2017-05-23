package com.hx.blog_v2.service.interf;

import com.hx.blog_v2.domain.po.BlogTypePO;
import com.hx.blog_v2.domain.form.BlogTypeAddForm;
import com.hx.blog_v2.domain.form.BlogTypeUpdateForm;
import com.hx.common.interf.common.Result;

/**
 * BlogService
 *
 * @author Jerry.X.He <970655147@qq.com>
 * @version 1.0
 * @date 5/20/2017 11:48 AM
 */
public interface BlogTypeService extends BaseService<BlogTypePO> {

    /**
     * 增加一个 BlogType
     *
     * @return result
     * @author Jerry.X.He
     * @date 5/20/2017 6:21 PM
     * @since 1.0
     */
    Result add(BlogTypeAddForm params);

    /**
     * 获取所有的 BlogType
     *
     * @return result
     * @author Jerry.X.He
     * @date 5/20/2017 6:21 PM
     * @since 1.0
     */
    Result list();

    /**
     * 更新一个 BlogType
     *
     * @return result
     * @author Jerry.X.He
     * @date 5/20/2017 6:21 PM
     * @since 1.0
     */
    Result update(BlogTypeUpdateForm params);

    /**
     * 移除一个 BlogType
     *
     * @return result
     * @author Jerry.X.He
     * @date 5/20/2017 6:21 PM
     * @since 1.0
     */
    Result remove(BlogTypeUpdateForm params);

}