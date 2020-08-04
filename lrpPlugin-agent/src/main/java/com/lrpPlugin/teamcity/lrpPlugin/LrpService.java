package com.lrpPlugin.teamcity.lrpPlugin;

import jetbrains.buildServer.RunBuildException;
import jetbrains.buildServer.agent.artifacts.ArtifactsWatcher;
import jetbrains.buildServer.agent.runner.BuildServiceAdapter;
import jetbrains.buildServer.agent.runner.ProgramCommandLine;
import jetbrains.buildServer.agent.runner.SimpleProgramCommandLine;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.*;

public class LrpService extends BuildServiceAdapter {
    private final ArtifactsWatcher _ArtifactsWatcher;

    private String _UniqueId, _ResultsPath, _BuildIdentifier;

    public LrpService(final ArtifactsWatcher artifactsWatcher) {
        _ArtifactsWatcher = artifactsWatcher;
    }

    @NotNull
    @Override
    public ProgramCommandLine makeProgramCommandLine() throws RunBuildException {
        _UniqueId = String.valueOf(getBuild().getBuildId());
        _BuildIdentifier = "Build-" + _UniqueId;
        _ResultsPath = getRunnerParameters().get(LrpConstants.RESULTS_PATH) + "\\" + _BuildIdentifier;

        return new SimpleProgramCommandLine(
                getBuildParameters().getEnvironmentVariables(),
                getLrpPluginRunnerBin(),
                "powershell.exe",
                getArguments());
    }

    @NotNull
    @Override
    public void afterProcessFinished() throws RunBuildException {
        _ArtifactsWatcher.addNewArtifactsPath(
                _ResultsPath + "\\**/* => " + LrpConstants.REPORT_DIRECTORY
        );
    }

    private List<String> getArguments() {
        List<String> arguments = new ArrayList<>();

        arguments.add(getLrpPluginRunnerBin() + "\\ExecuteScenarios.ps1");
        arguments.add("-sourcePath");
        arguments.add(getRunnerParameters().get(LrpConstants.TEST_PATH));
        arguments.add("-resultsPath");
        arguments.add(getRunnerParameters().get(LrpConstants.RESULTS_PATH));
        arguments.add("-controllerPollingInterval");
        arguments.add(getRunnerParameters().get(LrpConstants.CONTROLLER_POLLING_INTERVAL));
        arguments.add("-buildIdentifier");
        arguments.add(_BuildIdentifier);
        arguments.add("-scenarioExecutionTimeout");
        arguments.add(getRunnerParameters().get(LrpConstants.SCENARIO_EXECUTION_TIMEOUT));
        arguments.add("-analysisTemplate");
        arguments.add(getRunnerParameters().getOrDefault(LrpConstants.ANALYSIS_TEMPLATE, "''"));
        arguments.add("-timeout");
        arguments.add(getRunnerParameters().get(LrpConstants.TIMEOUT));
        arguments.add("-propertiesFile");
        arguments.add(getParamFilePath());

        return arguments;
    }

    private String getParamFilePath() {
        String workingDirectory = getWorkingDirectory().getAbsolutePath();
        return workingDirectory + "\\props"+ _UniqueId+ ".txt";
    }

    private String getLrpPluginRunnerBin() {
        String agentToolsDirectory = getAgentConfiguration().getAgentToolsDirectory().getAbsolutePath();

        return agentToolsDirectory + "\\lrpPlugin-runner\\bin";
    }
}
