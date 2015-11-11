package org.lightadmin.core.config.security.authorization.actionpathparser;

import javax.servlet.http.HttpServletRequest;

public class NewSimpleListPathParser extends BaseActionPathParser{
	
    private static final String newListResultForQuery = "queryFromServlet";
    private static final String newListResultForQueryJsp = "jsp/simpleQueryTemplate.jsp";
   
    public String doParse (String accessPath,HttpServletRequest request){
		logger.debug(">>>>>>>>doParse in:NewSimpleListPathParser"); 	
    	String resultActionPath = parseSimpleAccessSubmitPath(accessPath,request);	
    	return resultActionPath;
    }
    
    private String parseSimpleAccessSubmitPath(String accessPath,HttpServletRequest request) {
    	if(accessPath==null){
    		return null;
    	}
    	String resultSubmitPath = parseAccessSubmitPath(accessPath);
    	if(request.getParameter("queryEntityId") ==null || "".equals(request.getParameter("queryEntityId"))){
    		return null;
    	}else{
    		resultSubmitPath += "?queryEntityId="+request.getParameter("queryEntityId");
    	}
    	logger.debug(">>>>>>>>parseSimpleAccessSubmitPath resutl:"+resultSubmitPath);
    	return resultSubmitPath;
	}
	
	public boolean supports(String accesssPath){
		if(accesssPath.indexOf(newListResultForQuery) >-1 || accesssPath.indexOf(newListResultForQueryJsp) > -1){
			return true;
		}else{
		    return false;
		}
	}
	
}