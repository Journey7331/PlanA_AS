package com.example.plan.dao;

import com.example.plan.entity.Todos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TodosRepository extends JpaRepository<Todos, Integer> {


    @Query(value = "select * from t_todo", nativeQuery = true)
    List<Todos> getAll();


    @Query(value = "select * from t_todo where owner_id =:id", nativeQuery = true)
    List<Todos> getTodoList(
            @Param("id") Integer owner_id);

    @Query(value = "DELETE FROM t_todo WHERE owner_id =:id", nativeQuery = true)
    @Modifying
    void deleteAllByUserId(
            @Param("id") Integer owner_id);


}
