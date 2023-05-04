package com.jpbc.project.controllers;

import com.jpbc.project.dto.TeamMemberDTO;
import com.jpbc.project.models.TeamMember;
import com.jpbc.project.services.TeamMemberServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.ArrayList;
import java.util.List;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class TeamMemberControllerTest {

    @InjectMocks
    TeamMemberController teamMemberController;
    @Mock
    TeamMemberServiceImpl teamMemberService;
    TeamMemberDTO teamMemberDTO;

    TeamMember teamMember;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        teamMemberDTO = new TeamMemberDTO();
        teamMemberDTO.setId(1234L);
        teamMemberDTO.setFirstName("Monte");
        teamMemberDTO.setLastName("Carlo");
        teamMemberDTO.setEmail("mc@test.com");

        mockMvc = MockMvcBuilders.standaloneSetup(teamMemberController).build();
    }

    @Test
    void testGetAllUsers_StatusOK() throws Exception {
        //given
        List<TeamMemberDTO> users = new ArrayList<>();

        //when
        when(teamMemberService.getTeamMemberList()).thenReturn(users);

        //then
        mockMvc.perform(get("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    void testSearchForUserById_StatusOK() throws Exception {
        when(teamMemberController.getUserById(anyLong())).thenReturn(teamMemberDTO);

        mockMvc.perform(get("/api/v1/users/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testNewUser() throws Exception {

        //when
        when(teamMemberService.createNewUser(teamMember)).thenReturn(teamMemberDTO);

//        TODO need new-user page
        //then
        mockMvc.perform(post("/api/v1/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"first_Name\":0}"))
                .andExpect(status().isCreated());
    }

    @Test
    void deleteUser() throws Exception {
        mockMvc.perform(get("/api/v1/users/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void updateUser() throws Exception {

        //when
        when(teamMemberController.getUserById(anyLong())).thenReturn(teamMemberDTO);

        //then
        mockMvc.perform(put("/api/v1/users")
                        .content("{\"id\":1}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


}