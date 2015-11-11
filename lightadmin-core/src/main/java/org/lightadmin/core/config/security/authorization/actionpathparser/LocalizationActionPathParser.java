package org.lightadmin.core.config.security.authorization.actionpathparser;

import javax.servlet.http.HttpServletRequest;

// import java.util.ArrayList;
// import java.util.List;

/**
 * @author Mancy Chan
 * @version 1.0
 * @since 2011-12-29
 */
public class LocalizationActionPathParser extends BaseActionPathParser {

	public static final String	SUFFIX_OF_ACTION	= ".action";

	/*private static List<String>	suffixes	= new ArrayList<String>();

	static {
		suffixes.add(".action");
		suffixes.add(".query");
		suffixes.add(".page");
		suffixes.add(".function");
	}*/

	@Override
	  public String doParse (String accessPath,HttpServletRequest request){

		logger.info("#####RequestURL>>>>>" + accessPath);

		String parseResult = super.parseAccessSubmitPath(accessPath);
		logger.info("#####ParseResult>>>>>" + parseResult);

		return parseResult;
	}

	@Override
	public boolean supports(String accessPath) {

		boolean flag = false;

		String requestURI = super.parseAccessSubmitPath(accessPath);
		/*for (String suffix : suffixes) {
			if (requestURI.contains(suffix)) {
				flag = true;
				break;
			}
		}*/
		if (requestURI.lastIndexOf(SUFFIX_OF_ACTION) > -1) {
			flag = true;
		}

		return flag;
	}
}
