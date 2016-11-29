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
package ro.pippo.demo.ajax;

import ro.pippo.core.Application;
import ro.pippo.core.RedirectHandler;
import ro.pippo.demo.common.Contact;
import ro.pippo.demo.common.ContactService;
import ro.pippo.demo.common.InMemoryContactService;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Decebal Suiu
 */
public class AjaxApplication extends Application {

    private ContactService contactService;

    private long pageAccessTime;
    private long increment;

    @Override
    protected void onInit() {
        contactService = new InMemoryContactService();

        getRouter().ignorePaths("/favicon.ico");

        // add routes for static content
        addPublicResourceRoute();
        addWebjarsResourceRoute();

//        GET("/", new RedirectHandler("/simple"));
        GET("/", new RedirectHandler("/crud"));

        GET("/simple", routeContext -> {
            pageAccessTime = System.currentTimeMillis();
            routeContext.render("simple");
        });

        GET("/seconds", routeContext -> {
            long seconds = (System.currentTimeMillis() - pageAccessTime) / 1000;
            routeContext.getResponse().send("You have been on this page for {} seconds...", seconds);
        });

        POST("/increment", routeContext -> {
            increment++;
            routeContext.getResponse().send("Click Me! ({})", increment);
        });

        GET("/crud", routeContext -> {
            routeContext.setLocal("contacts", contactService.getContacts());
            routeContext.render("crud");
        });

        GET("/contact/{id}", routeContext -> {
            int id = routeContext.getParameter("id").toInt(0);
            Contact contact = (id > 0) ? contactService.getContact(id) : new Contact();
            routeContext.setLocal("contact", contact);

            Map<String, Object> parameters = new HashMap<>();
            if (id > 0) {
                parameters.put("id", id);
            }
//                routeContext.setLocal("saveUrl", getRouter().uriFor("/contact", parameters));
            routeContext.setLocal("saveUrl", getRouter().uriFor("postContact", parameters));

            routeContext.render("view/contact");
        });

        POST("/contact", routeContext -> {
            Contact contact = routeContext.createEntityFromParameters(Contact.class);
            contactService.save(contact);

            routeContext.getResponse().header("X-IC-Transition", "none");
            routeContext.setLocal("contacts", contactService.getContacts());
            routeContext.render("view/contacts");
        }).named("postContact");

        DELETE("/contact/{id}", routeContext -> {
            int id = routeContext.getParameter("id").toInt(0);
            contactService.delete(id);

            routeContext.getResponse().header("X-IC-Remove", "true").commit();
        });
    }

}
