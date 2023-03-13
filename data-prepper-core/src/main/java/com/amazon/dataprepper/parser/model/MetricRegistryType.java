/*
 * Copyright OpenSearch Contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package com.amazon.dataprepper.parser.model;

public enum MetricRegistryType {
    // TODO: capitalize enum values
    Prometheus,
    CloudWatch,
    EmbeddedMetricsFormat;
}