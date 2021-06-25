package com.example.tt_a106_v0.utils

enum class MessageTypes {
    AddPatient
}
data class Notification(var type: MessageTypes, var message: String) {
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "type" to type,
            "message" to message
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