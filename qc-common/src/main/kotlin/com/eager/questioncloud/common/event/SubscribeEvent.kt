package com.eager.questioncloud.common.event

import io.hypersistence.tsid.TSID

class SubscribeEvent(
    val creatorId: Long,
    val type: SubscribeEventType,
) : Event(TSID.Factory.getTsid().toString(), EventType.SubscribeEvent)