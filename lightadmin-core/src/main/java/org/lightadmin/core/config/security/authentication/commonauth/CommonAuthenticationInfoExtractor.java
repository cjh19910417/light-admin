package org.lightadmin.core.config.security.authentication.commonauth;

import javax.servlet.http.HttpServletRequest;

import org.lightadmin.core.config.security.authentication.AuthenticationInfoExtractor;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;


public class CommonAuthenticationInfoExtractor implements
        AuthenticationInfoExtractor {

	public static final String SECURITY_FORM_USERNAME_KEY = "j_username";
    public static final String SECURITY_FORM_PASSWORD_KEY = "j_password";
    
    
	private String usernameParameter = SECURITY_FORM_USERNAME_KEY;
    private String passwordParameter = SECURITY_FORM_PASSWORD_KEY;
    
    private AuthenticationDetailsSource authenticationDetailsSource = new WebAuthenticationDetailsSource();
    
	public Authentication extractAuthenticationInfo(HttpServletRequest request) {
		String username = obtainUsername(request);
        String password = obtainPassword(request);
        if (username == null) {
            username = "";
        }

        if (password == null) {
            password = "";
        }
        request.setAttribute(usernameParameter, username);
        request.setAttribute(passwordParameter, password);
        
        username = username.trim();
        Authentication authRequest = new UsernamePasswordAuthenticationToken(username, password);
        
        // Place the last username attempted into HttpSession for views
        /*HttpSession session = request.getSession(false);

        if (session != null || getAllowSessionCreation()) {
            request.getSession().setAttribute(SECURITY_LAST_USERNAME_KEY, TextEscapeUtils.escapeEntities(username));
        }*/

        // Allow subclasses to set the "details" property
        setDetails(request, (UsernamePasswordAuthenticationToken)authRequest);
		return authRequest;
	}
	
	/**
     * Enables subclasses to override the composition of the password, such as by including additional values
     * and a separator.<p>This might be used for example if a postcode/zipcode was required in addition to the
     * password. A delimiter such as a pipe (|) should be used to separate the password and extended value(s). The
     * <code>AuthenticationDao</code> will need to generate the expected password in a corresponding manner.</p>
     *
     * @param request so that request attributes can be retrieved
     *
     * @return the password that will be presented in the <code>Authentication</code> request token to the
     *         <code>AuthenticationManager</code>
     */
    protected String obtainPassword(HttpServletRequest request) {
        return request.getParameter(passwordParameter);
    }

    /**
     * Enables subclasses to override the composition of the username, such as by including additional values
     * and a separator.
     *
     * @param request so that request attributes can be retrieved
     *
     * @return the username that will be presented in the <code>Authentication</code> request token to the
     *         <code>AuthenticationManager</code>
     */
    protected String obtainUsername(HttpServletRequest request) {
        return request.getParameter(usernameParameter);
    }

    /**
     * Provided so that subclasses may configure what is put into the authentication request's details
     * property.
     *
     * @param request that an authentication request is being created for
     * @param authRequest the authentication request object that should have its details set
     */
    protected void setDetails(HttpServletRequest request, UsernamePasswordAuthenticationToken authRequest) {
        authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
    }

	public AuthenticationDetailsSource getAuthenticationDetailsSource() {
		return authenticationDetailsSource;
	}

	public void setAuthenticationDetailsSource(
			AuthenticationDetailsSource authenticationDetailsSource) {
		this.authenticationDetailsSource = authenticationDetailsSource;
	}

	public String getUsernameParameter() {
		return usernameParameter;
	}

	public void setUsernameParameter(String usernameParameter) {
		this.usernameParameter = usernameParameter;
	}

	public String getPasswordParameter() {
		return passwordParameter;
	}

	public void setPasswordParameter(String passwordParameter) {
		this.passwordParameter = passwordParameter;
	}

}
