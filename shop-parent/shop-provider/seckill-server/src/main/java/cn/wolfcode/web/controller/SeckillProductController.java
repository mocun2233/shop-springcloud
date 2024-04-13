package cn.wolfcode.web.controller;

import cn.wolfcode.common.web.Result;
import cn.wolfcode.domain.SeckillProduct;
import cn.wolfcode.domain.SeckillProductVo;
import cn.wolfcode.service.ISeckillProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by lanxw
 * 秒杀商品信息查询
 */
@RestController
@RequestMapping("/seckillProduct")
@Slf4j
public class SeckillProductController {
    @Autowired
    private ISeckillProductService seckillProductService;

    /**
     * 根据秒杀时间获取首页商品
     * @param time
     * @return
     */
    @RequestMapping("/queryByTime")
    public Result<List<SeckillProductVo>> queryByTime(Integer time){
        List<SeckillProductVo> productVoList=seckillProductService.queryByTime(time);
        return Result.success(productVoList );
    }

    /**
     * 根据id查找商品详情页信息
     * @param time
     * @param seckillId
     * @return
     */
    @RequestMapping("/find")
    public Result<SeckillProductVo> find(Integer time,Long seckillId){
        SeckillProductVo vp=seckillProductService.find(time,seckillId);
        return Result.success(vp);
    }


}
