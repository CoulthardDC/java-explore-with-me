package ru.practicum.ewm.publicApi.service.category;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.base.mapper.CategoryMapper;
import ru.practicum.ewm.base.repository.CategoriesRepository;
import ru.practicum.ewm.base.dto.category.CategoryDto;
import ru.practicum.ewm.base.exception.NotFoundException;
import ru.practicum.ewm.base.model.Category;
import ru.practicum.ewm.base.util.page.MyPageRequest;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(readOnly = true)
public class PublicCategoriesServiceImpl implements PublicCategoriesService {

    private final CategoriesRepository categoriesRepository;

    private final CategoryMapper categoryMapper;

    @Autowired
    public PublicCategoriesServiceImpl(CategoriesRepository categoriesRepository,
                                       CategoryMapper categoryMapper) {
        this.categoriesRepository = categoriesRepository;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public List<CategoryDto> getAll(int from, int size) {
        MyPageRequest pageable = new MyPageRequest(from, size,
                Sort.by(Sort.Direction.ASC, "id"));
        List<Category> categories = categoriesRepository.findAll(pageable).toList();
        log.info("Получен список всех категорий");
        return categories.stream()
                .map(categoryMapper::toCategoryDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto get(Long catId) {
        final Category category = categoriesRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException(String.format("Category not found with id = %s", catId)));
        log.info("Get Category: {}", category.getName());
        return categoryMapper.toCategoryDto(category);
    }
}
