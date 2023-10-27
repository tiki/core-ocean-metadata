/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.core.iceberg.metadata;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSBatchResponse;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.mytiki.core.iceberg.utils.Iceberg;
import com.mytiki.core.iceberg.utils.Initialize;
import org.apache.logging.log4j.Logger;

import java.util.List;


public class App implements RequestHandler<SQSEvent, SQSBatchResponse> {
    protected static final Logger logger = Initialize.logger(App.class);
    private final Iceberg iceberg = Iceberg.load();

    public SQSBatchResponse handleRequest(final SQSEvent event, final Context context) {
        try {
            iceberg.initialize();
            WriteHandler handler = new WriteHandler(iceberg);
            return handler.handleRequest(event, context);
        } catch (Exception ex) {
            logger.error(ex, ex.fillInStackTrace());
            List<SQSBatchResponse.BatchItemFailure> all = event.getRecords().stream()
                    .map(ev -> new SQSBatchResponse.BatchItemFailure(ev.getMessageId()))
                    .toList();
            return SQSBatchResponse.builder()
                    .withBatchItemFailures(all)
                    .build();
        }
    }
}
