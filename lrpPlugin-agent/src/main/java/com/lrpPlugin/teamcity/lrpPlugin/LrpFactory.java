package com.lrpPlugin.teamcity.lrpPlugin;

import jetbrains.buildServer.agent.AgentBuildRunnerInfo;
import jetbrains.buildServer.agent.BuildAgentConfiguration;
import jetbrains.buildServer.agent.artifacts.ArtifactsWatcher;
import jetbrains.buildServer.agent.runner.CommandLineBuildService;
import jetbrains.buildServer.agent.runner.CommandLineBuildServiceFactory;
import org.jetbrains.annotations.NotNull;

public class LrpFactory implements CommandLineBuildServiceFactory {
    private final ArtifactsWatcher _ArtifactsWatcher;

    public LrpFactory(final ArtifactsWatcher artifactsWatcher) {
        _ArtifactsWatcher = artifactsWatcher;
    }

    @NotNull
    @Override
    public CommandLineBuildService createService() {
        return new LrpService(_ArtifactsWatcher);
    }

    @NotNull
    @Override
    public AgentBuildRunnerInfo getBuildRunnerInfo() {
        return new AgentBuildRunnerInfo() {
            @NotNull
            @Override
            public String getType() {
                return LrpConstants.RUNNER_TYPE;
            }

            @Override
            public boolean canRun(@NotNull BuildAgentConfiguration buildAgentConfiguration) {
                return true;
            }
        };
    }
}
