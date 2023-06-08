package br.edu.ifsp.sharedlist.validator

import br.edu.ifsp.sharedlist.databinding.ActivityLoginBinding

class LoginValidator {
        companion object {
            private const val EMPTY_EMAIL_FORM_ERROR = "Email deve ser preenchido"
            private const val EMPTY_PASSWORD_FORM_ERROR = "Senha deve ser preenchida"

            fun getFormErrors(alb: ActivityLoginBinding): String {
                val formErrors = mutableListOf<String>()

                with(alb) {
                    val email = emailEt.text.toString()
                    val password = passwordEt.text.toString()

                    if (email.isEmpty()) {
                        formErrors.add(EMPTY_EMAIL_FORM_ERROR)
                    }
                    if (password.isEmpty()) {
                        formErrors.add(EMPTY_PASSWORD_FORM_ERROR)
                    }

                    return formErrors.joinToString(", ")
                }
            }
        }
}