package org.lightadmin.core.config.security.authorization.actionpathparser;

import javax.servlet.http.HttpServletRequest;

public class SpringMVCActionPathParaser extends BaseActionPathParser{
	
    private static final String Spring_MVC_Enter_Suffix = "mvc";
   
    public String doParse (String accessPath,HttpServletRequest request){
    	logger.debug(">>>>>>>>doParse in:SpringMVCActionPathParaser");
    	String resultActionPath = parseAccessSubmitPath(accessPath);
    	return resultActionPath;
    }
	
    
	public boolean supports(String accesssPath){
		String acceptSubmitPath = parseAccessSubmitPath(accesssPath);
		if(acceptSubmitPath.indexOf(Spring_MVC_Enter_Suffix)>-1){
			return true;
		}else{
		    return false;
		}
	}
	
}
