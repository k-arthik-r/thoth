package com.voidex.thoth.service;

import com.voidex.thoth.dto.Email;
import com.voidex.thoth.exception.ThothException;
import jakarta.mail.MessagingException;

public interface EmailService {

    void createAndSendEmail(Email email) throws ThothException, MessagingException;

}
