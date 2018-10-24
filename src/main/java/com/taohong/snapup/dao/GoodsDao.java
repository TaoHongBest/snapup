package com.taohong.snapup.dao;

import com.taohong.snapup.vo.GoodsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author taohong on 10/10/2018
 */
@Mapper
public interface GoodsDao {

    @Select("select g.*, sg.stock_count, sg.start_date, sg.end_date, sg.snapup_price " +
            "from snapup_goods sg left join goods g on sg.goods_id=g.id")
    List<GoodsVo> listGoodsVo();

    @Select("select g.*, sg.stock_count, sg.start_date, sg.end_date, sg.snapup_price " +
            "from snapup_goods sg left join goods g on sg.goods_id=g.id " +
            "where g.id=#{goodsId}")
    GoodsVo getGoodsVoByGoodsId(@Param("goodsId") long goodsId);
}
