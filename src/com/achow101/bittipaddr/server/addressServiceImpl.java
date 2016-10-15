/*
 * Copyright (c) 2016 Andrew Chow
 *
 * This software may be modified and distributed under the terms
 * of the MIT License. See the LICENSE file for details.
 */

package com.achow101.bittipaddr.server;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.blockcypher.context.BlockCypherContext;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andy Chow on 10/12/2016.
 */
public class addressServiceImpl extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        // Get the id
        String requestURI = request.getRequestURI();
        String id = requestURI.substring(requestURI.lastIndexOf("/") + 1);

        // Setup the aws dynamo db client
        AmazonDynamoDBClient client = new AmazonDynamoDBClient();
        DynamoDB dynamoDB = new DynamoDB(client);

        // Setup blockcypher API
        BlockCypherContext blockCypherContext = new BlockCypherContext("v1", "btc", "main", "4d3109a5c07f426da9ccc2943da39244");

        // Lookup ID and get current address, increment index
        String address = "";
        Table table = dynamoDB.getTable("Bittipaddrs");
        try {
            Item item = table.getItem("ID", id);
            int currAddrInx = item.getInt("AddrIndex");
            int origIndx = currAddrInx;
            List<String> addresses = item.getList("Addresses");
            if(currAddrInx < addresses.size()) {
                address = addresses.get(currAddrInx);

                while(blockCypherContext.getAddressService().getAddress(address).getnTx() > 0) {
                    // Increment index and get next address
                    currAddrInx++;
                    address = addresses.get(currAddrInx);
                }
            }
            else {
                address = addresses.get(addresses.size() - 1);
            }

            // Update index if DB if it has changed
            if(currAddrInx != origIndx)
            {
                UpdateItemSpec updateItemSpec = new UpdateItemSpec()
                        .withPrimaryKey("ID", id)
                        .withUpdateExpression("set AddrIndex=:i")
                        .withValueMap(new ValueMap()
                                .withNumber(":i", currAddrInx));
                table.updateItem(updateItemSpec);
            }
        }
        catch (Exception e) {
            System.out.println("Error in getting item.");
        }

        // Set response content type
        response.setContentType("text/html");

        // Actual logic goes here.
        PrintWriter out = response.getWriter();
        out.println("<a href=bitcoin:" + address + ">" + address + "</a>");
    }

}
