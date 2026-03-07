package com.voidex.thoth.controller;

import com.voidex.thoth.dto.Email;
import com.voidex.thoth.dto.EmailResponse;
import com.voidex.thoth.service.EmailNotificationCollector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/thoth/v1")
public class EmailNotificationController {

    private static final Logger LOG = LoggerFactory.getLogger(EmailNotificationController.class);

    private final EmailNotificationCollector emailNotificationCollector;

    EmailNotificationController(EmailNotificationCollector emailNotificationCollector){
        this.emailNotificationCollector = emailNotificationCollector;
    }

    @PostMapping("/send-email")
    public ResponseEntity<EmailResponse> sendEmail(@RequestBody Email email) {

        LOG.info("Received email request id={}", email.getId());

        emailNotificationCollector.processEmailNotification(email);

        return ResponseEntity.accepted()
                .body(new EmailResponse(
                        "Email request processed successfully",
                        email.getId(),
                        "PROCESSED"
                ));
    }

    @GetMapping("/status")
    public ResponseEntity<String> getApplicationStatus(){
        return ResponseEntity.ok("UP");
    }
}
