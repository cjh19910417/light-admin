package org.lightadmin.core.config.security.authorization.actionpathparser;

import javax.servlet.http.HttpServletRequest;

public class SimpleListPathParser extends BaseActionPathParser{
	
    private static final String listResultForQuery = "ListResultForQuery";
    private static final String formSN = "formSN";
   
    public String doParse (String accessPath,HttpServletRequest request){
    	logger.debug(">>>>>>>>doParse in:JspActionPathParser"); 	
    	String resultActionPath = parseSimpleAccessSubmitPath(accessPath,request);	
    	return resultActionPath;
    }
    
    
    private String parseSimpleAccessSubmitPath(String accessPath,HttpServletRequest request) {
    	if(accessPath==null){
    		return null;
    	}
    	String resultSubmitPath = parseAccessSubmitPath(accessPath);
    	
    	String[] paramters = accessPath.split("&");
    	boolean formSNInPath = false;
    	for(String paramter:paramters){
    		if(paramter.indexOf(formSN)>0){
    			resultSubmitPath = paramter;
    			formSNInPath = true;
    			break;
    		}
    	}
    	if(!formSNInPath){		
    		String formSNPareterValue = request.getParameter(formSN);
    		resultSubmitPath = resultSubmitPath + "?" + formSN + "=" +formSNPareterValue;
    		
    	}
    	return resultSubmitPath;
	}
	
	public boolean supports(String accesssPath){
		if(accesssPath.indexOf(listResultForQuery) >-1){
			return true;
		}else{
		    return false;
		}
	}
	
}