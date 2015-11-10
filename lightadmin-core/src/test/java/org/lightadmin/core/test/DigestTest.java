package org.lightadmin.core.test;

import org.junit.Test;
import org.lightadmin.core.config.util.DigestPasswordEncoder;
import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;
import org.springframework.security.authentication.encoding.PasswordEncoder;

import java.security.NoSuchAlgorithmException;

/**
 * Created by chenjianhua on 2015/11/10 0010.
 */
public class DigestTest {
    @Test public void testDigest() throws NoSuchAlgorithmException {
        PasswordEncoder passwordEncoder = new DigestPasswordEncoder();

        MessageDigestPasswordEncoder md = new MessageDigestPasswordEncoder("SHA");
        String encode = md.encodePassword("123456",null);
        System.out.println("encode = " + encode);

        encode = passwordEncoder.encodePassword("123456", null);
        System.out.println("encode = " + encode);
    }
}
