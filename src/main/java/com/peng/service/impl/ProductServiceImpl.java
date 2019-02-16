package com.peng.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.peng.common.Const;
import com.peng.common.ResponseCode;
import com.peng.common.ServerResponse;
import com.peng.dao.CategoryMapper;
import com.peng.dao.ProductMapper;
import com.peng.pojo.Category;
import com.peng.pojo.Product;
import com.peng.service.ICategoryService;
import com.peng.service.IProductService;
import com.peng.util.DateUtil;
import com.peng.util.PropertiesUtil;
import com.peng.vo.ProductDetailVo;
import com.peng.vo.ProductListVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImpl implements IProductService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private ICategoryService iCategoryService;

    public ServerResponse saveOrUpdateProduct(Product product) {

        if (product != null) {

            if (StringUtils.isNotBlank(product.getSubImages())) {
                String[] subImageArray = product.getSubImages().split(",");
                if (subImageArray.length > 0) {
                    product.setMainImage(subImageArray[0]);
                }
                if (product.getId() != null) {
                    int rowCount = productMapper.updateByPrimaryKeySelective(product);
                    if (rowCount > 0) {
                        return ServerResponse.createBySuccess("更新产品成功");
                    } else {
                        return ServerResponse.createBySuccess("更新产品失败");
                    }


                } else {
                    int rowCount = productMapper.insert(product);
                    if (rowCount > 0) {
                        return ServerResponse.createBySuccess("新增产品成功");
                    } else {
                        return ServerResponse.createBySuccess("新增产品失败");
                    }

                }


            }
        }

        return ServerResponse.createByErrorMessage("添加或更新产品失败");
    }


    public ServerResponse setSaleStatus(Integer productId, Integer status) {
        if (productId == null || status == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());


        }
        Product product = new Product();
        product.setId(productId);
        product.setStatus(status);
        int rowCount = productMapper.updateByPrimaryKeySelective(product);
        if (rowCount > 0) {
            return ServerResponse.createBySuccessMessage("更改产品销售状态成功");
        }

        return ServerResponse.createByErrorMessage("更改产品销售状态失败");

    }

    public ServerResponse<ProductDetailVo> manageProductDetail(Integer productId) {

        if (productId == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());

        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if (product == null) {
            return ServerResponse.createByErrorMessage("产品已下架或者删除");

        }

        ProductDetailVo productDetailVo = assembleProductDetailVo(product);


        return ServerResponse.createBySuccess(productDetailVo);
    }


    private ProductDetailVo assembleProductDetailVo(Product product) {
        ProductDetailVo productDetailVo = new ProductDetailVo();
        productDetailVo.setId(product.getId());
        productDetailVo.setCategoryId(product.getCategoryId());
        productDetailVo.setDetail(product.getDetail());
        productDetailVo.setMainImage(product.getMainImage());
        productDetailVo.setPrice(product.getPrice());
        productDetailVo.setStatu(product.getStatus());
        productDetailVo.setSubTitle(product.getSubtitle());
        productDetailVo.setStock(product.getStock());
        productDetailVo.setName(product.getName());

        productDetailVo.setSubImage(product.getSubImages());

        productDetailVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix", "http://img.happymmall.com/"));

        Category category = categoryMapper.selectByPrimaryKey(product.getCategoryId());
        if (category == null) {
            productDetailVo.setParentCategoryId(0);
        } else {
            productDetailVo.setParentCategoryId(category.getParentId());
        }
        productDetailVo.setCreateTime(DateUtil.dateToStr(product.getCreateTime()));
        productDetailVo.setUpdateTime(DateUtil.dateToStr(product.getUpdateTime()));

        return productDetailVo;

    }

    public ServerResponse<PageInfo> getProductList(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        List<Product> productList = productMapper.selectList();

        List<ProductListVo> productListVoList = Lists.newArrayList();

        for (Product productItem : productList) {
            ProductListVo productListVo = assembleProductListlVo(productItem);
            productListVoList.add(productListVo);
        }

        PageInfo pageResult = new PageInfo(productList);
        pageResult.setList(productListVoList);
        return ServerResponse.createBySuccess(pageResult);

    }


    private ProductListVo assembleProductListlVo(Product product) {
        ProductListVo productListVo = new ProductListVo();
        productListVo.setCategoryId(product.getCategoryId());
        productListVo.setId(product.getId());
        productListVo.setMainImage(product.getMainImage());
        productListVo.setName(product.getName());
        productListVo.setPrice(product.getPrice());
        productListVo.setStatus(product.getStatus());
        productListVo.setSubtitle(product.getSubtitle());
        productListVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix", "http://img.happymmall.com/"));

        return productListVo;


    }


    public ServerResponse<PageInfo> searchProduct(String productName, Integer productId, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        if (StringUtils.isNotBlank(productName)) {
            productName = new StringBuilder().append("%").append(productName).append("%").toString();
        }

        List<Product> productList = productMapper.selectByNameAndProductId(productName, productId);

        List<ProductListVo> productListVoList = Lists.newArrayList();

        for (Product productItem : productList) {
            ProductListVo productListVo = assembleProductListlVo(productItem);
            productListVoList.add(productListVo);
        }

        PageInfo pageResult = new PageInfo(productList);
        pageResult.setList(productListVoList);
        return ServerResponse.createBySuccess(pageResult);
    }

    public ServerResponse<ProductDetailVo> getProductDetail(Integer productId) {


        if (productId == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());

        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if (product == null) {
            return ServerResponse.createByErrorMessage("产品已下架或者删除");

        }

        if (product.getStatus() != Const.ProductStatusEnum.ON_SALE.getCode()) {
            return ServerResponse.createByErrorMessage("产品已下架");
        }

        ProductDetailVo productDetailVo = assembleProductDetailVo(product);


        return ServerResponse.createBySuccess(productDetailVo);
    }

    public ServerResponse<PageInfo> getProductByKeywordAndCategory(String keyword, Integer categoryId, int pageNum, int pageSize, String orderBy) {

        List<Integer> categoryList = new ArrayList();
        if (StringUtils.isBlank(keyword) && categoryId == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());


        }

        if (categoryId != null) {
            Category category = categoryMapper.selectByPrimaryKey(categoryId);


            if (category == null && StringUtils.isBlank(keyword)) {

                PageHelper.startPage(pageNum, pageSize);
                List<ProductListVo> productListVoList = Lists.newArrayList();

                PageInfo pageInfo = new PageInfo(productListVoList);
                return ServerResponse.createBySuccess(pageInfo);
            }

            categoryList = iCategoryService.selectCategoryAndChildrenById(category.getId()).getData();

        }
        if (StringUtils.isNotBlank(keyword)) {
            keyword = new StringBuilder().append("%").append(keyword).append("%").toString();

        }
        PageHelper.startPage(pageNum, pageSize);
        if (StringUtils.isNotBlank(orderBy)) {
            String[] orderByArray = orderBy.split("_");
            PageHelper.orderBy(orderByArray[0] + " " + orderByArray[1]);
        }
        List<Product> productList = productMapper.selectByNameAndCategoryIds(StringUtils.isBlank(keyword) ? null : keyword, categoryList.size() == 0 ? null : categoryList);

        List<ProductListVo> productListVoList = Lists.newArrayList();
        for (Product product: productList) {

            ProductListVo productListVo = assembleProductListlVo(product);
            productListVoList.add(productListVo);
        }
        PageInfo pageInfo = new PageInfo(productList);
        pageInfo.setList(productListVoList);
        return ServerResponse.createBySuccess(pageInfo);

    }
}

