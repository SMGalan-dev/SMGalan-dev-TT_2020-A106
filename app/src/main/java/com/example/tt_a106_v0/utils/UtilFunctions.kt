package com.example.tt_a106_v0.utils


fun isValidString(str: String): Boolean{
    return android.util.Patterns.EMAIL_ADDRESS.matcher(str).matches()
}