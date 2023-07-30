package com.example.java_task.entities;

import com.example.java_task.entities.template.AbstractEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.*;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Comment extends AbstractEntity {

    @Column(length = 400)
    private String description;
    private Boolean checked;
    private Integer likes;
    private Integer dislikes;
    @ManyToOne(fetch = FetchType.EAGER)
    private Blog blog;

    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

}
