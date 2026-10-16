package com.example.campushub.announcement;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class AnnouncementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAll_returnsOk() throws Exception {
        mockMvc.perform(get("/api/announcements"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void createAndGetAnnouncement() throws Exception {
        Announcement ann = new Announcement("Test Title", "Test content", "GENERAL");

        String location = mockMvc.perform(post("/api/announcements")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ann)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.title", is("Test Title")))
                .andReturn()
                .getResponse()
                .getHeader("Location");

        mockMvc.perform(get("/api/announcements"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));
    }

    @Test
    void getById_notFound_returns404() throws Exception {
        mockMvc.perform(get("/api/announcements/99999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createAnnouncement_missingTitle_returns400() throws Exception {
        Announcement ann = new Announcement("", "content", "GENERAL");

        mockMvc.perform(post("/api/announcements")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ann)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteAnnouncement_returnsNoContent() throws Exception {
        Announcement ann = new Announcement("Delete Me", "content", "EVENT");
        String body = mockMvc.perform(post("/api/announcements")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ann)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long id = objectMapper.readTree(body).get("id").asLong();

        mockMvc.perform(delete("/api/announcements/" + id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/announcements/" + id))
                .andExpect(status().isNotFound());
    }
}
