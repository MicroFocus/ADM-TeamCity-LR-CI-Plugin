package com.lrpPlugin.teamcity.lrpPlugin;

public class LrpConstants {
    public static final String RUNNER_TYPE = "LoadRunner Professional CI Test";
    public static final String RUNNER_DISPLAY_NAME = "LoadRunner Professional CI Test";
    public static final String RUNNER_DESCRIPTION = "Executing LoadRunner Professional load test";

    public static final String TEST_PATH = "com.lrpPlugin.teamcity.lrpPlugin.TestPath";
    public static final String TEST_PATH_ERROR = "Test Path cannot be empty";

    public static final String REPORT_DIRECTORY = "report";

    public String getTestPath() { return TEST_PATH; }
}
