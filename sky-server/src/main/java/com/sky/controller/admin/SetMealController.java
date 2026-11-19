package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetMealService;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/setmeal")
@Slf4j
@Api(tags = "套裁相关接口")
public class SetMealController {

    @Autowired
    private SetMealService setMealService;

    /**
     * 新增套餐
     * @param setmealDTO
     * @return
     */
    @PostMapping
    @ApiOperation("新增套餐")
    public Result saveSetMeal(@RequestBody SetmealDTO setmealDTO){
        log.info("新增套餐：{}", setmealDTO);
        setMealService.insert(setmealDTO);
        return Result.success();
    }

    /**
     * 套餐分页查询
     * @param setmealPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("套餐分页查询")
    public Result<PageResult> getSetmeal(SetmealPageQueryDTO setmealPageQueryDTO){
        log.info("套餐分页查询：{}", setmealPageQueryDTO);
        PageResult pageResult = setMealService.pageQuery(setmealPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 删除套餐
     * @param ids
     * @return
     */
    @DeleteMapping
    @ApiOperation("删除套餐")
    public Result delete(@RequestParam List<Long> ids){
        log.info("删除套餐ID:{}", ids);
        setMealService.delete(ids);
        return Result.success();
    }

    /**
     * 查询套餐
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("查询套餐")
    public Result<SetmealVO> getSetmeal(@PathVariable Long id){
        log.info("查询套餐Id:{}", id);
        SetmealVO setmealVO = setMealService.getSetmealById(id);
        return Result.success(setmealVO);
    }

    /**
     * 更新套餐
     * @param setmealDTO
     * @return
     */
    @PutMapping
    @ApiOperation("更新套餐")
    public Result updateSetmeal(@RequestBody SetmealDTO setmealDTO){
        log.info("套餐变更后：{}", setmealDTO);
        setMealService.updateSetmeal(setmealDTO);
        return Result.success();
    }

    @PostMapping("/status/{status}")
    @ApiOperation("启售/停售套餐")
    public Result setStatus(@PathVariable Integer status,@RequestParam Long id){
        log.info("套餐ID:{}, 要设置的状态：{}", id, status);
        setMealService.setStatus(id, status);
        return Result.success();
    }
}
