/*
 * Copyright (C) 2014 the original author or authors.
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
package ro.pippo.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ro.pippo.controller.SingletonControllerFactory;
import ro.pippo.core.route.RouteContext;
import ro.pippo.core.route.RouteHandler;
import ro.pippo.metrics.Counted;
import ro.pippo.metrics.Timed;

/**
 * @author Decebal Suiu
 */
public class ControllerApplication extends ro.pippo.controller.ControllerApplication {

    private static final Logger log = LoggerFactory.getLogger(ControllerApplication.class);

    @Override
    protected void onInit() {
        // if you wish a singleton for each controller
//        setControllerFactory(new SingletonControllerFactory());

        // add routes for static content
        addPublicResourceRoute();
        addWebjarsResourceRoute();

        // audit filter
        ALL("/(?!webjars).*", routeContext -> {
            log.debug("Request for {} '{}'", routeContext.getRequestMethod(), routeContext.getRequestUri());
            routeContext.next();
        });

        // add route
        GET("/", new RouteHandler() {

            @Timed
            @Counted
            @Override
            public void handle(RouteContext routeContext) {
                routeContext.send("Hello");
            }

        });

        // add controllers
        addControllers(CollectionsController.class);
        addControllers(ContactsController.class);
//        addControllers(new ContactsController());
        addControllers(FilesController.class);
    }

}
