// Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0

package org.custom.connector.jdbc.handler;

import com.amazonaws.appflow.custom.connector.handlers.MetadataHandler;
import com.amazonaws.appflow.custom.connector.model.ErrorCode;
import com.amazonaws.appflow.custom.connector.model.ImmutableErrorDetails;
import com.amazonaws.appflow.custom.connector.model.metadata.DescribeEntityRequest;
import com.amazonaws.appflow.custom.connector.model.metadata.DescribeEntityResponse;
import com.amazonaws.appflow.custom.connector.model.metadata.ImmutableDescribeEntityResponse;
import com.amazonaws.appflow.custom.connector.model.metadata.ImmutableEntity;
import com.amazonaws.appflow.custom.connector.model.metadata.ImmutableEntityDefinition;
import com.amazonaws.appflow.custom.connector.model.metadata.ImmutableListEntitiesResponse;
import com.amazonaws.appflow.custom.connector.model.metadata.ListEntitiesRequest;
import com.amazonaws.appflow.custom.connector.model.metadata.ListEntitiesResponse;
import org.custom.connector.jdbc.client.JDBCClient;
import org.custom.connector.jdbc.client.JDBCClientFactory;

public class JDBCConnectorMetadataHandler implements MetadataHandler {
  private final JDBCClientFactory jdbcClient = new JDBCClientFactory();

  /**
   * Lists all the entities available in a paginated fashion. This API is recursive in nature and provides a heretical
   * entity listing based on entityPath. If the ListEntityResponse returns hasChildren=true that indicates that
   * there are more entities in the next level.
   *
   * @param request - {@link ListEntitiesRequest}
   * @return - {@link ListEntitiesResponse}
   */
  @Override
  public ListEntitiesResponse listEntities(final ListEntitiesRequest request) {
    JDBCClient client = jdbcClient.create(request.connectorContext().credentials());

    try {
      return ImmutableListEntitiesResponse.builder()
        .addAllEntities(client.getEntities(request))
        .isSuccess(true)
        .build();
    } catch (Exception ex) {
      return ImmutableListEntitiesResponse.builder()
        .errorDetails(ImmutableErrorDetails.builder()
          .errorCode(ErrorCode.ClientError)
          .errorMessage(ex.getMessage())
          .build())
        .isSuccess(false).build();
    }
  }

  /**
   * Describes the entity definition with its field level metadata.
   *
   * @param request - {@link DescribeEntityRequest}
   * @return - {@link DescribeEntityResponse}
   */
  @Override
  public DescribeEntityResponse describeEntity(final DescribeEntityRequest request) {
    JDBCClient client = jdbcClient.create(request.connectorContext().credentials());
    try {
      return ImmutableDescribeEntityResponse.builder()
        .isSuccess(true)
        .entityDefinition(
          ImmutableEntityDefinition.builder()
            .entity(
              ImmutableEntity.builder()
                .entityIdentifier(request.entityIdentifier())
                .description(request.entityIdentifier())
                .label(request.entityIdentifier())
                .hasNestedEntities(true)
                .build())
            .fields(client.getFieldDefinitions(request)).build())
        .build();
    } catch (Exception ex) {
      return ImmutableDescribeEntityResponse.builder()
        .errorDetails(ImmutableErrorDetails.builder()
          .errorCode(ErrorCode.ClientError)
          .errorMessage(ex.getMessage())
          .build())
        .isSuccess(false).build();
    }
  }
}
