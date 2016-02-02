<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="true" contentType="text/html; charset=UTF-8"%>
<html>
<head>
<title>Some Ajax Request</title>
<script type="text/javascript" src='<c:url value="/resources/js/jquery-1.12.0.min.js"></c:url>'></script>
<script type="text/javascript">

</script>
</head>
<body>
  <h4>Send Request</h4>
  <div>
  <form action='<c:url value="/upload"></c:url>' method="post" enctype="multipart/form-data">
    <input id="file-btn" name="file" type="file" value="选择文件" />
    <input type="submit" value="提交" />
  </form>
  </div>
  <h4>Response Data</h4>
  <div id="response"></div>
</body>
</html>

