package com.jpbc.project.controllers;

import com.jpbc.project.models.MeObject;
import com.jpbc.project.models.TeamMember;
import com.jpbc.project.repos.TeamMemberRepo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

import static com.jpbc.project.security.SecurityConfig.ADMIN_AUTHORITY;
import static com.jpbc.project.security.SecurityConfig.USER_AUTHORITY;

@RestController
@CrossOrigin
@RequestMapping("/api/v1")
@AllArgsConstructor
@Slf4j
public class AuthController {
    private final TeamMemberRepo teamMemberRepo;

    @GetMapping("/me")
    public MeObject getUserAuth() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getPrincipal().toString();
        String role = auth.getAuthorities().toArray()[0].toString();

        MeObject me = new MeObject();

        if (Objects.equals(role, USER_AUTHORITY)) {
            TeamMember user = teamMemberRepo.findByEmail(email).orElse(null);

            if (user == null) {
                return null;
            }

            me.setId(user.getId());
            me.setIsAdmin(false);
            me.setFirstName(user.getFirstName());
            me.setLastName(user.getLastName());
            me.setEmail(user.getEmail());
        } else if (Objects.equals(role, ADMIN_AUTHORITY)) {
            TeamMember user = teamMemberRepo.findByEmail(email).orElse(null);

            if (user == null) {
                return null;
            }

            me.setId(user.getId());
            me.setIsAdmin(true);
            me.setFirstName(user.getFirstName());
            me.setLastName(user.getLastName());
            me.setEmail(user.getEmail());

        }

        return me;
    }

}
