package com.hx.blog_v2.domain.mapper.blog;

import com.hx.blog_v2.domain.POVOTransferUtils;
import com.hx.blog_v2.domain.po.blog.BlogCommentPO;
import com.hx.blog_v2.domain.vo.blog.CommentVO;
import com.hx.blog_v2.util.BlogConstants;
import com.hx.blog_v2.domain.BaseVO;
import com.hx.mongo.util.ResultSet2MapAdapter;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * CommentVOMapper
 *
 * @author Jerry.X.He <970655147@qq.com>
 * @version 1.0
 * @date 5/21/2017 8:42 PM
 */
public class CommentVOMapper implements RowMapper<CommentVO> {

    @Override
    public CommentVO mapRow(ResultSet resultSet, int i) throws SQLException {
        BlogCommentPO po = new BlogCommentPO();
        po.loadFromJSON(new ResultSet2MapAdapter(resultSet), BlogConstants.LOAD_ALL_CONFIG);
        CommentVO vo = POVOTransferUtils.blogCommentPO2CommentVO(po);
        return vo;
    }

}
