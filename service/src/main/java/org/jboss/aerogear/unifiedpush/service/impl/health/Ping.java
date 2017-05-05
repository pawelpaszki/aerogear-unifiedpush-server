/**
 * JBoss, Home of Professional Open Source
 * Copyright Red Hat, Inc., and individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.aerogear.unifiedpush.service.impl.health;

import org.jboss.aerogear.unifiedpush.service.proxy.ProxyConfiguration;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.Proxy;

public class Ping {

    public static boolean isReachable(String host, int port) {
        Socket socket = null;
        boolean reachable;
        try {

            if (ProxyConfiguration.hasSocks()) {
                Proxy socksProxy = new Proxy(Proxy.Type.SOCKS,new InetSocketAddress(ProxyConfiguration.socks().getAddress(), ProxyConfiguration.socks().getPort()));
                socket = new Socket(socksProxy);
            } else if (ProxyConfiguration.hasProxyConfig()) {
                Proxy httpProxy = new Proxy(Proxy.Type.HTTP,new InetSocketAddress(ProxyConfiguration.proxyAddress().getAddress(),ProxyConfiguration.proxyAddress().getPort()));
                socket = new Socket(httpProxy);

            } else {
                socket = new Socket();
            }
            socket.connect(new InetSocketAddress(host, port), 2000);
            reachable = true;
        } catch (IOException e) {
            reachable = false;
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    //ignore
                }
            }
        }
        return reachable;
    }
}
