package com.hx.blog_v2.dao.blog;

import com.hx.blog_v2.dao.interf.BaseDaoImpl;
import com.hx.blog_v2.dao.interf.BlogCreateTypeDao;
import com.hx.blog_v2.domain.po.blog.BlogCreateTypePO;
import com.hx.blog_v2.util.BlogConstants;
import com.hx.blog_v2.domain.BaseVO;
import com.hx.blog_v2.util.MyMysqlConnectionProvider;
import com.hx.mongo.config.MysqlDbConfig;
import org.springframework.stereotype.Repository;

/**
 * BlogCreateTypeDaoImpl
 *
 * @author Jerry.X.He <970655147@qq.com>
 * @version 1.0
 * @date 5/20/2017 10:38 AM
 */
@Repository
public class BlogCreateTypeDaoImpl extends BaseDaoImpl<BlogCreateTypePO> implements BlogCreateTypeDao {

    public BlogCreateTypeDaoImpl() {
        super(BlogCreateTypePO.PROTO_BEAN,
                new MysqlDbConfig(BlogConstants.MYSQL_DB_CONFIG).table(tableName()).id(id()),
                MyMysqlConnectionProvider.getInstance());
    }

    public static String tableName() {
        return BlogConstants.getInstance().tableBlogCreateType;
    }

    public static String id() {
        return BlogConstants.getInstance().tableId;
    }


}
