package com.example.plan.controller;


import com.example.plan.dao.SubjectRepository;
import com.example.plan.entity.Subject;
import com.example.plan.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.util.List;

@RestController
@Transactional
public class SubjectController {

    final SubjectRepository subjectRepository;

    @Autowired
    public SubjectController(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    @RequestMapping("/getSubjectList.do")
    public String getSubjectList(@RequestBody User user) {
        System.out.println("*********** getSubjectList ***********");
        System.out.println("user_id: " + user.getId());
        List<Subject> list = subjectRepository.getSubjectList(user.getId());
        return JSONController.jsonDataPush(list.size() > 0, list);
    }


    @RequestMapping("/deleteSubjectList.do")
    public String deleteSubjectList(@RequestBody User user){
        System.out.println("*********** deleteSubjectList ***********");
        System.out.println("user_id: " + user.getId());
        subjectRepository.deleteAllByUserId(user.getId());
        List<Subject> list = subjectRepository.getSubjectList(user.getId());
        return JSONController.jsonTemplate(list.size() == 0);
    }

    @RequestMapping("/updateSubjectList.do")
    public String updateSubjectList(
            @RequestParam(value = "owner_id") String owner_id,
            @RequestParam(value = "name") String name,
            @RequestParam(value = "room") String room,
            @RequestParam(value = "teacher") String teacher,
            @RequestParam(value = "week_list") String week_list,
            @RequestParam(value = "start") String start,
            @RequestParam(value = "step") String step,
            @RequestParam(value = "day") String day
    ) {
        System.out.println("********* updateSubjectList **********");
        Subject subject = Subject.builder()
                .owner_id(Integer.parseInt(owner_id))
                .name(name)
                .room(room)
                .teacher(teacher)
                .week_list(week_list)
                .start(Integer.parseInt(start))
                .step(Integer.parseInt(step))
                .day(Integer.parseInt(day))
                .build();

        System.out.println(subject);
        subjectRepository.save(subject);
        return JSONController.jsonTemplate(true);
    }

}
