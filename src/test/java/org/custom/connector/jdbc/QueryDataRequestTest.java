// Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0

package org.custom.connector.jdbc;

import com.amazonaws.appflow.custom.connector.lambda.handler.BaseLambdaConnectorHandler;
import com.amazonaws.appflow.custom.connector.model.ImmutableConnectorContext;
import com.amazonaws.appflow.custom.connector.model.credentials.AuthenticationType;
import com.amazonaws.appflow.custom.connector.model.credentials.ImmutableCredentials;
import com.amazonaws.appflow.custom.connector.model.query.ImmutableQueryDataRequest;
import com.amazonaws.appflow.custom.connector.model.query.QueryDataRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.custom.connector.jdbc.handler.JDBCConnectorLambdaHandler;
import org.custom.connector.jdbc.utils.TestContext;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.OutputStream;
import java.util.Arrays;

class QueryDataRequestTest {
  @Test
  void testQueryDataRequestTest() throws JsonProcessingException {
    // arrange
    final ObjectMapper objectMapper = new ObjectMapper();
    final BaseLambdaConnectorHandler requestHandler = new JDBCConnectorLambdaHandler();

    // act
    QueryDataRequest request = ImmutableQueryDataRequest.builder()
      .entityIdentifier("employees")
      .selectedFieldNames(Arrays.asList("id", "firstname", "lastname", "email"))
      .filterExpression("id >= \"30\" AND id > \"2000\"")
      .maxResults((long) 1000)
      .nextToken("2000")
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
