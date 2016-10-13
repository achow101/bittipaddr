/*
 * Copyright (c) 2016 Andrew Chow
 *
 * This software may be modified and distributed under the terms
 * of the MIT License. See the LICENSE file for details.
 */

package com.achow101.bittipaddr.server;

import com.achow101.bittipaddr.shared.AddrReq;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.achow101.bittipaddr.client.bittipaddrService;
import org.bitcoinj.core.Address;
import org.bitcoinj.core.AddressFormatException;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.WrongNetworkException;
import org.bitcoinj.crypto.DeterministicKey;
import org.bitcoinj.crypto.HDKeyDerivation;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.wallet.UnreadableWalletException;

import java.util.Arrays;

public class bittipaddrServiceImpl extends RemoteServiceServlet implements bittipaddrService {

    private NetworkParameters params = MainNetParams.get();

    public String addAddresses(AddrReq req) {

        // Setup the aws dynamo db client
        AmazonDynamoDBClient client = new AmazonDynamoDBClient();
        DynamoDB dynamoDB = new DynamoDB(client);
        Table table = dynamoDB.getTable("Bittipaddrs");

        // Check validity of addresses
        if(req.getXpub().equals("NONE") && req.getAddresses().length != 0)
        {
            for(int i = 0; i < req.getAddresses().length; i++)
            {
                try {
                    Address addr = Address.fromBase58(params, req.getAddresses()[i]);
                }
                catch(AddressFormatException e) {
                    return "<p style=\"color:red;\">Invalid address" + req.getAddresses()[i] + "</p>";
                }
            }
        }
        // Check validity of xpub
        else if(!req.getXpub().equals("NONE") && req.getAddresses().length == 0)
        {
            try {
                // Check Xpub
                DeterministicKey xpub = DeterministicKey.deserializeB58(req.getXpub(), params);
                DeterministicKey external = HDKeyDerivation.deriveChildKey(xpub, 0);

                // Derive 1000 addresses and add to req
                String[] addrs = new String[1000];
                for(int i = 0; i < 1000; i++)
                {
                    addrs[i] = HDKeyDerivation.deriveChildKey(external, i).toAddress(params).toBase58();
                }
                req.setAddresses(addrs);
            }
            catch(Exception e) {
                return "<p style=\"color:red;\">Invalid xpub" + req.getXpub() + "</p>";
            }
        }
        // It's a unit, return data
        else if(req.getXpub().equals("NONE") && req.getAddresses().length == 0) {
            try {
                Item item = table.getItem("ID", req.getId());
                return req.getHtml();
            }
            catch(Exception e) {
                return "<p style=\"color:red;\">An Error Occurred!</p>";
            }
        }

        // Add request to DynamoDB
        Item item = new Item().withPrimaryKey("ID", req.getId())
                .withInt("AddrIndex", 0)
                .withList("Addresses", Arrays.asList(req.getAddresses()))
                .withString("bip32-xpub", req.getXpub());
        table.putItem(item);

        return req.getHtml();
    }
}