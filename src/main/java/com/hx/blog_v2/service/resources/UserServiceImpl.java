package com.hx.blog_v2.service.resources;

import com.hx.blog_v2.cache_handler.CacheResultType;
import com.hx.blog_v2.cache_handler.CacheType;
import com.hx.blog_v2.cache_handler.anno.CacheEvictAll;
import com.hx.blog_v2.cache_handler.anno.CacheHandle;
import com.hx.blog_v2.context.CacheContext;
import com.hx.blog_v2.context.ConstantsContext;
import com.hx.blog_v2.context.WebContext;
import com.hx.blog_v2.dao.interf.UserDao;
import com.hx.blog_v2.domain.POVOTransferUtils;
import com.hx.blog_v2.domain.common.system.SessionUser;
import com.hx.blog_v2.domain.form.common.BeanIdForm;
import com.hx.blog_v2.domain.form.system.LoginForm;
import com.hx.blog_v2.domain.form.system.UpdatePwdForm;
import com.hx.blog_v2.domain.form.resources.UserSaveForm;
import com.hx.blog_v2.domain.mapper.resources.AdminUserVOMapper;
import com.hx.blog_v2.domain.mapper.resources.Id2NameRoleMapper;
import com.hx.blog_v2.domain.mapper.common.OneIntMapper;
import com.hx.blog_v2.domain.po.resources.UserPO;
import com.hx.blog_v2.domain.vo.message.MessageVO;
import com.hx.blog_v2.domain.vo.resources.AdminUserVO;
import com.hx.blog_v2.domain.vo.common.Id2NameVO;
import com.hx.blog_v2.service.interf.resources.UserService;
import com.hx.blog_v2.util.BlogConstants;
import com.hx.blog_v2.domain.BaseVO;
import com.hx.blog_v2.util.DateUtils;
import com.hx.blog_v2.util.ResultUtils;
import com.hx.common.interf.common.Page;
import com.hx.common.interf.common.Result;
import com.hx.json.JSONArray;
import com.hx.log.alogrithm.code.Codec;
import com.hx.log.util.Tools;
import com.hx.mongo.criteria.Criteria;
import com.hx.mongo.criteria.interf.IQueryCriteria;
import com.hx.mongo.criteria.interf.IUpdateCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import static com.hx.blog_v2.util.CacheConstants.*;

/**
 * UserServiceImpl
 *
 * @author Jerry.X.He <970655147@qq.com>
 * @version 1.0
 * @date 5/23/2017 8:22 PM
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private ConstantsContext constantsContext;
    @Autowired
    private CacheContext cacheContext;

    @Override
    @CacheEvictAll(ns = {CACHE_AOP_LIST_USER_ID_TO_NAME, CACHE_AOP_ADMIN_PAGE_USER})
    public Result add(UserSaveForm params) {
        String countSql = "select count(*) as totalRecord from `user` where user_name = ? ";
        Integer totalRecord = jdbcTemplate.queryForObject(countSql, new String[]{params.getUserName()}, new OneIntMapper("totalRecord"));
        if (totalRecord > 0) {
            return ResultUtils.failed("用户[" + params.getUserName() + "]已经存在 !");
        }

        UserPO po = new UserPO(params.getUserName(), params.getPassword(), params.getTitle(), params.getNickName(),
                params.getEmail(), params.getHeadImgUrl(), params.getMotto());
        String pwdSalt = newSalt();
        String pwd = encodePwd(params.getPassword(), pwdSalt);
        po.setPwdSalt(pwdSalt);
        po.setPassword(pwd);

        Result result = userDao.add(po);
        if (!result.isSuccess()) {
            return result;
        }
        return ResultUtils.success(po.getId());
    }

    @Override
    @CacheHandle(type = CacheType.PAGE_DEV_DEFINED, ns = CACHE_AOP_ADMIN_PAGE_USER, timeout = CACHE_DEFAULT_TIMEOUT,
            cacheResultType = CacheResultType.RESULT_PAGE, cacheResultClass = AdminUserVO.class)
    public Result adminList(Page<AdminUserVO> page) {
        String selectSql = " select * from `user` where deleted = 0 order by created_at desc limit ?, ? ";
        String countSql = " select count(*) as totalRecord from `user` where deleted = 0 ";
        Object[] params = new Object[]{page.recordOffset(), page.getPageSize()};

        Integer totalRecord = jdbcTemplate.queryForObject(countSql, new OneIntMapper("totalRecord"));
        if (totalRecord <= 0) {
            page.setList(Collections.<AdminUserVO>emptyList());
        } else {
            List<AdminUserVO> list = jdbcTemplate.query(selectSql, params, new AdminUserVOMapper());
            page.setList(list);
        }
        page.setTotalRecord(totalRecord);
        return ResultUtils.success(page);
    }

    @Override
    @CacheEvictAll(ns = {CACHE_AOP_LIST_USER_ID_TO_NAME, CACHE_AOP_ADMIN_PAGE_USER})
    public Result update(UserSaveForm params) {
        Result getUserResult = userDao.get(Criteria.eq("user_name", params.getUserName()));
        if (!getUserResult.isSuccess()) {
            return getUserResult;
        }
        UserPO userFromDb = (UserPO) getUserResult.getData();
        if (!userFromDb.getId().equals(params.getId())) {
            return ResultUtils.failed("用户[" + params.getUserName() + "]已经存在 !");
        }

        UserPO po = new UserPO(null, null, params.getNickName(), params.getTitle(),
                params.getEmail(), params.getHeadImgUrl(), params.getMotto());
        po.setId(params.getId());
        po.setUpdatedAt(DateUtils.format(new Date(), BlogConstants.FORMAT_YYYY_MM_DD_HH_MM_SS));

        Result result = userDao.update(po);
        if (!result.isSuccess()) {
            return result;
        }
        return ResultUtils.success(po.getId());
    }

    @Override
    public Result updatePwd(UpdatePwdForm params) {
        SessionUser user = (SessionUser) WebContext.getAttributeFromSession(BlogConstants.SESSION_USER);
        Result getUserResult = userDao.get(new BeanIdForm(user.getId()));
        if (!getUserResult.isSuccess()) {
            return getUserResult;
        }
        UserPO po = (UserPO) getUserResult.getData();
        if (!po.getPassword().equalsIgnoreCase(encodePwd(params.getOldPwd(), po.getPwdSalt()))) {
            return ResultUtils.failed("用户密码不正确 !");
        }

        String updatedAt = DateUtils.format(new Date(), BlogConstants.FORMAT_YYYY_MM_DD_HH_MM_SS);
        String newSalt = newSalt();
        String newPwd = encodePwd(params.getNewPwd(), newSalt);
        IQueryCriteria query = Criteria.eq("id", user.getId());
        IUpdateCriteria update = Criteria.set("updated_at", updatedAt).add("pwd_salt", newSalt)
                .add("password", newPwd);
        Result result = userDao.update(query, update);
        if (!result.isSuccess()) {
            return result;
        }
        return ResultUtils.success(user.getId());
    }

    @Override
    @CacheEvictAll(ns = {CACHE_AOP_LIST_USER_ID_TO_NAME, CACHE_AOP_ADMIN_PAGE_USER})
    public Result remove(BeanIdForm params) {
        String updatedAt = DateUtils.format(new Date(), BlogConstants.FORMAT_YYYY_MM_DD_HH_MM_SS);
        IQueryCriteria query = Criteria.eq("id", params.getId());
        IUpdateCriteria update = Criteria.set("deleted", "1").add("updated_at", updatedAt);
        Result result = userDao.update(query, update);
        if (!result.isSuccess()) {
            return result;
        }
        return ResultUtils.success(params.getId());
    }

    @Override
    public Result login(LoginForm params) {
        String checkCodeInServer = (String) WebContext.getAttributeFromSession(BlogConstants.SESSION_CHECK_CODE);
        if (Tools.isEmpty(checkCodeInServer)) {
            return ResultUtils.failed("您还没有验证码 !");
        }
        WebContext.removeAttributeFromSession(BlogConstants.SESSION_CHECK_CODE);
        if(! checkCodeInServer.equalsIgnoreCase(params.getCheckCode())) {
            return ResultUtils.failed("验证码不正确 !");
        }

        IQueryCriteria query = Criteria.and(Criteria.eq("user_name", params.getUserName()))
                .add(Criteria.eq("deleted", "0"));
        Result getResult = userDao.get(query);
        if (!getResult.isSuccess()) {
            return getResult;
        }
        UserPO user = (UserPO) getResult.getData();
        if (!encodePwd(params.getPassword(), user.getPwdSalt()).equals(user.getPassword())) {
            return ResultUtils.failed("用户名 或者密码不正确");
        }

        SessionUser sessionUser = POVOTransferUtils.userPO2SessionUser(user);
        String sql = " select role_id from rlt_user_role where user_id = ? order by role_id ";
        List<Integer> roleIds = jdbcTemplate.query(sql, new Object[]{user.getId()}, new OneIntMapper("role_id"));
        String roleIdsStr = collectRoleIds(roleIds);
        sessionUser.setRoleIds(roleIdsStr);
        sessionUser.setSystemUser(true);
        if (!Tools.isEmpty(params.getIp())) {
            sessionUser.setRequestIp(params.getIp());
            sessionUser.setIpAddr(params.getIpAddr());
        }
        WebContext.setAttributeForSession(BlogConstants.SESSION_USER, sessionUser);
        WebContext.setAttributeForSession(BlogConstants.SESSION_USER_ID, user.getId());
        cacheContext.removeForceOffLine(user.getId());

        IUpdateCriteria update = Criteria.set("last_login_ip", params.getIp())
                .add("last_login_at", DateUtils.format(new Date(), BlogConstants.FORMAT_YYYY_MM_DD_HH_MM_SS));
        Result updateResult = userDao.update(Criteria.eq("id", user.getId()), update);
        if (!updateResult.isSuccess()) {
            return updateResult;
        }
        return ResultUtils.success("success");
    }

    @Override
    public Result logout() {
        WebContext.removeAttributeFromSession(BlogConstants.SESSION_USER);
        WebContext.removeAttributeFromSession(BlogConstants.SESSION_CHECK_CODE);

        return ResultUtils.success("success");
    }

    @Override
    @CacheHandle(type = CacheType.DEV_DEFINED, ns = CACHE_AOP_LIST_USER_ID_TO_NAME, timeout = CACHE_DEFAULT_TIMEOUT,
            cacheResultType = CacheResultType.RESULT_PAGE, cacheResultClass = Id2NameVO.class)
    public Result allId2Name() {
        String sql = " select id, user_name from user where deleted = 0 order by created_at ";
        List<Id2NameVO> list = jdbcTemplate.query(sql, new Id2NameRoleMapper("id", "user_name"));
        return ResultUtils.success(list);
    }

    // -------------------- 辅助方法 --------------------------

    /**
     * 对给定的密码进行加密
     *
     * @param pwd  pwd
     * @param salt salt
     * @return java.lang.String
     * @author Jerry.X.He
     * @date 6/3/2017 10:31 AM
     * @since 1.0
     */
    private String encodePwd(String pwd, String salt) {
        String desEncoded = Codec.byte2Hex(Codec.desE(pwd.getBytes(), salt.getBytes()));
        String md5Encoded = Codec.byte2Hex(Codec.md5(desEncoded.getBytes()));
        return md5Encoded;
    }

    /**
     * 新建一个 salt
     *
     * @return java.lang.String
     * @author Jerry.X.He
     * @date 6/3/2017 10:34 AM
     * @since 1.0
     */
    private String newSalt() {
        StringBuilder sb = new StringBuilder(constantsContext.pwdSaltNums);
        for (int i = 0; i < constantsContext.pwdSaltNums; i++) {
            sb.append(Tools.ran.nextInt(10));
        }
        return sb.toString();
    }

    /**
     * 获取 roleIds 的字符串表示[作为key]
     *
     * @param roleIds roleIds
     * @return java.lang.String
     * @author Jerry.X.He
     * @date 6/3/2017 3:14 PM
     * @since 1.0
     */
    private String collectRoleIds(List<Integer> roleIds) {
        JSONArray arr = JSONArray.fromObject(roleIds);
        return arr.toString();
    }

}
