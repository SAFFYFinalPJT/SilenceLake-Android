package com.ssafy.cafe.model.dao;

import java.util.List;
import java.util.Map;

import org.springframework.cache.annotation.Cacheable;

import com.ssafy.cafe.model.dto.Product;

public interface ProductDao {
    int insert(Product product);

    int update(Product product);

    int delete(Integer productId);

    Product select(Integer productId);

    List<Product> selectAll();
    List<Product> selectRecommendedProduct(String userId);
    // backend 관통 과정에서 추가됨.
    List<Map<String, Object>> selectWithComment(Integer productId);
}
