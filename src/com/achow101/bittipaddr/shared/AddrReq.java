package com.achow101.bittipaddr.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigInteger;
import java.util.Random;

/**
 * Created by andy on 10/12/16.
 */
public class AddrReq implements IsSerializable {

    private String xpub = "NONE";
    private String[] addresses = new String[0];
    private String id = "NEW";
    private String password;
    private boolean edit = false;
    private boolean edited = false;

    public AddrReq(String xpub) {
        this.xpub = xpub;
    }

    public AddrReq(String[] addresses) {
        this.addresses = addresses;
    }

    public AddrReq() {
    }

    public String getXpub() {
        return this.xpub;
    }

    public void setXpub(String xpub)
    {
        this.xpub = xpub;
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

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public void setEditable(boolean edit)
    {
        this.edit = edit;
    }

    public boolean isEditable()
    {
        return edit;
    }

    public void setEdited()
    {
        this.edited = true;
    }

    public boolean isEdited()
    {
        return edited;
    }

    public String getHtml() {
        String out = "<table><tr></tr><tr><td>Your Unit ID is <b>" + this.id + "</b></td></tr>\n" +
                "<tr><td>Your password for editing these details (KEEP SAFE): </td><td>" + password + "</td></tr>" +
                "\n<tr></tr><tr><td>Embed this into your website and for Markdown (GitHub): </td><td>" +
                "<code>&lt;iframe src=\"http://localhost:8888/bittipaddr/addressfor/" + this.id + "\" style=\"border:none;\" scrolling=\"no\"&gt;&lt;/iframe&gt;</code></td></tr>\n" +
                "\n<tr><td>Use this for BBCode (Forums): </td><td><code>[url=https://localhost:8888/bittipaddr/addressfor/" + this.id + "?redirect]Tip Me![/url]</code></td></tr>\n" +
                "\n<tr><td>Use this for Reddit: </td><td><code>[Tip Me!](https://localhost:8888/bittipaddr/addressfor/" + this.id + "?redirect)</code></td></tr>\n";
        if(!xpub.equals("NONE"))
        {
            out += "<tr><td>This is your Extended Public Key:</td><td>" + xpub + "</td></tr>";
        }
        out += "<tr><td>Here are all of the addresses that will be used:</td></tr>\n";
        for(int i = 0; i < addresses.length; i++)
        {
            out += "<tr><td>" + addresses[i] + "</td></tr>\n";
        }
        out += "</table>";
        return out;
    }

    public String getPlain()
    {
        String out = "PLAIN\n" + xpub + "\n";
        for(int i = 0; i < addresses.length; i++)
        {
            out += addresses[i] + "\n";
        }
        return out;
    }

}
