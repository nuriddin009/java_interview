package com.example.java_task.entities;

import com.example.java_task.entities.enums.RoleName;
import com.example.java_task.entities.template.AbstractEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;



@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Role extends AbstractEntity implements GrantedAuthority {

    @Enumerated(EnumType.STRING)
    private RoleName roleName;

    @Override
    public String getAuthority() {
        return roleName.name();
    }
}
