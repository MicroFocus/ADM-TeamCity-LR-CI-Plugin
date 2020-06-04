<%@ taglib prefix="props" tagdir="/WEB-INF/tags/props" %>
<%@ taglib prefix="forms" tagdir="/WEB-INF/tags/forms" %>
<%@ taglib prefix="l" tagdir="/WEB-INF/tags/layout" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="bs" tagdir="/WEB-INF/tags" %>
<jsp:useBean id="constants" class="com.lrpPlugin.teamcity.lrpPlugin.LrpConstants"/>

<l:settingsGroup title="Parameters">
    <tr>
        <th>
            <label>Test Path</label>
        </th>
        <td>
            <props:textProperty name="${constants.testPath}"/>
            <span class="error" id="error_${constants.testPath}"></span>
        </td>
    </tr>
</l:settingsGroup>
