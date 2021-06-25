package com.example.tt_a106_v0.utils

enum class MessageTypes(i: Int) {
    AddPatient(0)
}
data class Notification(var type: MessageTypes, var message: String, var from: String) {
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "type" to type.ordinal,
            "message" to message,
            "from"  to from
        )
    }
}

data class Notifications(var notifications: Array< Map<String, Any?>>) {
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "notifications" to notifications.asList()
        )
    }
}