package guru.sfg.brewery.web.controllers.api;

import guru.sfg.brewery.web.controllers.BaseIT;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
public class BreweryControllerIT extends BaseIT {

    @Test
    void listBreweriesHttpBasicWithoutCustomerRole() throws Exception {
        mockMvc.perform(get("/brewery/api/v1/breweries"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void listBreweriesHttpBasicWithCustomerRole() throws Exception {
        mockMvc.perform(get("/brewery/api/v1/breweries")
                    .with(httpBasic("joakim", "kung")))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void listBreweriesHttpBasicWithAdminRole() throws Exception {
        mockMvc.perform(get("/brewery/api/v1/breweries")
                    .with(httpBasic("spring", "wuru")))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void listBreweriesHttpBasicWithUserRole() throws Exception {
        mockMvc.perform(get("/brewery/api/v1/breweries")
                    .with(httpBasic("user", "password")))
                .andExpect(status().isForbidden());
    }

    @Test
    void viewBreweryAsAdmin() throws Exception {
        mockMvc.perform(get("/brewery/breweries")
                    .with(httpBasic("spring","wuru")))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void viewBreweryAsUser() throws Exception {
        mockMvc.perform(get("/brewery/breweries")
                    .with(httpBasic("user","password")))
                .andExpect(status().isForbidden());
    }

    @Test
    void viewBreweryAsJoakim() throws Exception {
        mockMvc.perform(get("/brewery/breweries")
                    .with(httpBasic("joakim","kung")))
                .andExpect(status().isOk())
                .andExpect(view().name("breweries/index"))
                .andExpect(model().attributeExists("breweries"));
    }

    @Test
    void viewBreweryAsNoOne() throws Exception {
        mockMvc.perform(get("/brewery/breweries"))
                .andExpect(status().isUnauthorized());
    }
}
