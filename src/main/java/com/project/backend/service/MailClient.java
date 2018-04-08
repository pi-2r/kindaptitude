package com.project.backend.service;

/**
 * Created by zen on 22/08/17.
 */
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Locale;
import java.util.Properties;

@Service
public class MailClient {

    private static final Logger LOG = LoggerFactory.getLogger(MailClient.class);

    private JavaMailSender mailSender;
    private MailContentBuilder mailContentBuilder;

    @Value("${emailAccount.smtp.auth}")
    private String smtpAuth;
    @Value("${emailAccount.smtp.ssl.enable}")
    private String startTls;
    @Value("${emailAccount.host}")
    private String host;
    @Value("${emailAccount.smtp.socketFactory.port}")
    private String port;
    @Value("${emailAccount.username}")
    private String emailAccount;
    @Value("${emailAccount.password}")
    private String emailpassword;
    @Value("${default.to.address}")
    private String contactEmailWebSite;
    @Value("${about1}")
    private String about1;
    @Value("${about2}")
    private String about2;
    @Value("${titleCompany}")
    private String titleCompany;
    @Value("${urlWebsite}")
    private String urlWebsite;
    @Autowired
    private I18NService i18NService;



    @Autowired
    public MailClient(JavaMailSender mailSender, MailContentBuilder mailContentBuilder) {
        this.mailSender = mailSender;
        this.mailContentBuilder = mailContentBuilder;
    }

    public void welcomUser(String recipient, String password, String urlToValide, Locale locale) {
        Session session = Session.getInstance(getProperties(),new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailAccount, emailpassword);
            }
        });
        try {
            String kindOfEmail = "welcome";
            Message message2 = new MimeMessage(session);
            message2.setFrom(new InternetAddress(contactEmailWebSite));
            message2.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(recipient));
            message2.setSubject(i18NService.getMessage("email.welcome", locale));
            message2.setText("Dear Mail Crawler,"
                    + "\n\n No spam to my email, please!");
            //------- set all context
            Context context = new Context(locale);
            String content = mailContentBuilder.buildWelcome(kindOfEmail, context);
            message2.setContent(content, "text/html; charset=utf-8");
            Transport.send(message2);

            System.out.println("Done");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Method to send email when the user change password
     * @param recipient
     * @param locale
     * @param firstName
     * @param lastName
     */
    public void changePassword(String recipient, Locale locale, String firstName, String lastName) {
        Session session = Session.getInstance(getProperties(),new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailAccount, emailpassword);
            }
        });
        try {
            LOG.info("create email changePassword for email: "+ recipient);
            String kindOfEmail = "changePassword";
            //recipient = "pierre.therrode@gmail.com";
            Message message2 = new MimeMessage(session);
            message2.setFrom(new InternetAddress(contactEmailWebSite));
            message2.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(recipient));

            message2.setSubject(i18NService.getMessage("email.changepasswordIntra", locale));

            //------- set all context
            Context context = new Context(locale);
            context.setVariable("firstName", firstName);
            context.setVariable("lastName", lastName);
            context.setVariable("about1", about1);
            context.setVariable("about2", about2);
            context.setVariable("titleCompany", titleCompany);

            context.setVariable("urlWebsite", urlWebsite+"profile");

            String content = mailContentBuilder.buildchangePassword(kindOfEmail, context);

            message2.setContent(content, "text/html; charset=utf-8");
            Transport.send(message2);
            LOG.info("email sending at "+ recipient);

        } catch (MessagingException e) {
            LOG.error(e.toString());
            throw new RuntimeException(e);
        }

    }

    public void alerteArea(String recipient, Locale locale, String firstName, String ipaddress, String device, String browser, String areaZone ) {
        Session session = Session.getInstance(getProperties(),new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailAccount, emailpassword);
            }
        });
        try {
            LOG.info("create email changePassword for email: "+ recipient);
            String kindOfEmail = "alert-warning";
            //recipient = "pierre.therrode@gmail.com";
            Message message2 = new MimeMessage(session);
            message2.setFrom(new InternetAddress(contactEmailWebSite));
            message2.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(recipient));

            message2.setSubject(i18NService.getMessage("email.alertAreaAuth", locale));

            //------- set all context
            Context context = new Context(locale);
            context.setVariable("firstName", firstName);
            context.setVariable("browser", browser);
            context.setVariable("device", device);
            context.setVariable("ipaddress", ipaddress);
            context.setVariable("areaZone", areaZone);

            context.setVariable("about1", about1);
            context.setVariable("about2", about2);
            context.setVariable("titleCompany", titleCompany);

            context.setVariable("urlWebsite", urlWebsite+"profile");

            String content = mailContentBuilder.buildchangePassword(kindOfEmail, context);

            message2.setContent(content, "text/html; charset=utf-8");
            Transport.send(message2);
            LOG.info("email sending at "+ recipient);

        } catch (MessagingException e) {
            LOG.error(e.toString());
            throw new RuntimeException(e);
        }
    }

    public void forgotPassword(String recipient, String firstName, String url, Locale locale){
        Session session = Session.getInstance(getProperties(),new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailAccount, emailpassword);
            }
        });
        try {
            LOG.info("create email block account for email: "+ recipient);
            String kindOfEmail = "forgotPassword";
            Message message2 = new MimeMessage(session);
            message2.setFrom(new InternetAddress(contactEmailWebSite));
            message2.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(recipient));

            message2.setSubject(i18NService.getMessage("email.forgotPassword", locale));

            //------- set all context
            Context context = new Context(locale);
            context.setVariable("firstName", firstName);

            context.setVariable("about1", about1);
            context.setVariable("titleCompany", titleCompany);

            context.setVariable("urlWebsite", url);

            String content = mailContentBuilder.buildchangePassword(kindOfEmail, context);

            message2.setContent(content, "text/html; charset=utf-8");
            Transport.send(message2);
            LOG.info("email sending at "+ recipient);

        } catch (MessagingException e) {
            LOG.error(e.toString());
            throw new RuntimeException(e);
        }
    }

    public void newPassword(String recipient, String firstName, Locale locale){
        Session session = Session.getInstance(getProperties(),new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailAccount, emailpassword);
            }
        });
        try {
            LOG.info("send a new password at  "+ recipient);
            String kindOfEmail = "newPassword";
            Message message2 = new MimeMessage(session);
            message2.setFrom(new InternetAddress(contactEmailWebSite));
            message2.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(recipient));

            message2.setSubject(i18NService.getMessage("email.successNewPassword", locale));

            //------- set all context
            Context context = new Context(locale);
            context.setVariable("firstName", firstName);

            context.setVariable("about1", about1);
            context.setVariable("titleCompany", titleCompany);

            context.setVariable("urlWebsite", urlWebsite+"login");

            String content = mailContentBuilder.buildchangePassword(kindOfEmail, context);

            message2.setContent(content, "text/html; charset=utf-8");
            Transport.send(message2);
            LOG.info("email sending at "+ recipient);

        } catch (MessagingException e) {
            LOG.error(e.toString());
            throw new RuntimeException(e);
        }
    }

    public void blockAccount(String recipient, String firstName, String ip, Locale locale) {
        Session session = Session.getInstance(getProperties(),new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailAccount, emailpassword);
            }
        });
        try {
            LOG.info("create email block account for email: "+ recipient);
            String kindOfEmail = "blockAccount";
            //recipient = "pierre.therrode@gmail.com";
            Message message2 = new MimeMessage(session);
            message2.setFrom(new InternetAddress(contactEmailWebSite));
            message2.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(recipient));

            message2.setSubject(i18NService.getMessage("email.yourAccountBlockIsBlocked", locale));

            //------- set all context
            Context context = new Context(locale);
            context.setVariable("firstName", firstName);
            context.setVariable("ipaddress", ip);

            context.setVariable("about1", about1);
            context.setVariable("about2", about2);
            context.setVariable("titleCompany", titleCompany);

            context.setVariable("urlWebsite", urlWebsite+"forgotmypassword");

            String content = mailContentBuilder.buildchangePassword(kindOfEmail, context);

            message2.setContent(content, "text/html; charset=utf-8");
            Transport.send(message2);
            LOG.info("email sending at "+ recipient);

        } catch (MessagingException e) {
            LOG.error(e.toString());
            throw new RuntimeException(e);
        }
    }

    public Boolean blockUser(String recipient, Locale locale, String owner, String firstNameNewUser){
        Session session = Session.getInstance(getProperties(),new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailAccount, emailpassword);
            }
        });
        Boolean flag= true;
        try {
            LOG.info("add blockedAcountByOwner with email: "+ recipient);
            String kindOfEmail = "blockedAcountByOwner";
            //recipient = "pierre.therrode@gmail.com";
            Message message2 = new MimeMessage(session);
            message2.setFrom(new InternetAddress(contactEmailWebSite));
            message2.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(recipient));

            message2.setSubject(i18NService.getMessage("email.blockUserTitle", locale));

            //------- set all context
            Context context = new Context(locale);
            context.setVariable("owner", owner);
            context.setVariable("firstNameNewUser", firstNameNewUser);

            context.setVariable("about1", about1);
            context.setVariable("about2", about2);
            context.setVariable("titleCompany", titleCompany);
            context.setVariable("recipient", recipient);

            String content = mailContentBuilder.buildchangePassword(kindOfEmail, context);

            message2.setContent(content, "text/html; charset=utf-8");
            Transport.send(message2);
            LOG.info("email sending at "+ recipient);

        } catch (MessagingException e) {
            flag = false;
            LOG.error(e.toString());
        }
        return flag;
    }

    public Boolean unBlockUser(String recipient, Locale locale, String owner, String firstNameNewUser){
        Session session = Session.getInstance(getProperties(),new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailAccount, emailpassword);
            }
        });
        Boolean flag=true;
        try {
            LOG.info("add unblockedAcountByOwner with email: "+ recipient);
            String kindOfEmail = "unBlockUser";
            //recipient = "pierre.therrode@gmail.com";
            Message message2 = new MimeMessage(session);
            message2.setFrom(new InternetAddress(contactEmailWebSite));
            message2.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(recipient));

            message2.setSubject(i18NService.getMessage("email.unBlockUserTitle", locale));

            //------- set all context
            Context context = new Context(locale);
            context.setVariable("owner", owner);
            context.setVariable("firstNameNewUser", firstNameNewUser);

            context.setVariable("urlWebsite", urlWebsite+"login");

            context.setVariable("about1", about1);
            context.setVariable("about2", about2);
            context.setVariable("titleCompany", titleCompany);
            context.setVariable("recipient", recipient);



            String content = mailContentBuilder.buildchangePassword(kindOfEmail, context);

            message2.setContent(content, "text/html; charset=utf-8");
            Transport.send(message2);
            LOG.info("email sending at "+ recipient);

        } catch (MessagingException e) {
            flag=false;
            LOG.error(e.toString());
        }
        return flag;
    }

    public Boolean addNewUser(String recipient, Locale locale, String owner, String firstNameNewUser, String password){
        Session session = Session.getInstance(getProperties(),new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailAccount, emailpassword);
            }
        });
        Boolean flag = true;
        try {
            LOG.info("add newUser with email: "+ recipient);
            String kindOfEmail = "addNewUser";
            //recipient = "pierre.therrode@gmail.com";
            Message message2 = new MimeMessage(session);
            message2.setFrom(new InternetAddress(contactEmailWebSite));
            message2.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(recipient));

            message2.setSubject(i18NService.getMessage("email.addNewUserTitle", locale));

            //------- set all context
            Context context = new Context(locale);
            context.setVariable("owner", owner);
            context.setVariable("firstNameNewUser", firstNameNewUser);
            context.setVariable("password", password);

            context.setVariable("about1", about1);
            context.setVariable("about2", about2);
            context.setVariable("titleCompany", titleCompany);
            context.setVariable("recipient", recipient);

            context.setVariable("urlWebsite", urlWebsite+"login");

            String content = mailContentBuilder.buildchangePassword(kindOfEmail, context);

            message2.setContent(content, "text/html; charset=utf-8");
            Transport.send(message2);
            LOG.info("email sending at "+ recipient);

        } catch (MessagingException e) {
            flag =false;
            LOG.error(e.toString());
        }
        return flag;
    }

    /**
     *
     * @param recipient
     * @param locale
     * @param owner
     * @param totalAmount
     * @param period
     */
    public void sendEmailBuy(String recipient, Locale locale, String owner, String totalAmount, String period,
                             String kindOfChoise, String numberOption, String totalOPtion, String buyEmailPack,
                             String buySMSPack){
        Session session = Session.getInstance(getProperties(),new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailAccount, emailpassword);
            }
        });
        try {
            LOG.info("add newUser with email: "+ recipient);
            String kindOfEmail = "addNewUser";
            //recipient = "pierre.therrode@gmail.com";
            Message message2 = new MimeMessage(session);
            message2.setFrom(new InternetAddress(contactEmailWebSite));
            message2.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(recipient));

            message2.setSubject(i18NService.getMessage("email.addNewUserTitle", locale));

            //------- set all context
            Context context = new Context(locale);
            context.setVariable("owner", owner);
            context.setVariable("totalAmount", totalAmount);
            context.setVariable("period", period);


            context.setVariable("about1", about1);
            context.setVariable("about2", about2);
            context.setVariable("titleCompany", titleCompany);
            context.setVariable("recipient", recipient);

            context.setVariable("urlWebsite", urlWebsite+"login");

            String content = mailContentBuilder.buildchangePassword(kindOfEmail, context);

            message2.setContent(content, "text/html; charset=utf-8");
            Transport.send(message2);
            LOG.info("email sending at "+ recipient);

        } catch (MessagingException e) {
            LOG.error(e.toString());
            throw new RuntimeException(e);
        }
    }


    public void signUpNewUserWithNoCompany(String email, String firstNameNewUser, String lastNameNewUser,String password,
                                           String domaineName, Locale locale){
        Session session = Session.getInstance(getProperties(),new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailAccount, emailpassword);
            }
        });
        try {
            LOG.info("add newUser with a new company "+ domaineName +" and  with email: "+ email);
            String kindOfEmail = "signUpNewUserWithNoCompany";
            email = "pierre.therrode@gmail.com";
            Message message2 = new MimeMessage(session);
            message2.setFrom(new InternetAddress(contactEmailWebSite));
            message2.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));

            message2.setSubject(i18NService.getMessage("email.addNewUserTitle", locale));

            //------- set all context
            Context context = new Context(locale);
            context.setVariable("email", email);
            context.setVariable("firstNameNewUser", firstNameNewUser);
            context.setVariable("lastNameNewUser", lastNameNewUser);
            context.setVariable("password", password);


            context.setVariable("urlWebsite", urlWebsite+"login");

            String content = mailContentBuilder.buildchangePassword(kindOfEmail, context);

            message2.setContent(content, "text/html; charset=utf-8");
            Transport.send(message2);
            LOG.info("email sending at "+ email);

        } catch (MessagingException e) {
            LOG.error(e.toString());
            throw new RuntimeException(e);
        }
    }
    public void signUpNewUserWithCompany(String email, String firstNameNewUser, String lastNameNewUser,String password, Locale locale){
        Session session = Session.getInstance(getProperties(),new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailAccount, emailpassword);
            }
        });
        try {
            LOG.info("add newUser with email: "+ email);
            String kindOfEmail = "signupNewUser";
            email = "pierre.therrode@gmail.com";
            Message message2 = new MimeMessage(session);
            message2.setFrom(new InternetAddress(contactEmailWebSite));
            message2.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(email));

            message2.setSubject(i18NService.getMessage("email.addNewUserTitle", locale));

            //------- set all context
            Context context = new Context(locale);
            context.setVariable("email", email);
            context.setVariable("firstNameNewUser", firstNameNewUser);
            context.setVariable("lastNameNewUser", lastNameNewUser);
            context.setVariable("password", password);


            context.setVariable("urlWebsite", urlWebsite+"login");

            String content = mailContentBuilder.buildchangePassword(kindOfEmail, context);

            message2.setContent(content, "text/html; charset=utf-8");
            Transport.send(message2);
            LOG.info("email sending at "+ email);

        } catch (MessagingException e) {
            LOG.error(e.toString());
            throw new RuntimeException(e);
        }
    }
    public void changeUserToAdminGroup(String recipient, Locale locale, String owner, String firstNameNewUser){
        Session session = Session.getInstance(getProperties(),new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailAccount, emailpassword);
            }
        });
        try {
            LOG.info("add newUser with email: "+ recipient);
            String kindOfEmail = "changeUserToAdminGroup";
            //recipient = "pierre.therrode@gmail.com";
            Message message2 = new MimeMessage(session);
            message2.setFrom(new InternetAddress(contactEmailWebSite));
            message2.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(recipient));

            message2.setSubject(i18NService.getMessage("email.addNewUserTitle", locale));

            //------- set all context
            Context context = new Context(locale);
            context.setVariable("owner", owner);
            context.setVariable("firstNameNewUser", firstNameNewUser);

            context.setVariable("about1", about1);
            context.setVariable("about2", about2);
            context.setVariable("titleCompany", titleCompany);
            context.setVariable("recipient", recipient);

            context.setVariable("urlWebsite", urlWebsite+"login");

            String content = mailContentBuilder.buildchangePassword(kindOfEmail, context);

            message2.setContent(content, "text/html; charset=utf-8");
            Transport.send(message2);
            LOG.info("email sending at "+ recipient);

        } catch (MessagingException e) {
            LOG.error(e.toString());
            throw new RuntimeException(e);
        }
    }
    public void changeAdminGroupToUser(String recipient, Locale locale, String owner, String firstNameNewUser){
        Session session = Session.getInstance(getProperties(),new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailAccount, emailpassword);
            }
        });
        try {
            LOG.info("add newUser with email: "+ recipient);
            String kindOfEmail = "changeAdminGroupToUser";
            //recipient = "pierre.therrode@gmail.com";
            Message message2 = new MimeMessage(session);
            message2.setFrom(new InternetAddress(contactEmailWebSite));
            message2.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(recipient));

            message2.setSubject(i18NService.getMessage("email.addNewUserTitle", locale));

            //------- set all context
            Context context = new Context(locale);
            context.setVariable("owner", owner);
            context.setVariable("firstNameNewUser", firstNameNewUser);

            context.setVariable("about1", about1);
            context.setVariable("about2", about2);
            context.setVariable("titleCompany", titleCompany);
            context.setVariable("recipient", recipient);

            context.setVariable("urlWebsite", urlWebsite+"login");

            String content = mailContentBuilder.buildchangePassword(kindOfEmail, context);

            message2.setContent(content, "text/html; charset=utf-8");
            Transport.send(message2);
            LOG.info("email sending at "+ recipient);

        } catch (MessagingException e) {
            LOG.error(e.toString());
            throw new RuntimeException(e);
        }
    }

    public void forgotPassword(String recipient, Locale locale, String owner, String firstNameNewUser){
        Session session = Session.getInstance(getProperties(),new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailAccount, emailpassword);
            }
        });
        try {
            LOG.info("add newUser with email: "+ recipient);
            String kindOfEmail = "changeAdminGroupToUser";
            //recipient = "pierre.therrode@gmail.com";
            Message message2 = new MimeMessage(session);
            message2.setFrom(new InternetAddress(contactEmailWebSite));
            message2.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(recipient));

            message2.setSubject(i18NService.getMessage("email.addNewUserTitle", locale));

            //------- set all context
            Context context = new Context(locale);
            context.setVariable("owner", owner);
            context.setVariable("firstNameNewUser", firstNameNewUser);

            context.setVariable("about1", about1);
            context.setVariable("about2", about2);
            context.setVariable("titleCompany", titleCompany);
            context.setVariable("recipient", recipient);

            context.setVariable("urlWebsite", urlWebsite+"login");

            String content = mailContentBuilder.buildchangePassword(kindOfEmail, context);

            message2.setContent(content, "text/html; charset=utf-8");
            Transport.send(message2);
            LOG.info("email sending at "+ recipient);

        } catch (MessagingException e) {
            LOG.error(e.toString());
            throw new RuntimeException(e);
        }
    }


    private Properties getProperties(){
        Properties props = new Properties();
        props.put("mail.smtp.auth", smtpAuth);
        props.put("mail.smtp.starttls.enable", startTls);
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
        return props;
    }




}
