package com.smorales.dynamodb.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import com.smorales.dynamodb.entity.Employee;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class EmployeeRepository {

    private DynamoDBMapper dynamoDBMapper;

    public Employee save(Employee employee) {
        dynamoDBMapper.save(employee);
        return employee;
    }

    public Employee findById(String id) {
        return dynamoDBMapper.load(Employee.class, id);
    }

    public void delete(String id) {
        Employee employee = dynamoDBMapper.load(Employee.class, id);
        dynamoDBMapper.delete(employee);
    }

    public void update(String id, Employee employee) {
        dynamoDBMapper.save(employee, new DynamoDBSaveExpression()
                .withExpectedEntry("employeeId",
                        new ExpectedAttributeValue(new AttributeValue(id))));
    }

}
