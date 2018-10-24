package com.taohong.snapup.service;

import com.taohong.snapup.dao.GoodsDao;
import com.taohong.snapup.domain.Goods;
import com.taohong.snapup.domain.SnapupGoods;
import com.taohong.snapup.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author taohong on 10/10/2018
 */
@Service
public class GoodsService {

    @Autowired
    GoodsDao goodsDao;

    public List<GoodsVo> listGoodsVo() {
        return goodsDao.listGoodsVo();
    }


    public GoodsVo getGoodsVoByGoodsId(long goodsId) {
        return goodsDao.getGoodsVoByGoodsId(goodsId);
    }

    public void reduceStock(GoodsVo goods) {
        SnapupGoods g = new SnapupGoods();
        g.setGoodsId(goods.getId());
        goodsDao.reduceStock(g);
    }
}
