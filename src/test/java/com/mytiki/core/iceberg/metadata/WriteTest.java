/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.core.iceberg.metadata;

import com.amazonaws.services.lambda.runtime.events.SQSBatchResponse;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.mytiki.core.iceberg.metadata.mock.MockEvent;
import com.mytiki.core.iceberg.metadata.mock.MockIceberg;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class WriteTest {

    MockIceberg mockIceberg;

    @Before
    public void init() {
        mockIceberg = new MockIceberg();
    }

    @Test
    public void HandleRequest_Batch_Success() {
        SQSEvent event = MockEvent.event("dummy");
        WriteHandler handler = new WriteHandler(mockIceberg.iceberg());
        SQSBatchResponse response = handler.handleRequest(event, null);
        assertEquals(0, response.getBatchItemFailures().size());
    }

    @Test
    @Ignore
    public void HandleRequest_Batch_FailSome() {
        //TODO
    }

    @Test
    @Ignore
    public void HandleRequest_Batch_FailAll() {
        //TODO
    }
}
