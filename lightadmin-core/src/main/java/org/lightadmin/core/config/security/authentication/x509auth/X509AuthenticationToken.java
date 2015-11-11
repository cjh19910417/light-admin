package org.lightadmin.core.config.security.authentication.x509auth;

import java.util.Collection;

import org.lightadmin.core.config.security.authentication.LocalAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;


public class X509AuthenticationToken extends LocalAuthenticationToken {
	private static final long serialVersionUID = -1355405902432356180L;
	
	/*private Object credentials;
	private Object principal;*/
	
	public X509AuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
		super(principal,credentials,authorities);
    }

	public X509AuthenticationToken(Object credentials, Collection<? extends GrantedAuthority> authorities) {
		super(credentials,authorities);
    }
	
	public X509AuthenticationToken(Object credentials) {
		super(credentials);
    }
	
	/*public Object getCredentials() {
        return this.credentials;
    }

    public Object getPrincipal() {
        return this.principal;
    }
    
    void setPrincipal(Object principal) {
    	this.principal = principal;
    }*/

}
