package ru.savrey.Sozinov_AV_diplom.api;

import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.reactive.server.WebTestClient;
import ru.savrey.Sozinov_AV_diplom.JUnitSpringBootBase;
import ru.savrey.Sozinov_AV_diplom.model.Farm;
import ru.savrey.Sozinov_AV_diplom.repository.FarmRepository;

import java.util.List;
import java.util.Objects;

public class FarmControllerTest extends JUnitSpringBootBase {

    @Autowired
    WebTestClient webTestClient;
    @Autowired
    FarmRepository farmRepository;
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Data
    static class JUnitFarmResponse {
        private Long farmId;
        private String title;
        private String address;
    }

    @Test
    void testFindByIdSuccess() {
        Farm expected = farmRepository.save(new Farm("Random"));

        JUnitFarmResponse responseBody = webTestClient.get()
                .uri("/api/farm/" + expected.getFarmId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(JUnitFarmResponse.class)
                .returnResult().getResponseBody();

        Assertions.assertNotNull(responseBody);
        Assertions.assertEquals(expected.getFarmId(), responseBody.getFarmId());
        Assertions.assertEquals(expected.getTitle(), responseBody.getTitle());
        Assertions.assertEquals(expected.getAddress(), responseBody.getAddress());
    }

    @Test
    void testFindByIdNotFound() {
        Long maxId = jdbcTemplate.queryForObject("select max(id) from farms", Long.class);

        webTestClient.get()
                .uri("/api/farm/" + maxId + 1)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testGetAll() {
        farmRepository.saveAll(List.of(
                new Farm("first"),
                new Farm("second")
        ));

        List<Farm> expected = farmRepository.findAll();

        List<JUnitFarmResponse> responseBody = webTestClient.get()
                .uri("/api/farm/all")
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<List<JUnitFarmResponse>>() {
                })
                .returnResult()
                .getResponseBody();

        assert responseBody != null;
        Assertions.assertEquals(expected.size(), responseBody.size());
        for (JUnitFarmResponse farmResponse : responseBody) {
            boolean found = expected.stream()
                    .filter(it -> Objects.equals(it.getFarmId(), farmResponse.getFarmId()))
                    .anyMatch(it -> Objects.equals(it.getTitle(), farmResponse.getTitle()));
            Assertions.assertTrue(found);
        }
    }

    @Test
    void testSave() {
        JUnitFarmResponse request = new JUnitFarmResponse();

        JUnitFarmResponse responseBody = webTestClient.post()
                .uri("/api/farm")
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(JUnitFarmResponse.class)
                .returnResult().getResponseBody();

        Assertions.assertNotNull(responseBody);
        Assertions.assertNotNull(responseBody.getFarmId());

        Assertions.assertTrue(farmRepository.findById(request.getFarmId()).isPresent());
    }
}
