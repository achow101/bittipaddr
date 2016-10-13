package com.achow101.bittipaddr.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigInteger;
import java.util.Random;

/**
 * Created by andy on 10/12/16.
 */
public class AddrReq implements IsSerializable {

    private String xpub;
    private String[] addresses;
    private String id;
    private static Random random = new Random();

    public AddrReq(String xpub) {
        this.xpub = xpub;
        this.id = new BigInteger(40, random).toString(32);
    }

    public AddrReq(String[] addresses) {
        this.xpub = "NONE";
        this.addresses = addresses;
        this.id = new BigInteger(40, random).toString(32);
    }

    public AddrReq() {
        this(new String[0]);
    }

    public String getXpub() {
        return this.xpub;
    }

    public String[] getAddresses() {
        return this.addresses;
    }

    public void setAddresses(String[] addresses)
    {
        this.addresses = addresses;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHtml() {
        // TODO: Fix HTML
        String out = "<ul><li>Your Unit ID is <b>" + this.id + "</b></li>\n" +
                "\n<li></li><li>Embed this into your website: " +
                "<code><iframe src=\"http://localhost:8888/bittipaddr/addressfor/" + this.id + "\" style=\"border:none;\" scrolling=\"no\"></iframe></code></li>\n" +
                "\n<li></li><li>Use this for BBCode (Forums): STUFF</li>\n" +
                "\n<li></li><li>Use this for Markdown (Reddit, Github): STUFF</li>\n" +
                "\n<li></li><li>Here are all of the addresses that will be used:</li>\n";
        for(int i = 0; i < addresses.length; i++)
        {
            out += "<li>" + addresses[i] + "</li>\n";
        }
        return out;
    }

}
