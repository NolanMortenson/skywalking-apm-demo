package com.jpbc.project.security;

import com.jpbc.project.controllers.TeamMemberController;
import com.jpbc.project.dto.TeamMemberDTO;
import com.jpbc.project.services.TeamMemberServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SecurityTest {


    @InjectMocks
    TeamMemberController teamMemberController;
    @Mock
    TeamMemberServiceImpl teamMemberService;

    TeamMemberDTO teamMemberDTO;

    MockMvc mockMvc;
    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);


        teamMemberDTO = new TeamMemberDTO();
        teamMemberDTO.setId(1234L);
        teamMemberDTO.setFirstName("Monte");
        teamMemberDTO.setLastName("Carlo");
        teamMemberDTO.setEmail("mc@test.com");

        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }


    @Test
    @WithMockUser
    void testsGetAllUsersWhileLoggedIn_StatusOK() throws Exception {
        //given
        List<TeamMemberDTO> users = new ArrayList<>();

        //when
        when(teamMemberService.getTeamMemberList()).thenReturn(users);

        //then
        mockMvc.perform(get("/api/v1/users/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testsGetUsersWithoutLogin_StatusForbidden() throws Exception {
        //given
        List<TeamMemberDTO> users = new ArrayList<>();

        //when
        when(teamMemberService.getTeamMemberList()).thenReturn(users);

        //then
        mockMvc.perform(get("/api/v1/users/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "jane", authorities = {"ADMIN"})
    void testsUpdateUserWithAdminAuthority_StatusOK() throws Exception {

        //when
        when(teamMemberController.getUserById(anyLong())).thenReturn(teamMemberDTO);

        //then
        mockMvc.perform(put("/api/v1/users")
                        .content("{\"id\":1}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "jane", authorities = {"USER"})
    void testsUpdateUserWithNoAuthority_ExpectForbidden() throws Exception {

        //when
        when(teamMemberController.getUserById(anyLong())).thenReturn(teamMemberDTO);

        //then
        mockMvc.perform(put("/api/v1/users")
                        .content("{\"id\":1}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

}