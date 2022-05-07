package com.example.plan.dao;

import com.example.plan.entity.Subject;
import com.example.plan.entity.Todos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SubjectRepository extends JpaRepository<Subject, Integer> {


    @Query(value = "select * from t_subject where owner_id =:id", nativeQuery = true)
    List<Subject> getSubjectList(
            @Param("id") Integer owner_id);



    @Query(value = "DELETE FROM t_subject WHERE owner_id =:id", nativeQuery = true)
    @Modifying
    void deleteAllByUserId(
            @Param("id") Integer owner_id);


}
