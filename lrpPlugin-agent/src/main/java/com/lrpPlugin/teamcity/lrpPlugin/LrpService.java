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

    private String _UniqueId;

    public LrpService(final ArtifactsWatcher artifactsWatcher) {
        _ArtifactsWatcher = artifactsWatcher;
    }

    private Properties getPropertyList()
    {
        Properties propertyList = new Properties();
        propertyList.setProperty("Test1", getRunnerParameters().get(LrpConstants.TEST_PATH));
        propertyList.setProperty("controllerPollingInterval", "30");
        propertyList.setProperty("PerScenarioTimeOut", "10");
        propertyList.setProperty("analysisTemplate", "");
        propertyList.setProperty("displayController", "1");
        propertyList.setProperty("runType", "FileSystem");
        propertyList.setProperty("fsTimeout", "-1");
        propertyList.setProperty("resultsFilename", getWorkingDirectory().getAbsolutePath() + "\\Report" + _UniqueId + ".xml");
        propertyList.put("fsReportPath", getWorkingDirectory().getAbsolutePath() + "\\" + _UniqueId);

        return propertyList;
    }

    private void createPropertyFile(String filePath) {
        try (OutputStream output = new FileOutputStream(filePath)) {
            Properties prop = getPropertyList();
            prop.store(output, null);
        } catch (IOException e) {
            getBuild().getBuildLogger().message("An error occurred: " + e.getMessage());
        }
    }

    @NotNull
    @Override
    public ProgramCommandLine makeProgramCommandLine() throws RunBuildException {
        Date now = new Date();
        Format formatter = new SimpleDateFormat("ddMMyyyyHHmmssSSS");
        _UniqueId = formatter.format(now);

        createPropertyFile(getParamFilePath());

        return new SimpleProgramCommandLine(
                getBuildParameters().getEnvironmentVariables(),
                getLrpPluginRunnerBin(),
                getLrpPluginRunnerBin() + "\\HpToolsLauncher.exe",
                getArguments());
    }

    @NotNull
    @Override
    public void afterProcessFinished() throws RunBuildException {
        _ArtifactsWatcher.addNewArtifactsPath(_UniqueId + "\\**/* => " + LrpConstants.REPORT_DIRECTORY);
    }

    private List<String> getArguments() {
        List<String> arguments = new ArrayList<>();

        arguments.add("-paramfile");
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
