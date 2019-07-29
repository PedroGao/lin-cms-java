package com.lin.cms.demo.sleeve.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lin.cms.core.result.PageResult;
import com.lin.cms.demo.sleeve.dto.CategoryCreateOrUpdateDTO;
import com.lin.cms.demo.sleeve.model.Category;


/**
 * <p>
 * 服务类
 * </p>
 *
 * @author pedro
 * @since 2019-07-23
 */
public interface ICategoryService extends IService<Category> {

    void createCategory(CategoryCreateOrUpdateDTO dto);

    void updateCategory(CategoryCreateOrUpdateDTO dto, Long id);

    void deleteCategory(Long id);

    PageResult<Category> getCategoryByPage(Long count, Long page);
}