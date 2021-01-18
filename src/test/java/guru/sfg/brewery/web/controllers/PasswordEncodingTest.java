package guru.sfg.brewery.web.controllers;

import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.LdapShaPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.util.DigestUtils;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder.BCryptVersion.*;

public class PasswordEncodingTest {

    static final String PASSWORD = "password";

    @Test
    void bCryptExample() {
//        PasswordEncoder bCrypt = new BCryptPasswordEncoder($2B,12);
//        PasswordEncoder bCrypt = new BCryptPasswordEncoder(15);
        PasswordEncoder bCrypt = new BCryptPasswordEncoder();
        System.out.println(bCrypt.encode(PASSWORD));
        System.out.println(bCrypt.encode(PASSWORD));

        String encodedPwd = bCrypt.encode(PASSWORD);

        assertTrue(bCrypt.matches(PASSWORD, encodedPwd));
    }

    @Test
    void sha256Example() {
        PasswordEncoder sha256 = new StandardPasswordEncoder();
        System.out.println(sha256.encode(PASSWORD));
        System.out.println(sha256.encode(PASSWORD));

        String encodedPwd = sha256.encode(PASSWORD);

        assertTrue(sha256.matches(PASSWORD, encodedPwd));
    }

    @Test
    void ldapExample() {
        PasswordEncoder ldap = new LdapShaPasswordEncoder();
        System.out.println(ldap.encode(PASSWORD));
        System.out.println(ldap.encode("kung"));
        System.out.println(ldap.encode("KUNG"));

        String encodedPwd = ldap.encode(PASSWORD);

        assertTrue(ldap.matches(PASSWORD, encodedPwd));
    }

    @Test
    void noOpExample() {
        PasswordEncoder noOp = NoOpPasswordEncoder.getInstance();
        System.out.println(noOp.encode(PASSWORD));
    }

    @Test
    void hashingExample() {
        System.out.println(DigestUtils.md5DigestAsHex(PASSWORD.getBytes()));
        System.out.println(DigestUtils.md5DigestAsHex(PASSWORD.getBytes()));

        String salted = PASSWORD + "ThisIsMySaltValue";
        System.out.println(DigestUtils.md5DigestAsHex(salted.getBytes()));
    }

}
