package org.lightadmin.core.config.security.authorization.vote;

import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;

public class PermitAllVoter implements AccessDecisionVoter {

	private static final String PERMIT_ALL = "permitAll";

	protected static final Log logger = LogFactory.getLog(MaxPermitBaseVoter.class);
	private AccessDecisionVoter voter;


	@Override
	public boolean supports(ConfigAttribute configAttribute) {
		return true;
	}

	@Override
	public int vote(Authentication authentication, Object object, Collection attributes) {
		int voteResult = doVote(authentication,object,attributes);
		if(voteResult < 1&&voter!=null){
			voteResult =  voter.vote(authentication,object,attributes);
		}
		logger.debug(">>>>>>>>doVote result:"+voteResult);
		return voteResult;
	}

	public int doVote(Authentication authentication, Object object, Collection<ConfigAttribute> attributes) {
		logger.debug(">>>>>>>>doVote in:PermitAllVoter");
		int voteResult = -1;
		for(ConfigAttribute configAttribute : attributes){
			if(configAttribute.getAttribute().equals(PERMIT_ALL)){//最高权限，可以访问所有资源
				voteResult = 1;
				break;
			}
		}
		return voteResult;
	}

	public void setVoter(AccessDecisionVoter voter) {
		this.voter = voter;
	}

	@Override
	public boolean supports(Class aClass) {
		return true;
	}
}
