package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

import java.util.List;


public interface DishSerivce {
    void save(DishDTO dishDTO);

    PageResult page(DishPageQueryDTO dishPageQueryDTO);


    void delete(List<Long> ids);

    void modify(DishDTO dishDTO);

    DishVO getById(Long id);

    List<Dish> getByCategoryId(Long categoryId);
}
