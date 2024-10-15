package ru.savrey.Sozinov_AV_diplom;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import ru.savrey.Sozinov_AV_diplom.api.UIController;
import ru.savrey.Sozinov_AV_diplom.security.SecurityConfig;
import ru.savrey.Sozinov_AV_diplom.service.FarmService;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;


@WebMvcTest(UIController.class)
@ContextConfiguration(classes = {FarmService.class, SecurityConfig.class})
public class UIControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testHomePage() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(content().string(
                        containsString("Список хозяйств")
                ));
    }

    @Test
    public void testFarmFormPage() throws Exception {
        mockMvc.perform(get("/farm_form"))
                .andExpect(status().isOk())
                .andExpect(view().name("/farm/farm_form"))
                .andExpect(content().string(
                        containsString("Новое хозяйство")
                ));
    }
}