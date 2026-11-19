package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.SetmealVO;

import java.util.List;

public interface SetMealService {
    void insert(SetmealDTO setmealDTO);

    PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    void delete(List<Long> ids);

    SetmealVO getSetmealById(Long id);

    void updateSetmeal(SetmealDTO setmealDTO);

    void setStatus(Long id, Integer status);
}
