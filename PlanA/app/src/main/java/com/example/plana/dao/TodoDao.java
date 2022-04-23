package com.example.plana.dao;

import com.example.plana.bean.Todos;

import java.util.ArrayList;

public interface TodoDao {

    public void insertTodo(Todos todo);

    public void deleteTodoById(int id);

    public void updateTodoById(int id, Todos todo);

    public void updateTodoDoneStatus(int id, boolean status);

    public ArrayList<Todos> queryAllTodos();

}
