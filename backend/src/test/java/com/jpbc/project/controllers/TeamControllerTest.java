package com.jpbc.project.controllers;

import com.jpbc.project.mappers.MapstructMapper;
import com.jpbc.project.mappers.MapstructMapperImpl;
import com.jpbc.project.repos.TeamMemberRepo;
import com.jpbc.project.repos.TeamRepo;
import com.jpbc.project.services.TeamServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TeamControllerTest {


	@InjectMocks
	TeamController teamController;

	@Mock
	TeamRepo teamRepo;

	@Mock
	TeamMemberRepo teamMemberRepo;

	MapstructMapper mapper = new MapstructMapperImpl();


	@Mock
	TeamServiceImpl teamServiceImpl = new TeamServiceImpl(teamRepo, teamMemberRepo, mapper);

	MockMvc mockMvc;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);

		mockMvc = MockMvcBuilders.standaloneSetup(teamController).build();
	}

	@Test
	void createTeam() throws Exception {

		mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/teams")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"teamName\":0}"))
				.andExpect(status().isCreated());
	}

	@Test
	void getTeam() throws Exception {

		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/teams/1")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	@Test
	void deleteTeam() throws Exception {

		mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/teams/1/delete")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	@Test
	void updateTeam() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/teams")
						.content("{\"id\":1}")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}
}