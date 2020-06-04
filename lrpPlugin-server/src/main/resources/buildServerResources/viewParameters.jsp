<%@ taglib prefix="props" tagdir="/WEB-INF/tags/props" %>
<%@ taglib prefix="forms" tagdir="/WEB-INF/tags/forms" %>
<%@ taglib prefix="l" tagdir="/WEB-INF/tags/layout" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="bs" tagdir="/WEB-INF/tags" %>
<jsp:useBean id="constants" class="com.lrpPlugin.teamcity.lrpPlugin.LrpConstants"/>

<tr>
    <th>
        <label>Test Path</label>
    </th>
    <td>
        <strong><props:displayValue name="${constants.testPath}" emptyValue="N\A"/></strong>
    </td>
</tr>
