// Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0

package org.custom.connector.jdbc.config;

import com.amazonaws.appflow.custom.connector.model.connectorconfiguration.auth.AuthParameter;
import com.amazonaws.appflow.custom.connector.model.connectorconfiguration.auth.CustomAuthConfig;
import com.amazonaws.appflow.custom.connector.model.connectorconfiguration.auth.ImmutableAuthParameter;

import java.util.Arrays;
import java.util.List;

public class JDBCAuthConfig implements CustomAuthConfig {

  @Override
  public String authenticationType() {
    return "JDBC";
  }

  @Override
  public List<AuthParameter> authParameters() {
    AuthParameter driver = ImmutableAuthParameter.builder()
      .key("driver")
      .label("Driver")
      .description("Database driver")
      .addConnectorSuppliedValues("tidb")
      .required(true)
      .sensitiveField(false)
      .build();

    AuthParameter hostname = ImmutableAuthParameter.builder()
      .key("hostname")
      .label("Hostname")
      .description("Database hostname - must be reachable")
      .required(true)
      .sensitiveField(false)
      .build();
    AuthParameter port = ImmutableAuthParameter.builder()
      .key("port")
      .label("Port")
      .description("Database port")
      .required(true)
      .sensitiveField(false)
      .build();

    AuthParameter database = ImmutableAuthParameter.builder()
      .key("database")
      .label("Database Name")
      .description("Database name")
      .required(true)
      .sensitiveField(false)
      .build();
    AuthParameter username = ImmutableAuthParameter.builder()
      .key("username")
      .label("Username")
      .description("Database username")
      .required(true)
      .build();
    AuthParameter password = ImmutableAuthParameter.builder()
      .key("password")
      .label("Password")
      .description("Database password")
      .required(true)
      .sensitiveField(true)
      .build();

    AuthParameter tls = ImmutableAuthParameter.builder()
      .key("tls")
      .label("TLS")
      .description("If using TLS to connect")
      .addConnectorSuppliedValues("Yes")
      .addConnectorSuppliedValues("No")
      .required(true)
      .build();

    return Arrays.asList(driver, hostname, port, username, password, database, tls);
  }
}
