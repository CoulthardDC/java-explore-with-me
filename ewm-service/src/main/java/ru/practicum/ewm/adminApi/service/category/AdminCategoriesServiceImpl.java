package ru.practicum.ewm.adminApi.service.category;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.base.mapper.CategoryMapper;
import ru.practicum.ewm.base.repository.CategoriesRepository;
import ru.practicum.ewm.base.repository.EventRepository;
import ru.practicum.ewm.base.dto.category.CategoryDto;
import ru.practicum.ewm.base.dto.category.NewCategoryDto;
import ru.practicum.ewm.base.exception.ConditionsNotMetException;
import ru.practicum.ewm.base.exception.ConflictException;
import ru.practicum.ewm.base.exception.NotFoundException;
import ru.practicum.ewm.base.model.Category;
import ru.practicum.ewm.base.util.UtilMergeProperty;

@Service
@Slf4j
@Transactional(readOnly = true)
public class AdminCategoriesServiceImpl implements AdminCategoriesService {
    private final CategoriesRepository categoriesRepository;

    private final EventRepository eventRepository;

    private final CategoryMapper categoryMapper;

    @Autowired
    public AdminCategoriesServiceImpl(CategoriesRepository categoriesRepository,
                                      EventRepository eventRepository,
                                      CategoryMapper categoryMapper) {
        this.categoriesRepository = categoriesRepository;
        this.eventRepository = eventRepository;
        this.categoryMapper = categoryMapper;
    }

    @Transactional
    @Override
    public CategoryDto create(NewCategoryDto newCategoryDto) {
        Category category = categoryMapper.toCategory(newCategoryDto);
        try {
            category = categoriesRepository.save(category);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException(e.getMessage(), e);
        }
        log.info("Категория добавлена: {}", category.getName());
        return categoryMapper.toCategoryDto(category);
    }

    @Transactional
    @Override
    public void delete(Long catId) {
        if (eventRepository.existsByCategory(get(catId))) {
            throw new ConditionsNotMetException("The category is not empty");
        } else if (categoriesRepository.existsById(catId)) {
            log.info("Deleted category with id = {}", catId);
            categoriesRepository.deleteById(catId);
        }
    }

    @Transactional
    @Override
    public CategoryDto update(NewCategoryDto newCategoryDto, Long catId) {
        Category categoryUpdate = categoryMapper.toCategory(newCategoryDto);
        Category categoryTarget = get(catId);

        try {
            UtilMergeProperty.copyProperties(categoryUpdate, categoryTarget);
            categoriesRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException(e.getMessage(), e);
        }
        log.info("Update category: {}", categoryTarget.getName());
        return categoryMapper.toCategoryDto(categoryTarget);
    }

    private Category get(Long id) {
        final Category category = categoriesRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Category not found with id = %s", id)));
        log.info("Get category: {}", category.getName());
        return category;
    }

}
