package com.taohong.snapup.dao;

import com.taohong.snapup.domain.SnapupUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * @author taohong on 22/10/2018
 */
@Mapper
public interface SnapupUserDao {
    @Select("select * from snapup_user where id=#{id}")
    SnapupUser getById(@Param("id") long id);

    @Update("update snapup_user set password=#{password} where id=#{id}")
    void update(SnapupUser toBeUpdate);


}
