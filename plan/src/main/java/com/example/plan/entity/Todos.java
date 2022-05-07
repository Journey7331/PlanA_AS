package com.example.plan.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "t_todo")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Todos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer owner_id;
    private String content;
    private String memo;
    private String due_date;
    private String due_time;
    private Float importance;
    // 0: false
    // 1: true
    // 2: deleted
    private Integer done;

}
