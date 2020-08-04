param (
    [string]$sourcePath, [string]$resultsPath, 
    [string]$controllerPollingInterval, [string]$buildIdentifier,
    [string]$scenarioExecutionTimeout, [string]$analysisTemplate, 
    [string]$timeout, [string]$propertiesFile
)

function getPropertiesFileContent {
    
    param([string]$scenarioPath, [string]$scenarioResultsPath)

    $scenarioPath = $scenarioPath.replace("\", "\\");
    $scenarioResultsPath = $scenarioResultsPath.replace("\", "\\");
    $analysisTemplate = $analysisTemplate.replace("\", "\\");
    $content = "Test1=$scenarioPath`n";
    $content += "fsReportPath=$scenarioResultsPath`n";
    $content += "resultsFilename=$scenarioResultsPath\\Report.xml`n";
    $content += "PerScenarioTimeOut=$scenarioExecutionTimeout`n";
    $content += "analysisTemplate=$analysisTemplate`n";
    $content += "runType=FileSystem`n";
    $content += "fsTimeout=$timeout`n";
    $content += "displayController=0`n";
    $content += "controllerPollingInterval=$controllerPollingInterval`n";

    return $content;
}

function createPropertiesFile {
    
    param([string]$scenarioPath, [string]$scenarioResultsPath)

    if(Test-Path -Path "$propertiesFile"){
        Remove-Item -Path "$propertiesFile"
    }

    $propertiesFileContent = getPropertiesFileContent `
        -scenarioPath "$scenarioPath" `
        -scenarioResultsPath "$scenarioResultsPath"

    Set-Content -Path "$propertiesFile" `
        -Value "$propertiesFileContent" -Force

}

function getAllScenarioNamesFromDirectory {

    param([string]$sourcePath)

    if(Test-Path -Path "$sourcePath"){
        return Get-ChildItem -Path "$sourcePath" -Name | `
            Where-Object { $_ -match ".*\.lrs" } | `
            % { $_.replace(".lrs","") };
    } else{
        Write-Host "Path: '$sourcePath' does not exist!";
        exit 1;
    }
}

function getAllScenarioNames {
    
    param([string]$sourcePath)

    if("$sourcePath" -match ".*\.lrs"){
        return $sourcePath.replace(".lrs", "").split("\")[-1];
    }
    $scenarioNames = getAllScenarioNamesFromDirectory `
        -sourcePath "$sourcePath";
    return $scenarioNames.split("`n");
}

function getScenariosSourceDirectory {

    param([string]$sourcePath)

    if(Test-Path -Path "$sourcePath"){
        if("$sourcePath" -match ".*\.lrs"){
            $scenarioName = $sourcePath.split("\")[-1];
            return $sourcePath.replace("$scenarioName", "");
        }
        return $sourcePath
    } else {
        Write-Host "Path: '$sourcePath' does not exist!";
        exit 1;
    }
}

function getScenarioStatus {
    
    param([string]$scenarioResultsPath)
    
    $passedScenarioMsg = "Test result: Passed";

    $scenarioReportPath = "$scenarioResultsPath\Report.xml";
    if(Test-Path -Path "$scenarioReportPath"){
         $reportContent = Get-Content -Path "$scenarioReportPath";
         return $reportContent.contains("$passedScenarioMsg");
    } else {
        Write-Host "Path: '$scenarioResultsPath' does not exist!";
        return $false;
    }
    return $true;
}

function executeScenario {
    
    param([string]$scenarioName, [string]$scenarioSourceDirectory, [string]$scenarioResultsPath)

    $scenarioPath = "$scenarioSourceDirectory\$scenarioName.lrs";
    
    createPropertiesFile -scenarioPath "$scenarioPath" `
        -scenarioResultsPath "$scenarioResultsPath";
    
    .\HpToolsLauncher.exe -paramfile "$propertiesFile";
}

function formatScenarioOptionForReportHtml{
    
    param([string]$scenarioName)

    $optionFormat = '<option value="../{0}/HTML/IE/HTML.html">{0}</option>';
    return “$optionFormat” –f "$scenarioName";
}

function getScenarioOptionsForReportHtml{
    
    param([string[]]$scenarioNames)

    $scenarioOptions = "";
    ForEach($scenarioName in $scenarioNames){
        $scenarioOption = formatScenarioOptionForReportHtml `
            -scenarioName "$scenarioName";
        $scenarioOptions += "$scenarioOption`n";
    }
    return "$scenarioOptions"
}

function insertOptionsIntoRaport{

    param([string]$reportHtmlPath, [string]$scenarionOptions)

    $insertLineTag = "<!-- Insert Line-->"
    (Get-Content "$reportHtmlPath") `
        -replace "$insertLineTag", "$scenarionOptions" | `
        Set-Content "$reportHtmlPath"
}

function createReportTab {

    param([string]$targetPath, [string[]]$scenarioNames)

    if(Test-Path -Path "$targetPath"){
        Copy-Item -Path ".\ReportTab" `
            -Destination "$targetPath\" -Recurse -Force
        $scenarioOptionsHtml = getScenarioOptionsForReportHtml `
            -scenarioNames $scenarioNames;
        $reportHtmlPath = "$targetPath\ReportTab\ReportTab.html";
        insertOptionsIntoRaport -reportHtmlPath "$reportHtmlPath" `
            -scenarionOptions "$scenarioOptionsHtml";
    } else {
        Write-Host "Path '$targetPath' doesn't exist!";
        Write-Host "There was an error during the execution of the plugin";
        exit 1;
    }
}

$scenarioFailures = 0;
$summaryMessages = [System.Collections.ArrayList]@();

[string[]]$scenarioNames = getAllScenarioNames -sourcePath $sourcePath;
$scenarioSourceDirectory = getScenariosSourceDirectory -sourcePath $sourcePath;

ForEach($scenarioName in $scenarioNames){
    $scenarioResultsPath = "$resultsPath\$buildIdentifier\$scenarioName";

    executeScenario -scenarioName "$scenarioName" `
        -scenarioSourceDirectory "$scenarioSourceDirectory" `
        -scenarioResultsPath "$scenarioResultsPath"
    
    $scenarioPassed = getScenarioStatus -scenarioResultsPath "$scenarioResultsPath";
    if(-not $scenarioPassed){
        $scenarioFailures ++;
        $summaryMessages.Add("$scenarioName has failed!") | Out-Null;
    } else{
        $summaryMessages.Add("$scenarioName has passed!") | Out-Null;
    }
}

$targetPath = "$resultsPath\$buildIdentifier";

createReportTab -targetPath "$targetPath" -scenarioNames $scenarioNames;

Write-Host "LoadRunner Build Step Summary";
ForEach($summaryMsg in $summaryMessages){
    Write-Host $summaryMsg;
}
if($scenarioFailures -ne 0){
    Write-Host "Build step is going to be marked as failed!";
}
exit $scenarioFailures