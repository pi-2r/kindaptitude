package com.project.backend.persistence.domain.backend;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Created by zen on 11/06/17.
 */
@Entity
public class Company implements Serializable {

    /** The Serial Version UID for Serializable classes. */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String companyName;
    private String street;
    private String zipCode;
    private String country;
    private String town;

    private String webSite;
    private String siren;
    private String vatNumber;
    private String tvaNumber;

    private String phoneNumberCompany;
    private String emailCompany;
    private String aboutYourBusiness; // text in mysql

    /*
        - Individual / Sole Proprietorship
        - Corporation
        - Partnership
     */
    private String businessType;

    private String linkedinUrl;
    private String contactPersonEmail;
    private String phoneNumberContact;

    /** Default constructor. */
    public Company() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getWebSite() {
        return webSite;
    }

    public void setWebSite(String webSite) {
        this.webSite = webSite;
    }

    public String getSiren() {
        return siren;
    }

    public void setSiren(String siren) {
        this.siren = siren;
    }

    public String getVatNumber() {
        return vatNumber;
    }

    public void setVatNumber(String vatNumber) {
        this.vatNumber = vatNumber;
    }

    public String getTvaNumber() {
        return tvaNumber;
    }

    public void setTvaNumber(String tvaNumber) {
        this.tvaNumber = tvaNumber;
    }

    public String getPhoneNumberCompany() {
        return phoneNumberCompany;
    }

    public void setPhoneNumberCompany(String phoneNumberCompany) {
        this.phoneNumberCompany = phoneNumberCompany;
    }

    public String getEmailCompany() {
        return emailCompany;
    }

    public void setEmailCompany(String emailCompany) {
        this.emailCompany = emailCompany;
    }

    public String getLinkedinUrl() {
        return linkedinUrl;
    }

    public void setLinkedinUrl(String linkedinUrl) {
        this.linkedinUrl = linkedinUrl;
    }

    public String getAboutYourBusiness() {
        return aboutYourBusiness;
    }

    public void setAboutYourBusiness(String aboutYourBusiness) {
        this.aboutYourBusiness = aboutYourBusiness;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getContactPersonEmail() {
        return contactPersonEmail;
    }

    public void setContactPersonEmail(String contactPersonEmail) {
        this.contactPersonEmail = contactPersonEmail;
    }

    public String getPhoneNumberContact() {
        return phoneNumberContact;
    }

    public void setPhoneNumberContact(String phoneNumberContact) {
        this.phoneNumberContact = phoneNumberContact;
    }
}
