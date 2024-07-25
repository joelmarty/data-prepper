/*
 * Copyright OpenSearch Contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package org.opensearch.dataprepper.plugins.source.rds.export;

import org.opensearch.dataprepper.plugins.source.rds.model.SnapshotInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.services.rds.RdsClient;
import software.amazon.awssdk.services.rds.model.CreateDbSnapshotRequest;
import software.amazon.awssdk.services.rds.model.CreateDbSnapshotResponse;
import software.amazon.awssdk.services.rds.model.DescribeDbSnapshotsRequest;
import software.amazon.awssdk.services.rds.model.DescribeDbSnapshotsResponse;

import java.time.Instant;

/**
 * This snapshot strategy works with RDS Instances
 */
public class InstanceSnapshotStrategy implements SnapshotStrategy {
    private static final Logger LOG = LoggerFactory.getLogger(InstanceSnapshotStrategy.class);
    private final RdsClient rdsClient;

    public InstanceSnapshotStrategy(final RdsClient rdsClient) {
        this.rdsClient = rdsClient;
    }

    @Override
    public SnapshotInfo createSnapshot(String dbIdentifier, String snapshotId) {
        CreateDbSnapshotRequest request = CreateDbSnapshotRequest.builder()
                .dbInstanceIdentifier(dbIdentifier)
                .dbSnapshotIdentifier(snapshotId)
                .build();

        try {
            CreateDbSnapshotResponse response = rdsClient.createDBSnapshot(request);
            String snapshotArn = response.dbSnapshot().dbSnapshotArn();
            String status = response.dbSnapshot().status();
            Instant createTime = response.dbSnapshot().snapshotCreateTime();
            LOG.info("Creating snapshot with id {} for {} and the current status is {}", snapshotId, dbIdentifier, status);

            return new SnapshotInfo(snapshotId, snapshotArn, createTime, status);
        } catch (Exception e) {
            LOG.error("Failed to create snapshot for {}", dbIdentifier, e);
            return null;
        }
    }

    @Override
    public SnapshotInfo describeSnapshot(String snapshotId) {
        DescribeDbSnapshotsRequest request = DescribeDbSnapshotsRequest.builder()
                .dbSnapshotIdentifier(snapshotId)
                .build();

        DescribeDbSnapshotsResponse response = rdsClient.describeDBSnapshots(request);
        String snapshotArn = response.dbSnapshots().get(0).dbSnapshotArn();
        String status = response.dbSnapshots().get(0).status();
        Instant createTime = response.dbSnapshots().get(0).snapshotCreateTime();

        return new SnapshotInfo(snapshotId, snapshotArn, createTime, status);
    }
}
