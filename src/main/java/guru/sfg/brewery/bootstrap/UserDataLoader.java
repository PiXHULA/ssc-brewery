package guru.sfg.brewery.bootstrap;


import guru.sfg.brewery.domain.security.Authority;
import guru.sfg.brewery.domain.security.Role;
import guru.sfg.brewery.domain.security.User;
import guru.sfg.brewery.repositories.security.AuthorityRepository;
import guru.sfg.brewery.repositories.security.RoleRepository;
import guru.sfg.brewery.repositories.security.UserRepository;
import guru.sfg.brewery.security.SfgPasswordEncoderFactories;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


@RequiredArgsConstructor
@Component
@Slf4j
public class UserDataLoader implements CommandLineRunner {


    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (authorityRepository.count() == 0)
            loadUserData();
    }

    private void loadUserData() {

        //beer auths
        Authority createBeer     = authorityRepository.save(Authority.builder().permission("beer.create").build());
        Authority readBeer       = authorityRepository.save(Authority.builder().permission("beer.read").build());
        Authority updateBeer     = authorityRepository.save(Authority.builder().permission("beer.update").build());
        Authority deleteBeer     = authorityRepository.save(Authority.builder().permission("beer.delete").build());

        //customer auths
        Authority createCustomer = authorityRepository.save(Authority.builder().permission("customer.create").build());
        Authority readCustomer   = authorityRepository.save(Authority.builder().permission("customer.read").build());
        Authority updateCustomer = authorityRepository.save(Authority.builder().permission("customer.update").build());
        Authority deleteCustomer = authorityRepository.save(Authority.builder().permission("customer.delete").build());

        //brewery auths
        Authority createBrewery  = authorityRepository.save(Authority.builder().permission("brewery.create").build());
        Authority readBrewery    = authorityRepository.save(Authority.builder().permission("brewery.read").build());
        Authority updateBrewery  = authorityRepository.save(Authority.builder().permission("brewery.update").build());
        Authority deleteBrewery  = authorityRepository.save(Authority.builder().permission("brewery.delete").build());

        //ROLES
        Role adminRole = roleRepository.save(Role.builder().roleName("ADMIN").build());
        Role customerRole = roleRepository.save(Role.builder().roleName("CUSTOMER").build());
        Role userRole = roleRepository.save(Role.builder().roleName("USER").build());

        //SETTING AUTH FOR EACH ROLE
        adminRole.setAuthorities(new HashSet<>(Set.of(
                createBeer,updateBeer,readBeer,deleteBeer,
                createCustomer,updateCustomer,readCustomer,deleteCustomer,
                createBrewery,updateBrewery,readBrewery,deleteBrewery)));
        customerRole.setAuthorities(new HashSet<>(Set.of(readBeer,readCustomer,readBrewery)));
        userRole.setAuthorities(new HashSet<>(Set.of(readBeer)));

        roleRepository.saveAll(List.of(adminRole,customerRole,userRole));

        userRepository.save(User.builder()
                .username("spring")
                .password(passwordEncoder.encode("wuru"))
                .role(adminRole)
                .build());

        userRepository.save(User.builder()
                .username("user")
                .password(SfgPasswordEncoderFactories.createDelegatingPasswordEncoder().encode("password"))
                .role(userRole)
                .build());

        userRepository.save(User.builder()
                .username("joakim")
                .password(SfgPasswordEncoderFactories.createDelegatingPasswordEncoder().encode("kung"))
                .role(customerRole)
                .build());


        log.info("Users loaded : "       + userRepository.count());
        log.info("Authorities loaded : " + authorityRepository.count());
        log.info("Roles loaded : "       + authorityRepository.count());

    }
}
