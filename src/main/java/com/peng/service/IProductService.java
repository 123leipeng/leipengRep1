package com.peng.service;

import com.github.pagehelper.PageInfo;
import com.peng.common.ServerResponse;
import com.peng.pojo.Product;
import com.peng.vo.ProductDetailVo;

public interface IProductService {
    ServerResponse saveOrUpdateProduct(Product product);

    ServerResponse setSaleStatus(Integer productId, Integer status);

    ServerResponse<ProductDetailVo> manageProductDetail(Integer productId);

    ServerResponse<PageInfo> getProductList(int pageNum, int pageSize);

    ServerResponse<PageInfo> searchProduct(String productName, Integer productId, int pageNum, int pageSize);

    ServerResponse<ProductDetailVo> getProductDetail(Integer productId);

    ServerResponse<PageInfo> getProductByKeywordAndCategory(String keyword, Integer categoryId, int pageNum, int pageSize, String orderBy) ;

    }
