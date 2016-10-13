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
        TextArea addrsArea = new TextArea();
        addrsArea.setWidth("600");
        addrsArea.setHeight("600");

        // Add text elements
        HTML output = new HTML();

        // Create Button
        Button submitBtn = new Button("Submit");

        submitBtn.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                    bittipaddrService.App.getInstance().addAddresses(new AddrReq("asdf"), new AddAddrAsyncCallback(output));
            }
        });

        // Add to html
        RootPanel.get("submitBtn").add(submitBtn);
        RootPanel.get("unitLookup").add(unitLookupBox);
        RootPanel.get("enterxpub").add(xpubBox);
        RootPanel.get("enterAddrList").add(addrsArea);
        RootPanel.get("completedReqOutput").add(output);
    }

    private static class AddAddrAsyncCallback implements AsyncCallback<AddrReq> {
        private HTML outhtml;

        public AddAddrAsyncCallback(HTML outhtml) {
            this.outhtml = outhtml;
        }

        public void onSuccess(AddrReq result) {
            outhtml.setHTML(result.getHtml());
        }

        public void onFailure(Throwable throwable) {
            outhtml.setText("<p style=\"color:red;\">Failed to receive answer from server!</p>");
        }
    }
}
