package com.lrpPlugin.teamcity.lrpPlugin;

import jetbrains.buildServer.serverSide.InvalidProperty;
import jetbrains.buildServer.serverSide.PropertiesProcessor;
import jetbrains.buildServer.serverSide.RunType;
import jetbrains.buildServer.serverSide.RunTypeRegistry;
import jetbrains.buildServer.web.openapi.PluginDescriptor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static jetbrains.buildServer.util.PropertiesUtil.isEmptyOrNull;

public class LrpRunType extends RunType {
    private final PluginDescriptor _Descriptor;

    public LrpRunType(@NotNull final RunTypeRegistry registry, @NotNull final PluginDescriptor descriptor) {
        registry.registerRunType(this);
        _Descriptor = descriptor;
    }

    @NotNull
    @Override
    public String getType() {
        return LrpConstants.RUNNER_TYPE;
    }

    @NotNull
    @Override
    public String getDisplayName() {
        return LrpConstants.RUNNER_DISPLAY_NAME;
    }

    @NotNull
    @Override
    public String getDescription() {
        return LrpConstants.RUNNER_DESCRIPTION;
    }

    @Nullable
    @Override
    public PropertiesProcessor getRunnerPropertiesProcessor() {
        return properties -> {
            if (properties == null) {
                return Collections.emptyList();
            }

            List<InvalidProperty> errorList = new ArrayList<InvalidProperty>(0);
            if (isEmptyOrNull(properties.get(LrpConstants.TEST_PATH))) {
                errorList.add(new InvalidProperty(LrpConstants.TEST_PATH, LrpConstants.TEST_PATH_ERROR));
            }

            return errorList;
        };
    }

    @Nullable
    @Override
    public String getEditRunnerParamsJspFilePath() {
        return _Descriptor.getPluginResourcesPath("editParameters.jsp");
    }

    @Nullable
    @Override
    public String getViewRunnerParamsJspFilePath() {
        return _Descriptor.getPluginResourcesPath("viewParameters.jsp");
    }

    @Nullable
    @Override
    public Map<String, String> getDefaultRunnerProperties() {
        Map<String, String> defaultProperties = new HashMap<>();
        return defaultProperties;
    }
}
