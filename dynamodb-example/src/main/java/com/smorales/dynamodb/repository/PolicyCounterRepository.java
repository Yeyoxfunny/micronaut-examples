package com.smorales.dynamodb.repository;

import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.UpdateItemOutcome;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.NameMap;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@AllArgsConstructor
public class PolicyCounterRepository {

    private static final String CUSTOMER_POLICIES_COUNT_TABLE_NAME = "customer-policies-count";
    private static final String POLICY_ID_KEY_NAME = "policyId";
    private static final String COUNT_ATTRIBUTE = "count";

    private final DynamoDB dynamoDB;

    public Long increment(String policyId) {
        Table table = dynamoDB.getTable(CUSTOMER_POLICIES_COUNT_TABLE_NAME);

        PrimaryKey primaryKey = new PrimaryKey(POLICY_ID_KEY_NAME, policyId);
        UpdateItemSpec spec = new UpdateItemSpec()
                .withPrimaryKey(primaryKey)
                //.withUpdateExpression("SET #c = #c + :c")
                //.withUpdateExpression("SET #c = if_not_exists(#c, 0) + :c")
                .withUpdateExpression("ADD #c :c")
                .withValueMap(new ValueMap().withNumber(":c", 1))
                .withNameMap(new NameMap().with("#c", COUNT_ATTRIBUTE))
                .withReturnValues(ReturnValue.UPDATED_NEW);
        UpdateItemOutcome outcome = table.updateItem(spec);

        log.info("UpdateItem succeeded:\n{}", outcome.getItem().toJSONPretty());
        return outcome.getItem().getLong(COUNT_ATTRIBUTE);
    }

    public Long get(String policyId) {
        PrimaryKey primaryKey = new PrimaryKey(POLICY_ID_KEY_NAME, policyId);
        Table table = dynamoDB.getTable(CUSTOMER_POLICIES_COUNT_TABLE_NAME);
        Item item = table.getItem(primaryKey);
        return item != null ? item.getLong(COUNT_ATTRIBUTE) : 0L;
    }

}
