package com.watirna.shop.utils

import java.util.regex.Pattern

class  Validator{
    // at least 8 characters. => {8,}
    // at lease 1 numeric => (?=.*\\d)
    // at lease 1 capital => (?=.*[A-Z])
    // private static final String PWD_PATTERN = "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[@#$%^&+=])(?=\\S+$).{6,}$";

    fun passwordValidate(password: String): Boolean {
        var password = password
        password = password.trim { it <= ' ' }
        val pattern = Pattern.compile("^(?=.*[A-Za-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[@#$%^&+=])(?=\\S+$).{8,15}$")
        val matcher = pattern.matcher(password)
        return password.length > 0 && matcher.matches()
    }

    fun isValidateEmail(email: String): Boolean {
        var email = email
        val EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
        email = email.trim { it <= ' ' }
        val pattern = Pattern.compile(EMAIL_PATTERN)
        val matcher = pattern.matcher(email)
        return email.length > 0 && matcher.matches()
    }

    fun isValidPhoneNumber(phoneNumber: String): Boolean {
        val expression = "^([0-9\\+]|\\(\\d{1,3}\\))[0-9\\-\\. ]{5,16}$"
        val pattern = Pattern.compile(expression)
        val matcher = pattern.matcher(phoneNumber)
        return matcher.matches()


    }

    fun isValidPhoneNumber2(phoneNumber: String): Boolean {
        val expression = "^([0-9\\+]|\\(\\d{1,3}\\))[0-9\\-\\. ]{9,16}$"
        val pattern = Pattern.compile(expression)
        val matcher = pattern.matcher(phoneNumber)
        return matcher.matches()


    }

    fun isNumber(number: String): Boolean {
        return number.length > 0 && number.matches("^\\d+$".toRegex())
    }
}