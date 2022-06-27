package com.tencent.opensource.model;

public class Basicconfig {
    private int id;
    private String name;
    private String phone;
    private String website;
    private String icp;
    private String mail;
    private String company;
    private String address;
    private String datetime;
    private String title;
    private String keywords;
    private String description;
    private String kefu;
    private String logonmessage;
    private String Maintenancetips;
    private String sensitivewords;
    private int code;
    private int status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getIcp() {
        return icp;
    }

    public void setIcp(String icp) {
        this.icp = icp;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getKefu() {
        return kefu;
    }

    public void setKefu(String kefu) {
        this.kefu = kefu;
    }

    public String getLogonmessage() {
        return logonmessage;
    }

    public void setLogonmessage(String logonmessage) {
        this.logonmessage = logonmessage;
    }

    public String getMaintenancetips() {
        return Maintenancetips;
    }

    public void setMaintenancetips(String maintenancetips) {
        Maintenancetips = maintenancetips;
    }

    public String getSensitivewords() {
        return sensitivewords;
    }

    public void setSensitivewords(String sensitivewords) {
        this.sensitivewords = sensitivewords;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Basicconfig{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", website='" + website + '\'' +
                ", icp='" + icp + '\'' +
                ", mail='" + mail + '\'' +
                ", company='" + company + '\'' +
                ", address='" + address + '\'' +
                ", datetime='" + datetime + '\'' +
                ", title='" + title + '\'' +
                ", keywords='" + keywords + '\'' +
                ", description='" + description + '\'' +
                ", kefu='" + kefu + '\'' +
                ", logonmessage='" + logonmessage + '\'' +
                ", Maintenancetips='" + Maintenancetips + '\'' +
                ", sensitivewords='" + sensitivewords + '\'' +
                ", code=" + code +
                ", status=" + status +
                '}';
    }
}
