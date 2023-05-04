package com.jpbc.project;

import com.jpbc.project.models.Role;
import com.jpbc.project.models.TeamMember;
import com.jpbc.project.repos.ProjectRepo;
import com.jpbc.project.repos.TeamMemberRepo;
import com.jpbc.project.repos.TeamRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class ProjectApplication {


    public static void main(String[] args) {
        SpringApplication.run(ProjectApplication.class, args);
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    CommandLineRunner commandLineRunner(TeamMemberRepo teamMemberRepo, ProjectRepo projectRepo, TeamRepo teamRepo) {
        return args -> {
            TeamMember admin = new TeamMember(
                    "Bob",
                    "Bobberson",
                    "bob@bah.com",
                    "$2a$10$IehYSK1jjjyl3Rsccb0Ul.Iv3SODQ7FYVaT9/NeaBwkiiFEPWyDKW",
                    null,
                    Role.ADMIN,
                    null
            );
            teamMemberRepo.save(admin);

            TeamMember user1 = new TeamMember(
                    "Joe",
                    "Andro",
                    "jandro@bah.com",
                    "$2a$10$IehYSK1jjjyl3Rsccb0Ul.Iv3SODQ7FYVaT9/NeaBwkiiFEPWyDKW",
                    null,
                    Role.USER,
                    null);
            teamMemberRepo.save(user1);

            TeamMember user2 = new TeamMember(
                    "Jane",
                    "Warret",
                    "jwarret@bah.com",
                    "$2a$10$IehYSK1jjjyl3Rsccb0Ul.Iv3SODQ7FYVaT9/NeaBwkiiFEPWyDKW",
                    null,
                    Role.USER,
                    null);
            teamMemberRepo.save(user2);

            TeamMember user3 = new TeamMember(
                    "Jessie",
                    "Rush",
                    "jrush@bah.com",
                    "$2a$10$IehYSK1jjjyl3Rsccb0Ul.Iv3SODQ7FYVaT9/NeaBwkiiFEPWyDKW",
                    null,
                    Role.USER,
                    null);
            teamMemberRepo.save(user3);

            TeamMember user4 = new TeamMember(
                    "Noah",
                    "Morris",
                    "nmorris@bah.com",
                    "$2a$10$IehYSK1jjjyl3Rsccb0Ul.Iv3SODQ7FYVaT9/NeaBwkiiFEPWyDKW",
                    null,
                    Role.USER,
                    null);
            teamMemberRepo.save(user4);

            TeamMember user5 = new TeamMember(
                    "Kelly",
                    "Owen",
                    "kowen@bah.com",
                    "$2a$10$IehYSK1jjjyl3Rsccb0Ul.Iv3SODQ7FYVaT9/NeaBwkiiFEPWyDKW",
                    null,
                    Role.USER,
                    null);
            teamMemberRepo.save(user5);

            TeamMember user6 = new TeamMember(
                    "Jake",
                    "Ryan",
                    "jryan@bah.com",
                    "$2a$10$IehYSK1jjjyl3Rsccb0Ul.Iv3SODQ7FYVaT9/NeaBwkiiFEPWyDKW",
                    null,
                    Role.USER,
                    null);
            teamMemberRepo.save(user6);

            TeamMember user7 = new TeamMember(
                    "Vanessa",
                    "strider",
                    "vstrider@bah.com",
                    "$2a$10$IehYSK1jjjyl3Rsccb0Ul.Iv3SODQ7FYVaT9/NeaBwkiiFEPWyDKW",
                    null,
                    Role.USER,
                    null);
            teamMemberRepo.save(user7);

            TeamMember user8 = new TeamMember(
                    "Tim",
                    "woodman",
                    "twoodman@bah.com",
                    "$2a$10$IehYSK1jjjyl3Rsccb0Ul.Iv3SODQ7FYVaT9/NeaBwkiiFEPWyDKW",
                    null,
                    Role.USER,
                    null);
            teamMemberRepo.save(user8);

        };
    }


}
