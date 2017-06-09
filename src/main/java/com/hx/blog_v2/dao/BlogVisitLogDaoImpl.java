package com.hx.blog_v2.dao;

import com.hx.blog_v2.dao.interf.BaseDaoImpl;
import com.hx.blog_v2.dao.interf.BlogVisitLogDao;
import com.hx.blog_v2.domain.form.BlogVisitLogForm;
import com.hx.blog_v2.domain.po.BlogVisitLogPO;
import com.hx.blog_v2.util.BlogConstants;
import com.hx.blog_v2.util.CacheContext;
import com.hx.blog_v2.util.MyMysqlConnectionProvider;
import com.hx.log.util.Tools;
import com.hx.mongo.config.MysqlDbConfig;
import com.hx.mongo.criteria.Criteria;
import com.hx.mongo.criteria.interf.IQueryCriteria;
import com.hx.mongo.criteria.interf.MultiCriteriaQueryCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * BlogDaoImpl
 *
 * @author Jerry.X.He <970655147@qq.com>
 * @version 1.0
 * @date 5/20/2017 10:38 AM
 */
@Repository
public class BlogVisitLogDaoImpl extends BaseDaoImpl<BlogVisitLogPO> implements BlogVisitLogDao {

    @Autowired
    private CacheContext cacheContext;

    public BlogVisitLogDaoImpl() {
        super(BlogVisitLogPO.PROTO_BEAN,
                new MysqlDbConfig(BlogConstants.MYSQL_DB_CONFIG).table(tableName()).id(id()),
                new MyMysqlConnectionProvider());
    }


    public static String tableName() {
        return BlogConstants.getInstance().tableBlogVisitLog;
    }

    public static String id() {
        return BlogConstants.getInstance().tableId;
    }

    @Override
    public BlogVisitLogPO get(BlogVisitLogForm params) {
        BlogVisitLogPO po = cacheContext.getBlogVisitLog(params);
        if(po != null) {
            return po;
        }

        MultiCriteriaQueryCriteria query = Criteria.and(Criteria.eq("blog_id", params.getBlogId()))
                .add(Criteria.eq("request_ip", params.getRequestIp()));
        if(!Tools.isEmpty(params.getCreatedAtDay())) {
            query.add(Criteria.eq("created_at_day", params.getCreatedAtDay()));
        }

        try {
            po = findOne(query, BlogConstants.LOAD_ALL_CONFIG);
            return po;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void add(BlogVisitLogPO po) {
        try {
            insertOne(po, BlogConstants.ADD_BEAN_CONFIG);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}