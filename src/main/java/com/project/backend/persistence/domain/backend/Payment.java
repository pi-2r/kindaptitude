package com.project.backend.persistence.domain.backend;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Created by zen on 17/08/17.
 */
@Entity
public class Payment implements Serializable {

    /** The Serial Version UID for Serializable classes. */
    private static final long serialVersionUID = 1L;

    public Payment() {

    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;


    private String creationDate;

    private String expirationDate;

    private String detailsBuy;

    private String price;

    private String interval;
    /**
     * Token Stripe about CB customer
     */
    private String subscriptionId;

    private String idCompany;

    public long getId() {return id;}
    public void setId(long id) {this.id = id;}
    public String getDetailsBuy() {return detailsBuy;}
    public void setDetailsBuy(String detailsBuy) {this.detailsBuy = detailsBuy;}
    public String getPrice() {return price;}
    public void setPrice(String price) {this.price = price;}
    public String getInterval() {return interval;}
    public void setInterval(String interval) {this.interval = interval;}
    public String getCreationDate() {return creationDate;}
    public void setCreationDate(String creationDate) {this.creationDate = creationDate;}
    public String getExpirationDate() {return expirationDate;}
    public void setExpirationDate(String expirationDate) {this.expirationDate = expirationDate;}
    public String getIdCompany() {return idCompany;}
    public void setIdCompany(String idCompany) {this.idCompany = idCompany;}
    public String getSubscriptionId() {return subscriptionId;}
    public void setSubscriptionId(String subscriptionId) {this.subscriptionId = subscriptionId;}
}
