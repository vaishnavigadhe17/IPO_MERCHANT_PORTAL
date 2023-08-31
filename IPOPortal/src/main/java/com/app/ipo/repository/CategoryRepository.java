package com.app.ipo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.app.ipo.model.CategoryEntity;

@Repository
public interface CategoryRepository extends JpaRepository < CategoryEntity, Long >
{
	@Query("SELECT m FROM CategoryEntity m order by m.recordedDate asc")
	public List<CategoryEntity> findAllCategory();

	@Query("SELECT a FROM CategoryEntity a WHERE a.categoryId =:categoryId")
	public Optional<CategoryEntity> findByCategoryId(@Param("categoryId") Long categoryId);
}
