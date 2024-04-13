package cn.wolfcode.web.controller;

import cn.wolfcode.common.web.Result;
import cn.wolfcode.domain.Product;
import cn.wolfcode.service.IProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by lanxw
 */
@RestController
@RequestMapping("/product")
@Slf4j
public class ProductController {
    @Autowired
    private IProductService productService;

    @RequestMapping("/queryByPIds")
    public Result<List<Product>> queryByPIds(@RequestParam List<Long> pids){
        List<Product> productList=productService.queryByPIds(pids);
        return Result.success(productList);
    }
    @RequestMapping("/findByPid")
    public Result<Product> findByPid(Long pid){
        Product product=productService.findByPid(pid);
        return Result.success(product);
    }
}
