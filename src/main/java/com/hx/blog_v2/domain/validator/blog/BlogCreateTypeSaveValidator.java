package com.hx.blog_v2.domain.validator.blog;

import com.hx.blog_v2.domain.ErrorCode;
import com.hx.blog_v2.domain.form.blog.BlogCreateTypeSaveForm;
import com.hx.blog_v2.domain.validator.others.EntityDescValidator;
import com.hx.blog_v2.domain.validator.others.EntityNameValidator;
import com.hx.blog_v2.domain.validator.others.SortValidator;
import com.hx.blog_v2.domain.validator.common.UrlValidator;
import com.hx.blog_v2.domain.validator.common.BeanIdStrValidator;
import com.hx.common.interf.common.Result;
import com.hx.common.interf.validator.Validator;
import com.hx.blog_v2.util.ResultUtils;
import com.hx.log.util.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * BlogCreateTypeSaveValidator
 *
 * @author Jerry.X.He <970655147@qq.com>
 * @version 1.0
 * @date 6/15/2017 7:18 PM
 */
@Component
public class BlogCreateTypeSaveValidator implements Validator<BlogCreateTypeSaveForm> {

    @Autowired
    private BeanIdStrValidator beanIdValidator;
    @Autowired
    private EntityNameValidator entityNameValidator;
    @Autowired
    private EntityDescValidator entityDescValidator;
    @Autowired
    private UrlValidator urlValidator;
    @Autowired
    private SortValidator sortValidator;

    @Override
    public Result validate(BlogCreateTypeSaveForm form, Object extra) {
        Result errResult = sortValidator.validate(form.getSort(), extra);
        if (!errResult.isSuccess()) {
            return ResultUtils.failed(ErrorCode.INPUT_NOT_FORMAT, " sort 不合法 ! ");
        }

        if (!Tools.isEmpty(form.getId())) {
            errResult = beanIdValidator.validate(form.getId(), extra);
            if (!errResult.isSuccess()) {
                return ResultUtils.failed(ErrorCode.INPUT_NOT_FORMAT, " id 非数字 ! ");
            }
        }
        errResult = entityNameValidator.validate(form.getName(), extra);
        if (!errResult.isSuccess()) {
            return errResult;
        }
        errResult = entityDescValidator.validate(form.getDesc(), extra);
        if (!errResult.isSuccess()) {
            return errResult;
        }
        errResult = urlValidator.validate(form.getImgUrl(), extra);
        if (!errResult.isSuccess()) {
            return ResultUtils.failed(ErrorCode.INPUT_NOT_FORMAT, " url 格式不合法 ! ");
        }

        return ResultUtils.success();

    }
}
