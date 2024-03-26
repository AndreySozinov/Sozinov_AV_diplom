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
import ru.savrey.Sozinov_AV_diplom.model.Field;
import ru.savrey.Sozinov_AV_diplom.repository.FieldRepository;

import java.util.List;
import java.util.Objects;

public class FieldControllerTest extends JUnitSpringBootBase {

    @Autowired
    WebTestClient webTestClient;
    @Autowired
    FieldRepository fieldRepository;
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Data
    static class JUnitFieldResponse {
        private Long fieldId;
        private Farm farm;
        private double area;
        private String soil;
        private String description;
    }

    @Test
    void testFindByIdSuccess() {
        Field expected = fieldRepository.save(new Field(new Farm("random"), 256));

        JUnitFieldResponse responseBody = webTestClient.get()
                .uri("/api/field/" + expected.getFieldId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(JUnitFieldResponse.class)
                .returnResult().getResponseBody();

        Assertions.assertNotNull(responseBody);
        Assertions.assertEquals(expected.getFieldId(), responseBody.getFieldId());
        Assertions.assertEquals(expected.getFarm(), responseBody.getFarm());
        Assertions.assertEquals(expected.getArea(), responseBody.getArea());
        Assertions.assertEquals(expected.getSoil(), responseBody.getSoil());
        Assertions.assertEquals(expected.getDescription(), responseBody.getDescription());
    }

    @Test
    void testFindByIdNotFound() {
        Long maxId = jdbcTemplate.queryForObject("select max(id) from fields", Long.class);

        webTestClient.get()
                .uri("/api/field/" + maxId + 1)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testGetAll() {
        fieldRepository.saveAll(List.of(
                new Field(new Farm("first"), 256),
                new Field(new Farm("second"), 300)
        ));

        List<Field> expected = fieldRepository.findAll();

        List<JUnitFieldResponse> responseBody = webTestClient.get()
                .uri("/api/field/all")
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<List<JUnitFieldResponse>>() {
                })
                .returnResult()
                .getResponseBody();

        assert responseBody != null;
        Assertions.assertEquals(expected.size(), responseBody.size());
        for (JUnitFieldResponse fieldResponse : responseBody) {
            boolean found = expected.stream()
                    .filter(it -> Objects.equals(it.getFieldId(), fieldResponse.getFieldId()))
                    .anyMatch(it -> Objects.equals(it.getFarm(), fieldResponse.getFarm()));
            Assertions.assertTrue(found);
        }
    }

    @Test
    void testSave() {
        JUnitFieldResponse request = new JUnitFieldResponse();

        JUnitFieldResponse responseBody = webTestClient.post()
                .uri("/api/field")
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(JUnitFieldResponse.class)
                .returnResult().getResponseBody();

        Assertions.assertNotNull(responseBody);
        Assertions.assertNotNull(responseBody.getFieldId());

        Assertions.assertTrue(fieldRepository.findById(request.getFieldId()).isPresent());
    }
}
