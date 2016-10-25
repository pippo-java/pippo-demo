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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.EnumSet;

/**
 * @author Decebal Suiu
 */
public class FilterAppender implements ServletContextListener {

    private static final Logger log = LoggerFactory.getLogger(FilterAppender.class);

    public static final String FILTER_NAME = "logging";

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();
        FilterRegistration.Dynamic loggingFilter = servletContext.addFilter(FILTER_NAME, LoggingFilter.class);
        // other possible settings for loggingFilter
        EnumSet<DispatcherType> dispatcherTypes = EnumSet.of(DispatcherType.REQUEST);
        loggingFilter.addMappingForServletNames(dispatcherTypes, true, ServletAppender.SERVLET_NAME);

        log.debug("Added filter '{}'", loggingFilter.getClassName());
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // do nothing
    }

}
