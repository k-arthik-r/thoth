package com.voidex.thoth.service;

import com.voidex.thoth.dto.Email;
import com.voidex.thoth.exception.ThothException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

public interface EmailService {

    MimeMessage createEmail(Email email) throws ThothException, MessagingException;

    void sendEmail(MimeMessage mimeMessage) throws ThothException;

}
