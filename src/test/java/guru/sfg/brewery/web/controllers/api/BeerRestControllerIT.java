package guru.sfg.brewery.web.controllers.api;


import guru.sfg.brewery.domain.Beer;

import guru.sfg.brewery.repositories.BeerOrderRepository;
import guru.sfg.brewery.repositories.BeerRepository;
import guru.sfg.brewery.web.controllers.BaseIT;
import guru.sfg.brewery.web.model.BeerStyleEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Random;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


@SpringBootTest
public class BeerRestControllerIT extends BaseIT {

    @Autowired
    BeerRepository beerRepository;

    @Autowired
    BeerOrderRepository beerOrderRepository;

    @DisplayName("Delete Tests")
    @Nested
    class DeleteTests {

        public Beer beerToDelete() {
            Random rand = new Random();

            return beerRepository.saveAndFlush(Beer.builder()
            .beerName("Delete me beer")
            .beerStyle(BeerStyleEnum.PALE_ALE)
            .minOnHand(12)
            .quantityToBrew(200)
            .upc(String.valueOf(rand.nextInt(99999999)))
            .build());
        }

        @Test
        void deleteBeerUrl () throws Exception {
            mockMvc.perform(delete("/api/v1/beer/" + beerToDelete().getId())
                    .param("apiKey", "spring").param("apiSecret", "wuru"))
                    .andExpect(status().isOk());
        }

        @Test
        void deleteBeerBadCredsUrl () throws Exception {
            mockMvc.perform(delete("/api/v1/beer/" + beerToDelete().getId())
                    .param("apiKey", "spring").header("apiSecret", "wuruXXXXX"))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        void deleteBeerBadCreds () throws Exception {
            mockMvc.perform(delete("/api/v1/beer/" + beerToDelete().getId())
                    .header("Api-key", "spring").header("Api-Secret", "wuruXXXXX"))
                    .andExpect(status().isUnauthorized());

        }

        @Test
        void deleteBeerByID () throws Exception {
            mockMvc.perform(delete("/api/v1/beer/" + beerToDelete().getId())
                    .header("Api-key", "spring").header("Api-Secret", "wuru"))
                    .andExpect(status().isOk());
        }

        @Test
        void deleteBeerHttpBasicAdminRole () throws Exception {
            mockMvc.perform(delete("/api/v1/beer/" + beerToDelete().getId())
                    .with(httpBasic("spring", "wuru")))
                    .andExpect(status().isNoContent());
        }

        @Test
        void deleteBeerHttpBasicUserRole () throws Exception {
            mockMvc.perform(delete("/api/v1/beer/" + beerToDelete().getId())
                    .with(httpBasic("user", "password")))
                    .andExpect(status().isForbidden());
        }

        @Test
        void deleteBeerHttpBasicCustomerRole () throws Exception {
            mockMvc.perform(delete("/api/v1/beer/" + beerToDelete().getId())
                    .with(httpBasic("joakim", "kung")))
                    .andExpect(status().isForbidden());
        }

        @Test
        void deleteBeerNoAuth () throws Exception {
            mockMvc.perform(delete("/api/v1/beer/" + beerToDelete().getId()))
                    .andExpect(status().isUnauthorized());
        }

    }

    @Test
    void findBeers() throws Exception {
        mockMvc.perform(get("/api/v1/beer/"))
                .andExpect(status().isOk());
    }

    @Test
    void findBeerById() throws Exception {
        Beer beer = beerRepository.findAll().get(0);
        mockMvc.perform(get("/api/v1/beer/"+beer.getId()))
                .andExpect(status().isOk());
    }

    @Test
    void findBeerByUpcNoAuth() throws Exception {
        mockMvc.perform(get("/api/v1/beerUpc/0631234200036"))
                .andExpect(status().isUnauthorized());

    }

    @Test
    void findBeerFormADMIN() throws Exception {
        mockMvc.perform(get("/beers").param("beerName", "")
                .with(httpBasic("spring", "wuru")))
                .andExpect(status().isOk());
    }
}
