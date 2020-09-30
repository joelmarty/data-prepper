package com.amazon.situp.plugins.processor;

public class ServiceMapProcessorConfig {
    static final String WINDOW_DURATION = "window_duration";
    static final int DEFAULT_WINDOW_DURATION = 180;
    static final String DEFAULT_TRACES_LMDB_PATH = "data/service-map/spans";
    static final String DEFAULT_SPANS_LMDB_PATH = "data/service-map/traces";
}
