package com.peng.vo;

import java.math.BigDecimal;
import java.util.List;

public class CartVo {
    private List<CartProductVo> cartProductVoList;

    private BigDecimal cartTatalPrice;

    private Boolean allChecked;

    private String imageHost;

    public List<CartProductVo> getCartProductVoList() {
        return cartProductVoList;
    }

    public void setCartProductVoList(List<CartProductVo> cartProductVoList) {
        this.cartProductVoList = cartProductVoList;
    }

    public BigDecimal getCartTatalPrice() {
        return cartTatalPrice;
    }

    public void setCartTatalPrice(BigDecimal cartTatalPrice) {
        this.cartTatalPrice = cartTatalPrice;
    }

    public Boolean getAllChecked() {
        return allChecked;
    }

    public void setAllChecked(Boolean allChecked) {
        this.allChecked = allChecked;
    }

    public String getImageHost() {
        return imageHost;
    }

    public void setImageHost(String imageHost) {
        this.imageHost = imageHost;
    }
}
