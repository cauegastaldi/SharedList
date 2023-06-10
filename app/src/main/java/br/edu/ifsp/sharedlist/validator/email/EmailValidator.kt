package br.edu.ifsp.sharedlist.validator.email

import java.util.regex.Pattern

class EmailValidator {

    companion object {
        fun emailInValidFormat(email: String): Boolean {
            val regex = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$"

            return Pattern.compile(regex)
                .matcher(email)
                .matches()
        }
    }
}