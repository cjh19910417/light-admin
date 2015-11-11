package org.lightadmin.core.config.security.authorization.web;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.access.AccessDeniedHandler;

public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
    //~ Static fields/initializers =====================================================================================
    /**
     * @deprecated Use the value in {@link WebAttributes} directly.
     */
    @Deprecated
    public static final String SPRING_SECURITY_ACCESS_DENIED_EXCEPTION_KEY = WebAttributes.ACCESS_DENIED_403;
    private static final Log logger = LogFactory.getLog(AccessDeniedHandlerImpl.class);

    //~ Instance fields ================================================================================================

    private String errorPage;

    //~ Methods ========================================================================================================

    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException)
            throws IOException, ServletException {
        if (!response.isCommitted()) {
        	String contentType = request.getContentType();
        	if("application/json".equals(contentType)){
        		response.setContentType("text/html;charset=GBK");
        		response.getWriter().print("{\"errorcode\":\"Sec000\",\"errormessage\":\"��Ȩ����\"}");
    		    response.getWriter().flush();
    		    response.getWriter().close();
    		}else{
                if (errorPage != null) {
                    // Put exception into request scope (perhaps of use to a view)
                    request.setAttribute(WebAttributes.ACCESS_DENIED_403, accessDeniedException);

                    // Set the 403 status code.
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);

                    // forward to error page.
                    RequestDispatcher dispatcher = request.getRequestDispatcher(errorPage);
                    dispatcher.forward(request, response);
                } else {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, accessDeniedException.getMessage());
                }
    		}
        }
    }

    /**
     * The error page to use. Must begin with a "/" and is interpreted relative to the current context root.
     *
     * @param errorPage the dispatcher path to display
     *
     * @throws IllegalArgumentException if the argument doesn't comply with the above limitations
     */
    public void setErrorPage(String errorPage) {
        if ((errorPage != null) && !errorPage.startsWith("/")) {
            throw new IllegalArgumentException("errorPage must begin with '/'");
        }

        this.errorPage = errorPage;
    }
}
