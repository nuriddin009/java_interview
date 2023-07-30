package com.example.java_task.entities;

import com.example.java_task.entities.template.AbstractEntity;
import jakarta.persistence.*;
import lombok.*;




@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Blog extends AbstractEntity {


    private String title;
    private String topic;


    @Column(columnDefinition = "TEXT")
    private String text;
    private Integer views = 0;

    private Boolean checked = false;
    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

}
