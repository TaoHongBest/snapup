package com.taohong.snapup.rabbitmq;

import com.taohong.snapup.domain.SnapupOrder;
import com.taohong.snapup.domain.SnapupUser;
import com.taohong.snapup.redis.RedisService;
import com.taohong.snapup.service.GoodsService;
import com.taohong.snapup.service.OrderService;
import com.taohong.snapup.service.SnapupService;
import com.taohong.snapup.vo.GoodsVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author taohong on 07/11/2018
 */
@Service
public class MQReceiver {
    private static Logger log = LoggerFactory.getLogger(MQReceiver.class);

    @Autowired
    RedisService redisService;

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    SnapupService snapupService;

    @RabbitListener(queues = MQConfig.SNAPUP_QUEUE)
    public void receive(String message) {
        log.info("receive message:" + message);
        SnapupMessage sm = RedisService.stringToBean(message, SnapupMessage.class);
        SnapupUser user = sm.getUser();
        long goodsId = sm.getGoodsId();

        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        int stock = goods.getStockCount();
        if (stock <= 0) {
            return;
        }

        // Check if have snapped up an item
        SnapupOrder order = orderService.getSnapupOrderByUserIdGoodsId(user.getId(), goodsId);
        if (order != null) {
            return;
        }

        // Reduce stock, make order, write snap-up order
        snapupService.snapup(user, goods);
    }

//    /**
//     * Direct mode Exchange
//     */
//    @RabbitListener(queues = MQConfig.QUEUE)
//    public void receive(String message) {
//        log.info("receive message:" + message);
//    }
//
//    @RabbitListener(queues = MQConfig.TOPIC_QUEUE1)
//    public void receiveTopic1(String message) {
//        log.info("topic queue1 message:" + message);
//    }
//
//    @RabbitListener(queues = MQConfig.TOPIC_QUEUE2)
//    public void receiveTopic2(String message) {
//        log.info("topic queue2 message:" + message);
//    }
//
//    @RabbitListener(queues = MQConfig.HEADER_QUEUE)
//    public void receiveHeaderQueue(byte[] message) {
//        log.info("header queue message:" + new String(message));
//    }
}
