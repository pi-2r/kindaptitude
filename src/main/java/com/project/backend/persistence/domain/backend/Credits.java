package com.project.backend.persistence.domain.backend;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Created by zen on 18/08/17.
 */
@Entity
public class Credits implements Serializable {

    /** The Serial Version UID for Serializable classes. */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String companyName;

    private String smsNumber;

    private String emailNumber;

    private String tokenBuying;

    public long getId() {return id;}
    public void setId(long id) {this.id = id;}
    public String getCompanyName() {return companyName;}
    public void setCompanyName(String companyName) {this.companyName = companyName;}
    public String getSmsNumber() {return smsNumber;}
    public void setSmsNumber(String smsNumber) {this.smsNumber = smsNumber;}
    public String getEmailNumber() {return emailNumber;}
    public void setEmailNumber(String emailNumber) {this.emailNumber = emailNumber;}
    public String getTokenBuying() {return tokenBuying;}
    public void setTokenBuying(String tokenBuying) {this.tokenBuying = tokenBuying;}
}
