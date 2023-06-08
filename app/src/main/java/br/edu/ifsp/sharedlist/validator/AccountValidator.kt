package br.edu.ifsp.sharedlist.validator

import br.edu.ifsp.scl.sharedlist.TaskValidator
import br.edu.ifsp.sharedlist.databinding.ActivityCreateAccountBinding

class AccountValidator {
    companion object {
        private const val EMPTY_EMAIL_FORM_ERROR = "Email deve ser preenchido"
        private const val EMPTY_PASSWORD_FORM_ERROR = "Senha(s) deve(m) ser preenchida(s)"
        private const val PASSWORDS_NOT_MATCH_FORM_ERROR = "Senhas não coincidem"

        fun getFormErrors(acab: ActivityCreateAccountBinding): String {
            val formErrors = mutableListOf<String>()

            with(acab) {

                val email = emailEt.text.toString()
                val password = passwordEt.text.toString()
                val password2 = repeatPasswordEt.text.toString()

                if (email.isEmpty()) {
                    formErrors.add(EMPTY_EMAIL_FORM_ERROR)
                }

                if (password.isEmpty() || password2.isEmpty()) {
                    formErrors.add(EMPTY_PASSWORD_FORM_ERROR)
                } else if (!password.equals(password2)) {
                    formErrors.add(PASSWORDS_NOT_MATCH_FORM_ERROR)
                }

                return formErrors.joinToString(", ")
            }
        }
    }
}