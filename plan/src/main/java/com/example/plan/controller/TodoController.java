package com.example.plan.controller;


import com.example.plan.dao.TodosRepository;
import com.example.plan.entity.Todos;
import com.example.plan.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.util.List;

@RestController
@Transactional
public class TodoController {

    private final TodosRepository todosRepository;

    @Autowired
    public TodoController(TodosRepository todosRepository) {
        this.todosRepository = todosRepository;
    }

    @RequestMapping("/getTodoList.do")
    public String getTodoList(@RequestBody User user) {
        System.out.println("*********** getTodoList ***********");
        System.out.println("user_id: " + user.getId());
        List<Todos> list = todosRepository.getTodoList(user.getId());
        return JSONController.jsonDataPush(list.size() > 0, list);
    }

    @RequestMapping("/deleteTodoList.do")
    public String deleteTodoList(@RequestBody User user){
        System.out.println("*********** deleteTodoList ***********");
        System.out.println("user_id: " + user.getId());
        todosRepository.deleteAllByUserId(user.getId());
        List<Todos> list = todosRepository.getTodoList(user.getId());
        return JSONController.jsonTemplate(list.size() == 0);
    }

    @RequestMapping("/updateTodoList.do")
    public String updateTodoList(
            @RequestParam(value = "owner_id") String owner_id,
            @RequestParam(value = "content") String content,
            @RequestParam(value = "done") String done,
            @RequestParam(value = "memo") String memo,
            @RequestParam(value = "due_date") String due_date,
            @RequestParam(value = "due_time") String due_time,
            @RequestParam(value = "importance") String importance
            ) {
        System.out.println("********* updateTodoList **********");
        Todos todo = Todos.builder()
                .owner_id(Integer.parseInt(owner_id))
                .content(content)
                .done("true".equals(done) ? 1 : 0)
                .memo(memo)
                .due_date(due_date)
                .due_time(due_time)
                .importance(Float.parseFloat(importance))
                .build();

        System.out.println(todo);
        todosRepository.save(todo);
        return JSONController.jsonTemplate(true);
    }






}
