// Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0

package org.custom.connector.jdbc.handler;

import com.amazonaws.appflow.custom.connector.handlers.ConfigurationHandler;
import com.amazonaws.appflow.custom.connector.model.ErrorCode;
import com.amazonaws.appflow.custom.connector.model.ImmutableErrorDetails;
import com.amazonaws.appflow.custom.connector.model.connectorconfiguration.DescribeConnectorConfigurationRequest;
import com.amazonaws.appflow.custom.connector.model.connectorconfiguration.DescribeConnectorConfigurationResponse;
import com.amazonaws.appflow.custom.connector.model.connectorconfiguration.ImmutableDescribeConnectorConfigurationResponse;
import com.amazonaws.appflow.custom.connector.model.credentials.ImmutableValidateCredentialsResponse;
import com.amazonaws.appflow.custom.connector.model.credentials.ValidateCredentialsRequest;
import com.amazonaws.appflow.custom.connector.model.credentials.ValidateCredentialsResponse;
import com.amazonaws.appflow.custom.connector.model.settings.ImmutableValidateConnectorRuntimeSettingsResponse;
import com.amazonaws.appflow.custom.connector.model.settings.ValidateConnectorRuntimeSettingsRequest;
import com.amazonaws.appflow.custom.connector.model.settings.ValidateConnectorRuntimeSettingsResponse;
import org.custom.connector.jdbc.client.JDBCClient;
import org.custom.connector.jdbc.client.JDBCClientFactory;
import org.custom.connector.jdbc.config.JDBCConnectorConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;

public class JDBCConnectorConfigurationHandler implements ConfigurationHandler {
  private static final String CONNECTOR_OWNER = "ksharlandjiev";
  private static final String CONNECTOR_NAME = "JDBCConnector";
  private static final String CONNECTOR_VERSION = "1.0";
  private static final Logger LOGGER = LoggerFactory.getLogger(JDBCConnectorConfigurationHandler.class);
  private final JDBCClientFactory jdbcClient = new JDBCClientFactory();

  /**
   * Validates the user inputs corresponding to the connector settings for a given ConnectorRuntimeSettingScope.
   *
   * @param request - {@link ValidateConnectorRuntimeSettingsRequest}
   * @return - {@link ValidateConnectorRuntimeSettingsResponse}
   */
  @Override
  public ValidateConnectorRuntimeSettingsResponse validateConnectorRuntimeSettings(
    final ValidateConnectorRuntimeSettingsRequest request
  ) {
    return ImmutableValidateConnectorRuntimeSettingsResponse.builder().isSuccess(true).build();
  }

  /**
   * Validates the user provided credentials.
   *
   * @param request - {@link ValidateCredentialsRequest}
   * @return - {@link ValidateCredentialsResponse}
   */
  @Override
  public ValidateCredentialsResponse validateCredentials(final ValidateCredentialsRequest request) {
    JDBCClient client = jdbcClient.create(request.credentials());
    try (Connection conn = client.getConnection()) {
      // do nothing, connection successful
    } catch (Exception ex) {
      LOGGER.error(ex.getMessage());
      return ImmutableValidateCredentialsResponse.builder()
        .errorDetails(ImmutableErrorDetails.builder()
          .errorCode(ErrorCode.InvalidCredentials)
          .errorMessage(ex.getMessage()).build())
        .isSuccess(false).build();
    }
    return ImmutableValidateCredentialsResponse.builder().isSuccess(true).build();
  }

  /**
   * Describes the Connector Configuration supported by the connector.
   *
   * @param request - {@link DescribeConnectorConfigurationRequest}
   * @return {@link DescribeConnectorConfigurationResponse}
   */
  @Override
  public DescribeConnectorConfigurationResponse describeConnectorConfiguration(
    final DescribeConnectorConfigurationRequest request
  ) {
    return ImmutableDescribeConnectorConfigurationResponse.builder()
      .isSuccess(true)
      .connectorOwner(CONNECTOR_OWNER)
      .connectorName(CONNECTOR_NAME)
      .connectorVersion(CONNECTOR_VERSION)
      .connectorModes(JDBCConnectorConfiguration.getConnectorModes())
      .connectorRuntimeSetting(JDBCConnectorConfiguration.getConnectorRuntimeSettings())
      .authenticationConfig(JDBCConnectorConfiguration.getAuthenticationConfig())
      .supportedApiVersions(JDBCConnectorConfiguration.getSupportedApiVersions())
      .build();
  }
}
