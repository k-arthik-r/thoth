package com.voidex.thoth.service;

import java.util.Map;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;
import java.util.List;
import java.util.function.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.voidex.thoth.utils.ThothConstants.EMAIL_REGEX;
import static com.voidex.thoth.utils.ThothConstants.VALID;
import static com.voidex.thoth.utils.ThothConstants.IN_VALID;

@Component
public class EmailValidationService {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    public static final Predicate<String> IS_VALID_EMAIL = email ->
            email != null && EMAIL_PATTERN.matcher(email).matches();

    public static final Logger LOG = LoggerFactory.getLogger(EmailValidationService.class);


    public Map<String, String[]> validateEmails(String[] emails) {

        Map<String, String[]> result = new HashMap<>(2);

        if (emails == null) {
            LOG.warn("Email list is empty");
            result.put(VALID, new String[0]);
            result.put(IN_VALID, new String[0]);
            return result;
        }

        Map<Boolean, List<String>> partitioned =
                Arrays.stream(emails)
                        .filter(Objects::nonNull)
                        .collect(Collectors.partitioningBy(IS_VALID_EMAIL));

        result.put(VALID, partitioned.get(true).toArray(new String[0]));
        result.put(IN_VALID, partitioned.get(false).toArray(new String[0]));

        return result;
    }
}
