/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.ocean.metadata;

import com.amazonaws.services.lambda.runtime.events.SQSBatchResponse;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.mytiki.ocean.metadata.mock.MockEvent;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class AppTest {

    @Test
    public void success() {
        App app = new App();
        SQSEvent event = MockEvent.event("logs");
        SQSBatchResponse response = app.handleRequest(event, null);
        assertEquals(0, response.getBatchItemFailures().size());
    }
}
