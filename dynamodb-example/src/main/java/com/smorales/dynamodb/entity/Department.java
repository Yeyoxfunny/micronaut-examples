package com.smorales.dynamodb.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@DynamoDBDocument
public class Department {

    @DynamoDBAttribute
    private String name;

    @DynamoDBAttribute
    private String code;

}
