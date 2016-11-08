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
import ro.pippo.core.Application;
import ro.pippo.demo.common.Contact;
import ro.pippo.demo.common.ContactService;
import ro.pippo.demo.common.InMemoryContactService;

/**
 * @author Decebal Suiu
 */
public class JerseyApplication extends Application {

    private ContactService contactService;
    private ResourceConfig resourceConfig;

    @Override
    protected void onInit() {
        // add routes
        GET("/hello", routeContext -> routeContext.text().send("Hello from Pippo!"));
        GET("/contact/{id}", routeContext -> {
            int id = routeContext.getParameter("id").toInt(); // read parameter "id"
            Contact contact = getContactService().getContact(id);
            routeContext.json().send(contact);
        });
    }

    public final ContactService getContactService() {
        if (contactService == null) {
            contactService = createContactService();
        }

        return contactService;
    }

    public final ResourceConfig getResourceConfig() {
        if (resourceConfig == null) {
            resourceConfig = createResourceConfig();
        }

        return resourceConfig;
    }

    public void setContactService(ContactService contactService) {
        this.contactService = contactService;
    }

    protected ContactService createContactService() {
        return new InMemoryContactService();
    }

    protected ResourceConfig createResourceConfig() {
        ResourceConfig resourceConfig = new ResourceConfig();
        resourceConfig.register(HelloResource.class);
        resourceConfig.register(ContactResource.class);

        return resourceConfig;
    }

}
