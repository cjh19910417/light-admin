package org.lightadmin.core.config.security.authorization.vote;

import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;


public abstract class MaxPermitBaseVoter implements AccessDecisionVoter{
	
	protected static final Log logger = LogFactory.getLog(MaxPermitBaseVoter.class); 
	private AccessDecisionVoter voter;

	@Override
	public int vote(Authentication authentication, Object object, Collection attributes) {
		int voteResult = doVote(authentication,object,attributes);
		if(voteResult < 1&&voter!=null){
			voteResult =  voter.vote(authentication,object,attributes);
		}
		logger.debug(">>>>>>>>doVote result:"+voteResult);
		return voteResult;
    }
	
	public void setVoter(AccessDecisionVoter voter) {
		this.voter = voter;
	}
	
	public abstract int doVote(Authentication authentication, Object object, Collection<ConfigAttribute> attributes);

	@Override
	public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public boolean supports(Class clazz) {
        return true;
    }

}
