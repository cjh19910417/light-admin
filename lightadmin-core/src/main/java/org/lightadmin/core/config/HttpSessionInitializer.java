package org.lightadmin.core.config;

import org.lightadmin.core.util.LightAdminConfigurationUtils;
import org.springframework.session.web.context.AbstractHttpSessionApplicationInitializer;

// tag::class[]
public class HttpSessionInitializer extends AbstractHttpSessionApplicationInitializer {
    protected String getDispatcherWebApplicationContextSuffix() {
        return LightAdminConfigurationUtils.LIGHT_ADMIN_DISPATCHER_NAME;
    }
}