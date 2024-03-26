package ru.savrey.Sozinov_AV_diplom.api;

import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.reactive.server.WebTestClient;
import ru.savrey.Sozinov_AV_diplom.model.Farm;
import ru.savrey.Sozinov_AV_diplom.model.Field;
import ru.savrey.Sozinov_AV_diplom.model.Point;
import ru.savrey.Sozinov_AV_diplom.repository.PointRepository;

import java.util.List;
import java.util.Objects;

public class PointControllerTest {

    @Autowired
    WebTestClient webTestClient;
    @Autowired
    PointRepository pointRepository;
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Data
    static class JUnitPointResponse {
        private Long pointId;
        private Field field;
        private double latitude;
        private double longitude;
        private double humus;
        private double phosphorus;
        private double potassium;
        private double pH;
        private double density;
    }

    @Test
    void testFindByIdSuccess() {
        Point expected = pointRepository.save(
                new Point(new Field(new Farm("random"), 256), 55.6, 62.4));

        PointControllerTest.JUnitPointResponse responseBody = webTestClient.get()
                .uri("/api/point/" + expected.getPointId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(PointControllerTest.JUnitPointResponse.class)
                .returnResult().getResponseBody();

        Assertions.assertNotNull(responseBody);
        Assertions.assertEquals(expected.getPointId(), responseBody.getPointId());
        Assertions.assertEquals(expected.getField(), responseBody.getField());
        Assertions.assertEquals(expected.getLatitude(), responseBody.getLatitude());
        Assertions.assertEquals(expected.getLongitude(), responseBody.getLongitude());
        Assertions.assertEquals(expected.getHumus(), responseBody.getHumus());
        Assertions.assertEquals(expected.getPhosphorus(), responseBody.getPhosphorus());
        Assertions.assertEquals(expected.getPotassium(), responseBody.getPotassium());
        Assertions.assertEquals(expected.getPH(), responseBody.getPH());
        Assertions.assertEquals(expected.getDensity(), responseBody.getDensity());
    }

    @Test
    void testFindByIdNotFound() {
        Long maxId = jdbcTemplate.queryForObject("select max(id) from points", Long.class);

        webTestClient.get()
                .uri("/api/point/" + maxId + 1)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testGetAll() {
        pointRepository.saveAll(List.of(
                new Point(new Field(new Farm("first"), 256), 55.6, 62.4),
                new Point(new Field(new Farm("second"), 300), 54.1, 60.9))
        );

        List<Point> expected = pointRepository.findAll();

        List<PointControllerTest.JUnitPointResponse> responseBody = webTestClient.get()
                .uri("/api/point/all")
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<List<PointControllerTest.JUnitPointResponse>>() {
                })
                .returnResult()
                .getResponseBody();

        assert responseBody != null;
        Assertions.assertEquals(expected.size(), responseBody.size());
        for (PointControllerTest.JUnitPointResponse pointResponse : responseBody) {
            boolean found = expected.stream()
                    .filter(it -> Objects.equals(it.getPointId(), pointResponse.getPointId()))
                    .anyMatch(it -> Objects.equals(it.getLatitude(), pointResponse.getLatitude()));
            Assertions.assertTrue(found);
        }
    }

    @Test
    void testSave() {
        PointControllerTest.JUnitPointResponse request = new PointControllerTest.JUnitPointResponse();

        PointControllerTest.JUnitPointResponse responseBody = webTestClient.post()
                .uri("/api/point")
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(PointControllerTest.JUnitPointResponse.class)
                .returnResult().getResponseBody();

        Assertions.assertNotNull(responseBody);
        Assertions.assertNotNull(responseBody.getPointId());

        Assertions.assertTrue(pointRepository.findById(request.getPointId()).isPresent());
    }
}

