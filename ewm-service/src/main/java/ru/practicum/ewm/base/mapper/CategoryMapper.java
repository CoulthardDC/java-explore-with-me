package ru.practicum.ewm.base.mapper;

import org.mapstruct.Mapper;
import ru.practicum.ewm.base.dto.Category.CategoryDto;
import ru.practicum.ewm.base.dto.Category.NewCategoryDto;
import ru.practicum.ewm.base.model.Category;

@Mapper(componentModel = "spring")
public abstract class CategoryMapper {
    public abstract Category toCategory(NewCategoryDto newCategoryDto);

    public abstract CategoryDto toCategoryDto(Category category);


}
