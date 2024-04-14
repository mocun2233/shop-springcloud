package cn.wolfcode.service.impl;

import cn.wolfcode.domain.OrderInfo;
import cn.wolfcode.domain.SeckillProductVo;
import cn.wolfcode.mapper.OrderInfoMapper;
import cn.wolfcode.mapper.PayLogMapper;
import cn.wolfcode.mapper.RefundLogMapper;
import cn.wolfcode.service.IOrderInfoService;
import cn.wolfcode.service.ISeckillProductService;
import cn.wolfcode.util.IdGenerateUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Created by wolfcode-lanxw
 */
@Service
public class OrderInfoSeviceImpl implements IOrderInfoService {
    @Autowired
    private ISeckillProductService seckillProductService;
    @Autowired
    private OrderInfoMapper orderInfoMapper;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private PayLogMapper payLogMapper;
    @Autowired
    private RefundLogMapper refundLogMapper;


    @Override
    public OrderInfo findByPhoneAndSeckillId(String phone, Long seckillId) {
        return orderInfoMapper.findByPhoneAndSeckillId(phone,seckillId);
    }




    @Override
    @Transactional
    public String doSeckill(Integer time, Long seckillId, String phone) {
        //库存扣减的操作
        seckillProductService.desrStockCount(seckillId);
        String orderNo = createOrder(time,seckillId,phone);
        return orderNo;
    }

    private String createOrder(Integer time, Long seckillId, String phone) {
        OrderInfo orderInfo=new OrderInfo();
        SeckillProductVo vo = seckillProductService.find(time, seckillId);
        BeanUtils.copyProperties(vo,orderInfo);
        orderInfo.setCreateDate(new Date());//订单创建时间
        orderInfo.setPhone(phone);//秒杀手机号
        orderInfo.setSeckillDate(vo.getStartDate());//秒杀日期
        orderInfo.setSeckillId(vo.getId());//秒杀ID
        orderInfo.setSeckillTime(vo.getTime());

        orderInfo.setOrderNo(IdGenerateUtil.get().nextId()+"");//订单号

        orderInfoMapper.insert(orderInfo);
        return orderInfo.getOrderNo();
    }

    @Override
    public OrderInfo find(String orderNo) {
        OrderInfo orderInfo = orderInfoMapper.find(orderNo);
        return orderInfo;
    }
}
