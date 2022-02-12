// Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0

package org.custom.connector.jdbc.handler;

import com.amazonaws.appflow.custom.connector.lambda.handler.BaseLambdaConnectorHandler;

public class JDBCConnectorLambdaHandler extends BaseLambdaConnectorHandler {

  public JDBCConnectorLambdaHandler() {
    super(
      new JDBCConnectorMetadataHandler(),
      new JDBCConnectorRecordHandler(),
      new JDBCConnectorConfigurationHandler()
    );
  }
}
