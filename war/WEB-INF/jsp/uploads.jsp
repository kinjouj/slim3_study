<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="f" uri="http://www.slim3.org/functions"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<html>
  <head>

  </head>
  <body>
    <form action="/uploads/upload" method="post" enctype="multipart/form-data">
      <input type="file" name="file" />
      <input type="submit" value="upload" />
    </form><p />
    <h2>Uploads</h2>
    <c:set var="loop_count" value="0" />
    <c:forEach var="upload" items="${uploads}">
      <c:set var="loop_count" value="${loop_count + 1}" />
      <div style="display: inline;width: 250px;margin: 20px">
        <a href="/uploads/show/${f:h(upload.key)}">
          <img src="/uploads/thumb/${f:h(upload.key)}" width="250px" height="250px" title="<fmt:formatDate value="${upload.createdAt}" pattern="yyyy-MM-dd HH:mm:ss" />" />
        </a>
        <%--
        <div style="text-align: right">
          ${f:h(upload.createdAt)}<br />
          ${f:h(upload.fileName)}
          <a href="/uploads/delete/${f:h(upload.key)}">削除</a>
        </div>
        --%>
      </div>
      <c:if test="${loop_count % 3 == 0}">
        <div style="margin: 20px 0px 20px 0px" />
      </c:if>
    </c:forEach>
    </ul>
  </body>
</html>
