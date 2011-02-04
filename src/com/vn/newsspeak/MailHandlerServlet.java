package com.vn.newsspeak;

import java.io.IOException;
import java.util.Properties;

import javax.jdo.PersistenceManager;
import javax.mail.Address;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Handle all incoming emails to appEngine.
 * @author narla
 *
 */
public class MailHandlerServlet extends HttpServlet {

	/**
	 * Default UID
	 */
	private static final long serialVersionUID = 1L;

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		Properties props = new Properties();
		
		// Add the {@code Email} object to the persistent store
        PersistenceManager pm = PMF.get().getPersistenceManager();
        Session email = Session.getDefaultInstance(props, null);

		try {
			MimeMessage message = new MimeMessage(email, req.getInputStream());
			String summary = message.getSubject();
			String description = getText(message);
			Address[] fromAddresses = message.getFrom();
			Address[] toAddresses = message.getRecipients(RecipientType.TO);
			
			Email emailObj = new Email(fromAddresses[0].toString(), 
										toAddresses[0].toString(),
										summary,
										description);
			pm.makePersistent(emailObj);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pm.close();
		}
	}

	/**
     * Return the primary text content of the message.
     */
    private String getText(Part p) throws MessagingException, IOException {
		if (p.isMimeType("text/*")) {
            String s = (String)p.getContent();
            return s;
        }

        if (p.isMimeType("multipart/alternative")) {
            // prefer html text over plain text
            Multipart mp = (Multipart)p.getContent();
            String text = null;
            for (int i = 0; i < mp.getCount(); i++) {
                Part bp = mp.getBodyPart(i);
                if (bp.isMimeType("text/plain")) {
                    if (text == null)
                        text = getText(bp);
                    continue;
                } else if (bp.isMimeType("text/html")) {
                    String s = getText(bp);
                    if (s != null)
                        return s;
                } else {
                    return getText(bp);
                }
            }
            return text;
        } else if (p.isMimeType("multipart/*")) {
            Multipart mp = (Multipart)p.getContent();
            for (int i = 0; i < mp.getCount(); i++) {
                String s = getText(mp.getBodyPart(i));
                if (s != null)
                    return s;
            }
        }

        return null;
    }

}
