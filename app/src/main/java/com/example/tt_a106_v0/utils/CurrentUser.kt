package com.example.tt_a106_v0.utils

import java.util.*
import kotlin.collections.HashMap


object CurrentUser {
    var id = ""
    var name = ""
    var lastName = ""
    var notifications = ArrayList<Any>()
    init {    }
    fun setName(newName: String): Boolean{
        this.name = newName
        return true
    }
}