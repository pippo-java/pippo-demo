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

import ro.pippo.core.WebServer;
import ro.pippo.demo.common.Contact;
import ro.pippo.demo.common.ContactService;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

/**
 * @author Decebal Suiu
 */
@Path("/contact")
public class ContactResource {

    @Context
    private ServletContext servletContext;

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Contact getContact(@PathParam("id") int id ) {
        return getContactService().getContact(id);
    }

    private ContactService getContactService() {
        // retrieve contact service from the application instance
        return ((JerseyApplication) servletContext.getAttribute(WebServer.PIPPO_APPLICATION)).getContactService();
    }

}
