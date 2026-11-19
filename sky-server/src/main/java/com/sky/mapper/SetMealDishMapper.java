package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetMealDishMapper {
    /**
     * 根据菜品Id查询套餐Id
     * @param dishIds
     * @return
     */
    //select setmeal_id from setmeal_dish where dish_id in (.....)
    List<Long> getSetMealIdsByDishIds(List<Long> dishIds);

    void insertBatch(List<SetmealDish> setmealDishList);

    void delete(List<Long> ids);

    @Select("select * from setmeal_dish where setmeal_id = #{setmealId}")
    List<SetmealDish> getSetmealDishById(Long setmealId);

    @Delete("delete from setmeal_dish where setmeal_id = #{setealId}")
    void deleteBySetmealId(Long setmealId);
}
