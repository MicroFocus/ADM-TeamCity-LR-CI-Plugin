package com.lrpPlugin.teamcity.lrpPlugin;

import jetbrains.buildServer.serverSide.BuildsManager;
import jetbrains.buildServer.serverSide.SBuild;
import jetbrains.buildServer.serverSide.artifacts.BuildArtifact;
import jetbrains.buildServer.serverSide.artifacts.BuildArtifactsViewMode;
import jetbrains.buildServer.web.openapi.BuildTab;
import jetbrains.buildServer.web.openapi.PluginDescriptor;
import jetbrains.buildServer.web.openapi.PositionConstraint;
import jetbrains.buildServer.web.openapi.WebControllerManager;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class LrpReportTab extends BuildTab {
    protected LrpReportTab(WebControllerManager manager, BuildsManager buildManager, PluginDescriptor descriptor) {
        super("lrpReportTab", "LoadRunner Report Tab", manager, buildManager, "");
        setIncludeUrl("/artifactsViewer.jsp");
        setPosition(PositionConstraint.after("artifacts"));
    }

    @Override
    protected void fillModel(@NotNull Map<String, Object> map, @NotNull SBuild sBuild) {
        map.put("startPage", getHtmlReportPath(sBuild));
    }

    private String getHtmlReportPath(SBuild sBuild) {
        String htmlReportPath = LrpConstants.REPORT_DIRECTORY + "\\ReportTab\\ReportTab.html";
        BuildArtifact artifact = sBuild.getArtifacts(BuildArtifactsViewMode.VIEW_ALL).getArtifact(htmlReportPath);

        return artifact != null ? artifact.getRelativePath() : "ReportTab.html";
    }
}
