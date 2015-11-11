package org.lightadmin.core.config.security.authentication.x509auth;

import java.security.cert.X509Certificate;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.lightadmin.core.config.security.authentication.AuthenticationInfoExtractor;
import org.springframework.security.core.Authentication;


public class X509AuthenticationInfoExtractor implements
        AuthenticationInfoExtractor {

	protected final Log logger = LogFactory.getLog(getClass());
	
	public Authentication extractAuthenticationInfo(HttpServletRequest request) {
		X509Certificate cert = extractClientCertificate(request);
		Authentication authRequest = new X509AuthenticationToken(cert);
		return authRequest;
	}
	
	private X509Certificate extractClientCertificate(HttpServletRequest request) {
        X509Certificate[] certs = (X509Certificate[]) request.getAttribute("javax.servlet.request.X509Certificate");

        if (certs != null && certs.length > 0) {
            if (logger.isDebugEnabled()) {
                logger.debug("X.509 client authentication certificate:" + certs[0]);
            }

            return certs[0];
        }

        if (logger.isDebugEnabled()) {
            logger.debug("No client certificate found in request.");
        }

        return null;
    }

}
