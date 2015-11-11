package org.lightadmin.core.config.security.authorization.actionpathparser;

import javax.servlet.http.HttpServletRequest;

public class JspActionPathParser extends BaseActionPathParser{
	
    private static final String JSP_Suffix = ".jsp";
    private static final String listResultForQuery = "ListResultForQuery";
   
    public String doParse (String accessPath,HttpServletRequest request){
    	logger.debug(">>>>>>>>doParse in:JspActionPathParser");
    	String resultActionPath = parseAccessSubmitPath(accessPath);
    	return resultActionPath;
    }
	   
	public boolean supports(String accesssPath){
		if(accesssPath.indexOf(JSP_Suffix)>-1 && accesssPath.indexOf(listResultForQuery) < 0 && accesssPath.indexOf("jsp/simpleQueryTemplate") < 0){
			logger.debug("===========================================================");
			return true;
		}else{
		    return false;
		}
	}
	
}