package br.edu.ifsp.sharedlist.validator

import br.edu.ifsp.sharedlist.databinding.ActivityCreateAccountBinding
import br.edu.ifsp.sharedlist.validator.email.EmailValidator

class CreateAccountValidator {
    companion object {
        private const val EMPTY_EMAIL_FORM_ERROR = "Email deve ser preenchido"
        private const val INVALID_EMAIL_FORM_ERROR = "Email deve estar em um formato válido"
        private const val EMPTY_PASSWORD_FORM_ERROR = "Senha(s) deve(m) ser preenchida(s)"
        private const val MINIMUM_LENGTH_PASSWORD_FORM_ERROR = "Senha deve possuir ao menos seis caracteres"
        private const val PASSWORDS_NOT_MATCH_FORM_ERROR = "Senhas não coincidem"

        fun getFormErrors(acab: ActivityCreateAccountBinding): String {
            val formErrors = mutableListOf<String>()

            with(acab) {

                val email = emailEt.text.toString().trim()
                val password = passwordEt.text.toString().trim()
                val password2 = repeatPasswordEt.text.toString().trim()

                if (email.isEmpty()) {
                    formErrors.add(EMPTY_EMAIL_FORM_ERROR)
                }
                else if (!EmailValidator.emailInValidFormat(email)) {
                    formErrors.add(INVALID_EMAIL_FORM_ERROR)
                }

                if (password.isEmpty() || password2.isEmpty()) {
                    formErrors.add(EMPTY_PASSWORD_FORM_ERROR)
                }
                else if (password != password2) {
                    formErrors.add(PASSWORDS_NOT_MATCH_FORM_ERROR)
                }
                else if (password.length < 6) {
                    formErrors.add(MINIMUM_LENGTH_PASSWORD_FORM_ERROR)
                }

                return formErrors.joinToString(", ")
            }
        }
    }
}