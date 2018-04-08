package com.project.test.unit;

import com.project.backend.service.MailClient;
import com.icegreen.greenmail.junit.GreenMailRule;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.icegreen.greenmail.util.ServerSetupTest;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.SpringApplicationConfiguration;

import javax.mail.MessagingException;

import static org.junit.Assert.*;


/**
 * Created by zen on 22/08/17.
 */

public class EmailSenderTest {

    @Autowired
    private MailClient mailClient;

    @Rule
    public final GreenMailRule greenMail = new GreenMailRule(ServerSetupTest.SMTP);

    @Test
    public void testSend() throws MessagingException {
        GreenMailUtil.sendTextEmailTest("to@localhost.com", "from@localhost.com",
                "some subject", "some body"); // --- Place your sending code here instead
        assertEquals("some body", GreenMailUtil.getBody(greenMail.getReceivedMessages()[0]));
    }


/*
    @Test
    public void shouldSendMail() throws Exception {
        //given
        String recipient = "name@dolszewski.com";
        String message = "Test message content";
        //when
        mailClient.prepareAndSend(recipient, message,"plain");
        //then
        String content = "<span>" + message + "</span>";
        assertNotNull(content);
    }
    */
/*
  private void assertReceivedMessageContains(String expected) throws IOException, MessagingException {
      MimeMessage[] receivedMessages = greenMail.getReceivedMessages();
      assertEquals(1, receivedMessages.length);
      String content = (String) receivedMessages[0].getContent();
      assertTrue(content.contains(expected));
  }
  */
}
