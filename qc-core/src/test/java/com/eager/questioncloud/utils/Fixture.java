package com.eager.questioncloud.utils;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.plugin.SimpleValueJqwikPlugin;
import com.navercorp.fixturemonkey.kotlin.KotlinPlugin;

public class Fixture {
    public static SimpleValueJqwikPlugin simpleValueJqwikPlugin = new SimpleValueJqwikPlugin().minStringLength(10).maxStringLength(15);
    public static FixtureMonkey fixtureMonkey = FixtureMonkey
        .builder()
        .plugin(simpleValueJqwikPlugin)
        .plugin(new KotlinPlugin())
        .defaultNotNull(true)
        .build();
}
