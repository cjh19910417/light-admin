package org.lightadmin.core.config.security.authentication.commonauth;

import java.util.Collection;

import org.lightadmin.core.config.security.authentication.LocalAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;


public class UsernamePasswordAuthenticationToken extends LocalAuthenticationToken {

	private static final long serialVersionUID = 7151802449119575454L;

	public UsernamePasswordAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
		super(principal,credentials,authorities);
    }

	public UsernamePasswordAuthenticationToken(Object credentials, Collection<? extends GrantedAuthority> authorities) {
		super(credentials,authorities);
    }
	
	public UsernamePasswordAuthenticationToken(Object principal, Object credentials) {
		super(principal,credentials);
	}
	public UsernamePasswordAuthenticationToken(Object credentials) {
		super(credentials);
    }
}
