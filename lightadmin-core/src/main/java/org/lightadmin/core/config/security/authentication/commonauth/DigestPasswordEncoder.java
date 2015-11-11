package org.lightadmin.core.config.security.authentication.commonauth;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.mail.internet.MimeUtility;

import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.encoding.PasswordEncoder;

public class DigestPasswordEncoder implements PasswordEncoder {

	public String encodePassword(String rawPass, Object salt)
			throws DataAccessException {
		try {
			if(salt==null||"".equals(salt)){
				return digestString(rawPass, "SHA");
			}else{
				return digestString(rawPass, salt.toString());
			}
    	    
    	} catch (Exception e) {
            throw new RuntimeException("Fatal error: " + e);
        }
	}

	private String digestString(String pass, String algorithm )throws NoSuchAlgorithmException  {

         MessageDigest md;
         ByteArrayOutputStream bos;

         try {
             md = MessageDigest.getInstance(algorithm);
             byte[] digest = md.digest(pass.getBytes("iso-8859-1"));
             bos = new ByteArrayOutputStream();
             OutputStream encodedStream = MimeUtility.encode(bos, "base64");
             encodedStream.write(digest);
             return bos.toString("iso-8859-1");
         } catch (Exception e) {
             throw new RuntimeException("Fatal error: " + e);
         } 
    }
	public boolean isPasswordValid(String encPass, String rawPass, Object salt)
			throws DataAccessException {
		return true;
	}

}
