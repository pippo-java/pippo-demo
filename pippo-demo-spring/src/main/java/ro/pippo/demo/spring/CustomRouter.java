/*
 * Copyright (C) 2015 the original author or authors.
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
package ro.pippo.demo.spring;

import ro.pippo.core.route.DefaultRouter;
import ro.pippo.core.route.Route;

/**
 * @author Decebal Suiu
 */
public class CustomRouter extends DefaultRouter {

    public CustomRouter() {
        super();
    }

    @Override
    public void addRoute(Route route) {
        System.out.println("CustomRouter.addRoute");
        super.addRoute(route);
    }

}
