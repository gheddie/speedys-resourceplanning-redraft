<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%@page import="de.trispeedys.resourceplanning.context.ApplicationContext"%>
<%@page import="de.trispeedys.resourceplanning.interaction.RequestHandler"%>
<head>
  <link rel="stylesheet" href="resources/css/tri.css" type="text/css"/>
  <meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
  <title><%out.println(ApplicationContext.getText("jsp.helpercallback.request"));%></title>
</head>
<body>
	<%
	  // render confirmation dialog
      out.println(RequestHandler.renderConfirmationForm(request, null));
	%>
</body>
</html>