/*
 * Copyright (C) 2017 the original author or authors.
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
package ro.pippo.demo.pac4j;

import org.pac4j.core.client.Client;
import org.pac4j.core.client.Clients;
import org.pac4j.core.config.Config;
import org.pac4j.core.exception.HttpAction;
import org.pac4j.core.profile.CommonProfile;
import org.pac4j.core.profile.ProfileManager;
import org.pac4j.http.client.indirect.FormClient;
import org.pac4j.jwt.config.signature.SecretSignatureConfiguration;
import org.pac4j.jwt.profile.JwtGenerator;
import org.pac4j.saml.client.SAML2Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ro.pippo.core.Application;
import ro.pippo.core.route.RouteContext;
import ro.pippo.pac4j.Pac4jCallbackHandler;
import ro.pippo.pac4j.Pac4jLogoutHandler;
import ro.pippo.pac4j.Pac4jSecurityHandler;
import ro.pippo.pac4j.PippoWebContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author Decebal Suiu
 */
public class Pac4jApplication extends Application {

    private final static Logger log = LoggerFactory.getLogger(Pac4jApplication.class);

    private final static String JWT_SALT = "12345678901234567890123456789012";

    @Override
    protected void onInit() {
        getRouter().ignorePaths("/favicon.ico");

        Config config = new DemoConfigFactory(JWT_SALT).build();

        // security (before) filters

        Pac4jSecurityHandler facebookFilter = new Pac4jSecurityHandler(config, "FacebookClient", "", "excludedPath");
        GET("/facebook", facebookFilter);
        GET("/facebook/*", facebookFilter);

        GET("/facebookadmin", new Pac4jSecurityHandler(config, "FacebookClient", "admin"));
        GET("/facebookcustom", new Pac4jSecurityHandler(config, "FacebookClient", "custom"));
        GET("/twitter", new Pac4jSecurityHandler(config, "TwitterClient,FacebookClient"));
        GET("/form", new Pac4jSecurityHandler(config, "FormClient"));
        GET("/basicauth", new Pac4jSecurityHandler(config, "IndirectBasicAuthClient"));
        GET("/cas", new Pac4jSecurityHandler(config, "CasClient"));
        GET("/saml2", new Pac4jSecurityHandler(config, "SAML2Client"));
        GET("/oidc", new Pac4jSecurityHandler(config, "OidcClient"));
        GET("/protected", new Pac4jSecurityHandler(config, null));
        GET("/dba", new Pac4jSecurityHandler(config, "DirectBasicAuthClient,ParameterClient"));
        GET("/rest-jwt", new Pac4jSecurityHandler(config, "ParameterClient"));

        // security routes

        Pac4jLogoutHandler localLogout = new Pac4jLogoutHandler(config, "/?defaulturlafterlogout");
        localLogout.setDestroySession(true);

        GET("/logout", localLogout);

        Pac4jLogoutHandler centralLogout = new Pac4jLogoutHandler(config);
        centralLogout.setDefaultUrl("http://localhost:8338/?defaulturlafterlogoutafteridp");
        centralLogout.setLogoutUrlPattern("http://localhost:8338/.*");
        centralLogout.setLocalLogout(false);
        centralLogout.setCentralLogout(true);
        centralLogout.setDestroySession(true);

        GET("/centralLogout", centralLogout);
        GET("/forceLogin", routeContext -> forceLogin(routeContext, config));

        Pac4jCallbackHandler callbackHandler = new Pac4jCallbackHandler(config, null, true);
//        callbackHandler.setRenewSession(false);

        GET("/callback", callbackHandler);
        POST("/callback", callbackHandler);

        // business routes

        GET("/", Pac4jApplication::index);

        GET("/facebook", Pac4jApplication::protectedIndex);
        GET("/facebook/notprotected", Pac4jApplication::protectedIndex);
        GET("/facebookadmin", Pac4jApplication::protectedIndex);
        GET("/facebookcustom", Pac4jApplication::protectedIndex);
        GET("/twitter", Pac4jApplication::protectedIndex);
        GET("/form", Pac4jApplication::protectedIndex);
        GET("/basicauth", Pac4jApplication::protectedIndex);
        GET("/cas", Pac4jApplication::protectedIndex);
        GET("/saml2", Pac4jApplication::protectedIndex);
        GET("/saml2-metadata", routeContext -> {
            SAML2Client samlClient = config.getClients().findClient(SAML2Client.class);
            samlClient.init(new PippoWebContext(routeContext));
            String metadata = samlClient.getServiceProviderMetadataResolver().getMetadata();
            routeContext.send(metadata);
        });
        GET("/jwt", Pac4jApplication::jwt);
        GET("/oidc", Pac4jApplication::protectedIndex);
        GET("/protected", Pac4jApplication::protectedIndex);
        GET("/dba", Pac4jApplication::protectedIndex);
        GET("/rest-jwt", Pac4jApplication::protectedIndex);
        GET("/loginForm", routeContext -> loginForm(routeContext, config));
    }

    private static List<CommonProfile> getProfiles(RouteContext routeContext) {
        PippoWebContext webContext = new PippoWebContext(routeContext);
        ProfileManager manager = new ProfileManager(webContext);

        return manager.getAll(true);
    }

    private static void index(RouteContext routeContext) {
        Map<String, Object> model = new HashMap<>();
        model.put("profiles", getProfiles(routeContext));
        PippoWebContext webContext = new PippoWebContext(routeContext);
        model.put("sessionId", webContext.getSessionIdentifier());

        routeContext.render("index", model);
    }

    private static void protectedIndex(RouteContext routeContext) {
        Map<String, Object> model = new HashMap<>();
        model.put("profiles", getProfiles(routeContext));

        routeContext.render("protectedIndex", model);
    }

    private static void jwt(RouteContext routeContext) {
        PippoWebContext webContext = new PippoWebContext(routeContext);
        ProfileManager manager = new ProfileManager(webContext);
        Optional<CommonProfile> profile = manager.get(true);
        String token = "";
        if (profile.isPresent()) {
            JwtGenerator generator = new JwtGenerator(new SecretSignatureConfiguration(JWT_SALT));
            token = generator.generate(profile.get());
        }

        Map<String, Object> model = new HashMap<>();
        model.put("token", token);

        routeContext.render("jwt", model);
    }

    private static void loginForm(RouteContext routeContext, Config config) {
        Map<String, Object> model = new HashMap<>();
        FormClient formClient = config.getClients().findClient(FormClient.class);
        model.put("callbackUrl", formClient.getCallbackUrl());

        routeContext.render("loginForm", model);
    }

    private static void forceLogin(RouteContext routeContext, Config config) {
        PippoWebContext context = new PippoWebContext(routeContext);
        String clientName = context.getRequestParameter(Clients.DEFAULT_CLIENT_NAME_PARAMETER);
        Client client = config.getClients().findClient(clientName);
        HttpAction action;
        try {
            action = client.redirect(context);
        } catch (HttpAction e) {
            action = e;
        }
        config.getHttpActionAdapter().adapt(action.getCode(), context);

        routeContext.send(""); // ?!
    }

}
