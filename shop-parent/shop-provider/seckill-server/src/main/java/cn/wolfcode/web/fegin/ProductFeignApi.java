package cn.wolfcode.web.fegin;

import cn.wolfcode.common.web.Result;
import cn.wolfcode.domain.Product;
import cn.wolfcode.web.fegin.fallback.ProductFeignFillback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "product-service",fallback = ProductFeignFillback.class)
public interface ProductFeignApi {

    @RequestMapping("/product/queryByPIds")
    Result<List<Product>> queryByPids(@RequestParam("pids") List<Long> pids);
}