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

import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;
import ro.pippo.demo.common.Contact;
import ro.pippo.demo.common.ContactService;
import ro.pippo.test.PippoRule;
import ro.pippo.test.PippoTest;

import static org.junit.Assert.assertEquals;

/**
 * @author Decebal Suiu
 */
public class AppTest extends PippoTest {

    @Rule
    public PippoRule pippoRule = new PippoRule(JerseyDemo.pippo());

    @Test
    public void testPippoHello() {
        // set base path for pippo routes
        basePath = "/pippo";

        Response response = get("/hello");
        response.then()
            .statusCode(200)
            .contentType(ContentType.TEXT);
        assertEquals("Hello from Pippo!", response.asString());
    }

    @Test
    public void testJerseyHello() {
        // set base path for jersey resources
        basePath = "/jersey";

        Response response = get("/hello");
        response.then()
            .statusCode(200)
            .contentType(ContentType.TEXT);
        assertEquals("Hello from Jersey!", response.asString());
    }

    @Test
    public void testPippoGetContact() {
        // mock service
        mockContactService();

        // test pippo (web) aspects below
        basePath = "/pippo";
        Response response = get("/contact/1");
        response.then()
            .statusCode(200)
            .contentType(ContentType.JSON);

        Contact contact = response.as(Contact.class);
        assertEquals("Maria", contact.getName());
    }

    @Test
    public void testJerseyGetContact() {
        // mock service
        mockContactService();

        // test pippo (web) aspects below
        basePath = "/jersey";
        Response response = get("/contact/1");
        response.then()
            .statusCode(200)
            .contentType(ContentType.JSON);

        Contact contact = response.as(Contact.class);
        assertEquals("Maria", contact.getName());
    }

    private void mockContactService() {
        ContactService contactService = Mockito.mock(ContactService.class);
        Contact contact = new Contact(1)
            .setName("Maria")
            .setPhone("0741200000")
            .setAddress("Sunflower Street, No. 3");
        Mockito.when(contactService.getContact(1)).thenReturn(contact);

        getApplication().setContactService(contactService);
    }

    private JerseyApplication getApplication() {
        return (JerseyApplication) pippoRule.getApplication();
    }

}
