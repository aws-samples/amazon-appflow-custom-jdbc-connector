// Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0

package org.custom.connector.jdbc.client;

import com.amazonaws.appflow.custom.connector.util.CredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.secretsmanager.AWSSecretsManagerClient;
import lombok.var;
import org.apache.commons.collections4.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public final class SecretsManagerHelper {
  private static final Logger LOGGER = LoggerFactory.getLogger(SecretsManagerHelper.class);

  private SecretsManagerHelper() {
  }

  /**
   * @param arn Secrets Manager Arn
   * @return Map<String, String> Credentials
   */
  public static Map<String, String> getSecret(final String arn) {
    Map<String, String> config = new HashedMap<>();
    try {
      String[] parts = arn.split(":");
      if (parts.length <= 1) {
        throw new Exception("Invalid Secrets Manager Arn.");
      }
      String region = parts[3];

      if (region == null) {
        // defaulting to us-east-1
        region = "us-east-1";
      }
      var sc = AWSSecretsManagerClient.builder().withRegion(Regions.fromName(region)).build();
      config = CredentialsProvider.getCustomAuthCredentials(sc, arn)
        .customCredentials();
    } catch (Exception ex) {
      LOGGER.error("Error fetching secret: " + ex.getMessage());
    }
    return config;
  }
}
