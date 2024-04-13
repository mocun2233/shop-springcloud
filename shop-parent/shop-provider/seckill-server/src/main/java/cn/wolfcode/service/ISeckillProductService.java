package cn.wolfcode.service;

import cn.wolfcode.domain.SeckillProduct;
import cn.wolfcode.domain.SeckillProductVo;

import java.util.List;

/**
 * Created by lanxw
 */
public interface ISeckillProductService {
    /**
     * 根据场次查询对应的商品
     * @param time
     * @return
     */
    List<SeckillProductVo> queryByTime(Integer time);

    /**
     * 根据场次和秒杀商品查询详情页面
     * @param time
     * @param seckillId
     * @return
     */
    SeckillProductVo find(Integer time, Long seckillId);
}
