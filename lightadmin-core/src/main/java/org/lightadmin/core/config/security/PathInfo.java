package org.lightadmin.core.config.security;

public class PathInfo {
	private String url;
	private String actionPath;
	
	public PathInfo(String url,String actionPath){
		this.url = url;
		this.actionPath = actionPath;
	}
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getActionPath() {
		return actionPath;
	}
	public void setActionPath(String actionPath) {
		this.actionPath = actionPath;
	}

}