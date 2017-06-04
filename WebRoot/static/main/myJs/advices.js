/**
 * blogList.js
 *
 * @author Jerry.X.He <970655147@qq.com>
 * @version 1.0
 * @date 5/28/2017 11:05 AM
 */

/**
 * 初始化博客列表
 */
function contentInit() {

    var app = new Vue({
        el: '#bodyContent',
        data: {
            blog: {},
            userInfo: {
                userName: "",
                email: "",
                headImgUrl: "",
            },
            comments: [],
            headImages: [],
            replyInfo: {
                blogId : "",
                floorId: "",
                commentId: "",
                toUser: ""
            }
        },
        mounted: function () {
            var that = this

            that.initBlogInfo(that)
            that.initHeadImages(that)
            SyntaxHighlighter.all()
            that.initEmoji()

            heartClick("#blogHeart", "#blogLikeCount", function (isPrise) {

            })
        },
        methods: {
            replyFunc: function (event) {
                var floorInfo = $(event.currentTarget).parents(".replyDiv").find(".floorInfo")

                this.replyInfo.floorId = floorInfo.attr("floorId")
                this.replyInfo.commentId = floorInfo.attr("commentId")
                this.replyInfo.toUser = floorInfo.attr("name")
                $("#comment").text("[reply]" + floorInfo.attr("name") + "[/reply]\r\n")
                $("html,body").animate({scrollTop: $("#comment").offset().top}, 1000);
            },
            initEmoji: function () {
                $("#comment").emoji({
                    button: "#looks",
                    showTab: true,
                    animation: 'fade',
                    icons: [{
                        name: "qq",
                        path: "/static/main/images/qq/",
                        maxNum: 91,
                        file: ".gif",
                        placeholder: "#qq_{alias}#"
                    }]
                });
                this.parse();
            },
            parse: function () {
                $(".sourceText").emojiParse({
                    icons: [{
                        path: "/static/main/images/qq/",
                        file: ".gif",
                        placeholder: "#qq_{alias}#"
                    }]
                });
            },
            updateHeadImg: function (event) {
                this.userInfo.headImgUrl = $(event.target).find("option:selected").attr("value")
            },
            initBlogInfo: function (that) {
                $.ajax({
                    url: "/blog/advices",
                    data: { },
                    success: function (resp) {
                        if (resp.success) {
                            that.blog = resp.data.blog
                            var comments = resp.data.comments
                            $("#blogContent").html(that.blog.content)
                            that.replyInfo.blogId = that.blog.id
                            that.replyInfo.toUser = that.blog.author

                            for (idx in comments) {
                                that.comments.push({
                                    floorComment: comments[idx][0],
                                    replies: comments[idx].slice(1)
                                })
                            }

                            var userInfo = resp.extra
                            if (userInfo !== null) {
                                that.userInfo = userInfo
                                that.userInfo.headImgUrl = userInfo.headImgUrl
                                if(userInfo.systemUser) {
                                    $("[name='name']").attr("readonly", "readonly")
                                    $("[name='email']").attr("readonly", "readonly")
                                }
                            }
                        } else {
                            layer.alert("拉取博客列表失败 !")
                        }
                    }
                });
            },
            initHeadImages: function (that) {
                $.ajax({
                    url: "/image/headImgList",
                    data: {},
                    type: "GET",
                    success: function (resp) {
                        if (resp.success) {
                            that.headImages = resp.data
                            if (that.userInfo.headImgUrl === "") {
                                that.userInfo.headImgUrl = that.headImages[0].url
                            }
                        } else {
                            layer.alert("拉取头像列表失败 !")
                        }
                    }
                });
            },
            reUseMyHeadImg: function (event) {
                if (this.userInfo !== null) {
                    this.userInfo.headImgUrl = this.userInfo.headImgUrl
                }
            },
            addComment: function () {
                var that = this
                var replyForm = $("#replyForm")
                var params = {
                    "blogId": that.blog.id,
                    "floorId": that.replyInfo.floorId,
                    "commentId": that.replyInfo.commentId,
                    "name": that.userInfo.userName,
                    "email": that.userInfo.email,
                    "headImgUrl": that.userInfo.headImgUrl,
                    "toUser": that.replyInfo.toUser,
                    "comment": replyForm.find("[name='comment']").html()
                }

                $.ajax({
                    url: "/comment/add",
                    data: params,
                    type: "POST",
                    success: function (resp) {
                        if (resp.success) {
                            var addedComment = {
                                "blogId": params.blogId,
                                "floorId": 1,
                                "commentId": 1,
                                "name": params.name,
                                "email": params.email,
                                "headImgUrl": params.headImgUrl,
                                "toUser": params.toUser,
                                "role": isEmpty(that.userInfo.title) ? "guest" : that.userInfo.title,
                                "comment": params.comment,
                                "createdAt": new Date().format("yyyy-MM-dd hh:mm:ss")
                            }

                            var endReplyFlag = "[/reply]"
                            if (isEmpty(params.floorId) || (addedComment.comment.indexOf(endReplyFlag) < 0)) {
                                addedComment.floorId = that.comments.length + 1
                                that.comments.push({
                                    floorComment: addedComment,
                                    replies: []
                                })
                            } else {
                                var floorId = params.floorId
                                var floorComments = that.locateFloorComment(that.comments, floorId)
                                if (floorComments !== null) {
                                    addedComment.floorId = floorId
                                    addedComment.commentId = floorComments.replies.length + 2
                                    var endOfReply = addedComment.comment.indexOf(endReplyFlag) + endReplyFlag.length
                                    addedComment.comment = addedComment.comment.substr(endOfReply).trim()

                                    floorComments.replies.push(addedComment)
                                }
                            }

                            replyForm.find("[name='comment']").html("")
                            layer.msg("添加评论成功 !")
                        } else {
                            layer.msg("添加评论失败 !")
                        }
                    }
                });
            },
            locateFloorComment: function (comments, floorId) {
                var floorIdInt = parseInt(floorId)
                if (floorIdInt < 0) {
                    return null;
                }
                if (floorIdInt >= comments.length) {
                    floorIdInt = comments.length - 1
                }

                for (i = floorIdInt; i >= 0; i--) {
                    if (comments[i].floorComment.floorId === floorId) {
                        return comments[i]
                    }
                }
                return null;
            }

        }
    })

}
