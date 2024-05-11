package ru.savrey.Sozinov_AV_diplom.api;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.reactive.server.WebTestClient;
import ru.savrey.Sozinov_AV_diplom.JUnitSpringBootBase;
import ru.savrey.Sozinov_AV_diplom.model.Farm;
import ru.savrey.Sozinov_AV_diplom.repository.FarmRepository;

import java.util.List;

public class FarmControllerTest extends JUnitSpringBootBase {

    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private FarmRepository farmRepository;
    @Autowired
    JdbcTemplate jdbcTemplate;

    @BeforeEach
    void tearUp() {
        // Fill up the database before each test
        farmRepository.saveAll(List.of(
                new Farm("Farm1"),
                new Farm("Farm2"),
                new Farm("Farm3")
        ));
    }

    @Test
    void testFindByIdSuccess() {
        Farm expected = farmRepository.save(new Farm("Random"));

        webTestClient.get()
                .uri("/api/farm/" + expected.getFarmId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .value(responseBody -> {
                    Assertions.assertNotNull(responseBody);
                    Assertions.assertTrue(responseBody.contains("Информация о хозяйстве"));
                    Assertions.assertTrue(responseBody.contains(expected.getTitle()));
                });

        Assertions.assertEquals(4, farmRepository.count());
        Farm savedFarm = farmRepository.findByTitle(expected.getTitle());
        Assertions.assertNotNull(savedFarm);
        Assertions.assertEquals(expected.getTitle(), savedFarm.getTitle());
    }

    @Test
    void testFindByIdNotFound() {
        Long maxId = jdbcTemplate.queryForObject("select max(farm_id) from farms", Long.class);

        webTestClient.get()
                .uri("/api/farm/" + maxId + 1)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith(MediaType.TEXT_HTML)
                .expectBody(String.class)
                .value(responseBody -> {
                    Assertions.assertTrue(responseBody.contains("Не найдено хозяйство"));
                });
    }

    @Test
    void testGetAll() {
        List<Farm> expected = farmRepository.findAll();

        webTestClient.get()
                .uri("/api/farm/all")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith(MediaType.TEXT_HTML)
                .expectBody(String.class)
                .value(responseBody -> {
                    Assertions.assertTrue(responseBody.contains("Список хозяйств"));
                    Assertions.assertTrue(responseBody.contains(expected.getFirst().getTitle()));
                    Assertions.assertTrue(responseBody.contains(expected.getLast().getTitle()));
                });
    }

    @Test
    void testSave() {
        FarmRequest request = new FarmRequest();
        request.setTitle("Example");
        request.setAddress("Moscow");

        webTestClient.post()
                .uri("/api/farm")
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith(MediaType.TEXT_HTML)
                .expectBody(String.class)
                .value(responseBody -> {
                    Assertions.assertTrue(responseBody.contains("Хозяйство добавлено"));
                });

        Assertions.assertEquals(4, farmRepository.count());
        Farm savedFarm = farmRepository.findByTitle(request.getTitle());
        //Assertions.assertNotNull(savedFarm);
    }

    @AfterEach
    void tearDown() {
        // Clean up the database after each test
        farmRepository.deleteAll();
    }
}
