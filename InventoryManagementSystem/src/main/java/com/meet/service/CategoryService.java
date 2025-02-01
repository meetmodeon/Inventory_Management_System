package com.meet.service;

import com.meet.dto.CategoryDto;
import com.meet.dto.Response;

public interface CategoryService {

    Response createCategory(CategoryDto categoryDto);
    Response getAllCategories();
    Response getCategoryById(Long id);
    Response updateCategory(Long id,CategoryDto categoryDto);
    Response deleteCategory(Long id);
}
