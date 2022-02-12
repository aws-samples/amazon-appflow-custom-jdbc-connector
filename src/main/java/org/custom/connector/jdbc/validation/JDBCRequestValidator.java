// Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0

package org.custom.connector.jdbc.validation;

import com.amazonaws.appflow.custom.connector.model.ConnectorContext;
import com.amazonaws.appflow.custom.connector.model.ErrorCode;
import com.amazonaws.appflow.custom.connector.model.ErrorDetails;
import com.amazonaws.appflow.custom.connector.model.ImmutableErrorDetails;
import com.amazonaws.appflow.custom.connector.model.credentials.Credentials;
import com.amazonaws.appflow.custom.connector.model.query.QueryDataRequest;
import com.amazonaws.appflow.custom.connector.model.retreive.RetrieveDataRequest;
import com.amazonaws.appflow.custom.connector.model.write.WriteDataRequest;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Request Validator. Contains sample validation for each type of request.
 */
public final class JDBCRequestValidator {

  private JDBCRequestValidator() {
  }

  public static ErrorDetails validateRetrieveDataRequest(final RetrieveDataRequest request) {
    final List<String> errors = validateConnectorContext(request.connectorContext());
    if (CollectionUtils.isEmpty(errors)) {
      return null;
    }
    return ImmutableErrorDetails.builder()
      .errorCode(ErrorCode.InvalidArgument)
      .errorMessage(String.join(",", errors))
      .build();
  }

  public static ErrorDetails validateWriteDataRequest(final WriteDataRequest request) {
    final List<String> errors = new ArrayList<>(validateConnectorContext(request.connectorContext()));
    if (CollectionUtils.isEmpty(errors)) {
      return null;
    }
    return ImmutableErrorDetails.builder()
      .errorCode(ErrorCode.InvalidArgument)
      .errorMessage(String.join(",", errors))
      .build();
  }

  public static ErrorDetails validateQueryDataRequest(final QueryDataRequest request) {
    final List<String> errors = validateConnectorContext(request.connectorContext());
    if (CollectionUtils.isEmpty(errors)) {
      return null;
    }
    return ImmutableErrorDetails.builder()
      .errorCode(ErrorCode.InvalidArgument)
      .errorMessage(String.join(",", errors))
      .build();
  }

  private static List<String> validateConnectorContext(final ConnectorContext connectorContext) {
    return new ArrayList<>(validateCredentialsInput(connectorContext.credentials()));
  }

  private static List<String> validateCredentialsInput(final Credentials credentials) {
    final List<String> errors = new ArrayList<>();
    if (Objects.isNull(credentials.secretArn())) {
      errors.add("Secret Arn for credentials should be provided as part of request in ConnectorContext");
    }
    return errors;
  }
}
