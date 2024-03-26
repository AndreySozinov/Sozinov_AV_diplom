package ru.savrey.Sozinov_AV_diplom;

import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = JUnitSpringBootBase.TestSecurityConfiguration.class)
@AutoConfigureWebTestClient
public abstract class JUnitSpringBootBase {

    @TestConfiguration
    static class TestSecurityConfiguration {
        //    @Bean
        //    SecurityFilterChain testSecurityFilterChain(HttpSecurity security) throws Exception {
        //      return security.authorizedHttpRequests(registry -> registry
        //        .anyRequest().permitAll()
        //      ).build;
        //    }
    }
}
