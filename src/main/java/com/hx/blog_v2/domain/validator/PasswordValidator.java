package com.hx.blog_v2.domain.validator;

import com.hx.common.interf.common.Result;
import com.hx.common.interf.validator.Validator;
import com.hx.blog_v2.util.ResultUtils;
import com.hx.log.util.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * UserNameValidator
 *
 * @author Jerry.X.He <970655147@qq.com>
 * @version 1.0
 * @date 6/15/2017 8:25 PM
 */
@Component
public class PasswordValidator extends ConfigRefreshableValidator<String> implements Validator<String> {

    @Autowired
    private VisibleValidator visibleValidator;
    /**
     * 最小长度, 最大长度
     */
    private int minLen = -1;
    private int maxLen = -1;

    @Override
    public Result doValidate(String password, Object extra) {
        if (Tools.isEmpty(password)) {
            return ResultUtils.failed(" password 为空 !");
        }
        if ((password.length() < minLen) || (password.length() > maxLen)) {
            return ResultUtils.failed(" password 长度不在范围内 !");
        }
        Result result = visibleValidator.validate(password, extra);
        if (!result.isSuccess()) {
            return result;
        }

        return ResultUtils.success();
    }

    @Override
    public boolean needRefresh() {
        return minLen < 0;
    }

    @Override
    public void refreshConfig() {
        minLen = Integer.parseInt(constantsContext.ruleConfig("user.pwd.min.length", "3"));
        maxLen = Integer.parseInt(constantsContext.ruleConfig("user.pwd.max.length", "32"));
    }
}
