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
package ro.pippo.demo.websocket;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.UpgradeRequest;
import ro.pippo.core.Application;
import ro.pippo.core.util.IoUtils;
import ro.pippo.core.websocket.WebSocketConnection;
import ro.pippo.core.websocket.WebSocketContext;
import ro.pippo.core.websocket.WebSocketHandler;
import ro.pippo.jetty.websocket.JettyWebSocketConnection;

import java.io.IOException;

/**
 * @author Decebal Suiu
 */
public class WebSocketApplication extends Application {

    @Override
    protected void onInit() {
        getRouter().ignorePaths("/favicon.ico");

        // add web socket
        addWebSocket("/ws/echo", (webSocketContext, message) -> {
            try {
                webSocketContext.sendMessage(message);
//                webSocketContext.broadcastMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        /*
        // expanded WebSocketHandler variation
        addWebSocket("/ws/echo", new WebSocketHandler() {

            @Override
            public void onMessage(WebSocketContext webSocketContext, String message) {
                System.out.println("TestWebSocket.onMessage");
                System.out.println("message = " + message);
                try {
                    webSocketContext.sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onMessage(WebSocketContext webSocketContext, byte[] message) {
                System.out.println("TestWebSocket.onMessage");
            }

            @Override
            public void onOpen(WebSocketContext webSocketContext) {
                System.out.println("TestWebSocket.onOpen");
            }

            @Override
            public void onClose(WebSocketContext webSocketContext, int closeCode, String message) {
                System.out.println("TestWebSocket.onClose");
            }

            @Override
            public void onTimeout(WebSocketContext webSocketContext) {
                System.out.println("TestWebSocket.onTimeout");
            }

            @Override
            public void onError(WebSocketContext webSocketContext, Throwable t) {
                System.out.println("TestWebSocket.onError");
            }

        });
        */

        // add routes (serve an html that contains the javascript - websocket client)
        GET("/", routeContext -> {
            try {
                routeContext.send(IoUtils.toString(WebSocketApplication.class.getResourceAsStream("/index.html")));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        addWebSocket("/ws-debug", (webSocketContext, message) -> {
            WebSocketConnection connection = webSocketContext.getConnection();
            // cast WebSocketConnection to JettyWebSocketConnection
            JettyWebSocketConnection jettyConnection = (JettyWebSocketConnection) connection;
            String remoteAddress = jettyConnection.getRemoteAddress().toString();
            System.out.println("remoteAddress = " + remoteAddress);
            Session session = jettyConnection.getSession();
            String protocolVersion = session.getProtocolVersion();
            System.out.println("protocolVersion = " + protocolVersion);
            UpgradeRequest upgradeRequest = session.getUpgradeRequest();
//            upgradeRequest.getCookies();
//            upgradeRequest.getHeaders();
//            upgradeRequest.getParameterMap();
            String query = upgradeRequest.getQueryString();
            System.out.println("query = " + query);
        });
    }

}
