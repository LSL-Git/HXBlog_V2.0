/**
 * 保存常量信息
 *
 * @author Jerry.X.He <970655147@qq.com>
 * @version 1.0
 * @date 5/21/2017 7:02 PM
 */

/**
 * 分页的时候, 页面的记录条数
 */
var pageSize = 10
/**
 * 评论的最大字符数量
 */
var commentMaxLen = 1024
/**
 * 评论的数量提醒的阈值
 */
var commentLenThreshold = 100

/**
 * 如果 monthFacet 则部分隐藏
 */
var monthFacetHideIfOver = 5

/**
 * 周期保存博客信息的周期
 */
var saveBlogInfoInterval = 5 * 1000

/**
 * 加载广告延迟的时间长度
 */
var fetchAdvDelayInMs = 1000

/**
 * 存放暂存的博客信息的key
 * @type {string}
 */
var saveBlogInfoKey = "staged.blog.info"
var saveBlogContentKey = "staged.blog.info.content"
/**
 * 暂存的博客信息的备份
 * @type {string}
 */
var saveBlogInfoBakKey = "staged.blog.info.bak"
var saveBlogContentBakKey = "staged.blog.info.content"

/**
 * 读取文件的编码
 * @type {string}
 */
var encoding = "gbk"

/**
 * 项目的 contextPath
 * @type {string}
 */
var contextPath = "/HXBlog_V2.0/"

/**
 * 存放小笼包位置的key
 * @type {string}
 */
var xiaolongbaoPosKey = "xiaolongbaoPos"

/**
 * default 的字面量
 * @type {string}
 */
var defaultKey = "default"

function formatContextUrl(requestUri) {
    if (requestUri.startsWith("/")) {
        requestUri = requestUri.substr(1)
    }
    return contextPath + requestUri
}

/**
 * 所有的请求的路由
 *
 * @type {{}}
 */
var reqMap = {

    /**
     * 博客相关
     */
    blog: {
        add: formatContextUrl("/admin/blog/add"),
        get: formatContextUrl("/blog/get"),
        adminGet: formatContextUrl("/admin/blog/get"),
        list: formatContextUrl("/blog/list"),
        adminAdminList: formatContextUrl("/admin/blog/adminList"),
        adminList: formatContextUrl("/admin/blog/list"),
        sense: formatContextUrl("/blog/sense/sense"),
        adminUpdate: formatContextUrl("/admin/blog/adminUpdate"),
        update: formatContextUrl("/admin/blog/update"),
        transfer: formatContextUrl("/admin/blog/transfer"),
        adminRemove: formatContextUrl("/admin/blog/adminRemove"),
        remove: formatContextUrl("/admin/blog/remove")
    },

    /**
     * 博客类型相关
     */
    type: {
        add: formatContextUrl("/admin/type/add"),
        list: formatContextUrl("/admin/type/list"),
        update: formatContextUrl("/admin/type/update"),
        reSort: formatContextUrl("/admin/type/reSort"),
        remove: formatContextUrl("/admin/type/remove")
    },

    /**
     * 博客标签相关
     */
    tag: {
        add: formatContextUrl("/admin/tag/add"),
        list: formatContextUrl("/admin/tag/list"),
        update: formatContextUrl("/admin/tag/update"),
        reSort: formatContextUrl("/admin/tag/reSort"),
        remove: formatContextUrl("/admin/tag/remove")
    },

    /**
     * 博客创建类型相关
     */
    createType: {
        add: formatContextUrl("/admin/blog/createType/add"),
        list: formatContextUrl("/admin/blog/createType/list"),
        update: formatContextUrl("/admin/blog/createType/update"),
        reSort: formatContextUrl("/admin/blog/createType/reSort"),
        remove: formatContextUrl("/admin/blog/createType/remove")
    }
    ,

    /**
     * 用户相关
     */
    user: {
        add: formatContextUrl("/admin/user/add"),
        list: formatContextUrl("/admin/user/list"),
        update: formatContextUrl("/admin/user/update"),
        updatePwd: formatContextUrl("/admin/user/updatePwd"),
        remove: formatContextUrl("/admin/user/remove"),

        login: formatContextUrl("/admin/user/login"),
        logout: formatContextUrl("/admin/user/logout")
    }
    ,

    /**
     * 消息相关
     */
    message: {
        add: formatContextUrl("/admin/message/add"),
        list: formatContextUrl("/admin/message/list"),
        adminList: formatContextUrl("/admin/message/adminList"),
        unread: formatContextUrl("/admin/message/unread"),
        update: formatContextUrl("/admin/message/update"),
        markConsumed: formatContextUrl("/admin/message/markConsumed"),
        markAllConsumed: formatContextUrl("/admin/message/markAllConsumed"),
        remove: formatContextUrl("/admin/message/remove")
    }
    ,

    /**
     * 用户角色关联相关
     */
    userRole: {
        list: formatContextUrl("/admin/role/userRole/list"),
        update: formatContextUrl("/admin/role/userRole/update")
    }
    ,

    /**
     * 角色相关
     */
    role: {
        add: formatContextUrl("/admin/role/add"),
        list: formatContextUrl("/admin/role/list"),
        update: formatContextUrl("/admin/role/update"),
        reSort: formatContextUrl("/admin/role/reSort"),
        remove: formatContextUrl("/admin/role/remove")
    }
    ,

    /**
     * 角色资源相关
     */
    roleResource: {
        list: formatContextUrl("/admin/resource/roleResource/list"),
        update: formatContextUrl("/admin/resource/roleResource/update")
    },

    /**
     * 资源相关
     */
    resource: {
        add: formatContextUrl("/admin/resource/add"),
        list: formatContextUrl("/admin/resource/list"),
        adminTreeList: formatContextUrl("/admin/resource/adminTreeList"),
        update: formatContextUrl("/admin/resource/update"),
        reSort: formatContextUrl("/admin/resource/reSort"),
        remove: formatContextUrl("/admin/resource/remove")
    },

    /**
     * 资源接口关联相关
     */
    resourceInterf: {
        list: formatContextUrl("/admin/interf/resourceInterf/list"),
        update: formatContextUrl("/admin/interf/resourceInterf/update")
    },

    /**
     * 接口相关
     */
    interf: {
        add: formatContextUrl("/admin/interf/add"),
        list: formatContextUrl("/admin/interf/list"),
        update: formatContextUrl("/admin/interf/update"),
        reSort: formatContextUrl("/admin/interf/reSort"),
        remove: formatContextUrl("/admin/interf/remove")
    },

    /**
     * 友情链接相关
     */
    link: {
        add: formatContextUrl("/admin/link/add"),
        list: formatContextUrl("/admin/link/list"),
        update: formatContextUrl("/admin/link/update"),
        reSort: formatContextUrl("/admin/link/reSort"),
        remove: formatContextUrl("/admin/link/remove")
    },

    /**
     * 图片相关
     */
    image: {
        add: formatContextUrl("/admin/image/add"),
        upload: formatContextUrl("/admin/upload/image"),
        headImgList: formatContextUrl("/image/headImgList"),
        list: formatContextUrl("/admin/image/list"),
        update: formatContextUrl("/admin/image/update"),
        reSort: formatContextUrl("/admin/image/reSort"),
        remove: formatContextUrl("/admin/image/remove")
    },

    file: {
        upload: formatContextUrl("/admin/upload/file"),
    },

    /**
     * 心情相关
     */
    mood: {
        add: formatContextUrl("/admin/mood/add"),
        list: formatContextUrl("/admin/mood/list"),
        update: formatContextUrl("/admin/mood/update"),
        remove: formatContextUrl("/admin/mood/remove")
    },

    /**
     * 评论相关
     */
    comment: {
        add: formatContextUrl("/comment/add"),
        adminAdd: formatContextUrl("/admin/comment/add"),
        list: formatContextUrl("/comment/list"),
        reply: formatContextUrl("/admin/comment/reply"),
        adminList: formatContextUrl("/admin/comment/list"),
        commentsForFloor: formatContextUrl("/admin/comment/comment/list"),
        update: formatContextUrl("/admin/comment/update"),
        remove: formatContextUrl("/admin/comment/remove")
    }
    ,

    /**
     * 配置相关
     */
    config: {
        add: formatContextUrl("/admin/config/add"),
        list: formatContextUrl("/admin/config/list"),
        update: formatContextUrl("/admin/config/update"),
        remove: formatContextUrl("/admin/config/remove"),
        reSort: formatContextUrl("/admin/config/reSort")
    },

    /**
     * 首页相关
     */
    index: {
        index: formatContextUrl("/index/index"),
        latest: formatContextUrl("/index/latest"),
        adminMenu: formatContextUrl("/admin/index/menus"),
        adminStatistics: formatContextUrl("/admin/index/statistics")
    }
    ,

    /**
     * 返回复合数据的请求
     */
    composite: {
        moodAndImages: formatContextUrl("/composite/moodAndImages"),
        typeAndTags: formatContextUrl("/composite/typeAndTags"),
        userAndRoles: formatContextUrl("/composite/userAndRoles")
    }
    ,

    /**
     * 缓存相关
     */
    cache: {
        cacheSummary: formatContextUrl("/admin/cache/cacheSummary"),
        localCacheSummary: formatContextUrl("/admin/cache/localCacheSummary"),
        aopCacheSummary: formatContextUrl("/admin/cache/aopCacheSummary"),
        cacheDetail: formatContextUrl("/admin/cache/cacheDetail"),
        cacheVisitInfo: formatContextUrl("/admin/cache/cacheVisitInfo"),
        refreshAll: formatContextUrl("/admin/cache/refreshAll"),
        refreshAllCached: formatContextUrl("/admin/cache/refreshAllCached"),
        refreshTableCached: formatContextUrl("/admin/cache/refreshTableCached"),
        refreshLocalCached: formatContextUrl("/admin/cache/refreshLocalCached"),
        refreshStatisticsInfo: formatContextUrl("/admin/cache/refreshStatisticsInfo"),
        refreshOtherCached: formatContextUrl("/admin/cache/refreshOtherCached"),
        refreshAllConfigured: formatContextUrl("/admin/cache/refreshAllConfigured"),
        refreshSystemConfig: formatContextUrl("/admin/cache/refreshSystemConfig"),
        refreshRuleConfig: formatContextUrl("/admin/cache/refreshRuleConfig"),
        refreshFrontIdxConfig: formatContextUrl("/admin/cache/refreshFrontIdxConfig"),
        cacheRemove: formatContextUrl("/admin/cache/cacheRemove")
    },

    /**
     * 系统管理相关
     */
    system: {
        statsSummary: formatContextUrl("/admin/system/statsSummary"),
        refreshAuthority: formatContextUrl("/admin/system/refreshAuthority")
    },

    /**
     * 校正数据相关
     */
    correction: {
        list: formatContextUrl("/admin/correction/list"),
        doCorrection: formatContextUrl("/admin/correction/doCorrection")
    },

    /**
     * 日志相关
     */
    log: {
        requestLogList: formatContextUrl("/admin/log/request/list"),
        exceptionLogList: formatContextUrl("/admin/log/exception/list")
    },

    /**
     * 广告相关
     */
    adv : {
        list : formatContextUrl("/adv/list"),
        add : formatContextUrl("/admin/adv/add"),
        adminList : formatContextUrl("/admin/adv/list"),
        update : formatContextUrl("/admin/adv/update"),
        remove : formatContextUrl("/admin/adv/remove"),
        reSort : formatContextUrl("/admin/adv/reSort")
    },

    /**
     * 其他配置
     */
    other: {
        templateUrl: formatContextUrl("/static/main/templates.html")

    }

}

/**
 * ajax() 方法需要过滤的请求, 直接使用 原生的 jq
 * @type {{}}
 */
ajaxNeedFilter = [reqMap.other.templateUrl]

/**
 * 广告的类型
 * @type {{img_fixed: string, text_fixed: string, img_redirect: string, text_redirect: string}}
 */
advTypeKeyLiteral = {
    imgFixed : "img_fixed",
    textFixed : "text_fixed",
    imgRedirect : "img_redirect",
    textRedirect : "text_redirect"
}
advTypeKey2Tips = {
    img_fixed : "固定图片",
    text_fixed : "固定文字",
    img_redirect : "跳转图片",
    text_redirect : "跳转文字"
}





