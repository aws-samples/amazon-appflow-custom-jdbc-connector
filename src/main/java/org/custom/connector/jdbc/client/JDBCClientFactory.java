// Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0

package org.custom.connector.jdbc.client;

import java.util.Map;

import org.apache.commons.lang3.NotImplementedException;

import com.amazonaws.appflow.custom.connector.model.credentials.Credentials;

public class JDBCClientFactory implements AbstractFactory<JDBCClient> {

  @Override
  public JDBCClient create(final Credentials creds) {
    Map<String, String> secrets = SecretsManagerHelper.getSecret(creds.secretArn());
    String driver = secrets.getOrDefault("driver", null);

    switch (driver) {
      case "mysql":
        return new MySQLClient(secrets);
      default:
        throw new NotImplementedException("JDBC Driver: " + driver + " is not yet implemented");
    }
  }
}
