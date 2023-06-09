package ru.practicum.ewm.base.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.base.model.Category;

public interface CategoriesRepository extends JpaRepository<Category, Long> {
}
