package com.imooc.snapup.dao;

import com.imooc.snapup.domain.SnapupUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author taohong on 22/10/2018
 */
@Mapper
public interface SnapupUserDao {
    @Select("select * from snapup_user where id=#{id}")
    SnapupUser getById(@Param("id") long id);

}
