// Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0

package org.custom.connector.jdbc;

import com.amazonaws.appflow.custom.connector.lambda.handler.BaseLambdaConnectorHandler;
import com.amazonaws.appflow.custom.connector.model.ImmutableConnectorContext;
import com.amazonaws.appflow.custom.connector.model.credentials.AuthenticationType;
import com.amazonaws.appflow.custom.connector.model.credentials.ImmutableCredentials;
import com.amazonaws.appflow.custom.connector.model.metadata.DescribeEntityRequest;
import com.amazonaws.appflow.custom.connector.model.metadata.ImmutableDescribeEntityRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.custom.connector.jdbc.handler.JDBCConnectorLambdaHandler;
import org.custom.connector.jdbc.utils.TestContext;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.OutputStream;

public class DescribeEntityRequestTest {
  @Test
  void testDescribeEntityRequest() throws JsonProcessingException {
    // arrange
    final ObjectMapper objectMapper = new ObjectMapper();
    final BaseLambdaConnectorHandler requestHandler = new JDBCConnectorLambdaHandler();

    // act
    DescribeEntityRequest req = ImmutableDescribeEntityRequest.builder()
      .entityIdentifier("employees")
      .connectorContext(
        ImmutableConnectorContext.builder()
          .apiVersion("v1")
          .credentials(
            ImmutableCredentials.builder().secretArn("your-secret-arn")
              .authenticationType(AuthenticationType.CustomAuth)
              .build())
          .build())
      .build();

    requestHandler.handleRequest(
      new ByteArrayInputStream(objectMapper.writeValueAsBytes(req)),
      Mockito.mock(OutputStream.class),
      new TestContext()
    );
  }
}
