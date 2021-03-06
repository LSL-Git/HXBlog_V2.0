/**
 * cacheManagement.js
 *
 * @author Jerry.X.He <970655147@qq.com>
 * @version 1.0
 * @date 5/24/2017 9:56 PM
 */

/**
 * 刷新统计的时间间隔
 */
var statsInterval = 10;
var statsIntervalInMs = statsInterval * 1000;

/**
 * echart API 委托
 * @type {null}
 */
var eChart = null
/**
 * 局部缓存的图表
 * @type {null}
 */
var localCachedChart = null


initCacheTable()
/**
 * 初始化图表
 */
initCharts()
/**
 * 定时轮询
 */
setInterval(refreshCharts, statsIntervalInMs)

/**
 * layer 相关元素的控制
 */
layui.define(['element', 'laypage', 'layer', 'form', 'upload'], function (exports) {
    var $ = layui.jquery;
    var layer = layui.layer;
    var form = layui.form();

    form.on('submit(infoBtn)', function (data) {
        var cacheDetailUrl = formatContextUrl("/static/admin/html/system/cacheDetailManagement.html")
        parent.switchTab(parent.$, parent.element, '缓存详情', cacheDetailUrl, "cacheDetail");
        return false
    })

    form.on('submit(viewAopCacheBtn)', function (data) {
        ajax({
            url: reqMap.cache.aopCacheSummary,
            data: {},
            type: "GET",
            success: function (resp) {
                if (resp.success) {
                    layer.alert(prettyJson(resp))
                } else {
                    layer.alert("刷新获取 aop缓存信息 出现了点问题[" + resp.data + "], 请联系管理员 !")
                }
            }
        })

        return false
    })

    /**
     * 增加 tips
     */
    $("[name='refreshAll']").hover(function () {
        layer.tips("全部缓存 [⑴ + ⑵]", "[name='refreshAll']", {
            tips: [1, '#ff0000'],
            time: 1000
        })
    }, function () {
    })
    $("[name='refreshAuthority']").hover(function () {
        layer.tips("刷新权限数据", "[name='refreshAuthority']", {
            tips: [1, '#ff0000'],
            time: 1000
        })
    }, function () {
    })

    $("[name='allCached']").hover(function () {
        layer.tips("CacheContext [① + ② + ③ + ④]", "[name='allCached']", {
            tips: [1, '#ff0000'],
            time: 1000
        })
    }, function () {
    })
    $("[name='tableCached']").hover(function () {
        layer.tips("[type, tag, link, role, resource, interf, blogCreateType]", "[name='tableCached']", {
            tips: [1, '#ff0000'],
            time: 1000
        })
    }, function () {
    })
    $("[name='localCached']").hover(function () {
        layer.tips("[uploadFile, roleIds2ResIds, res2Interfs, info2sense, id2blogEx, info2VisitLog]", "[name='localCached']", {
            tips: [1, '#ff0000'],
            time: 1000
        })
    }, function () {
    })
    $("[name='statisticsInfo']").hover(function () {
        layer.tips("[allStatistics, all5SecStatistics, monthFacet, latestBlog/Comment]", "[name='statisticsInfo']", {
            tips: [1, '#ff0000'],
            time: 1000
        })
    }, function () {
    })
    $("[name='otherCached']").hover(function () {
        layer.tips("[floorIdx, commentIdx, forceOffLine]", "[name='otherCached']", {
            tips: [1, '#ff0000'],
            time: 1000
        })
    }, function () {
    })

    $("[name='allConfigured']").hover(function () {
        layer.tips("ConstantsContext [① + ② + ③]", "[name='allConfigured']", {
            tips: [1, '#ff0000'],
            time: 1000
        })
    }, function () {
    })
    $("[name='systemConfig']").hover(function () {
        layer.tips("[systemConfig]", "[name='systemConfig']", {
            tips: [1, '#ff0000'],
            time: 1000
        })
    }, function () {
    })
    $("[name='ruleConfig']").hover(function () {
        layer.tips("[ruleConfig]", "[name='ruleConfig']", {
            tips: [1, '#ff0000'],
            time: 1000
        })
    }, function () {
    })
    $("[name='frontIdxConfig']").hover(function () {
        layer.tips("[frontIdxConfig]", "[name='frontIdxConfig']", {
            tips: [1, '#ff0000'],
            time: 1000
        })
    }, function () {
    })

    var funcs = {
        refreshAll: function () {
            ajax({
                url: reqMap.cache.refreshAll,
                data: {},
                type: "POST",
                success: function (resp) {
                    if (resp.success) {
                        layer.alert("刷新缓存配置成功 !")
                    } else {
                        layer.alert("刷新缓存配置出现了点问题[" + resp.data + "], 请联系管理员 !")
                    }
                }
            })
        },
        refreshAuthority: function () {
            ajax({
                url: reqMap.system.refreshAuthority,
                data: {},
                type: "POST",
                success: function (resp) {
                    if (resp.success) {
                        layer.alert("刷新权限成功 !")
                    } else {
                        layer.alert("刷新权限出现了点问题[" + resp.data + "], 请联系管理员 !")
                    }
                }
            })
        },
        refreshAllCached: function () {
            ajax({
                url: reqMap.cache.refreshAllCached,
                data: {},
                type: "POST",
                success: function (resp) {
                    if (resp.success) {
                        layer.alert("刷新CacheContext的缓存成功 !")
                    } else {
                        layer.alert("刷新CacheContext的缓存出现了点问题[" + resp.data + "], 请联系管理员 !")
                    }
                }
            })
        },
        refreshTableCached: function () {
            ajax({
                url: reqMap.cache.refreshTableCached,
                data: {},
                type: "POST",
                success: function (resp) {
                    if (resp.success) {
                        layer.alert("刷新全表缓存配置成功 !")
                    } else {
                        layer.alert("刷新全表缓存配置出现了点问题[" + resp.data + "], 请联系管理员 !")
                    }
                }
            })
        },
        refreshLocalCached: function () {
            ajax({
                url: reqMap.cache.refreshLocalCached,
                data: {},
                type: "POST",
                success: function (resp) {
                    if (resp.success) {
                        layer.alert("刷新局部缓存配置成功 !")
                    } else {
                        layer.alert("刷新局部缓存配置出现了点问题[" + resp.data + "], 请联系管理员 !")
                    }
                }
            })
        },
        refreshStatisticsInfo: function () {
            ajax({
                url: reqMap.cache.refreshStatisticsInfo,
                data: {},
                type: "POST",
                success: function (resp) {
                    if (resp.success) {
                        layer.alert("刷新统计缓存配置成功 !")
                    } else {
                        layer.alert("刷新统计缓存配置出现了点问题[" + resp.data + "], 请联系管理员 !")
                    }
                }
            })
        },
        refreshOtherCached: function () {
            ajax({
                url: reqMap.cache.refreshOtherCached,
                data: {},
                type: "POST",
                success: function (resp) {
                    if (resp.success) {
                        layer.alert("刷新其他配置缓存成功 !")
                    } else {
                        layer.alert("刷新其他配置缓存出现了点问题[" + resp.data + "], 请联系管理员 !")
                    }
                }
            })
        },

        refreshAllConfigured: function () {
            ajax({
                url: reqMap.cache.refreshAllConfigured,
                data: {},
                type: "POST",
                success: function (resp) {
                    if (resp.success) {
                        layer.alert("刷新ConstantsContext的缓存成功 !")
                    } else {
                        layer.alert("刷新ConstantsContext的缓存出现了点问题[" + resp.data + "], 请联系管理员 !")
                    }
                }
            })
        },
        refreshSystemConfig: function () {
            ajax({
                url: reqMap.cache.refreshSystemConfig,
                data: {},
                type: "POST",
                success: function (resp) {
                    if (resp.success) {
                        layer.alert("刷新系统缓存配置成功 !")
                    } else {
                        layer.alert("刷新系统缓存配置出现了点问题[" + resp.data + "], 请联系管理员 !")
                    }
                }
            })
        },
        refreshRuleConfig: function () {
            ajax({
                url: reqMap.cache.refreshRuleConfig,
                data: {},
                type: "POST",
                success: function (resp) {
                    if (resp.success) {
                        layer.alert("刷新规则配置缓存配置成功 !")
                    } else {
                        layer.alert("刷新规则配置缓存配置出现了点问题[" + resp.data + "], 请联系管理员 !")
                    }
                }
            })
        },
        refreshFrontIdxConfig: function () {
            ajax({
                url: reqMap.cache.refreshFrontIdxConfig,
                data: {},
                type: "POST",
                success: function (resp) {
                    if (resp.success) {
                        layer.alert("刷新前台配置缓存配置成功 !")
                    } else {
                        layer.alert("刷新前台配置缓存配置出现了点问题[" + resp.data + "], 请联系管理员 !")
                    }
                }
            })
        }

    }
    exports('funcs', funcs);

});

/**
 * 填充相对比较固定的缓存的数据
 */
function initCacheTable() {
    ajax({
        url: reqMap.cache.cacheSummary,
        type: "GET",
        data: {},
        success: function (resp) {
            if (resp.success) {
                var capacities = resp.data.capacities
                var html = new StringBuilder();
                html.append('<tr>')
                for (var i in capacities) {
                    html.append('<td>' + capacities[i] + '</td>')
                }
                html.append('</tr>')
                $('#dataContent').html(html.toString());
            } else {
                layer.alert("拉取缓存统计列表失败[" + resp.data + "] !", {icon: 5});
            }
        }
    });
}

/**
 * 初始化 echarts 图标
 */
function initCharts() {
    // 路径配置
    require.config({
        paths: {
            echarts: 'http://echarts.baidu.com/build/dist'
        }
    });

    // 使用
    require(
        [
            'echarts',
            'echarts/chart/line',
            'echarts/chart/bar'
        ],
        function (ec) {
            eChart = ec
            localCachedChart = eChart.init(document.getElementsByName('localCachedChart').item(0))
            refreshCharts();
        }
    );
}

/**
 * 向服务器拉数据 刷新图表
 */
function refreshCharts() {
    var options = encapStats()
    localCachedChart.setOption(options);
}

/**
 * 发送请求封装统计数据
 */
function encapStats() {
    var options = {};
    ajax({
        url: reqMap.cache.localCacheSummary,
        data: {},
        async: false,
        success: function (resp) {
            if (resp.success) {
                options = encapOption(resp)
            } else {
                layer.alert("拉取统计数据失败[" + resp.data + "] !")
            }
        }
    });

    return options;
}

/**
 * 根据给定的结果, 封装 echarts 需要的数据
 * @param resp
 * @returns
 */
function encapOption(resp) {
    return {
        title: {
            text: '缓存图表',
            subtext: '缓存图表'
        },
        tooltip: {
            trigger: 'axis'
        },
        legend: {
            data: [
                '总共容量',
                '已使用'
            ]
        },
        toolbox: {
            show: true,
            feature: {
                mark: {show: true},
                dataView: {show: true, readOnly: false},
                magicType: {show: true, type: ['line', 'bar']},
                restore: {show: true},
                saveAsImage: {show: true}
            }
        },
        xAxis: [
            {
                type: 'category',
                data: ['blog', 'tagIds', 'comment', 'id2blogEx', 'info2VisitLog', 'info2sense', 'roleIds2ResIds', 'res2Interfs', 'uploadFile', "blackList"]
            },
            {
                type: 'category',
                axisLine: {show: true}, axisTick: {show: false}, axisLabel: {show: false},
                splitArea: {show: false}, splitLine: {show: false},
                data: ['blog', 'tagIds', 'comment', 'id2blogEx', 'info2VisitLog', 'info2sense', 'roleIds2ResIds', 'res2Interfs', 'uploadFile', "blackList"]
            }
        ],
        yAxis: [
            {
                type: 'value',
                axisLabel: {formatter: '{value} ms'}
            }
        ],
        series: [
            {
                name: '总共容量',
                type: 'bar',
                itemStyle: {normal: {color: 'rgba(200,206,16,1)', label: {show: true}}},
                data: resp.data.capacities
            },
            {
                name: '已使用',
                type: 'bar',
                xAxisIndex: 1,
                itemStyle: {
                    normal: {
                        color: 'rgba(252,206,16,1)',
                        label: {
                            show: true, formatter: function (p) {
                                return p.value > 0 ? (p.value + '\n') : '';
                            }
                        }
                    }
                },
                data: resp.data.used
            }
        ]
    }
}


