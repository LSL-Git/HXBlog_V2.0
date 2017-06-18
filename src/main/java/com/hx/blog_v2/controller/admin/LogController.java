package com.hx.blog_v2.controller.admin;

import com.hx.blog_v2.domain.form.LogSearchForm;
import com.hx.blog_v2.domain.validator.LogSearchValidator;
import com.hx.blog_v2.domain.validator.PageValidator;
import com.hx.blog_v2.domain.vo.ExceptionLogVO;
import com.hx.blog_v2.domain.vo.RequestLogVO;
import com.hx.blog_v2.service.interf.LogService;
import com.hx.common.interf.common.Result;
import com.hx.common.result.SimplePage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * SystemController
 *
 * @author Jerry.X.He <970655147@qq.com>
 * @version 1.0
 * @date 5/20/2017 4:57 PM
 */
@RestController("adminLogController")
@RequestMapping("/admin/log")
public class LogController {

    @Autowired
    private LogService logService;
    @Autowired
    private LogSearchValidator logSearchValidator;
    @Autowired
    private PageValidator pageValidator;

    @RequestMapping(value = "/request/list", method = RequestMethod.GET)
    public Result requestLogList(LogSearchForm params, SimplePage<RequestLogVO> page) {
        Result errResult = logSearchValidator.validate(params, null);
        if (!errResult.isSuccess()) {
            return errResult;
        }
        errResult = pageValidator.validate(page, null);
        if (!errResult.isSuccess()) {
            return errResult;
        }

        return logService.requestLogList(params, page);
    }

    @RequestMapping(value = "/exception/list", method = RequestMethod.GET)
    public Result exceptionLogList(LogSearchForm params, SimplePage<ExceptionLogVO> page) {
        Result errResult = logSearchValidator.validate(params, null);
        if (!errResult.isSuccess()) {
            return errResult;
        }
        errResult = pageValidator.validate(page, null);
        if (!errResult.isSuccess()) {
            return errResult;
        }

        return logService.exceptionLogList(params, page);
    }

}
