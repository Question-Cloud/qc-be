package com.eager.questioncloud.validator;

import com.eager.questioncloud.core.exception.CustomException;
import com.eager.questioncloud.core.exception.Error;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailValidator {
    public static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    public static final Pattern pattern = Pattern.compile(EMAIL_REGEX);

    public static void validate(String email) {
        Matcher matcher = pattern.matcher(email);
        if (!matcher.matches()) {
            throw new CustomException(Error.BAD_REQUEST);
        }
    }
}
