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

import ro.pippo.controller.Controller;
import ro.pippo.controller.GET;
import ro.pippo.controller.Named;
import ro.pippo.controller.NoCache;
import ro.pippo.controller.Path;
import ro.pippo.controller.Produces;
import ro.pippo.controller.extractor.Header;
import ro.pippo.controller.extractor.Param;
import ro.pippo.controller.extractor.Session;
import ro.pippo.demo.common.Contact;
import ro.pippo.demo.common.ContactService;
import ro.pippo.demo.common.InMemoryContactService;
import ro.pippo.metrics.Metered;
import ro.pippo.metrics.Timed;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Decebal Suiu
 */
@Path("/contacts")
@Logging
public class ContactsController extends Controller {

    private ContactService contactService;

    public ContactsController() {
        contactService = new InMemoryContactService();
    }

    @GET
    @Named("index")
//    @Produces(Produces.HTML)
    @Metered
    @Logging
    public void index() {
        // inject "user" attribute in session
        getRouteContext().setSession("user", "decebal");

        getResponse()
            .bind("contacts", contactService.getContacts())
            .render("contacts");
    }

    @GET("/uriFor/{id: [0-9]+}")
    @Named("uriFor")
    @Produces(Produces.TEXT)
    @Timed
    public String uriFor(@Param int id, @Header String host, @Session String user) {
        System.out.println("id = " + id);
        System.out.println("host = " + host);
        System.out.println("user = " + user);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("id", id);

        String uri = getApplication().getRouter().uriFor("api.get", parameters);

        return "id = " + id + "; uri = " + uri;
    }

    @GET("/api")
    @Named("api.getAll")
    @Produces(Produces.JSON)
    @NoCache
    public List<Contact> getAll() {
        return contactService.getContacts();
    }

    @GET("/api/{id: [0-9]+}")
    @Named("api.get")
    @Produces(Produces.JSON)
    public Contact get(@Param int id) {
        return contactService.getContact(id);
    }

}
