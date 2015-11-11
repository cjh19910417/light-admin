package org.lightadmin.core.config.security.authorization.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;

public class ForbiddenEntryPointForJsonResponse implements
		AuthenticationEntryPoint {

	private static final Log logger = LogFactory.getLog(Http403ForbiddenEntryPoint.class);
	
	public void commence(HttpServletRequest request,
			HttpServletResponse response, AuthenticationException authException)
			throws IOException, ServletException {
		if (logger.isDebugEnabled()) {
            logger.debug("Pre-authenticated entry point called. Rejecting access");
        }
		String contentType = request.getContentType();
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		if("application/json".equals(contentType)){
    		response.setContentType("text/html;charset=GBK");
    		response.getWriter().print("{\"errorcode\":\"Sec000\",\"errormessage\":\"��Ȩ����\"}");
		    response.getWriter().flush();
		    response.getWriter().close();
		}else{
            httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied");
		}
	}

}
