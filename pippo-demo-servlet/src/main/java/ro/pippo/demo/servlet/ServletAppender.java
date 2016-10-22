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
package ro.pippo.demo.servlet;

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
public class ServletAppender implements WebServerInitializer {

    private static final Logger log = LoggerFactory.getLogger(ServletAppender.class);

    @Override
    public void init(ServletContext servletContext) {
        ServletRegistration.Dynamic demoServlet = servletContext.addServlet("demo", DemoServlet.class);
        demoServlet.setLoadOnStartup(1);
        demoServlet.addMapping("/demo");
        // other possible settings for demoServlet

        log.debug("Added servlet '{}' to '{}'", demoServlet.getClassName(), demoServlet.getMappings().iterator().next());
    }

    @Override
    public void destroy(ServletContext servletContext) {
        // do nothing
    }

}
