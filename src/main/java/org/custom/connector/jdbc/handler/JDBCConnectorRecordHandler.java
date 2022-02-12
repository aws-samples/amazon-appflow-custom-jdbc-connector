// Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0

package org.custom.connector.jdbc.handler;

import com.amazonaws.appflow.custom.connector.handlers.RecordHandler;
import com.amazonaws.appflow.custom.connector.model.ErrorCode;
import com.amazonaws.appflow.custom.connector.model.ErrorDetails;
import com.amazonaws.appflow.custom.connector.model.ImmutableErrorDetails;
import com.amazonaws.appflow.custom.connector.model.query.ImmutableQueryDataResponse;
import com.amazonaws.appflow.custom.connector.model.query.QueryDataRequest;
import com.amazonaws.appflow.custom.connector.model.query.QueryDataResponse;
import com.amazonaws.appflow.custom.connector.model.retreive.ImmutableRetrieveDataResponse;
import com.amazonaws.appflow.custom.connector.model.retreive.RetrieveDataRequest;
import com.amazonaws.appflow.custom.connector.model.retreive.RetrieveDataResponse;
import com.amazonaws.appflow.custom.connector.model.write.ImmutableWriteDataResponse;
import com.amazonaws.appflow.custom.connector.model.write.WriteDataRequest;
import com.amazonaws.appflow.custom.connector.model.write.WriteDataResponse;
import org.custom.connector.jdbc.client.JDBCClient;
import org.custom.connector.jdbc.client.JDBCClientFactory;
import org.custom.connector.jdbc.validation.JDBCRequestValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.Objects;

public final class JDBCConnectorRecordHandler implements RecordHandler {
  private final Logger logger = LoggerFactory.getLogger(JDBCConnectorRecordHandler.class);
  private final JDBCClientFactory jdbcClient = new JDBCClientFactory();

  /**
   * Retrieves the batch of records against a set of identifiers from the source application
   * Not used at the moment.
   *
   * @param request - {@link RetrieveDataRequest}
   * @return - {@link RetrieveDataResponse}
   */
  @Override
  public RetrieveDataResponse retrieveData(final RetrieveDataRequest request) {
    ErrorDetails errorDetails = JDBCRequestValidator.validateRetrieveDataRequest(request);
    if (Objects.nonNull(errorDetails)) {
      logger.error("RetrieveData request failed with errorDetails " + errorDetails);
      return ImmutableRetrieveDataResponse.builder().isSuccess(false).errorDetails(errorDetails).build();
    }
    return null;
  }

  /**
   * Writes batch of records to the destination application
   *
   * @param request - {@link WriteDataRequest}
   * @return - {@link WriteDataResponse}
   */
  @Override
  public WriteDataResponse writeData(final WriteDataRequest request) {
    ErrorDetails errorDetails = JDBCRequestValidator.validateWriteDataRequest(request);
    if (Objects.nonNull(errorDetails)) {
      logger.error("WriteData request failed with errorDetails " + errorDetails);
      return ImmutableWriteDataResponse.builder().isSuccess(false).errorDetails(errorDetails).build();
    }
    try {
      JDBCClient client = jdbcClient.create(request.connectorContext().credentials());
      client.writeData(request);
      return ImmutableWriteDataResponse.builder()
        .isSuccess(true)
        .build();
    } catch (SQLException ex) {
      logger.error("SQLException: " + ex.getMessage());
      logger.error("SQLState: " + ex.getSQLState());
      logger.error("VendorError: " + ex.getErrorCode());
      return ImmutableWriteDataResponse.builder()
        .isSuccess(false)
        .errorDetails(ImmutableErrorDetails.builder()
          .errorCode(ErrorCode.ClientError)
          .errorMessage(ex.getMessage())
          .build())
        .build();
    }
  }

  /**
   * Queries the data from the source application against the supplied filter conditions.
   *
   * @param request - {@link QueryDataRequest}
   * @return - {@link QueryDataResponse}
   */
  @Override
  public QueryDataResponse queryData(final QueryDataRequest request) {
    ErrorDetails errorDetails = JDBCRequestValidator.validateQueryDataRequest(request);
    if (Objects.nonNull(errorDetails)) {
      logger.error("QueryData request failed with errorDetails " + errorDetails);
      return ImmutableQueryDataResponse.builder().errorDetails(errorDetails).isSuccess(false).build();
    }

    try {
      JDBCClient client = jdbcClient.create(request.connectorContext().credentials());

      // get Total rows.
      long totalRows = client.getTotalData(request);

      String nextToken = request.nextToken();

      if (totalRows > request.maxResults()) {
        if (request.nextToken() != null) {
          long n = Long.parseLong(request.nextToken()) + request.maxResults();
          if (n >= totalRows) {
            nextToken = null;
          } else {
            nextToken = Long.toString(n);
          }
        } else {
          nextToken = request.maxResults().toString();
        }
      }
      return ImmutableQueryDataResponse.builder()
        .records(client.queryData(request))
        .nextToken(nextToken)
        .isSuccess(true)
        .build();
    } catch (SQLException ex) {
      logger.error("SQLException: " + ex.getMessage());
      logger.error("SQLState: " + ex.getSQLState());
      logger.error("VendorError: " + ex.getErrorCode());
      return ImmutableQueryDataResponse.builder()
        .errorDetails(
          ImmutableErrorDetails.builder()
            .errorCode(ErrorCode.ClientError)
            .errorMessage(ex.getMessage())
            .build()
        ).isSuccess(false).build();
    }
  }
}
