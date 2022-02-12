// Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0

package org.custom.connector.jdbc.client;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.amazonaws.appflow.custom.connector.model.metadata.DescribeEntityRequest;
import com.amazonaws.appflow.custom.connector.model.metadata.Entity;
import com.amazonaws.appflow.custom.connector.model.metadata.FieldDefinition;
import com.amazonaws.appflow.custom.connector.model.metadata.ListEntitiesRequest;
import com.amazonaws.appflow.custom.connector.model.query.QueryDataRequest;
import com.amazonaws.appflow.custom.connector.model.write.WriteDataRequest;
import com.amazonaws.appflow.custom.connector.model.write.WriteOperationType;

public interface JDBCClient {
  List<WriteOperationType> getWriteOperations();

  List<Entity> getEntities(final ListEntitiesRequest request) throws SQLException;

  List<FieldDefinition> getFieldDefinitions(final DescribeEntityRequest request) throws SQLException;

  Connection getConnection() throws SQLException;

  long getTotalData(final QueryDataRequest request) throws SQLException;

  List<String> queryData(final QueryDataRequest request) throws SQLException;

  int[] writeData(final WriteDataRequest request) throws SQLException;
}
