<%@ taglib prefix="props" tagdir="/WEB-INF/tags/props" %>
<%@ taglib prefix="forms" tagdir="/WEB-INF/tags/forms" %>
<%@ taglib prefix="l" tagdir="/WEB-INF/tags/layout" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="bs" tagdir="/WEB-INF/tags" %>
<jsp:useBean id="constants" class="com.lrpPlugin.teamcity.lrpPlugin.LrpConstants"/>

<l:settingsGroup title="LoadRunner Professional Parameters">
    <tr>
        <th>
            <label>Test Path</label>
        </th>
        <td>
            <props:textProperty name="${constants.testPath}" className="longField"/>
            <span class="error" id="error_${constants.testPath}"></span>
        </td>
    </tr>
    <tr>
        <th>
            <label>Results Path</label>
        </th>
        <td>
            <props:textProperty name="${constants.resultsPath}" className="longField"/>
            <span class="error" id="error_${constants.resultsPath}"></span>
        </td>
    </tr>
    <tr>
        <th>
            <label>Timeout</label>
        </th>
        <td>
            <props:textProperty name="${constants.timeout}" className="longField"/>
            <span class="error" id="error_${constants.timeout}"></span>
        </td>
    </tr>
    <l:settingsGroup title="Settings">
        <tr>
            <th>
                <label>Controller Polling Interval</label>
            </th>
            <td>
                <props:textProperty name="${constants.controllerPollingInterval}" className="longField"/>
                <span class="error" id="error_${constants.controllerPollingInterval}"></span>
            </td>
        </tr>
        <tr>
            <th>
                <label>Scenario Execution Timeout</label>
            </th>
            <td>
                <props:textProperty name="${constants.scenarioExecutionTimeout}" className="longField"/>
                <span class="error" id="error_${constants.scenarioExecutionTimeout}"></span>
            </td>
        </tr>
        <tr>
            <th>
                <label>Analysis Template</label>
            </th>
            <td>
                <props:textProperty name="${constants.analysisTemplate}" className="longField"/>
                <span class="error" id="error_${constants.analysisTemplate}"></span>
            </td>
        </tr>
    </l:settingsGroup>
</l:settingsGroup>