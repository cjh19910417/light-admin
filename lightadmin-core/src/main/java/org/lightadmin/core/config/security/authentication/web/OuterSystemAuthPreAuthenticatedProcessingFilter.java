package org.lightadmin.core.config.security.authentication.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

public class OuterSystemAuthPreAuthenticatedProcessingFilter extends
		AbstractPreAuthenticatedProcessingFilter {
	private String outerSystemLoginFilterAddress;

	@Override
	protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
//		UserPrincipal userPrincipal = SPUtil.getUserPrincipal(request);
//		Object principal = userPrincipal == null ? null : userPrincipal.getName();
//        if (logger.isDebugEnabled()) {
//            logger.debug("PreAuthenticated J2EE principal: " + principal);
//        }
//        return principal;
		return null;
	}

	@Override
	protected Object getPreAuthenticatedCredentials(HttpServletRequest request) {
		return "N/A";
	}
	
	protected boolean requiresAuthentication(HttpServletRequest request, HttpServletResponse response) {
        String uri = request.getRequestURI();
        int pathParamIndex = uri.indexOf(';');

        if (pathParamIndex > 0) {
            uri = uri.substring(0, pathParamIndex);
        }

        if ("".equals(request.getContextPath())) {
            return uri.endsWith(outerSystemLoginFilterAddress);
        }

        return uri.endsWith(request.getContextPath() + outerSystemLoginFilterAddress);
    }

	public String getOuterSystemLoginFilterAddress() {
		return outerSystemLoginFilterAddress;
	}

	public void setOuterSystemLoginFilterAddress(
			String outerSystemLoginFilterAddress) {
		this.outerSystemLoginFilterAddress = outerSystemLoginFilterAddress;
	}

}
