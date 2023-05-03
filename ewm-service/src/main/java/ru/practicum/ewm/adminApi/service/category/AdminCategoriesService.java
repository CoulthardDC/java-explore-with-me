package ru.practicum.ewm.adminApi.service.category;

import ru.practicum.ewm.base.dto.Category.CategoryDto;
import ru.practicum.ewm.base.dto.Category.NewCategoryDto;

public interface AdminCategoriesService {
    CategoryDto create(NewCategoryDto newCategoryDto);

    void delete(Long catId);

    CategoryDto update(NewCategoryDto newCategoryDto, Long catId);
}
