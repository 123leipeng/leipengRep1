package com.peng.controller.protal;

import com.github.pagehelper.PageInfo;
import com.peng.common.ServerResponse;
import com.peng.service.IProductService;
import com.peng.vo.ProductDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private IProductService iProductService;

    @RequestMapping("detail.do")
    @ResponseBody
    public ServerResponse<ProductDetailVo> detail(Integer productId) {

        return iProductService.getProductDetail(productId);
    }

    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse<PageInfo> list(@RequestParam(value = "keyword",required = false ,defaultValue ="" ) String keyword,@RequestParam(value = "categoryId",required = false) Integer categoryId ,@RequestParam(value = "pageNum",defaultValue = "1")int pageNum,@RequestParam(value = "pageSize",defaultValue = "10") int pageSize,@RequestParam(value = "orderBy") String orderBy){

        return iProductService.getProductByKeywordAndCategory(keyword, categoryId, pageNum, pageSize, orderBy);
    }
}
