// Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0

package org.custom.connector.jdbc.client;

import com.amazonaws.appflow.custom.connector.model.credentials.Credentials;

public interface AbstractFactory<T> {
  T create(final Credentials creds);
}
