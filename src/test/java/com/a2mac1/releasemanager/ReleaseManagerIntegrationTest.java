
package com.a2mac1.releasemanager;

import com.a2mac1.releasemanager.repository.DeploymentSnapshotRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(statements = {
        "DELETE FROM deployment_snapshots",
        "DELETE FROM current_deployments",
        "UPDATE system_state SET current_version = 0"
})
class ReleaseManagerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DeploymentSnapshotRepository snapshotRepository;

    @Test
    void shouldHandleDeploymentScenario() throws Exception {
        deploy("Service A", 1, 1);
        deploy("Service B", 1, 2);
        deploy("Service A", 2, 3);
        deploy("Service B", 1, 3);

        mockMvc.perform(get("/services")
                        .param("systemVersion", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].version").value(1))
                .andExpect(jsonPath("$[1].version").value(1));

        mockMvc.perform(get("/services")
                        .param("systemVersion", "3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].version").value(2))
                .andExpect(jsonPath("$[1].version").value(1));

        assertThat(snapshotRepository.count()).isEqualTo(5);
    }

    @Test
    void shouldRejectInvalidDeployment() throws Exception {
        mockMvc.perform(post("/deploy")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {"name": "", "version": -1}
                            """))
                .andExpect(status().isBadRequest());
    }

    private void deploy(String name, int version, int expected)
            throws Exception {

        String request = """
                {"name": "%s", "version": %d}
                """.formatted(name, version);

        mockMvc.perform(post("/deploy")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(expected)));
    }
}
