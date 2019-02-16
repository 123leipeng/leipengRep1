package com.peng.dao;

import com.peng.pojo.Shipping;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShippingMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Shipping record);

    int insertSelective(Shipping record);

    Shipping selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Shipping record);

    int updateByPrimaryKey(Shipping record);

    int deleteByShippingIdAndUserId(@Param("userId") Integer userId,@Param("shippingId")  Integer shippingId);

    int updateByShipping(Shipping record);

    Shipping selectByShippingIdUserId(@Param("shippingId")Integer shippingId,@Param("userId") Integer userId);

    List<Shipping> selecteByUserId(@Param("userId")Integer userId);
}