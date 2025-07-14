package gift.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ProductNameValidator implements ConstraintValidator<ValidProductName, String> {

    private static final String ALLOWED_SPECIAL_CHARS = "()+\\-\\[\\]&/_"; // ( ), [ ], +, -, &, /, _
    private static final String PATTERN = "^[a-zA-Z0-9" + ALLOWED_SPECIAL_CHARS + "ㄱ-ㅎ가-힣\\s]*$";

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // null인 경우에는 Product name필드의 NotBlank 조건에 걸림.
        }
        return value.matches(PATTERN);
    }
}