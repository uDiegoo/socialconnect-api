package br.com.socialconnect.api.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EstoqueNaoNegativoValidator implements ConstraintValidator<EstoqueNaoNegativo, Integer> {

    @Override
    public boolean isValid(Integer quantidade, ConstraintValidatorContext context) {
        return quantidade == null || quantidade >= 0;
    }
}
