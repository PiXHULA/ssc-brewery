package guru.sfg.brewery.web.controllers.api;

import guru.sfg.brewery.domain.Beer;
import guru.sfg.brewery.repositories.BeerRepository;
import guru.sfg.brewery.web.controllers.BaseIT;
import guru.sfg.brewery.web.model.BeerStyleEnum;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Random;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
public class findBeerControllerIT extends BaseIT {


    Beer beerToFind;

    @Autowired
    private BeerRepository beerRepository;

    @DisplayName("Find beers")
    @Nested
    class FindTests {
        public Beer beerToFind() {
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
        void findBeersWithNoUserAuth() throws Exception {
            mockMvc.perform(get("/beers/find"))
                    .andExpect(status().isUnauthorized());
        }

        @ParameterizedTest(name ="#{index} with [{arguments}]")
        @MethodSource("guru.sfg.brewery.web.controllers.BeerControllerIT#getStreamAllUsers")
        void findBeersWithHttpsBasicAsAllUsers(String user, String password) throws Exception {
            mockMvc.perform(get("/beers/find").with(httpBasic(user, password)))
                    .andExpect(status().is2xxSuccessful())
                    .andExpect(model().attributeExists("beer"))
                    .andExpect(view().name("beers/findBeers"));
        }

        @Disabled
        @Test
        @DisplayName("Find beers as admin")
        void findBeersWithHttpsBasicAsAdmin() throws Exception {
            mockMvc.perform(get("/beers/find").with(httpBasic("spring", "wuru")))
                    .andExpect(status().is2xxSuccessful())
                    .andExpect(model().attributeExists("beer"))
                    .andExpect(view().name("beers/findBeers"));
        }

        @Disabled
        @Test
        @DisplayName("Find beers as user")
        void findBeersWithHttpsBasicAsUser() throws Exception {
            mockMvc.perform(get("/beers/find").with(httpBasic("user", "password")))
                    .andExpect(status().is2xxSuccessful())
                    .andExpect(model().attributeExists("beer"))
                    .andExpect(view().name("beers/findBeers"));
        }

        @Disabled
        @Test
        @DisplayName("Find beers as customer")
        void findBeersWithHttpsBasicAsCustomer() throws Exception {
            mockMvc.perform(get("/beers/find").with(httpBasic("joakim", "kung")))
                    .andExpect(status().is2xxSuccessful())
                    .andExpect(model().attributeExists("beer"))
                    .andExpect(view().name("beers/findBeers"));
        }

        @Test
        @DisplayName("Will fail to find beers by UPC with wrong credentials")
        void dontFindBeersWithHttpsBasic() throws Exception {
            mockMvc.perform(get("/api/v1/beerUpc/"+ beerToFind().getUpc()).with(httpBasic("wrong", "credentials")))
                    .andExpect(status().isUnauthorized());
        }

        @ParameterizedTest(name = "#index with [{arguments}]")
        @MethodSource("guru.sfg.brewery.web.controllers.BeerControllerIT#getStreamAllUsers")
        void findBeersByUPCAsAllUsers(String user, String password) throws Exception {
            mockMvc.perform(get("/api/v1/beerUpc/" + beerToFind().getUpc())
                        .with(httpBasic(user, password)))
                    .andExpect(status().is2xxSuccessful());
        }

        @Test
        @DisplayName("Find beer by UPC as Admin")
        void findBeersByUPCAsAdmin() throws Exception {
            mockMvc.perform(get("/api/v1/beerUpc/" + beerToFind().getUpc()).with(httpBasic("spring", "wuru")))
                    .andExpect(status().is2xxSuccessful());
        }

        @Test
        @DisplayName("Find beer by UPC as Customer")
        void findBeersByUPCAsCustomer() throws Exception {
            mockMvc.perform(get("/api/v1/beerUpc/" + beerToFind().getUpc()).with(httpBasic("joakim", "kung")))
                    .andExpect(status().is2xxSuccessful());
        }

        @Test
        @DisplayName("Find beer by UPC as User")
        void findBeersByUPCAsUser() throws Exception {
            mockMvc.perform(get("/api/v1/beerUpc/" + beerToFind().getUpc()).with(httpBasic("user", "password")))
                    .andExpect(status().is2xxSuccessful());
        }
    }
}
