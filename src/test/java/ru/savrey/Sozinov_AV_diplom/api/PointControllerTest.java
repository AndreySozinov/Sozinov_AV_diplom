package ru.savrey.Sozinov_AV_diplom.api;

import lombok.Data;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.reactive.server.WebTestClient;
import ru.savrey.Sozinov_AV_diplom.JUnitSpringBootBase;
import ru.savrey.Sozinov_AV_diplom.model.Farm;
import ru.savrey.Sozinov_AV_diplom.model.Field;
import ru.savrey.Sozinov_AV_diplom.model.Point;
import ru.savrey.Sozinov_AV_diplom.repository.FarmRepository;
import ru.savrey.Sozinov_AV_diplom.repository.FieldRepository;
import ru.savrey.Sozinov_AV_diplom.repository.PointRepository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class PointControllerTest extends JUnitSpringBootBase {

    @Autowired
    WebTestClient webTestClient;
    @Autowired
    PointRepository pointRepository;
    @Autowired
    FieldRepository fieldRepository;
    @Autowired
    FarmRepository farmRepository;
    @Autowired
    JdbcTemplate jdbcTemplate;

    @BeforeEach
    void tearUp() {
        // Fill up the database before each test
        Farm farm = farmRepository.save(new Farm("Farm1"));
        Field field = fieldRepository.save(new Field(farm, 42));
        pointRepository.saveAll(List.of(
                new Point(field, 55.5, 66.5, LocalDate.now()),
                new Point(field, 55.6, 66.6, LocalDate.now()),
                new Point(field, 55.7, 66.7, LocalDate.now())
        ));
    }

    @Test
    void testFindByIdSuccess() {
        Farm farm = farmRepository.save(new Farm("Farm2"));
        Field field = fieldRepository.save(new Field(farm, 256));
        Point expected = pointRepository.save(new Point(field, 55.6, 62.4, LocalDate.now()));

        webTestClient.get()
                .uri("/api/point/" + expected.getPointId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .value(responseBody -> {
                    Assertions.assertNotNull(responseBody);
                    Assertions.assertTrue(responseBody.contains("Получена точка"));
                    Assertions.assertTrue(responseBody.contains(String.valueOf(expected.getPointId())));
                    Assertions.assertTrue(responseBody.contains(String.valueOf(expected.getField().getFieldId())));
                });
    }

    @Test
    void testFindByIdNotFound() {
        Long maxId = jdbcTemplate.queryForObject("select max(point_id) from points", Long.class);

        webTestClient.get()
                .uri("/api/point/" + maxId + 1)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith(MediaType.TEXT_HTML)
                .expectBody(String.class)
                .value(responseBody -> {
                    Assertions.assertTrue(responseBody.contains("Не найдена точка"));
                });
    }

//    @Test
//    void testGetAll() {
//        Field field = pointRepository.findAll().getFirst().getField();
//        List<Point> expected = pointRepository.findAll().stream()
//                .filter(it -> it.getField().getFieldId() == field.getFieldId()).toList();
//
//        webTestClient.get()
//                .uri("/api/point/all/" + expected.getFirst().getField().getFieldId())
//                .exchange()
//                .expectStatus().isOk()
//                .expectHeader().contentTypeCompatibleWith(MediaType.TEXT_HTML)
//                .expectBody(String.class)
//                .value(responseBody -> {
//                    Assertions.assertTrue(responseBody.contains("Получен список точек"));
////                    Assertions.assertTrue(responseBody.contains(expected.getFirst().getField().getFieldId()));
//                    Assertions.assertTrue(responseBody.contains(String.valueOf(expected.getFirst().getPointId())));
////                    Assertions.assertTrue(responseBody.contains(expected.getLast().getField().getFieldId()));
//                    Assertions.assertTrue(responseBody.contains(String.valueOf(expected.getLast().getPointId())));
//                });
//    }

//    @Test
//    void testSave() {
//        Farm farm = farmRepository.save(new Farm("Farm2"));
//        Field field = fieldRepository.save(new Field(farm, 300));
//        PointRequest request = new PointRequest(field.getFieldId());
//        request.setLatitude(52.0);
//        request.setLongitude(48.0);
//
//        webTestClient.post()
//                .uri("/api/point")
//                .bodyValue(request)
//                .exchange()
//                .expectStatus().isOk()
//                .expectHeader().contentTypeCompatibleWith(MediaType.TEXT_HTML)
//                .expectBody(String.class)
//                .value(responseBody -> {
//                    Assertions.assertTrue(responseBody.contains("Добавлена новая точка"));
//                });

//        Assertions.assertEquals(4, pointRepository.count());
//        Assertions.assertTrue(pointRepository.findById(1L).isPresent());
//    }

    @AfterEach
    void tearDown() {
        // Clean up the database after each test
        farmRepository.deleteAll();
        fieldRepository.deleteAll();
        pointRepository.deleteAll();
    }
}

