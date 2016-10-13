/*
 * Copyright (c) 2016 Andrew Chow
 *
 * This software may be modified and distributed under the terms
 * of the MIT License. See the LICENSE file for details.
 */

package com.achow101.bittipaddr.client;

import com.achow101.bittipaddr.shared.AddrReq;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("bittipaddrService")
public interface bittipaddrService extends RemoteService {
    // Sample interface method of remote interface
    AddrReq addAddresses(AddrReq req);

    /**
     * Utility/Convenience class.
     * Use bittipaddrService.App.getInstance() to access static instance of bittipaddrServiceAsync
     */
    public static class App {
        private static bittipaddrServiceAsync ourInstance = GWT.create(bittipaddrService.class);

        public static synchronized bittipaddrServiceAsync getInstance() {
            return ourInstance;
        }
    }
}
