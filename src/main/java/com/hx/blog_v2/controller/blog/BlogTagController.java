package com.hx.blog_v2.controller.blog;

import com.hx.blog_v2.biz_log.anno.BizLogger;
import com.hx.blog_v2.service.interf.blog.BlogTagService;
import com.hx.common.interf.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * BlogTagController
 *
 * @author Jerry.X.He <970655147@qq.com>
 * @version 1.0
 * @date 5/20/2017 4:57 PM
 */
@RestController
@RequestMapping("/tag")
public class BlogTagController {

    @Autowired
    private BlogTagService blogTagService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @BizLogger(req = false, resp = false)
    public Result list() {

        return blogTagService.list();
    }

}
