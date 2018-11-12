package com.taohong.snapup.dao;

import com.taohong.snapup.domain.OrderInfo;
import com.taohong.snapup.domain.SnapupOrder;
import com.taohong.snapup.vo.GoodsVo;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author taohong on 10/10/2018
 */
@Mapper
public interface OrderDao {

    @Select("select * from snapup_order where user_id=#{userId} and goods_id=#{goodsId}")
    SnapupOrder getSnapupOrderByUserIdGoodsId(@Param("userId") long userId, @Param("goodsId") long goodsId);

    @Insert("insert into order_info (user_id, goods_id, goods_name, goods_count, goods_price, order_channel, status, create_date) " +
            "values(#{userId}, #{goodsId}, #{goodsName}, #{goodsCount}, #{goodsPrice}, #{orderChannel}, #{status}, #{createDate})")
    @SelectKey(keyColumn = "id", keyProperty = "id", resultType = long.class, before = false, statement = "select last_insert_id()")
    long insert(OrderInfo orderInfo);

    @Insert("insert into snapup_order (user_id, goods_id, order_id) values (#{userId}, #{goodsId}, #{orderId})")
    int insertSnapupOrder(SnapupOrder snapupOrder);

    @Select("select * from order_info where id=#{orderId}")
    OrderInfo getOrderById(@Param("orderId") long orderId);

    @Delete("delete from order_info")
    void deleteOrders();

    @Delete("delete from snapup_order")
    void deleteSnapupOrders();
}
