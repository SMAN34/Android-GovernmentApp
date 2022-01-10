package com.manderfield.knowyourgovernment;

import java.io.Serializable;

public class Officials implements Serializable {

    private String office;

    private String name;
    private String address;
    private String party;
    private String phones;
    private String url;
    private String emails;
    private String photoUrl;
    private SiteChanger type;

    public Officials(String office) {
        this.office = office;
    }

    public Officials(String office, String name) {
        this.name = name;
        this.office = office;
    }

    public String getOffice() {
        return office;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getParty() {
        return party;
    }

    public String getPhone() {
        return phones;
    }

    public String getUrl() {
        return url;
    }

    public String getEmail() {
        return emails;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public SiteChanger getChannel() {
        return type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setParty(String party) {
        this.party = party;
    }

    public void setPhone(String phone) {
        this.phones = phone;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setEmail(String email) {
        this.emails = email;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public void setChannel(SiteChanger type) {
        this.type = type;
    }
}