package cn.wolfcode.service.impl;

import cn.wolfcode.common.exception.BusinessException;
import cn.wolfcode.common.web.Result;
import cn.wolfcode.domain.Product;
import cn.wolfcode.domain.SeckillProduct;
import cn.wolfcode.domain.SeckillProductVo;
import cn.wolfcode.mapper.SeckillProductMapper;
import cn.wolfcode.service.ISeckillProductService;
import cn.wolfcode.web.fegin.ProductFeignApi;
import cn.wolfcode.web.msg.SeckillCodeMsg;
import com.fasterxml.jackson.databind.util.BeanUtil;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.net.ProtocolFamily;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by lanxw
 */
@Service
public class SeckillProductServiceImpl implements ISeckillProductService {
    @Autowired
    private SeckillProductMapper seckillProductMapper;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private RocketMQTemplate rocketMQTemplate;
    @Autowired
    private ProductFeignApi productFeignApi;

    @Override
    public List<SeckillProductVo> queryByTime(Integer time) {
        // 1.根据场次查询当天的秒杀商品数据
        List<SeckillProduct> seckillProducts = seckillProductMapper.queryCurrentlySeckillProduct(time);
// 2.获取到商品的ID集合

        List<Long> productIds = seckillProducts.stream().map(SeckillProduct::getProductId).collect(Collectors.toList());
// 3.把商品ID集合作为参数远程调用商品服务获取商品集合
        Result<List<Product>> result = productFeignApi.queryByPids(productIds);
        if(result==null || result.hasError()){
            throw new BusinessException(SeckillCodeMsg.PRODUCT_SERVER_ERROR);
        }

        List<Product> products=result.getData();//远程调用 TODO

        Map<Long, Product> productMap = products.stream().collect((Collectors.toMap(Product::getId, Function.identity())));
// 4.将商品集合和秒杀商品集合进行聚合封装成Vo
        // 5.返回

        return seckillProducts.stream().map(sp->{
            SeckillProductVo vo = new SeckillProductVo();
            Product product=productMap.get(sp.getProductId());

            BeanUtils.copyProperties(product,vo );
            BeanUtils.copyProperties(sp,vo);

            return vo;

        }).collect(Collectors.toList()) ;



    }
    @Override
    public SeckillProductVo find(Integer time, Long seckillId) {
        SeckillProduct seckillProduct = seckillProductMapper.find(seckillId);
        Result<Product> result = productFeignApi.findByPid(seckillProduct.getProductId());
        if(result==null || result.hasError()){
            throw new BusinessException(SeckillCodeMsg.PRODUCT_SERVER_ERROR);
        }
        Product product=result.getData();
        SeckillProductVo vo=new SeckillProductVo();
        //注意顺序 商品在前 秒杀在后
        BeanUtils.copyProperties(product,vo);
        BeanUtils.copyProperties(seckillProduct,vo);
        return vo;
    }

    @Override
    public void desrStockCount(Long seckillId) {


        seckillProductMapper.decrStock(seckillId);


    }
}
