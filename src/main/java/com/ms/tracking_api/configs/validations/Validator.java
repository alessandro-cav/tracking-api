package com.ms.tracking_api.configs.validations;

import br.com.caelum.stella.validation.CNPJValidator;
import br.com.caelum.stella.validation.CPFValidator;
import br.com.caelum.stella.validation.InvalidStateException;
import com.ms.tracking_api.handlers.BadRequestException;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.context.annotation.Configuration;


@Configuration
public class Validator {

    public void validaCPF(String cpf) {
        CPFValidator cpfValidator = new CPFValidator();
        try {
            cpfValidator.assertValid(cpf.replaceAll("[^0-9]", "")); // Remove pontos e traços
        } catch (InvalidStateException e) {
            throw new BadRequestException("CPF inválido");
        }
    }

    public void validaCNPJ(String cnpj) {
        CNPJValidator cnpjValidator = new CNPJValidator();
        try {
            cnpjValidator.assertValid(cnpj.replaceAll("[^0-9]", "")); // Remove pontos e traços
        } catch (InvalidStateException e) {
            throw new BadRequestException("CNPJ inválido");
        }
    }

    public void validaEmail(String email) {
        EmailValidator validator = EmailValidator.getInstance();
        if (!validator.isValid(email)) {
            throw new BadRequestException("Email inválido");
        }
    }

    public boolean isEmailValid(String email) {
        EmailValidator validator = EmailValidator.getInstance();
        return validator.isValid(email);
    }
}
