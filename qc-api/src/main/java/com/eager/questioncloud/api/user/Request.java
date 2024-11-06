package com.eager.questioncloud.api.user;

import com.eager.questioncloud.core.domain.payment.point.vo.ChargePointType;
import com.eager.questioncloud.core.domain.user.vo.AccountType;
import com.eager.questioncloud.core.exception.CustomException;
import com.eager.questioncloud.core.exception.Error;
import com.eager.questioncloud.pg.portone.enums.PortoneWebhookStatus;
import com.eager.questioncloud.validator.EmailValidator;
import com.eager.questioncloud.validator.PasswordValidator;
import com.eager.questioncloud.validator.Validatable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import org.springframework.util.StringUtils;

public class Request {
    @Getter
    public static class CreateUserRequest implements Validatable {
        private String email;

        private String password;

        private String socialRegisterToken;

        private AccountType accountType;

        @NotBlank
        private String phone;

        @NotBlank
        @Size(min = 2)
        private String name;

        public void validate() {
            if (accountType.equals(AccountType.EMAIL)) {
                EmailValidator.validate(email);
                PasswordValidator.validate(password);
                return;
            }
            socialRegisterTokenValidate();
        }

        public void socialRegisterTokenValidate() {
            if (!StringUtils.hasText(socialRegisterToken)) {
                throw new CustomException(Error.BAD_REQUEST);
            }
        }
    }

    @Getter
    public static class ChangePasswordRequest implements Validatable {
        @NotBlank
        private String token;
        private String newPassword;

        public void validate() {
            PasswordValidator.validate(newPassword);
        }
    }

    @Getter
    public static class UpdateMyInformationRequest {
        private String profileImage;
        @NotBlank
        private String name;
    }

    @Getter
    public static class ChargePointRequest {
        @NotNull
        private ChargePointType chargePointType;

        @NotBlank
        private String paymentId;
    }

    @Getter
    public static class ChargePointOrderRequest {
        @NotNull
        private ChargePointType chargePointType;

        @NotBlank
        private String paymentId;
    }

    @Getter
    public static class ChargePointPaymentRequest {
        private String payment_id;
        private PortoneWebhookStatus status;
    }

    @Getter
    public static class RegisterCouponRequest {
        @NotBlank
        private String code;
    }
}
