package com.example.plan.dao;

import com.example.plan.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {


    @Query(value = "select * from t_user where phone =:phone and password =:password", nativeQuery = true)
    List<User> login(
            @Param("phone") String phone,
            @Param("password") String password);


    @Query(value = "select * from t_user where phone =:phone", nativeQuery = true)
    List<User> checkPhoneExist(@Param("phone") String phone);

}
