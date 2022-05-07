package com.example.plan.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "t_subject")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer owner_id;
    private String name;
    private String room;
    private String teacher;
    private String week_list;
    private Integer start;
    private Integer step;
    private Integer day;

}
