// Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0

package org.custom.connector.jdbc.utils;

import com.amazonaws.services.lambda.runtime.LambdaLogger;

import java.util.Arrays;

public class ConsoleLogger implements LambdaLogger {

  @Override
  public void log(final String message) {
    System.out.println(message);
  }

  @Override
  public void log(final byte[] message) {
    System.out.println(Arrays.toString(message));
  }
}
