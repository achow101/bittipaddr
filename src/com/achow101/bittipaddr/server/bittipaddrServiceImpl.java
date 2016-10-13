/*
 * Copyright (c) 2016 Andrew Chow
 *
 * This software may be modified and distributed under the terms
 * of the MIT License. See the LICENSE file for details.
 */

package com.achow101.bittipaddr.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.achow101.bittipaddr.client.bittipaddrService;

public class bittipaddrServiceImpl extends RemoteServiceServlet implements bittipaddrService {
    // Implementation of sample interface method
    public String getMessage(String msg) {
        return "Client said: \"" + msg + "\"<br>Server answered: \"Hi!\"";
    }
}