param (
    [string]$sourcePath, [string]$resultsPath, 
    [string]$controllerPollingInterval, [string]$buildIdentifier,
    [string]$scenarioExecutionTimeout, [string]$analysisTemplate, 
    [string]$timeout, [string]$propertiesFile
)

function getPropertiesFileContent {
    
    param([string]$scenarioPath, [string]$scenarioResultsPath)

    $content = "Test1=$scenarioPath`n";
    $content = "fsReportPath=$scenarioResultsPath`n";
    $content = "resultsFilename=$scenarioResultsPath\\Report.xml`n";
    $content = "PerScenarioTimeOut=$scenarioExecutionTimeout`n";
    $content = "analysisTemplate=$analysisTemplate`n";
    $content = "runType=$FileSystem`n";
    $content = "fsTimeout=$timeout`n";
    $content = "displayController=0`n";
    $content = "controllerPollingInterval=$controllerPollingInterval`n";

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

    $scenarioNames = "";
    if("$sourcePath" -match ".*\.lrs"){
        return $sourcePath.replace(".lrs", "").split("\")[-1];
    }
    return getAllScenarioNamesFromDirectory -sourcePath "$sourcePath"
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

function executeScenario {
    
    param([string]$scenarioName, [string]$scenarioSourceDirectory)

    $scenarioPath = "$scenarioSourceDirectory\$scenarioName.lrs"
    $scenarioResultsPath = "$resultsPath\$buildIdentifier\$scenarioName";
    createPropertiesFile -scenarioPath "$scenarioPath" `
        -scenarioResultsPath "$scenarioResultsPath" 
    
    .\HpToolsLauncher.exe -paramfile "$propertiesFile"
    return $LASTEXITCODE;
}

$sourcePath = "C:\Scenarios-Multiple2";

$scenarioNames = getAllScenarioNames -sourcePath $sourcePath;

$scenarioSourceDirectory = getScenariosSourceDirectory -sourcePath $sourcePath;

ForEach($scenarioName in $scenarioNames){
    write-host "$scenarioName";

}

