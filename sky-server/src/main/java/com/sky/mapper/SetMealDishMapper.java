package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SetMealDishMapper {
    /**
     * 根据菜品id查询对应的套餐ID
     * @param dishIds
     * @return
     */
    List<Long> getSetmealDishIds(List<Long> dishIds);
}
