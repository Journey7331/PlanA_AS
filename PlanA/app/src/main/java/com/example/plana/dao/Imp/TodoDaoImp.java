package com.example.plana.dao.Imp;

import com.example.plana.bean.Todos;
import com.example.plana.dao.TodoDao;
import com.example.plana.database.MyDatabaseHelper;

import java.util.ArrayList;

/**
 * @program: PlanA
 * @description:
 */
public class TodoDaoImp implements TodoDao {

    MyDatabaseHelper db = null;

    @Override
    public void insertTodo(Todos todo) {

    }

    @Override
    public void deleteTodoById(int id) {

    }

    @Override
    public void updateTodoById(int id, Todos todo) {

    }

    @Override
    public void updateTodoDoneStatus(int id, boolean status) {

    }

    @Override
    public ArrayList<Todos> queryAllTodos() {
        return null;
    }

}
