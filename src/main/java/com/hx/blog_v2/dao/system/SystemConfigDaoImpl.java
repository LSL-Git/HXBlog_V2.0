package com.hx.blog_v2.dao.system;

import com.hx.blog_v2.dao.interf.BaseDaoImpl;
import com.hx.blog_v2.dao.interf.SystemConfigDao;
import com.hx.blog_v2.domain.po.system.SystemConfigPO;
import com.hx.blog_v2.util.BlogConstants;
import com.hx.blog_v2.domain.BaseVO;
import com.hx.blog_v2.util.MyMysqlConnectionProvider;
import com.hx.mongo.config.MysqlDbConfig;
import org.springframework.stereotype.Repository;

/**
 * SystemConfigDaoImpl
 *
 * @author Jerry.X.He <970655147@qq.com>
 * @version 1.0
 * @date 5/20/2017 10:38 AM
 */
@Repository
public class SystemConfigDaoImpl extends BaseDaoImpl<SystemConfigPO> implements SystemConfigDao {

    public SystemConfigDaoImpl() {
        super(SystemConfigPO.PROTO_BEAN,
                new MysqlDbConfig(BlogConstants.MYSQL_DB_CONFIG).table(tableName()).id(id()),
                MyMysqlConnectionProvider.getInstance());
    }


    public static String tableName() {
        return BlogConstants.getInstance().tableSystemConfig;
    }

    public static String id() {
        return BlogConstants.getInstance().tableId;
    }


}
