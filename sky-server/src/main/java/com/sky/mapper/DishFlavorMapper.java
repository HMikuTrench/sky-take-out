package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DishFlavorMapper {

    /**
     * 批量插入口味数据
     * 参数名要和 DishFlavorMapper.xml 里 collection="flavors" 对上，否则 MyBatis 只会提供 list/collection 两个键
     * @param flavors
     */
    void insertBatch(@Param("flavors") List<DishFlavor> flavors);
}
