package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.mapper.SetMealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetMealService;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SetMealServiceImpl implements SetMealService {

    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private SetMealDishMapper setMealDishMapper;

    @Override
    @Transactional
    public void insert(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setmealMapper.insert(setmeal);
        Long setmealId = setmeal.getId();
        List<SetmealDish> setmealDishList = setmealDTO.getSetmealDishes();
        setmealDishList.forEach(setmealDish -> {
            setmealDish.setSetmealId(setmealId);
        });
        setMealDishMapper.insertBatch(setmealDishList);
    }

    @Override
    public PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageHelper.startPage(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());
        Page<SetmealVO> setmealVOPage = setmealMapper.pageQuery(setmealPageQueryDTO);
        return new PageResult(setmealVOPage.getTotal(), setmealVOPage.getResult());
    }

    @Override
    @Transactional
    public void delete(List<Long> ids) {
        setmealMapper.delete(ids);
        setMealDishMapper.delete(ids);
    }

    @Override
    public SetmealVO getSetmealById(Long id) {
        Setmeal setmeal = setmealMapper.getSetmealById(id);
        List<SetmealDish> setmealDishes = setMealDishMapper.getSetmealDishById(id);
        SetmealVO setmealVO = new SetmealVO();
        BeanUtils.copyProperties(setmeal, setmealVO);
        setmealVO.setSetmealDishes(setmealDishes);
        return setmealVO;
    }

    @Override
    @Transactional
    public void updateSetmeal(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setmealMapper.updateSetmeal(setmeal);
        Long setmealId = setmeal.getId();
        setMealDishMapper.deleteBySetmealId(setmealId);
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        setmealDishes.forEach(setmealDish -> {
            setmealDish.setSetmealId(setmealId);
        });
        setMealDishMapper.insertBatch(setmealDishes);
    }

    @Override
    public void setStatus(Long setmealId, Integer status) {
        setmealMapper.setStatus(setmealId, status);
    }
}
