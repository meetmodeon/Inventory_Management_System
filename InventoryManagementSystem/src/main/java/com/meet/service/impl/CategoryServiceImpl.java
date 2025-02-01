package com.meet.service.impl;

import com.meet.dto.CategoryDto;
import com.meet.dto.Response;
import com.meet.entity.Category;
import com.meet.exceptions.NotFoundException;
import com.meet.repository.CategoryRepository;
import com.meet.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    @Override
    public Response createCategory(CategoryDto categoryDto) {
       Category categoryToSave = modelMapper.map(categoryDto,Category.class);
       categoryRepository.save(categoryToSave);

       return Response.builder()
               .status(200)
               .message("Category created successfully")
               .build();
    }

    @Override
    public Response getAllCategories() {
        List<Category> categories= categoryRepository.findAll(Sort.by(Sort.Direction.DESC,"id"));
        List<CategoryDto> categoryDtos=modelMapper.map(categories,new TypeToken<List<CategoryDto>>(){}.getType());

       return Response.builder()
               .status(200)
               .message("success")
               .categories(categoryDtos)
               .build();
    }

    @Override
    public Response getCategoryById(Long id) {
       Category category=categoryRepository.findById(id).orElseThrow(()-> new NotFoundException("Category Not Found"));
       CategoryDto categoryDto= modelMapper.map(category,CategoryDto.class);


       return Response.builder()
               .status(200)
               .message("success")
               .category(categoryDto)
               .build();
    }

    @Override
    public Response updateCategory(Long id, CategoryDto categoryDto) {

        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(()->new NotFoundException("Category Not Found"));

        existingCategory.setName(categoryDto.getName());
        categoryRepository.save(existingCategory);

        return Response.builder()
                .status(200)
                .message("Category Successfully Updated")
                .build();
    }

    @Override
    public Response deleteCategory(Long id) {

        categoryRepository.findById(id)
                .orElseThrow(()-> new NotFoundException("Category Not Found"));

        categoryRepository.deleteById(id);

        return Response.builder()
                .status(200)
                .message("Category Successfully Deleted")
                .build();
    }
}
