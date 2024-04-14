package cn.wolfcode.service;


import cn.wolfcode.domain.OrderInfo;

import java.util.Map;

/**
 * Created by wolfcode-lanxw
 */
public interface IOrderInfoService {

    String doSeckill(Integer time, Long seckillId, String phone);

    /**
     * 根据phone和seckill_id查找是否存在订单
     * @param phone
     * @param seckillId
     * @return
     */
    OrderInfo findByPhoneAndSeckillId(String phone, Long seckillId);

    OrderInfo find(String orderNo);
}
