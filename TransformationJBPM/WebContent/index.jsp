<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Magic tool</title>
</head>
<body>
    <h1>BIenvenido a SHM (Super Herramienta Mágica)</h1>
    <h2>Por favor, introduce la siguiente información:</h2>
    <form action="ArisToJbpmServlet" method="post" enctype="multipart/form-data">
    	<div style="padding-bottom: 2%;">
	    	Nombre del proyecto: <input type="text" name="projectName" size="20">
	    </div>
	    <div style="padding-bottom: 2%;">
	    	Paquete del proyecto: <input type="text" name="projectPackage" size="20">
	    </div>
	    <div style="padding-bottom: 2%;">
	    	Repositorio Git: <input type="text" name="gitRepository" size="20">
	    </div>
	    <div style="padding-bottom: 2%;">
	    	Fichero .bpmn2 que quiere transformar: <input type="file" name="arisFile" size="50" />
	    </div>
	    <div>
	    	<input type="submit" value="Leña al mono" />
	    </div>
	</form>
</body>
</html>