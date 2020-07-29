package com.lrpPlugin.teamcity.lrpPlugin;

public class LrpConstants {

    public static final String LRP_PLUGIN_PATH = "com.lrpPlugin.teamcity.lrpPlugin";
    public static final String RUNNER_TYPE = "LoadRunner Professional CI Test";
    public static final String RUNNER_DISPLAY_NAME = "LoadRunner Professional CI Test";
    public static final String RUNNER_DESCRIPTION = "Executing LoadRunner Professional load test";

    public static final String TEST_PATH = String.format("%s.%s", LRP_PLUGIN_PATH, "TestPath");
    public static final String TEST_PATH_ERROR = getEmptyStringParameterErrorMsg("Test Path");

    public static final String RESULTS_PATH = String.format("%s.%s", LRP_PLUGIN_PATH, "ResultsPath");
    public static final String RESULTS_PATH_ERROR = getEmptyStringParameterErrorMsg("Results Path");

    public static final String CONTROLLER_POLLING_INTERVAL = String.format("%s.%s", LRP_PLUGIN_PATH,
            "ControllerPollingInterval");

    public static final String SCENARIO_EXECUTION_TIMEOUT = String.format("%s.%s", LRP_PLUGIN_PATH,
            "ScenarioExecutionTimeout");

    public static final String ANALYSIS_TEMPLATE = String.format("%s.%s", LRP_PLUGIN_PATH, "AnalysisTemplate");

    public static final String TIMEOUT = String.format("%s.%s", LRP_PLUGIN_PATH, "Timeout");

    public static final String REPORT_DIRECTORY = "LoadRunner-Results";

    public String getTestPath() { return TEST_PATH; }

    public String getResultsPath() { return RESULTS_PATH; }

    public String getControllerPollingInterval() { return CONTROLLER_POLLING_INTERVAL; }

    public String getScenarioExecutionTimeout() { return SCENARIO_EXECUTION_TIMEOUT; }

    public String getAnalysisTemplate() { return ANALYSIS_TEMPLATE; }

    public String getTimeout() { return TIMEOUT; }

    public static String getEmptyStringParameterErrorMsg(String parameterName){
        return String.format("%s cannot be empty", parameterName);
    }

    public static String getNonIntegerParameterErrorMsg(String parameterName) {
        return String.format("%s must be numerical", parameterName);
    }
}
