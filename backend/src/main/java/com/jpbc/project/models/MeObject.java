package com.jpbc.project.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MeObject {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private Boolean isAdmin;
}
