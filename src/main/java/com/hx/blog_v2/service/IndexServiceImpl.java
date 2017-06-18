package com.hx.blog_v2.service;

import com.hx.blog_v2.context.CacheContext;
import com.hx.blog_v2.context.ConstantsContext;
import com.hx.blog_v2.context.WebContext;
import com.hx.blog_v2.dao.interf.BlogExDao;
import com.hx.blog_v2.dao.interf.RltRoleResourceDao;
import com.hx.blog_v2.domain.POVOTransferUtils;
import com.hx.blog_v2.domain.dto.SessionUser;
import com.hx.blog_v2.domain.extractor.ResourceTreeInfoExtractor;
import com.hx.blog_v2.domain.form.BeanIdsForm;
import com.hx.blog_v2.domain.mapper.BlogVOMapper;
import com.hx.blog_v2.domain.mapper.CommentVOMapper;
import com.hx.blog_v2.domain.mapper.FacetByMonthMapper;
import com.hx.blog_v2.domain.mapper.OneIntMapper;
import com.hx.blog_v2.domain.po.BlogTagPO;
import com.hx.blog_v2.domain.po.BlogTypePO;
import com.hx.blog_v2.domain.po.ResourcePO;
import com.hx.blog_v2.domain.vo.BlogVO;
import com.hx.blog_v2.domain.vo.CommentVO;
import com.hx.blog_v2.domain.vo.FacetByMonthVO;
import com.hx.blog_v2.domain.vo.ResourceVO;
import com.hx.blog_v2.service.interf.BaseServiceImpl;
import com.hx.blog_v2.service.interf.IndexService;
import com.hx.blog_v2.service.interf.LinkService;
import com.hx.blog_v2.util.BlogConstants;
import com.hx.common.interf.common.Result;
import com.hx.blog_v2.util.ResultUtils;
import com.hx.json.JSONObject;
import com.hx.log.alogrithm.tree.TreeUtils;
import com.hx.log.util.Log;
import com.hx.log.util.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * BlogServiceImpl
 *
 * @author Jerry.X.He <970655147@qq.com>
 * @version 1.0
 * @date 5/20/2017 11:47 AM
 */
@Service
public class IndexServiceImpl extends BaseServiceImpl<Object> implements IndexService {

    @Autowired
    private LinkService linkService;
    @Autowired
    private BlogServiceImpl blogService;
    @Autowired
    private BlogExDao blogExDao;
    @Autowired
    private RltRoleResourceDao rltRoleResourceDao;
    @Autowired
    private CacheContext cacheContext;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private BlogConstants constants;
    @Autowired
    private ConstantsContext constantsContext;

    @Override
    public Result index() {
        String hotBlogsSql = " select b.* from blog as b inner join blog_ex as e on b.id = e.blog_id " +
                " where b.deleted = 0 and b.id >= 0 order by e.comment_cnt desc limit 0, 5 ";
        String latestCommentsSql = " select * from blog_comment order by created_at desc limit 0, 5 ";
        String contextBlogSql = " select b.* from blog as b inner join blog_ex as e on b.id = e.blog_id " +
                " where b.id = " + constantsContext.contextBlogId;
        String facetByMogroupnthSql = " select b.created_at_month as month, count(*) as cnt from blog as b " +
                "where b.deleted = 0 and b.id >= 0 group by b.created_at_month ";
        String todayVisitedSql = " select count(*) as cnt from visitor where created_at >= (select cast(cast(sysdate() as date) AS datetime))";

        List<BlogVO> hotBlogs = jdbcTemplate.query(hotBlogsSql, new BlogVOMapper());
        List<CommentVO> latestComments = jdbcTemplate.query(latestCommentsSql, new CommentVOMapper());
        BlogVO contextBlog = jdbcTemplate.queryForObject(contextBlogSql, new BlogVOMapper());
        List<FacetByMonthVO> facetByMonth = jdbcTemplate.query(facetByMogroupnthSql, new FacetByMonthMapper());
        Integer todayVisited = jdbcTemplate.queryForObject(todayVisitedSql, new OneIntMapper("cnt"));
        encapBlogVo(hotBlogs);
        encapBlogVo(Collections.singletonList(contextBlog));

        JSONObject data = new JSONObject();
        data.put("title", "生活有度, 人生添寿");
        data.put("subTitle", "如果你浪费了自己的年龄, 那是挺可悲的 因为你的青春只能持续一点儿时间 -- 很短的一点儿时间 ");
        data.put("tags", tags2List(cacheContext.allBlogTags()));
        data.put("types", tags2List(cacheContext.allBlogTypes()));
        data.put("links", linkService.adminList().getData());

        data.put("hotBlogs", hotBlogs);
        data.put("latestComments", latestComments);
        data.put("facetByMonth", facetByMonth);
        data.put("goodSensed", contextBlog.isGoodSensed());
        data.put("goodCnt", contextBlog.getGoodCnt());
        data.put("todayVisited", todayVisited);
        // 本周, 本月, 合计

        return ResultUtils.success(data);
    }

    @Override
    public Result latest() {
        String recommendBlogsSql = " select b.* from blog as b inner join blog_ex as e on b.id = e.blog_id " +
                " where b.deleted = 0 and b.id >= 0 order by e.comment_cnt desc limit 0, 1 ";
        String latestBlogsSql = " select b.* from blog as b where b.deleted = 0 and b.id >= 0 order by b.created_at desc limit 0, 5 ";

        BlogVO recommendBlog = null;
        try {
            recommendBlog = jdbcTemplate.queryForObject(recommendBlogsSql, new BlogVOMapper());
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<BlogVO> latestBlogs = jdbcTemplate.query(latestBlogsSql, new BlogVOMapper());
        if (recommendBlog != null) {
            encapBlogVo(Collections.singletonList(recommendBlog));
        }
        encapBlogVo(latestBlogs);

        JSONObject data = new JSONObject();
        data.put("recommend", recommendBlog);
        data.put("latestBlogs", latestBlogs);

        return ResultUtils.success(data);
    }

    @Override
    public Result adminMenus() {
        SessionUser user = (SessionUser) WebContext.getAttributeFromSession(BlogConstants.SESSION_USER);
        Result getResourceIdsResult = rltRoleResourceDao.getResourceIdsByRoleIds(new BeanIdsForm(user.getRoleIds()));
        if (!getResourceIdsResult.isSuccess()) {
            return getResourceIdsResult;
        }

        List<String> resourceIds = (List<String>) getResourceIdsResult.getData();
        Map<String, ResourcePO> resourcesById = cacheContext.allResources();
        Set<String> needToGet = collectAllParentIds(resourceIds, resourcesById);
        List<ResourceVO> resources = new ArrayList<>(resourcesById.size());
        for (Map.Entry<String, ResourcePO> entry : resourcesById.entrySet()) {
            if (needToGet.contains(entry.getKey()) && (entry.getValue().getEnable() == 1)) {
                resources.add(POVOTransferUtils.resourcePO2ResourceVO(entry.getValue()));
            }
        }

        final String childStr = "childs";
        JSONObject root = TreeUtils.generateTree(resources, new ResourceTreeInfoExtractor(), childStr,
                constantsContext.resourceRootParentId);
        TreeUtils.childArrayify(root, childStr);
        return ResultUtils.success(root);
    }

    @Override
    public Result adminStatistics() {
        SessionUser user = (SessionUser) WebContext.getAttributeFromSession(BlogConstants.SESSION_USER);
        JSONObject data = new JSONObject()
                .element("loginInfo", new JSONObject().element("loginIp", user.getRequestIp())
                        .element("loginDate", user.getLoginDate()).element("loginAddr", user.getIpAddr()))
                .element("todayStats", cacheContext.todaysStatistics())
                .element("sumStats", cacheContext.sumStatistics());

        return ResultUtils.success(data);
    }

    // -------------------- 辅助方法 --------------------------

    /**
     * 把给定的Map集合转换为List
     *
     * @param mapById mapById
     * @return java.util.List<com.hx.blog_v2.domain.po.BlogTagPO>
     * @author Jerry.X.He
     * @date 5/27/2017 9:21 PM
     * @since 1.0
     */
    private <T> List<T> tags2List(Map<String, T> mapById) {
        List<T> result = new ArrayList<>(mapById.size());
        for (Map.Entry<String, T> entry : mapById.entrySet()) {
            result.add(entry.getValue());
        }
        return result;
    }

    /**
     * 封装给定的博客列表的信息
     *
     * @param voes voes
     * @return void
     * @author Jerry.X.He
     * @date 5/27/2017 11:26 PM
     * @since 1.0
     */
    private void encapBlogVo(List<BlogVO> voes) {
        for (BlogVO vo : voes) {
            blogService.encapSenseAndBlogEx(vo);
            encapTypeTagInfo(vo);
//            encapContent(vo);
        }
    }

    /**
     * 封装给定的 BlogVO 的 type, tag 的信息
     *
     * @param vo vo
     * @return void
     * @author Jerry.X.He
     * @date 6/11/2017 8:56 PM
     * @since 1.0
     */
    private void encapTypeTagInfo(BlogVO vo) {
        BlogTypePO type = cacheContext.blogType(vo.getBlogTypeId());
        if (type != null) {
            vo.setBlogTypeName(type.getName());
        }
        if (vo.getBlogTagIds() != null) {
            List<String> tagIds = vo.getBlogTagIds();
            List<String> tagNames = new ArrayList<>(tagIds.size());
            for (String tagId : tagIds) {
                BlogTagPO tag = cacheContext.blogTag(tagId);
                tagNames.add(tag == null ? Tools.NULL : tag.getName());
            }
            vo.setBlogTagNames(tagNames);
        }
    }

    /**
     * 封装给定的博客的内容信息
     *
     * @param vo vo
     * @return void
     * @author Jerry.X.He
     * @date 5/21/2017 8:48 PM
     * @since 1.0
     */
    private void encapContent(BlogVO vo) {
        if (!Tools.isEmpty(vo.getContentUrl())) {
            try {
                vo.setContent(Tools.getContent(Tools.getFilePath(constants.blogRootDir, vo.getContentUrl())));
            } catch (Exception e) {
                Log.err(Tools.errorMsg(e));
            }
        }
    }

    /**
     * 根据给定的叶节点的资源, 收集其所有的上层节点
     *
     * @param resourceIds   resourceIds
     * @param resourcesById resourcesById
     * @return java.util.Set<java.lang.String>
     * @author Jerry.X.He
     * @date 6/3/2017 3:41 PM
     * @since 1.0
     */
    private Set<String> collectAllParentIds(List<String> resourceIds, Map<String, ResourcePO> resourcesById) {
        Set<String> needToGet = new HashSet<>(Tools.estimateMapSize(resourcesById.size()));
        needToGet.addAll(resourceIds);
        List<String> parentIds = new ArrayList<>(resourcesById.size());
        for (Map.Entry<String, ResourcePO> entry : resourcesById.entrySet()) {
            ResourcePO po = entry.getValue();
            if (constantsContext.resourceRootParentId.equals(po.getParentId())) {
                continue;
            }

            if (resourceIds.contains(po.getId())) {
                parentIds.add(po.getParentId());
            }
        }
        if (!parentIds.isEmpty()) {
            needToGet.addAll(collectAllParentIds(parentIds, resourcesById));
        }

        return needToGet;
    }

}
