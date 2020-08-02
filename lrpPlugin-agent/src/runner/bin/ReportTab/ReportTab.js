
function changeScenarioSourcePath() {
	var selectedScenarioPath = document.getElementById("SelectScenario").value;
	var iframeElement = document.getElementById("ScenarioLrAnalysisReport");

	if(iframeElement.hidden){
		iframeElement.hidden = false;
	}
	iframeElement.src = selectedScenarioPath;
}