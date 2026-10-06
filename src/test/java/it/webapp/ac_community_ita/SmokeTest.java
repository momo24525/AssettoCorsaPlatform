package it.webapp.ac_community_ita;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
        "ac.results.api-key=test-key",
        "spring.datasource.url=jdbc:h2:mem:testdb"
})
@AutoConfigureMockMvc
class SmokeTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void homeIsPublic() throws Exception {
        mockMvc.perform(get("/home"))
                .andExpect(status().isOk());
    }

    @Test
    void adminIsNotAccessibleWithoutLogin() throws Exception {
        mockMvc.perform(get("/admin/cars"))
                .andExpect(status().isForbidden());
    }

    @Test
    void incomingResultsRejectsMissingApiKey() throws Exception {
        mockMvc.perform(post("/admin/results/incoming")
                        .param("filename", "test.json")
                        .content("not json"))
                .andExpect(status().isForbidden());
    }

    @Test
    void incomingResultsAcceptsValidApiKey() throws Exception {
        // JSON non valido apposta: la risposta 400 dimostra che l'autenticazione è passata
        // e che non scriviamo nulla nel database
        mockMvc.perform(post("/admin/results/incoming")
                        .header("X-API-KEY", "test-key")
                        .param("filename", "test.json")
                        .content("not json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void adminPagesRenderForAdmin() throws Exception {
        mockMvc.perform(get("/admin/cars")).andExpect(status().isOk());
        mockMvc.perform(get("/admin/tracks")).andExpect(status().isOk());
        mockMvc.perform(get("/admin/event")).andExpect(status().isOk());
        mockMvc.perform(get("/admin/pending-results")).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    void adminIsForbiddenForNormalUser() throws Exception {
        mockMvc.perform(get("/admin/cars"))
                .andExpect(status().isForbidden());
    }

    @Test
    void cssIsServed() throws Exception {
        mockMvc.perform(get("/css/style.css"))
                .andExpect(status().isOk());
    }
}
