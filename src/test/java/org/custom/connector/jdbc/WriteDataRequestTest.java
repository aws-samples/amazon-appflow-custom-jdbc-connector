// Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0

package org.custom.connector.jdbc;

import com.amazonaws.appflow.custom.connector.lambda.handler.BaseLambdaConnectorHandler;
import com.amazonaws.appflow.custom.connector.model.ImmutableConnectorContext;
import com.amazonaws.appflow.custom.connector.model.credentials.AuthenticationType;
import com.amazonaws.appflow.custom.connector.model.credentials.ImmutableCredentials;
import com.amazonaws.appflow.custom.connector.model.write.ImmutableWriteDataRequest;
import com.amazonaws.appflow.custom.connector.model.write.WriteDataRequest;
import com.amazonaws.appflow.custom.connector.model.write.WriteOperationType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.custom.connector.jdbc.handler.JDBCConnectorLambdaHandler;
import org.custom.connector.jdbc.utils.TestContext;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Collections;

public class WriteDataRequestTest {
  @Test
  void testWriteDataRequestTest() throws JsonProcessingException {
    // arrange
    final ObjectMapper objectMapper = new ObjectMapper();
    final BaseLambdaConnectorHandler requestHandler = new JDBCConnectorLambdaHandler();

    // act
    WriteDataRequest request = ImmutableWriteDataRequest.builder()
      .entityIdentifier("employees-backup")
      .idFieldNames(Collections.singletonList("id"))
      .operation(WriteOperationType.UPDATE)
      .allOrNone(true)
      .records(
        Arrays.asList("{\"id\":\"1\",\"firstname\":\"Sophi3333444a\",\"lastname\":\"Oldham\",\"age\":\"11\",\"email\":\"\"}",
          "{\"id\":\"2\",\"firstname\":\"Augustina\",\"lastname\":\"R55555evey\",\"age\":\"4\",\"email\":\"\"}"))
      .connectorContext(
        ImmutableConnectorContext.builder()
          .apiVersion("v1")
          .credentials(
            ImmutableCredentials.builder().secretArn("your-secret-arn")
              .authenticationType(AuthenticationType.CustomAuth)
              .build()).build())
      .build();

    requestHandler.handleRequest(
      new ByteArrayInputStream(objectMapper.writeValueAsBytes(request)),
      Mockito.mock(OutputStream.class),
      new TestContext()
    );
  }
}
