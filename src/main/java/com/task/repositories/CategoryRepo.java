package com.task.repositories;

import com.task.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepo extends JpaRepository<Category, Long> {

    Optional<Category> findByCategoryName(@Param("categoryName") String categoryName);
}
