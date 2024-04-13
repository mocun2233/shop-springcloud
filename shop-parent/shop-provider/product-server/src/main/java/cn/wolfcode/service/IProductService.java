package cn.wolfcode.service;

import cn.wolfcode.domain.Product;

import java.util.List;

/**
 * Created by lanxw
 */
public interface IProductService {

    /**
     * 根据商品ID查询对应商品
     * @param pids
     * @return
     */
    List<Product> queryByPIds(List<Long> pids);
}
