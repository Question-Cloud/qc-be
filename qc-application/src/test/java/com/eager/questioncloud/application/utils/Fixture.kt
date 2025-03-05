package com.eager.questioncloud.application.utils

import com.navercorp.fixturemonkey.FixtureMonkey
import com.navercorp.fixturemonkey.api.plugin.SimpleValueJqwikPlugin
import com.navercorp.fixturemonkey.kotlin.KotlinPlugin

object Fixture {
    private var simpleValueJqwikPlugin: SimpleValueJqwikPlugin =
        SimpleValueJqwikPlugin().minStringLength(10).maxStringLength(15)
    var fixtureMonkey: FixtureMonkey = FixtureMonkey
        .builder()
        .plugin(simpleValueJqwikPlugin)
        .plugin(KotlinPlugin())
        .defaultNotNull(true)
        .build()
}