<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://www.slim3.org/functions" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" charset="utf-8" />
    <title>slim3 study</title>
  </head>
  <body>
    <form action="/create" method="post">
      <input type="text" name="name" value="hoge fuga foobar" />
      <input type="submit" value="create" />
    </form><p />

    <h2>Sample</h2>
    <ul>
    <c:forEach var="sample" items="${samples}">
      <li>
        ${f:h(sample.key)}: ${f:h(sample.name)} -> ${f:h(sample.profileRef.model.screenName)}
        <a href="/delete/${f:h(sample.key)}">削除</a>
        <c:set var="items" value="${sample.itemRef.modelList}" />
        <c:if test="${fn:length(items) > 0}">
        <ul style="margin: 10px">
          <c:forEach var="item" items="${items}">
          <li>${item.itemName}</li>
          </c:forEach>
        </ul>
        </c:if>
      </li>
    </c:forEach>
    </ul>
    <h2>Profiles</h2>
    <ul>
    <c:forEach var="sample" items="${samples}" end="0">
      <li>${sample.profileRef.model.screenName}(${f:h(sample.profileRef.model.key)})</li>
      <ul>
      <c:forEach var="sampleRef" items="${sample.profileRef.model.samplesRef.modelList}">
        <li style="margin: 5px;list-style: none">${sampleRef.name}(${f:h(sample.key)})</li>
      </c:forEach>
      </ul>
    </c:forEach>
    </ul>
  </body>
</html>
