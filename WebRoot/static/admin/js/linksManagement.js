/**
 * linksManagement.js
 *
 * @author Jerry.X.He <970655147@qq.com>
 * @version 1.0
 * @date 5/24/2017 9:49 PM
 */

var linkNum = 3;

layui.define(['element', 'laypage', 'layer', 'form'], function (exports) {
    var $ = layui.jquery;
    var layer = layui.layer;
    var form = layui.form();
    var laypage = layui.laypage;
    var element = layui.element();
    var laypageId = 'pageNav';

    initilData(1);
    //页数据初始化
    //currentIndex：当前页面
    //pageSize：页容量（每页显示的条数）
    function initilData(pageNow) {
        var index = layer.load(1);
        //模拟数据加载
        setTimeout(function () {
            layer.close(index);
            $.ajax({
                url: "/admin/link/list",
                type: "GET",
                data: {
                    "pageNow": pageNow,
                    "pageSize": pageSize
                },
                success: function (resp) {
                    if (resp.success) {
                        var links = resp.data.list
                        var html = '';
                        for (var i in links) {
                            var item = links[i];
                            html += '<tr>';
                            html += '<td>' + item.id + '</td>';
                            html += '<td>' + item.name + '</td>';
                            html += '<td>' + item.desc + '</td>';
                            html += '<td>' + item.url + '</td>';
                            html += '<td>' + item.sort + '</td>';
                            if (item.enable) {
                                html += '<td><i class="layui-icon" style="font-size: 30px; color: #009688;vertical-align: middle;">&#xe609;</i> </td>';
                            } else {
                                html += '<td><i class="layui-icon" style="font-size: 30px; color:#d2d2d2; vertical-align: middle;">&#xe60f;</i> </td>';
                            }
                            html += '<td>' + item.createdAt + '</td>';
                            html += '<td>' + item.updatedAt + '</td>';
                            html += '<td><button class="layui-btn layui-btn-small layui-btn-normal" onclick="layui.funcs.editData(' + item.id + ',\'' + item.name + '\',\'' + item.desc + '\',\'' + item.url + '\',\'' + item.sort + '\',' + item.enable + ')"><i class="layui-icon">&#xe642;</i></button></td>';
                            html += '<td><button class="layui-btn layui-btn-small layui-btn-danger" onclick="layui.funcs.deleteData(' + item.id + ')"><i class="layui-icon">&#xe640;</i></button></td>';
                            html += '</tr>';
                        }
                        $('#dataContent').html(html);
                        element.init();

                        laypage({
                            cont: laypageId,
                            pages: resp.data.totalPage,
                            groups: 5,
                            skip: true,
                            curr: pageNow,
                            jump: function (obj, first) {
                                var pageNow = obj.curr;
                                if (!first) {
                                    initilData(pageNow);
                                }
                            }
                        });
                    }
                }
            });

            $('#dataConsole,#dataList').attr('style', 'display:block'); //显示FiledBox
        }, 500);
    }

    form.on('submit(addLinkSubmit)', function(data){
        $.ajax({
            url : "/admin/link/add",
            type : "POST",
            data : $(".layui-form").serialize(),
            success : function (result) {
                if(result.success) {
                    layer.alert('添加有情连接成功!', {
                        closeBtn: 0,
                        icon: 1
                    }, function () {
                        location.reload()
                    });
                } else {
                    layer.alert('添加心情失败 !', {icon: 5});
                }
            }
        });
        return false
    })

    form.on('submit(updateLinkSubmit)', function(data){
        $.ajax({
            url : "/admin/link/update",
            type : "POST",
            data : $(".layui-form").serialize(),
            success : function (result) {
                if(result.success) {
                    var addTopId = layer.alert('修改友情链接成功 !', {
                        closeBtn: 0,
                        icon: 1
                    }, function () {
                        location.reload()
                    });
                } else {
                    layer.alert('修改友情链接失败 !', {icon: 5});
                }
            }
        });
        return false
    });

    //输出接口，主要是两个函数，一个删除一个编辑
    var funcs = {
        addData: function () {
            var html = '';
            html += '<form class="layui-form layui-form-pane" action="/admin/link/add" method="post">';
            html += '<label class="layui-form-label" style="border: none;width: 180px;" >友情链接名称:</label>';
            html += '<input  style="width:87%;margin: auto;color: #000!important;" name="name" lay-verify="required"  class="layui-input" >';
            html += '<label class="layui-form-label" style="border: none;width: 180px;" >友情链接描述:</label>';
            html += '<input  style="width:87%;margin: auto;color: #000!important;" name="desc" lay-verify="required"  class="layui-input" >';
            html += '<label class="layui-form-label" style="border: none;width: 180px;" >友情链接地址:</label>';
            html += '<input  style="width:87%;margin: auto;color: #000!important;" name="url" lay-verify="url"  class="layui-input" >';
            html += '<label class="layui-form-label" style="border: none;" >顺序:</label>';
            html += '<input  style="width:87%;margin: auto;color: #000!important;" name="sort" lay-verify="number" class="layui-input" >';
            html += '<label class="layui-form-label" style="border: none" >是否可用:</label>';
            html += '<input type="radio" name="enable" value="1" title="是" checked />';
            html += '<input type="radio" name="enable" value="0" title="否" />';
            html += '<div class="layui-form-item">';
            html += '<div class="layui-input-inline" style="margin:10px auto 0 auto;display: block;float: none;">';
            html += '<button class="layui-btn" id="submit"  lay-submit="" lay-filter="addLinkSubmit">添加</button>';
            html += '<button type="reset" class="layui-btn layui-btn-primary">重置</button>';
            html += '</div>';
            html += '</div>';
            html += '</form>';

            layer.open({
                type: 1,
                skin: 'layui-layer-rim', //加上边框
                area: '620px', //宽高
                title: '添加友情链接',
                content: html
            });
            form.render()
        },
        editData: function (id, name, desc, url, sort, enable) {
            var html = '';
            html += '<form class="layui-form layui-form-pane" action="/admin/link/update" method="post">';
            html += '<input type="hidden" name="id" value="' + id + '"/>';
            html += '<label class="layui-form-label" style="border: none;width: 180px;"  >友情链接名称:</label>';
            html += '<input  style="width:87%;margin: auto;color: #000!important;" name="name" lay-verify="required" class="layui-input" value="' + name + '">';
            html += '<label class="layui-form-label" style="border: none;width: 180px;"  >友情链接描述:</label>';
            html += '<input  style="width:87%;margin: auto;color: #000!important;" name="desc" lay-verify="required" class="layui-input" value="' + desc + '">';
            html += '<label class="layui-form-label" style="border: none;width: 180px;" >友情链接地址:</label>';
            html += '<input  style="width:87%;margin: auto;color: #000!important;" name="url"  class="layui-input" lay-verify="url" value="' + url + '">';
            html += '<label class="layui-form-label" style="border: none;" >顺序:</label>';
            html += '<input  style="width:87%;margin: auto;color: #000!important;" name="sort"  class="layui-input" lay-verify="number" value="' + sort + '">';
            html += '<label class="layui-form-label" style="border: none" >是否可用:</label>';
            if(enable) {
                html += '<input type="radio" name="enable" value="1" title="是" checked />';
                html += '<input type="radio" name="enable" value="0" title="否" />';
            } else {
                html += '<input type="radio" name="enable" value="1" title="是" />';
                html += '<input type="radio" name="enable" value="0" title="否" checked />';
            }
            html += '<div class="layui-form-item">';
            html += '<div class="layui-input-inline" style="margin:10px auto 0 auto;display: block;float: none;">';
            html += '<button class="layui-btn" id="submit"  lay-submit="" lay-filter="updateLinkSubmit">立即修改</button>';
            html += '<button type="reset" class="layui-btn layui-btn-primary">重置</button>';
            html += '</div>';
            html += '</div>';
            html += '</form>';

            layer.open({
                type: 1,
                skin: 'layui-layer-rim', //加上边框
                area: '620px', //宽高
                title: '修改友情链接',
                content: html
            });
            form.render()
        },
        deleteData: function (id) {
            layer.confirm('您确定要删除吗？', {
                btn: ['确定', '取消'] //按钮
            }, function () {
                $.ajax({
                    url: '/admin/link/remove',
                    data: {"id" : id },
                    type: 'POST',
                    success: function (result) {
                        if (result.success) {
                            layer.alert('删除成功!', {
                                closeBtn: 0,
                                icon: 1
                            }, function () {
                                location.reload()
                            });
                        } else {
                            layer.alert('删除失败!', {icon: 5});
                        }
                    }
                });
            }, function () {

            });
        }
    }
    exports('funcs', funcs);
});