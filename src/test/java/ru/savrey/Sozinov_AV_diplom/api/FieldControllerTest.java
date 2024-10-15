package ru.savrey.Sozinov_AV_diplom.api;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.reactive.server.WebTestClient;
import ru.savrey.Sozinov_AV_diplom.JUnitSpringBootBase;
import ru.savrey.Sozinov_AV_diplom.model.Farm;
import ru.savrey.Sozinov_AV_diplom.model.Field;
import ru.savrey.Sozinov_AV_diplom.model.User;
import ru.savrey.Sozinov_AV_diplom.repository.FarmRepository;
import ru.savrey.Sozinov_AV_diplom.repository.FieldRepository;

import java.util.List;

public class FieldControllerTest extends JUnitSpringBootBase {

    private static SessionFactory sessionFactory;

    @Autowired
    WebTestClient webTestClient;
    @Autowired
    FarmRepository farmRepository;
    @Autowired
    FieldRepository fieldRepository;
    @Autowired
    JdbcTemplate jdbcTemplate;

//    @BeforeAll
//    public static void init() {
//        sessionFactory = HibernateUtil.getSessionFactory();
//    }
    @BeforeEach
    void tearUp() {
        // Fill up the database before each test
        Farm farm = farmRepository.save(new Farm(new User(), "Farm1"));
        fieldRepository.saveAll(List.of(
                new Field(farm, 42),
                new Field(farm, 525),
                new Field(farm, 400)
        ));
    }

    @Test
    void testFindByIdSuccess() {
        Farm farm = farmRepository.save(new Farm(new User(),"Farm2"));
        Field expected = fieldRepository.save(new Field(farm, 256));

        webTestClient.get()
                .uri("/api/field/" + expected.getFieldId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .value(responseBody -> {
                    Assertions.assertNotNull(responseBody);
                    Assertions.assertTrue(responseBody.contains("Поле"));
                    Assertions.assertTrue(responseBody.contains(String.valueOf(expected.getFieldId())));
                    Assertions.assertTrue(responseBody.contains(expected.getFarm().getTitle()));
                    Assertions.assertTrue(responseBody.contains(String.valueOf(expected.getArea())));
                });
    }

    @Test
    void testFindByIdNotFound() {
        Long maxId = jdbcTemplate.queryForObject("select max(field_id) from fields", Long.class);

        webTestClient.get()
                .uri("/api/field/" + maxId + 1)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith(MediaType.TEXT_HTML)
                .expectBody(String.class)
                .value(responseBody -> {
                    Assertions.assertTrue(responseBody.contains("Не найдено поле"));
                });
    }

    @Test
    void testGetAll() {
        List<Field> expected = fieldRepository.findAll();

        webTestClient.get()
                .uri("/api/field/all/" + expected.getFirst().getFarm().getFarmId())
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith(MediaType.TEXT_HTML)
                .expectBody(String.class)
                .value(responseBody -> {
                    Assertions.assertTrue(responseBody.contains("Список полей"));
//                    Assertions.assertTrue(responseBody.contains(expected.getFirst().getFarm().getTitle()));
                    Assertions.assertTrue(responseBody.contains(String.valueOf(expected.getFirst().getArea())));
//                    Assertions.assertTrue(responseBody.contains(expected.getLast().getFarm().getTitle()));
                    Assertions.assertTrue(responseBody.contains(String.valueOf(expected.getLast().getArea())));
                });
    }

//    @Test
//    void testSave() {
//        Farm farm = farmRepository.save(new Farm("Farm2"));
//        FieldRequest request = new FieldRequest(farm.getFarmId());
//        request.setArea(256);
//
//        webTestClient.post()
//                .uri("/api/field")
//                .bodyValue(request)
//                .exchange()
//                .expectStatus().isOk()
//                .expectHeader().contentTypeCompatibleWith(MediaType.TEXT_HTML)
//                .expectBody(String.class)
//                .value(responseBody -> {
//                    Assertions.assertTrue(responseBody.contains("Добавлено новое поле"));
//                });
//
//        Assertions.assertEquals(4, fieldRepository.count());
//        Assertions.assertTrue(fieldRepository.findById(1L).isPresent());
//    }

    @AfterEach
    void tearDown() {
        // Clean up the database after each test
        farmRepository.deleteAll();
        fieldRepository.deleteAll();
    }
}
