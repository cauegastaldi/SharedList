package br.edu.ifsp.sharedlist.validator

import br.edu.ifsp.sharedlist.databinding.ActivityLoginBinding
import br.edu.ifsp.sharedlist.validator.email.EmailValidator

class LoginValidator {
        companion object {
            private const val EMPTY_EMAIL_FORM_ERROR = "Email deve ser preenchido"
            private const val INVALID_EMAIL_FORM_ERROR = "Email deve estar em um formato válido"
            private const val EMPTY_PASSWORD_FORM_ERROR = "Senha deve ser preenchida"

            fun getFormErrors(alb: ActivityLoginBinding): String {
                val formErrors = mutableListOf<String>()

                with(alb) {
                    val email = emailEt.text.toString().trim()
                    val password = passwordEt.text.toString().trim()

                    if (email.isEmpty()) {
                        formErrors.add(EMPTY_EMAIL_FORM_ERROR)
                    }
                    else if (!EmailValidator.emailInValidFormat(email)) {
                        formErrors.add(INVALID_EMAIL_FORM_ERROR)
                    }

                    if (password.isEmpty()) {
                        formErrors.add(EMPTY_PASSWORD_FORM_ERROR)
                    }

                    return formErrors.joinToString(", ")
                }
            }
        }
}