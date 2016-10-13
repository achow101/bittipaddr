/*
 * Copyright (c) 2016 Andrew Chow
 *
 * This software may be modified and distributed under the terms
 * of the MIT License. See the LICENSE file for details.
 */

package com.achow101.bittipaddr.client;

import com.achow101.bittipaddr.shared.AddrReq;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface bittipaddrServiceAsync {
    void addAddresses(AddrReq req, AsyncCallback<String> async);
}
