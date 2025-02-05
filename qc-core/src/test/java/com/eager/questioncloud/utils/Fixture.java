package com.eager.questioncloud.utils;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.FieldReflectionArbitraryIntrospector;
import com.navercorp.fixturemonkey.api.plugin.SimpleValueJqwikPlugin;

public class Fixture {
    public static SimpleValueJqwikPlugin simpleValueJqwikPlugin = new SimpleValueJqwikPlugin().minStringLength(10).maxStringLength(15);
    public static FixtureMonkey fixtureMonkey = FixtureMonkey
        .builder()
        .plugin(simpleValueJqwikPlugin)
        .defaultNotNull(true)
        .objectIntrospector(FieldReflectionArbitraryIntrospector.INSTANCE)
        .build();
}
