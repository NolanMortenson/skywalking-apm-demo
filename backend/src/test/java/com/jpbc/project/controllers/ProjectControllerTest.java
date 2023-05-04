package com.jpbc.project.controllers;

import com.jpbc.project.services.ProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProjectControllerTest {

    @InjectMocks
    ProjectController projectController;

    @Mock
    ProjectService projectService;

    MockMvc mockMvc;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(projectController).build();
    }

    @Test
    void createProject() throws Exception {
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "",
                "text/plain", "test data".getBytes());
        MockMultipartFile project = new MockMultipartFile("project", "", "application/json", "{\"id\":1}".getBytes());
        Long id = 1L;
        MockMultipartFile teamId = new MockMultipartFile("team_id", "", "application/json", id.toString().getBytes());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/projects")
                        .file(mockMultipartFile)
                        .file(project)
                        .file(teamId)
                )
                .andExpect(status().isCreated());
    }


    @Test
    void getProject() throws Exception {

        mockMvc.perform(get("/api/v1/projects/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void updateProject() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/projects")
                        .content("{\"id\":1}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

}