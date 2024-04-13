package cn.wolfcode.service.impl;

import cn.wolfcode.domain.Product;
import cn.wolfcode.mapper.ProductMapper;
import cn.wolfcode.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * Created by lanxw
 */
@Service
public class ProductServiceImpl implements IProductService {
    @Autowired
    private ProductMapper productMapper;


    @Override
    public List<Product> queryByPIds(List<Long> pids) {
        if(pids == null || pids.size()==0){
            return Collections.EMPTY_LIST;
        }
        List<Product> products = productMapper.queryProductByIds(pids);
        return products;
    }

    @Override
    public Product findByPid(Long pid) {
        return productMapper.findByPid(pid);
    }
}
