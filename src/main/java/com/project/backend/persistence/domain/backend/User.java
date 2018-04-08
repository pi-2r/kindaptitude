package com.project.backend.persistence.domain.backend;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by zen on 14/05/17.
 */
@Entity
public class User implements Serializable, UserDetails {

    /** The Serial Version UID for Serializable classes. */
    private static final long serialVersionUID = 1L;


    public User() {

    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String username;

    private String password;

    @Column(unique = true)
    private String email;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Length(max = 500)
    private String description;


    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @Column(name = "stripe_customer_id")
    private String stripeCustomerId ="no";

    private boolean enabled;

    private boolean blockedByOwnAdmin;

    private boolean blockedByAdmin;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "plan_id")
    private Plan plan;

    // number of credits
    private String credits;

    @Column(name = "optionChoise")
    private String optionChoise;

    private String designation; // job title

    private String department; // departement

    private String country;

    private String language;

    private String birthdayDate;

    @Column(name = "mobilePhone_number")
    private String mobilePhoneNumber;

    @Column(name = "skype_number")
    private String skypeNumer;

    @Column(name = "facebook_account")
    private String facebookAccount;

    @Column(name = "linkedin_account")
    private String linkedinAccount;

    @ManyToOne(fetch = FetchType.EAGER, cascade=CascadeType.REMOVE)
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "company_id")
    private Company company;

    @Column(nullable = false)
    private Boolean masterGroupe;

    private Boolean strongAUth;

    private String token2FA;

    private String urlToDisplayQRCode;

    private Boolean alertArea;

    private String areaConnexion;

    private Boolean leftpanelCollapsed;

    private Boolean activUserFromOwnerGroup = false;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<UserRole> userRoles = new HashSet<>();

    @OneToMany( cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user" )
    private Set<PasswordResetToken> passwordResetTokenset = new HashSet<>();

    public Set<PasswordResetToken> getPasswordResetTokenset() {
        return passwordResetTokenset;
    }

    public void setPasswordResetTokenset(Set<PasswordResetToken> passwordResetTokenset) {
        this.passwordResetTokenset = passwordResetTokenset;
    }


    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country = country;
    }
    public String getProfileImageUrl() {
        return profileImageUrl;
    }
    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
    public String getStripeCustomerId() {
        return stripeCustomerId;
    }
    public void setStripeCustomerId(String stripeCustomerId) {
        this.stripeCustomerId = stripeCustomerId;
    }
    public boolean isEnabled() {
        return enabled;
    }
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    public String getCredits() {
        return credits;
    }
    public void setCredits(String credits) {
        this.credits = credits;
    }
    public String getOptionChoise() {
        return optionChoise;
    }
    public void setOptionChoise(String optionChoise) {
        this.optionChoise = optionChoise;
    }
    public String getDesignation() {
        return designation;
    }
    public void setDesignation(String designation) {
        this.designation = designation;
    }
    public String getDepartment() {
        return department;
    }
    public void setDepartment(String department) {
        this.department = department;
    }
    public String getBirthdayDate() {
        return birthdayDate;
    }
    public void setBirthdayDate(String birthdayDate) {
        this.birthdayDate = birthdayDate;
    }
    public String getMobilePhoneNumber() {
        return mobilePhoneNumber;
    }
    public void setMobilePhoneNumber(String mobilePhoneNumber) {
        this.mobilePhoneNumber = mobilePhoneNumber;
    }
    public String getSkypeNumer() {
        return skypeNumer;
    }
    public void setSkypeNumer(String skypeNumer) {
        this.skypeNumer = skypeNumer;
    }
    public String getFacebookAccount() {
        return facebookAccount;
    }
    public void setFacebookAccount(String facebookAccount) {
        this.facebookAccount = facebookAccount;
    }
    public String getLinkedinAccount() {
        return linkedinAccount;
    }
    public void setLinkedinAccount(String linkedinAccount) {
        this.linkedinAccount = linkedinAccount;
    }
    public Company getCompany() {
        return company;
    }
    public void setCompany(Company company) {
        this.company = company;
    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        userRoles.forEach(ur -> authorities.add(new Authority(ur.getRole().getName())));
        return authorities;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public Plan getPlan() {
        return plan;
    }
    public void setPlan(Plan plan) {
        this.plan = plan;
    }
    public Set<UserRole> getUserRoles() {
        return userRoles;
    }
    public void setUserRoles(Set<UserRole> userRoles) {
        this.userRoles = userRoles;
    }
    public Boolean getMasterGroupe() {
        return masterGroupe;
    }
    public void setMasterGroupe(Boolean masterGroupe) {
        this.masterGroupe = masterGroupe;
    }
    public String getLanguage() {
        return language;
    }
    public void setLanguage(String language) {
        this.language = language;
    }
    public boolean isBlockedByOwnAdmin() {return blockedByOwnAdmin;}
    public void setBlockedByOwnAdmin(boolean blockedByOwnAdmin) {this.blockedByOwnAdmin = blockedByOwnAdmin;}
    public boolean isBlockedByAdmin() {return blockedByAdmin;}
    public void setBlockedByAdmin(boolean blockedByAdmin) {this.blockedByAdmin = blockedByAdmin;}
    public Boolean getStrongAUth() { return strongAUth; }
    public void setStrongAUth(Boolean strongAUth) { this.strongAUth = strongAUth; }
    public String getAreaConnexion() { return areaConnexion; }
    public void setAreaConnexion(String areaConnexion) {this.areaConnexion = areaConnexion;}
    public Boolean getAlertArea() {return alertArea;}
    public void setAlertArea(Boolean alertArea) {this.alertArea = alertArea;}
    public Boolean getLeftpanelCollapsed() {return leftpanelCollapsed;}
    public void setLeftpanelCollapsed(Boolean leftpanelCollapsed) {this.leftpanelCollapsed = leftpanelCollapsed;}
    public String getToken2FA() {return token2FA;}
    public void setToken2FA(String token2FA) {this.token2FA = token2FA;}
    public String getUrlToDisplayQRCode() {return urlToDisplayQRCode;}
    public void setUrlToDisplayQRCode(String urlToDisplayQRCode) {this.urlToDisplayQRCode = urlToDisplayQRCode;}
    public Boolean getActivUserFromOwnerGroup() {return activUserFromOwnerGroup;}
    public void setActivUserFromOwnerGroup(Boolean activUserFromOwnerGroup) {this.activUserFromOwnerGroup = activUserFromOwnerGroup;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return id == user.id;

    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }


}

