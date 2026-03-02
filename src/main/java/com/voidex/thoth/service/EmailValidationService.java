package com.voidex.thoth.service;

import java.util.Map;
import java.util.Collections;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.voidex.thoth.utils.ThothConstants.*;

public class EmailValidationService {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    public static final Predicate<String> IS_VALID_EMAIL = email ->
            email != null && EMAIL_PATTERN.matcher(email).matches();


    public Map<String, String[]> validateEmails(String[] emails) {
        if (emails == null) {
            return Collections.emptyMap();
        }

        Map<Boolean, List<String>> partitioned = Arrays.stream(emails)
                .filter(Objects::nonNull)
                .collect(Collectors.partitioningBy(IS_VALID_EMAIL));

        Map<String, String[]> result = new HashMap<>(2);
        result.put(VALID, partitioned.get(true).toArray(new String[0]));
        result.put(IN_VALID, partitioned.get(false).toArray(new String[0]));

        return result;
    }
}
