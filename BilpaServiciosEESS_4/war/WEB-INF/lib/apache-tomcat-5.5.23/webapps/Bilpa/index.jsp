<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Bilpa</title>
</head>
<body>
Hello world!

<html:form action="/LogonSubmit">

Usuario: <html:text property="usuario"></html:text>
<html:errors property="usuario"/>
<br>
Clave: <html:text property="clave"></html:text>
<html:errors property="clave"/>
<br>
<html:submit>OK</html:submit>


</html:form>

</body>
</html>