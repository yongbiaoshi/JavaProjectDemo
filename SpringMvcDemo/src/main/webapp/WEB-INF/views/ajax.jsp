<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="true" contentType="text/html; charset=UTF-8"%>
<html>
<head>
<title>Some Ajax Request</title>
<script type="text/javascript" src='<c:url value="/resources/js/jquery-1.12.0.min.js"></c:url>'></script>
<script type="text/javascript">
  $(function() {
    $("#req-btn, #req-btn-ex").click(function() {
        $.ajax({
          url: $(this).attr("data-url"),
          success: function(data){
            $("#response").text(JSON.stringify(data));
          },
          error: function(){
            $("#response").text("");
          }
        });
    });
  });
</script>
</head>
<body>
  <h4>Send Request</h4>
  <div>
    <input id="req-btn" type="button" data-url='<c:url value="/u"></c:url>' value="发送请求" />
    <input id="req-btn-ex" type="button" data-url='<c:url value="/uu"></c:url>' value="发送请求(ex)" />
  </div>
  <h4>Response Data</h4>
  <div id="response"></div>
</body>
</html>
