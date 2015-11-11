package org.lightadmin.core.config.security.authentication;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class LocalAuthenticationToken extends
		AbstractAuthenticationToken {

	private static final long serialVersionUID = 7097624348743907897L;

	public LocalAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
		this(credentials,authorities);
		this.principal = principal;
    }

	public LocalAuthenticationToken(Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.credentials = credentials;
        super.setAuthenticated(true);
    }
	
	public LocalAuthenticationToken(Object principal, Object credentials) {
		this(credentials);
        this.principal = principal;
    }
	
	public LocalAuthenticationToken(Object credentials) {
        this(credentials,null);
        setAuthenticated(false);
    }
	
	public LocalAuthenticationToken(
			Collection<? extends GrantedAuthority> authorities) {
		super(authorities);
	}

	private Object credentials;
	private Object principal;
	
	public Object getCredentials() {
		return this.credentials;
	}

	@Override
	public Object getPrincipal() {
		return this.principal;
	}

	public void setCredentials(Object credentials) {
		this.credentials = credentials;
	}

	public void setPrincipal(Object principal) {
		this.principal = principal;
	}

}
