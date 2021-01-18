package guru.sfg.brewery.web.controllers;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
public class BeerControllerIT extends BaseIT{

    @Test
    void initCreationFormAsAdmin() throws Exception {
        mockMvc.perform(get("/beers/new").with(httpBasic("spring","wuru")))
                .andExpect(status().isOk())
                .andExpect(view().name("beers/createBeer"))
                .andExpect(model().attributeExists("beer"));
    }

    @Test
    void initCreationFormAsUser() throws Exception {
        mockMvc.perform(get("/beers/new").with(httpBasic("user","password")))
                .andExpect(status().isForbidden());
    }

    @Test
    void initCreationFormWithJoakim() throws Exception {
        mockMvc.perform(get("/beers/new").with(httpBasic("joakim","kung")))
                .andExpect(status().isForbidden());
    }
}
