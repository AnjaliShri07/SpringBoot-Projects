package com.dynamodb_springboot.dynamodb.commons;

import software.amazon.awssdk.regions.Region;

public final class Constant {
    public static final Region REGION = Region.US_EAST_1;
    public static final String HYPHEN = "-";
    public static final String HASH = "#";
    public static final String JOB = "job";
    public static final String NULL = "null";
    public static final String LATEST = "latest";
    public static final String VERSION = "version";
    public static final String VERSION_V1 = "v1";
    public static final String SYSTEM = "system";

    public enum QueryOperator {
        EQUALS,
        BEGINS_WITH,
        GREATER_THAN,
        GREATER_THAN_OR_EQUALS,
        LESS_THAN,
        LESS_THAN_OR_EQUALS,
        BETWEEN
    }
}
