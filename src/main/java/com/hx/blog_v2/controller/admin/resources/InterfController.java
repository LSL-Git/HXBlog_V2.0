package com.hx.blog_v2.controller.admin.resources;

import com.hx.blog_v2.biz_handler.anno.BizHandle;
import com.hx.blog_v2.biz_handler.handler.AuthUpdatedHandler;
import com.hx.blog_v2.biz_log.anno.BizLogger;
import com.hx.blog_v2.domain.ErrorCode;
import com.hx.blog_v2.domain.form.common.BeanIdForm;
import com.hx.blog_v2.domain.form.resources.InterfSaveForm;
import com.hx.blog_v2.domain.form.rlt.ResourceInterfUpdateForm;
import com.hx.blog_v2.domain.validator.common.BeanIdValidator;
import com.hx.blog_v2.domain.validator.resources.InterfSaveValidator;
import com.hx.blog_v2.domain.validator.rlt.ResourceInterfUpdateValidator;
import com.hx.blog_v2.service.interf.resources.InterfService;
import com.hx.common.interf.common.Result;
import com.hx.blog_v2.util.ResultUtils;
import com.hx.log.util.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * InterfController
 *
 * @author Jerry.X.He <970655147@qq.com>
 * @version 1.0
 * @date 5/20/2017 4:57 PM
 */
@RestController("adminInterfController")
@RequestMapping("/admin/interf")
public class InterfController {

    @Autowired
    private InterfService interfService;
    @Autowired
    private InterfSaveValidator interfSaveValidator;
    @Autowired
    private ResourceInterfUpdateValidator resourceInterfUpdateValidator;
    @Autowired
    private BeanIdValidator beanIdValidator;

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @BizLogger
    public Result add(InterfSaveForm params) {
        Result errResult = interfSaveValidator.validate(params, null);
        if (!errResult.isSuccess()) {
            return errResult;
        }
        if (!Tools.isEmpty(params.getId())) {
            return ResultUtils.failed(ErrorCode.INPUT_NOT_FORMAT, " id 不为空 ! ");
        }

        return interfService.add(params);
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @BizLogger(req = false, resp = false)
    public Result list() {
        return interfService.adminList();
    }

    @RequestMapping(value = "/resourceInterf/list", method = RequestMethod.GET)
    @BizLogger(req = false, resp = false)
    public Result resourceInterfList() {
        return interfService.resourceInterfList();
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @BizLogger
    public Result update(InterfSaveForm params) {
        Result errResult = interfSaveValidator.validate(params, null);
        if (!errResult.isSuccess()) {
            return errResult;
        }
        if (Tools.isEmpty(params.getId())) {
            return ResultUtils.failed(ErrorCode.INPUT_NOT_FORMAT, " id 为空 ! ");
        }

        return interfService.update(params);
    }

    @RequestMapping(value = "/resourceInterf/update", method = RequestMethod.POST)
    @BizHandle(handler = "authUpdatedHandler", others = AuthUpdatedHandler.RESOURCE_INTERF)
    @BizLogger
    public Result userRoleUpdate(ResourceInterfUpdateForm params) {
        Result errResult = resourceInterfUpdateValidator.validate(params, null);
        if (!errResult.isSuccess()) {
            return errResult;
        }

        return interfService.userRoleUpdate(params);
    }

    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    @BizLogger
    public Result remove(BeanIdForm params) {
        Result errResult = beanIdValidator.validate(params, null);
        if (!errResult.isSuccess()) {
            return errResult;
        }

        return interfService.remove(params);
    }

    @RequestMapping(value = "/reSort", method = RequestMethod.POST)
    @BizLogger
    public Result reSort() {
        return interfService.reSort();
    }

}
