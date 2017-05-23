﻿// 加载类型, 标签
$.ajax({
    url : "/composite/typeAndTags",
    type : "GET",
    success : function (result) {
        if(result.success) {
            var types = result.data.types
            for(idx in types) {
                $("#blogTypeId").append("<option value='" + types[idx].id + "'> " + types[idx].name + " </option>")
            }
            var tags = result.data.tags
            for(idx in tags) {
                $("#blogTagId").append("<option value='" + tags[idx].id + "'> " + tags[idx].name + " </option>")
            }
        }
    }
});

layui.define(['element', 'laypage', 'layer', 'form', 'pagesize'], function (exports) {
    var $ = layui.jquery;
    var layer = layui.layer;
    var form = layui.form();
    var laypage = layui.laypage;
    var element = layui.element();
    var laypageId = 'pageNav';

    form.on('submit(formSearch)', function(data){
        var index = layer.load(1);
        doSearch(layer, index, 1)
        return false;
    });

    initilData(1, 8);
    //页数据初始化

    function initilData() {
        var index = layer.load(1);
        //模拟数据加载
        setTimeout(doSearch(layer, index, 1), 500);
    }

    function doSearch(layer, index, pageNow)  {
        layer.close(index);
        var params = $("#blogSearchForm").serialize()
        params.pageNow = pageNow
        params.pageSize = pageSize

        $.ajax({
            url: "/admin/blog/list",
            type: "GET",
            data: params,
            success: function (resp) {
                if(resp.success) {
                    var html = '';
                    for (var i in resp.data.list) {
                        var item = resp.data.list[i];
                        html += '<tr>';
                        html += '<td>' + item.id + '</td>';
                        html += '<td>' + item.title + '</td>';
                        html += '<td>' + item.author + '</td>';
                        html += '<td>' + item.summary + '</td>';
                        html += '<td>' + item.createdAt + '</td>';
                        html += '<td>' + item.blogTypeName + '</td>';
                        html += '<td>' + item.blogTagNames + '</td>';
                        html += '<td><button class="layui-btn layui-btn-small layui-btn-normal" onclick="layui.datalist.editData(' + item.id + ')"><i class="layui-icon">&#xe642;</i></button></td>';
                        html += '<td><button class="layui-btn layui-btn-small layui-btn-danger" onclick="layui.datalist.deleteData(' + item.id + ')"><i class="layui-icon">&#xe640;</i></button></td>';
                        html += '</tr>';
                    }
                    $('#dataContent').html(html);
                    element.init();

                    form.render('checkbox');  //重新渲染CheckBox，编辑和添加的时候
                    $('#dataConsole, #dataList').attr('style', 'display:block'); //显示FiledBox
                    laypage({
                        cont: laypageId,
                        pages: resp.data.totalPage,
                        groups: 5,
                        skip: true,
                        curr: params.pageNow,
                        jump: function (obj, first) {
                            var currentIndex = obj.curr;
                            if (!first) {
                                initilData(currentIndex, pageSize);
                            }
                        }
                    });

                    //laypageId:laypage对象的id同laypage({})里面的cont属性
                    //pagesize当前页容量，用于显示当前页容量
                    //callback用于设置pagesize确定按钮点击时的回掉函数，返回新的页容量
                    layui.pagesize(laypageId, params.pageSize).callback(function (newPageSize) {
                        initilData(1, newPageSize);
                    });
                }
            }
        });

    }

    //输出接口，主要是两个函数，一个删除一个编辑
    var datalist = {
        deleteData: function (id) {
            layer.confirm('同时会删除对应评论，确定删除？', {
                btn: ['确定', '取消'] //按钮
            }, function () {
                $.ajax({
                    url: '/admin/blog/remove',
                    data: {"id" : id },
                    type: 'POST',
                    success: function (result) {
                        if (result.success) {
                            layer.alert('删除成功!', {
                                closeBtn: 0,
                                icon: 1
                            }, function () {
                                window.location.href = "/static/admin/blogManage.html"
                            });
                        } else {
                            layer.alert('删除失败!', {icon: 5});
                        }
                    }
                });
            }, function () {

            });
        },
        editData: function (id) {
            parent.switchTab(parent.$, parent.element, '修改博客', '/static/admin/writeBlog.html?id=' + id, 'Blog' + id);
        }
    };
    exports('datalist', datalist);
});