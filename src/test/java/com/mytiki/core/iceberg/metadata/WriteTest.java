/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.core.iceberg.metadata;

import com.amazonaws.services.lambda.runtime.events.SQSBatchResponse;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.amazonaws.services.lambda.runtime.tests.annotations.Event;
import com.mytiki.core.iceberg.metadata.mock.MockIceberg;
import com.mytiki.core.iceberg.utils.ApiException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import software.amazon.awssdk.http.HttpStatusCode;

public class WriteTest {

    MockIceberg mockIceberg;

    @BeforeEach
    public void init() {
        mockIceberg = new MockIceberg();
    }

    @ParameterizedTest
    @Event(value = "events/sqs_event_success.json", type = SQSEvent.class)
    public void HandleRequest_Batch_Success(SQSEvent event) {
        WriteHandler handler = new WriteHandler(mockIceberg.iceberg());
        SQSBatchResponse response = handler.handleRequest(event, null);
        Assertions.assertEquals(0, response.getBatchItemFailures().size());
    }

    @ParameterizedTest
    @Event(value = "events/sqs_event_fail_one.json", type = SQSEvent.class)
    public void HandleRequest_Batch_FailSome(SQSEvent event) {
        WriteHandler handler = new WriteHandler(mockIceberg.iceberg());
        SQSBatchResponse response = handler.handleRequest(event, null);
        Assertions.assertEquals(1, response.getBatchItemFailures().size());
    }

    @ParameterizedTest
    @Event(value = "events/sqs_event_fail_all.json", type = SQSEvent.class)
    public void HandleRequest_Batch_FailAll(SQSEvent event) {
        WriteHandler handler = new WriteHandler(mockIceberg.iceberg());
        ApiException ex = Assertions.assertThrows(ApiException.class, () ->
                handler.handleRequest(event, null));
        Assertions.assertEquals(HttpStatusCode.BAD_REQUEST, ex.getStatus());
    }
}
