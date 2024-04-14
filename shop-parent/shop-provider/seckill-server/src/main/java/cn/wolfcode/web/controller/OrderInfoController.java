package cn.wolfcode.web.controller;

import cn.wolfcode.common.constants.CommonConstants;
import cn.wolfcode.common.exception.BusinessException;
import cn.wolfcode.common.web.CommonCodeMsg;
import cn.wolfcode.common.web.Result;
import cn.wolfcode.common.web.anno.RequireLogin;
import cn.wolfcode.domain.OrderInfo;
import cn.wolfcode.domain.SeckillProductVo;
import cn.wolfcode.service.IOrderInfoService;
import cn.wolfcode.service.ISeckillProductService;
import cn.wolfcode.util.UserUtil;
import cn.wolfcode.web.msg.SeckillCodeMsg;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by lanxw
 */
@RestController
@RequestMapping("/order")
@Slf4j
public class OrderInfoController {
    @Autowired
    private ISeckillProductService seckillProductService;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private RocketMQTemplate rocketMQTemplate;
    @Autowired
    private IOrderInfoService orderInfoService;


@RequestMapping("/doSeckill")
@RequireLogin  //自定义的判断是否登录的注解
public Result<String> doSeckill(Integer time, Long seckillId, HttpServletRequest request){
    /**
     * 1. 登录状态才能参与抢购
     * 2. 在规定时间之内进行抢购
     * 3. 对于用户而言,针对一个秒杀商品只能抢购一个商品
     * 4. 保证不出现超卖的情况
     * ______________原子性操作_____________________
     * 5. 库存扣减的操作
     * 6. 创建订单
     * ------------------------------------
     */

    //2. 在规定时间之内进行抢购
    SeckillProductVo vo = seckillProductService.find(time, seckillId);
      /*  boolean legalTime = DateUtil.isLegalTime(vo.getStartDate(), vo.getTime());
        if(!legalTime){
            throw new BusinessException(CommonCodeMsg.ILLEGAL_OPERATION);
        }*/

    String token = request.getHeader(CommonConstants.TOKEN_NAME); //拿到token
    String phone = UserUtil.getUserPhone(redisTemplate, token);//拿到用户手机
    //3. 对于用户而言,针对一个秒杀商品只能抢购一个商品
    OrderInfo orderInfo=orderInfoService.findByPhoneAndSeckillId(phone,seckillId);
    if(orderInfo!=null){
        throw new BusinessException(SeckillCodeMsg.REPEAT_SECKILL);
    }
    //4. 保证不出现超卖的情况
    if(vo.getStockCount()<=0){
        throw new BusinessException(SeckillCodeMsg.SECKILL_STOCK_OVER);
    }
    //5. 库存扣减的操作+6. 创建订单
    String orderNo = orderInfoService.doSeckill(time,seckillId,phone);
    System.out.println("订单号"+orderNo);

    return Result.success("登录成功");
}


    @RequestMapping("/find")
    @RequireLogin
    public Result<OrderInfo> find(String orderNo,HttpServletRequest request){

        OrderInfo orderInfo=orderInfoService.find(orderNo);
        String token = request.getHeader(CommonConstants.TOKEN_NAME);
        String phone = UserUtil.getUserPhone(redisTemplate, token);
        //不允许当前登录的用户想查看别人的订单
        if(!phone.equals(orderInfo.getPhone())){
            throw new BusinessException(CommonCodeMsg.ILLEGAL_OPERATION);
        }

        return Result.success(orderInfo);
    }
}
