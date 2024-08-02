package com.ms.tracking_api.configs.validations;


import br.com.caelum.stella.validation.CNPJValidator;
import br.com.caelum.stella.validation.CPFValidator;
import com.ms.tracking_api.handlers.ErrorResponseValid;
import com.ms.tracking_api.handlers.ValidExceptionResponse;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Configuration
public class Validator {
    private void validaCPF(String cpf) throws Exception {
        CPFValidator cpfValidator = new CPFValidator();
        cpfValidator.assertValid(cpf);
    }

    private void validaCNPJ(String cnpj) throws Exception {
        CNPJValidator cnpjValidator = new CNPJValidator();
        cnpjValidator.assertValid(cnpj);
    }

    private Boolean emailValidator(String email) {
        boolean valido = false;
        EmailValidator validator = EmailValidator.getInstance();
        if (validator.isValid(email)) {
            valido = true;
        }
        return valido;
    }

    public void validacaoDoCpfECnpjEEmail(String cpf, String cnpj, String email) {
        List<ErrorResponseValid> errorResponseValids = new ArrayList<>();

        if (cpf != null) {
            try {
                this.validaCPF(cpf);
            } catch (Exception e) {
                errorResponseValids.add(new ErrorResponseValid(LocalDateTime.now(), e.getMessage()));
            }
        }

        if (cnpj != null) {
            try {
                this.validaCNPJ(cnpj);
            } catch (Exception e) {
                errorResponseValids.add(new ErrorResponseValid(LocalDateTime.now(), e.getMessage()));
            }
        }

        if (email != null) {
            Boolean valido = this.emailValidator(email);

            if (valido == false) {
                errorResponseValids.add(new ErrorResponseValid(LocalDateTime.now(), "Email inválido"));
            }
        }

        if( errorResponseValids.size() > 0) {
            throw new ValidExceptionResponse(errorResponseValids);
        }
    }
}
