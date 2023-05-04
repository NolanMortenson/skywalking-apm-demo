package com.jpbc.project.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
@Data
@NoArgsConstructor
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String teamName;

    @ManyToOne // Fetch type stays EAGER or else it crashes (why?)
    @JoinColumn(name = "admin_id")
    private TeamMember admin;


    @OneToOne
    @JoinColumn(name = "project_id", referencedColumnName = "id")
    private Project project;

    public Team(String teamName) {
        this.teamName = teamName;
    }
}
