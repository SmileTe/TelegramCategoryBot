package com.homework.repository;

import com.homework.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.TreeMap;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {
    @Query(value ="WITH RECURSIVE temp1 ( title,parent_title,PATH, LEVEL ) AS (SELECT T1.title, T1.parent_title, CAST (T1.title AS VARCHAR (50)) as PATH, 1  FROM category T1 WHERE T1.parent_title IS NULL union select T2.title, T2.parent_title,  CAST ( temp1.PATH ||'->'|| T2.title AS VARCHAR(50)) ,LEVEL + 1 FROM category T2 INNER JOIN temp1 ON( temp1.title= T2.parent_title) ) select * from temp1 ORDER BY PATH ", nativeQuery = true)
    List<Category> getViewTree();

    @Query(value ="WITH RECURSIVE temp1 ( title,parent_title,PATH, LEVEL ) AS (SELECT T1.title, T1.parent_title, CAST (T1.title AS VARCHAR (50)) as PATH, 1  FROM category T1 WHERE T1.title = :title union select T2.title, T2.parent_title,  CAST ( temp1.PATH ||'->'|| T2.title AS VARCHAR(50)) ,LEVEL + 1 FROM category T2 INNER JOIN temp1 ON( temp1.title= T2.parent_title) ) select * from temp1 ORDER BY PATH ", nativeQuery = true)
    List<Category> getViewTreeById(@Param("title") String name);
    Category getByName(String name);
}
