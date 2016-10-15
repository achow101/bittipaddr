/*
 * Copyright (c) 2016 Andrew Chow
 *
 * This software may be modified and distributed under the terms
 * of the MIT License. See the LICENSE file for details.
 */

package com.achow101.bittipaddr.client;

import com.achow101.bittipaddr.shared.AddrReq;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ClickEvent;

/**
 * Entry point classes define <code>onModuleLoad()</code>
 */
public class bittipaddr implements EntryPoint {

    /**
     * This is the entry point method.
     */
    public void onModuleLoad() {

        // Add textboxes
        TextBox unitLookupBox = new TextBox();
        TextBox xpubBox = new TextBox();
        xpubBox.setWidth("600");
        TextArea addrsArea = new TextArea();
        addrsArea.setWidth("300");
        addrsArea.setHeight("300");

        // Add text elements
        HTML output = new HTML();

        // Create Button
        Button submitBtn = new Button("Submit");

        submitBtn.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {

                // Get entered data and some prelim checking
                String xpub = xpubBox.getText();
                String unit = unitLookupBox.getText();
                String[] addrs = addrsArea.getText().split("\n");
                if(!xpub.isEmpty() && !addrs[0].isEmpty())
                {
                    output.setHTML("<p style=\"color:red;\">Cannot set both xpub and a list of addresses</p>");
                    return;
                }

                // Send to server
                AddrReq req = new AddrReq();
                if(!unit.isEmpty())
                {
                    req.setId(unit);
                }
                else if(!xpub.isEmpty())
                {
                    req = new AddrReq(xpub);
                }
                else if(addrs.length != 0)
                {
                    req = new AddrReq(addrs);
                }
                bittipaddrService.App.getInstance().addAddresses(req, new AddAddrAsyncCallback(output));
            }
        });

        // Add to html
        RootPanel.get("submitBtn").add(submitBtn);
        RootPanel.get("unitLookup").add(unitLookupBox);
        RootPanel.get("enterxpub").add(xpubBox);
        RootPanel.get("enterAddrList").add(addrsArea);
        RootPanel.get("completedReqOutput").add(output);
    }

    private static class AddAddrAsyncCallback implements AsyncCallback<String> {
        private HTML outhtml;

        public AddAddrAsyncCallback(HTML outhtml) {
            this.outhtml = outhtml;
        }

        public void onSuccess(String result) {
            outhtml.setHTML(result);
        }

        public void onFailure(Throwable throwable) {
            outhtml.setHTML("<p style=\"color:red;\">Failed to receive answer from server!</p>");
        }
    }
}
