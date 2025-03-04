package com.eager.questioncloud.pg.portone

enum class PortoneWebhookStatus(val value: String) {
    Ready("Ready"), Paid("Paid"), VirtualAccountIssued("VirtualAccountIssued"), PartialCancelled("PartialCancelled"),
    Cancelled("Cancelled"), Failed("Failed"), PayPending("PayPending"), CancelPending("CancelPending")
}
