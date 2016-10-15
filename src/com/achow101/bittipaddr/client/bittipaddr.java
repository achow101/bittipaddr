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
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.TextArea;

/**
 * Entry point classes define <code>onModuleLoad()</code>
 */
public class bittipaddr implements EntryPoint {

    private boolean edited = false;

    /**
     * This is the entry point method.
     */
    public void onModuleLoad() {

        // Add textboxes
        TextBox unitLookupBox = new TextBox();
        TextBox unitPassBox = new TextBox();
        TextBox xpubBox = new TextBox();
        xpubBox.setWidth("600");
        TextArea addrsArea = new TextArea();
        addrsArea.setWidth("300");
        addrsArea.setHeight("300");

        // Checkbox to enable editing with lookup
        CheckBox submitEdit = new CheckBox("Submit changes after clicking button");
        CheckBox allowEdit = new CheckBox("Allow editing the unit");

        // Add text elements
        HTML output = new HTML();

        // Create Button
        Button submitBtn = new Button("Submit");

        submitBtn.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {

                // Clear previous output
                output.setHTML("");

                // Get entered data and some prelim checking
                String xpub = xpubBox.getText();
                String pass = unitPassBox.getText();
                String unit = unitLookupBox.getText();
                String[] addrs = addrsArea.getText().split("\n");
                if(!xpub.isEmpty() && !addrs[0].isEmpty() && unit.isEmpty() && pass.isEmpty())
                {
                    output.setHTML("<p style=\"color:red;\">Cannot set both xpub and a list of addresses</p>");
                    return;
                }

                // Send to server
                AddrReq req = new AddrReq();
                if(!unit.isEmpty())
                {
                    req.setId(unit);
                    req.setPassword(pass);
                    req.setEditable(allowEdit.getValue());
                    if(edited)
                    {
                        if(xpub.isEmpty())
                        {
                            output.setHTML("<p style=\"color:red;\">Must have an xpub. Set as \"NONE\" (without quotes) if no xpub</p>");
                            return;
                        }

                        req.setEdited();
                        req.setAddresses(addrs);
                        req.setXpub(xpub.isEmpty() ? "NONE" : xpub);
                    }
                }
                else if(!xpub.isEmpty())
                {
                    req = new AddrReq(xpub);
                }
                else if(addrs.length != 0)
                {
                    req = new AddrReq(addrs);
                }
                bittipaddrService.App.getInstance().addAddresses(req, new AddAddrAsyncCallback(output, xpubBox, addrsArea));
            }
        });

        // Add to html
        RootPanel.get("submitBtn").add(submitBtn);
        RootPanel.get("unitLookup").add(unitLookupBox);
        RootPanel.get("unitPass").add(unitPassBox);
        RootPanel.get("enterxpub").add(xpubBox);
        RootPanel.get("enterAddrList").add(addrsArea);
        RootPanel.get("completedReqOutput").add(output);
        RootPanel.get("edit").add(submitEdit);
        RootPanel.get("allowEdit").add(allowEdit);
    }

    private class AddAddrAsyncCallback implements AsyncCallback<String> {
        private HTML outhtml;
        private TextBox xpubBox;
        private TextArea addrsArea;

        public AddAddrAsyncCallback(HTML outhtml, TextBox xpubBox, TextArea addrsArea) {
            this.outhtml = outhtml;
            this.xpubBox = xpubBox;
            this.addrsArea = addrsArea;
        }

        public void onSuccess(String result) {
            // Check for editable, will begin with PLAIN keyword
            if(result.startsWith("PLAIN"))
            {
                result = result.substring(result.indexOf("\n") + 1);
                xpubBox.setText(result.substring(0, result.indexOf("\n")));
                result = result.substring(result.indexOf("\n") + 1);
                addrsArea.setText(result);
                edited = true;
            }
            else
                outhtml.setHTML(result);
        }

        public void onFailure(Throwable throwable) {
            outhtml.setHTML("<p style=\"color:red;\">Failed to receive answer from server!</p>");
        }
    }
}
