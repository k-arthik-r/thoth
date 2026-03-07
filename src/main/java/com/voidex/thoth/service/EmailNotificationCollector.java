package com.voidex.thoth.service;

import com.voidex.thoth.dto.Attachment;
import com.voidex.thoth.dto.Email;
import com.voidex.thoth.exception.ThothException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.voidex.thoth.utils.ThothConstants.VALID;
import static com.voidex.thoth.utils.ThothConstants.IN_VALID;
import static com.voidex.thoth.utils.ThothConstants.ZERO;

public class EmailNotificationCollector {

    private final EmailValidationService emailValidationService;
    private final EmailServiceImpl emailService;
    private final Logger LOG = LoggerFactory.getLogger(EmailNotificationCollector.class);

    EmailNotificationCollector(EmailValidationService emailValidationService,
                               EmailServiceImpl emailService) {
        this.emailValidationService = emailValidationService;
        this.emailService = emailService;
    }

    public void processEmailNotification(Email email) {
        try {

            if (email.getRecipient() == null) {
                LOG.warn("Email recipient cannot be null. Dropping notification: {}", email);
                return;
            }

            if (email.getBody() == null && (email.getAttachments() == null || email.getAttachments().isEmpty())) {
                LOG.warn("Email body & attachments cannot both be empty. Dropping notification: {}", email.getId());
                return;
            }

            if (!hasValidAttachments(email.getAttachments())) {
                LOG.warn("Email attachments is invalid. Dropping notification: {}", email.getId());
                return;
            }

            Email processedEmail = new Email();
            processedEmail.setId(email.getId());
            processedEmail.setSubject(email.getSubject());
            processedEmail.setBody(email.getBody());
            processedEmail.setIsHtmlContent(email.getIsHtmlContent());
            processedEmail.setAttachments(email.getAttachments());

            filterInvalidRecipients(email, processedEmail);
            filterInvalidCcAndBcc(email, processedEmail);
            enhanceAttachmentMetadata(processedEmail);

            MimeMessage mimeMessage = emailService.createEmail(processedEmail);
            emailService.sendEmail(mimeMessage);

        } catch (ThothException e) {
            LOG.warn("Terminating process: {}", e.getMessage());
        } catch (Exception e) {
            LOG.error("Unexpected error processing email ID: {}", email.getId(), e);
        }
    }


    private void enhanceAttachmentMetadata(Email email) {
        List<Attachment> attachments = email.getAttachments();

        if (attachments == null || attachments.isEmpty()) {
            return;
        }

        attachments.forEach(attachment -> {
            String fileName = attachment.getFileName();
            if (fileName == null || fileName.trim().isEmpty()) {
                String generatedName = "attachment_" + System.nanoTime();
                attachment.setFileName(generatedName);
                LOG.info("Generated missing filename: {} for attachment in Email: {}",
                        generatedName, email.getId());
            }
        });
    }

    private boolean hasValidAttachments(List<Attachment> attachments) {

        if (attachments == null || attachments.isEmpty()) {
            return true;
        }

        return attachments.stream().allMatch(attachment -> {
            if (attachment.getContent() == null || attachment.getContentType() == null) {
                LOG.warn("Attachment validation failed: Content or ContentType is null. Attachment: {}", attachment);
                return false;
            }
            return true;
        });
    }

    private void filterInvalidRecipients(Email email, Email processedEmail) throws ThothException {
        Map<String, String[]> recipients = emailValidationService.validateEmails(email.getRecipient());

        if(recipients.get(VALID).length == ZERO){
            throw new ThothException("No valid recipients. Dropping notification: " + email.getId());        }

        if(recipients.get(IN_VALID).length > 0){
            LOG.warn("Found Invalid email recipients: {} for Notification: {}. Skipping them",
                    Arrays.toString(recipients.get(IN_VALID)), email.getId());
        }

        processedEmail.setRecipient(recipients.get(VALID));
    }

    private void filterInvalidCcAndBcc(Email email, Email processedEmail) throws ThothException {
        Map<String, String[]> ccRecipients = emailValidationService.validateEmails(email.getCc());
        Map<String, String[]> bccRecipients = emailValidationService.validateEmails(email.getBcc());

        if(ccRecipients.get(IN_VALID).length > 0){
            LOG.warn("Found Invalid CC email recipients: {} for Notification: {}. Skipping them",
                    Arrays.toString(ccRecipients.get(IN_VALID)), email.getId());
        }
        if(bccRecipients.get(IN_VALID).length > 0){
            LOG.warn("Found Invalid BCC email recipients: {} for Notification: {}. Skipping them",
                    Arrays.toString(ccRecipients.get(IN_VALID)), email.getId());
        }

        processedEmail.setCc(ccRecipients.get(VALID));
        processedEmail.setBcc(bccRecipients.get(VALID));
    }
}
