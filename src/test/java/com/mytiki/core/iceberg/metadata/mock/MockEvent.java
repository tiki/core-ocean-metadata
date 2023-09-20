/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.core.iceberg.metadata.mock;

import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.mytiki.core.iceberg.metadata.ReqBody;
import com.mytiki.core.iceberg.utils.Mapper;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class MockEvent {
    public static final String URI_BASE = "s3://mytiki-ocean-test/data";

    public static SQSEvent event(String table) {
        List<SQSEvent.SQSMessage> messages = new ArrayList<>(10);
        for(int i=0; i<10; i++){
            messages.add(message(table));
        }
        SQSEvent event = new SQSEvent();
        event.setRecords(messages);
        return event;
    }

    public static SQSEvent.SQSMessage message(String table) {
       SQSEvent.SQSMessage msg = new SQSEvent.SQSMessage();
       msg.setMessageId(UUID.randomUUID().toString());
       msg.setBody(new Mapper().writeValueAsString(body(table)));
       msg.setAttributes(new HashMap<>(){{
           put("MessageGroupId", groupId(table));
       }});
       return msg;
    }

   public static ReqBody body(String table) {
        ReqBody body = new ReqBody();
        body.setSize(420);
        body.setCount(69);
        body.setTable(table);
        body.setUri(String.join("/", URI_BASE, table, UUID.randomUUID() + ".avro"));
        return body;
   }

   public static String groupId(String table) {
       MessageDigest digest = null;
       try {
           digest = MessageDigest.getInstance("SHA-256");
           byte[] hash = digest.digest(table.getBytes(StandardCharsets.UTF_8));
           return Base64.getEncoder().encodeToString(hash);
       } catch (NoSuchAlgorithmException e) {
           throw new RuntimeException(e);
       }
   }
}
