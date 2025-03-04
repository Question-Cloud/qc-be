package com.eager.questioncloud.application.validator;

import com.eager.questioncloud.core.exception.CoreException;
import com.eager.questioncloud.core.exception.Error;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordValidator {
    private static final String PASSWORD_REGEX = "^(?!.*\\s).{8,}$";
    private static final Pattern pattern = Pattern.compile(PASSWORD_REGEX);

    public static void validate(String password) {
        Matcher matcher = pattern.matcher(password);
        if (!matcher.matches()) {
            throw new CoreException(Error.BAD_REQUEST);
        }
    }
}
