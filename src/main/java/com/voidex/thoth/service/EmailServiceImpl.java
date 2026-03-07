package com.voidex.thoth.service;

import com.voidex.thoth.dto.Attachment;
import com.voidex.thoth.dto.Email;
import com.voidex.thoth.exception.ThothException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailServiceImpl implements EmailService{

    private final String senderEmail;
    private final JavaMailSender javaMailSender;
    private final Logger LOG = LoggerFactory.getLogger(EmailServiceImpl.class);


    EmailServiceImpl(@Value("${spring.mail.username}") String senderEmail,
                     JavaMailSender javaMailSender){
        this.senderEmail = senderEmail;
        this.javaMailSender = javaMailSender;
    }


    @Override
    public MimeMessage createEmail(Email email) throws ThothException, MessagingException {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);

        messageHelper.setFrom(senderEmail);
        messageHelper.setTo(email.getRecipient());
        messageHelper.setSubject(email.getSubject());
        messageHelper.setText(email.getBody(), email.getIsHtmlContent());

        if (null != email.getCc()) {
            messageHelper.setCc(email.getCc());
        }

        if (null != email.getBcc()) {
            messageHelper.setBcc(email.getBcc());
        }

        if (email.getAttachments() != null && !email.getAttachments().isEmpty()) {
            addAttachments(messageHelper, email.getAttachments());
        }

        return mimeMessage;
    }

    private void addAttachments(MimeMessageHelper messageHelper, List<Attachment> attachments) throws ThothException {
        for (Attachment attachment : attachments) {
            if (attachment.getContent() != null && attachment.getFileName() != null) {
                try {

                    messageHelper.addAttachment(
                            attachment.getFileName(),
                            new ByteArrayResource(attachment.getContent()),
                            attachment.getContentType()
                    );
                } catch (MessagingException e) {
                    LOG.error("Failed to attach file: {}", attachment.getFileName());
                    throw new ThothException("Attachment failure: " + attachment.getFileName(), e);
                }
            }
        }
    }

    public void sendEmail(MimeMessage mailMessage) throws ThothException {
        try {
            javaMailSender.send(mailMessage);
            LOG.info("Email sent successfully");
        } catch (Exception e) {
            LOG.error("SMTP server error while sending email", e);
            throw new ThothException("Failed to send email via SMTP", e);
        }
    }
}


