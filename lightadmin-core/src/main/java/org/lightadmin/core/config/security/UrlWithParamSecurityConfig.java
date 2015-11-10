package org.lightadmin.core.config.security;

import org.springframework.security.access.SecurityConfig;

public class UrlWithParamSecurityConfig extends SecurityConfig{

	private static final long serialVersionUID = -7232683490320252948L;
	private String parameter;
	
	public UrlWithParamSecurityConfig(String config,String parameter) {
		super(config);
		this.parameter = parameter;
	}
	
	public String getParameter() {
		return parameter;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((parameter == null) ? 0 : parameter.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		UrlWithParamSecurityConfig other = (UrlWithParamSecurityConfig) obj;
		if (parameter == null) {
			if (other.parameter != null)
				return false;
		} else if (!parameter.equals(other.parameter))
			return false;
		return true;
	}

}