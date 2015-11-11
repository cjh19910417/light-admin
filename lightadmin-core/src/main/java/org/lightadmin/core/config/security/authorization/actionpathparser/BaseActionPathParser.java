package org.lightadmin.core.config.security.authorization.actionpathparser;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.lightadmin.core.config.security.authorization.ActionPathParser;


public abstract class BaseActionPathParser implements ActionPathParser {
	protected static final Log logger = LogFactory.getLog(BaseActionPathParser.class); 
	private ActionPathParser nextActionPathParser;
	
	public String parse (String accessPath, HttpServletRequest request){
		logger.debug(">>>>>>>>>in "+this.getClass().getName()+" start to parse actionPath: "+accessPath);
		String resultActionPath = "";
		if(supports(accessPath)){
			resultActionPath =  doParse (accessPath,request);
			//ΪʲôresultActionPath����ֵ���д���null�Ŀ����У�
			if(null == resultActionPath) {
				resultActionPath = "";
			} else {
				resultActionPath = resultActionPath.trim();
			}
			 if(resultActionPath.length()>0&&resultActionPath.charAt(0)=='/'){
				 resultActionPath = resultActionPath.substring(1);
		     }
		}else if(nextActionPathParser!=null){
			resultActionPath =  nextActionPathParser.parse(accessPath,request);
		}else{
			resultActionPath =  accessPath;
		}
		logger.debug(">>>>>>>>doParse result:"+resultActionPath);
		return resultActionPath;
	}
	
	protected String parseAccessSubmitPath(String accessPath){
		String resultSubmitPath = accessPath;
		int submitPathEndIndex = accessPath.lastIndexOf("?");
		if(submitPathEndIndex>-1){
			resultSubmitPath = accessPath.substring(0, submitPathEndIndex);
		}
		return resultSubmitPath;
	}
	
	protected String parseAccessParameter(String accessPath){
		String resultAccessParameterPath = "";
		int accessParameterPathStartIndex = accessPath.lastIndexOf("?");
		if(accessParameterPathStartIndex>-1&&accessParameterPathStartIndex<accessPath.length()-1){
			resultAccessParameterPath = accessPath.substring(accessParameterPathStartIndex+1);
		}
		return resultAccessParameterPath;
	}
	protected abstract String doParse (String accessPath,HttpServletRequest request);
	
	public abstract boolean supports(String accessPath);

	public void setNextActionPathParser(ActionPathParser nextActionPathParser) {
		this.nextActionPathParser = nextActionPathParser;
	}
}
