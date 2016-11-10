/*
 * Copyright (C) 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ro.pippo.demo.jersey;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.kohsuke.MetaInfServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ro.pippo.core.WebServerInitializer;

import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;

/**
 * @author Decebal Suiu
 */
@MetaInfServices
public class JerseyInitializer implements WebServerInitializer {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void init(ServletContext servletContext) {
        ResourceConfig resourceConfig = createResourceConfig();

        // add jersey filter
        ServletRegistration.Dynamic jerseyServlet = servletContext.addServlet("jersey", new ServletContainer(resourceConfig));
        jerseyServlet.setLoadOnStartup(1);
        jerseyServlet.addMapping("/jersey/*");

        logger.debug("Jersey initialized");
    }

    @Override
    public void destroy(ServletContext servletContext) {
        // do nothing
    }

    private ResourceConfig createResourceConfig() {
        ResourceConfig resourceConfig = new ResourceConfig();
        resourceConfig.register(HelloResource.class);
        resourceConfig.register(ContactResource.class);

        return resourceConfig;
    }

}
