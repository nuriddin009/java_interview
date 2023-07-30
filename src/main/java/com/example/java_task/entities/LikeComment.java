package com.example.java_task.entities;

import com.example.java_task.entities.enums.LikeEnum;
import com.example.java_task.entities.template.AbstractEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class LikeComment extends AbstractEntity {


    @ManyToOne(fetch = FetchType.EAGER)
    private Comment comment;

    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

    @Enumerated(EnumType.STRING)
    private LikeEnum likeEnum;
}
