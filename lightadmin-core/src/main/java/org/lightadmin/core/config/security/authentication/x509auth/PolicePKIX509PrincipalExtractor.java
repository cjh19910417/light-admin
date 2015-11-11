package org.lightadmin.core.config.security.authentication.x509auth;

import java.security.cert.X509Certificate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.web.authentication.preauth.x509.X509PrincipalExtractor;
import org.springframework.util.Assert;

public class PolicePKIX509PrincipalExtractor implements X509PrincipalExtractor {

	protected final Log logger = LogFactory.getLog(getClass());
	private Pattern subjectDnPattern;
	
	public PolicePKIX509PrincipalExtractor() {
        setSubjectDnRegex("CN=(.*),?");
    }
	@Override
	public Object extractPrincipal(X509Certificate certificate) {

        
        String subjectDN = certificate.getSubjectDN().getName();

        logger.debug("Subject DN is '" + subjectDN + "'");

        Matcher matcher = subjectDnPattern.matcher(subjectDN);

        if (!matcher.find()) {
            throw new BadCredentialsException("DaoX509AuthoritiesPopulator.noMatching"+subjectDN
            		+"No matching pattern was found in subject DN: {0}");
        }

        if (matcher.groupCount() != 1) {
            throw new IllegalArgumentException("Regular expression must contain a single group ");
        }

        String userInfo = matcher.group(1);
        String personIDCode = "";
        if(userInfo!=null){
        	personIDCode = userInfo.replaceAll("[^0-9|Xx]","").trim();
        }
        
        logger.debug("Extracted Principal name is '" + personIDCode + "'");

        return personIDCode;
        
	}
	
	public void setSubjectDnRegex(String subjectDnRegex) {
        Assert.hasText(subjectDnRegex, "Regular expression may not be null or empty");
        subjectDnPattern = Pattern.compile(subjectDnRegex, Pattern.CASE_INSENSITIVE);
    }

}
