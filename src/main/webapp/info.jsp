<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:useBean id="storage" scope="request" type="java.util.Map"/>
<jsp:useBean id="list" scope="request" type="java.util.List"/>
<jsp:useBean id="flight" scope="request" class="entities.FlightEntity"/>
<html>
<head><title>Airport Info</title>
    <link type="text/css" rel="stylesheet" href="<c:url value="/css/bootstrap.min.css"/>"/>
    <style type="text/css">
        table {
            border-collapse: collapse;
            margin: auto;
        }

        th, td {
            text-align: center;
            padding: 8px;
        }

        tr:nth-child(even) {
            background-color: #f2f2f2
        }

        th {
            background-color: #4CAF50;
            color: white;
        }
    </style>
</head>
<body style="font-family: 'Noto Sans',serif <%--Graphik Regular,sans-serif--%>">
<H1 align="center"><a href="info">Flight List</a></H1>

<hr>
<%--FILTERS--%>
<p align="center">
    <a href="info?directionFilter=${storage.get("directionFilter") == "leave" ? 'leave' : 'arrive'}&dateFilter=yesterday"
       class="${storage.get("dateFilter") == "yesterday" ? 'btn btn-primary' : 'btn btn-default'}"
       style="width: 75pt">
        Yesterday
    </a>
    <a href="info?directionFilter=${storage.get("directionFilter") == "leave" ? 'leave' : 'arrive'}&dateFilter=today"
       class="${storage.get("dateFilter") == "today" ? 'btn btn-primary' : 'btn btn-default'}"
       style="width: 75pt">
        Today
    </a>
    <a href="info?directionFilter=${storage.get("directionFilter") == "leave" ? 'leave' : 'arrive'}&dateFilter=tomorrow"
       class="${storage.get("dateFilter") == "tomorrow" ? 'btn btn-primary' : 'btn btn-default'}"
       style="width: 75pt">
        Tomorrow
    </a>
</p>

<p align="center">
    <a href="info?directionFilter=arrive&dateFilter=${storage.get("dateFilter") == "today" ? 'today' : (storage.get("dateFilter") == "yesterday" ? 'yesterday' : 'tomorrow')}"
       class="${storage.get("directionFilter") == "arrive" ? 'btn btn-primary' : 'btn btn-default'}"
       style="width: 75pt">
        Arriving
    </a>
    <a href="info?directionFilter=leave&dateFilter=${storage.get("dateFilter") == "today" ? 'today' : (storage.get("dateFilter") == "yesterday" ? 'yesterday' : 'tomorrow')}"
       class="${storage.get("directionFilter") == "leave" ? 'btn btn-primary' : 'btn btn-default'}"
       style="width: 75pt">
        Leaving
    </a>
</p>

<hr>
<%------------------------------------<<    T A B L E    >>-------------------------------------------------%>
<table>
    <thead>
        <tr>
            <th>
                <a href="info?directionFilter=${storage.get("directionFilter") == "leave" ? 'leave' : 'arrive'}&dateFilter=${storage.get("dateFilter") == "today" ? 'today' : (storage.get("dateFilter") == "yesterday" ? 'yesterday' : 'tomorrow')}&sort=time"
                   style="color: white">Time</a>
            </th>
            <th>
                <a href="info?directionFilter=${storage.get("directionFilter") == "leave" ? 'leave' : 'arrive'}&dateFilter=${storage.get("dateFilter") == "today" ? 'today' : (storage.get("dateFilter") == "yesterday" ? 'yesterday' : 'tomorrow')}&sort=flightNumber"
                   style="color: white">Flight Number</a>
            </th>
            <th>
                <a href="info?directionFilter=${storage.get("directionFilter") == "leave" ? 'leave' : 'arrive'}&dateFilter=${storage.get("dateFilter") == "today" ? 'today' : (storage.get("dateFilter") == "yesterday" ? 'yesterday' : 'tomorrow')}&sort=waypoint"
                   style="color: white">${storage.get("directionFilter") == "leave" ? 'Destination' : 'Departure'}</a>
            </th>
            <th>
                <a href="info?directionFilter=${storage.get("directionFilter") == "leave" ? 'leave' : 'arrive'}&dateFilter=${storage.get("dateFilter") == "today" ? 'today' : (storage.get("dateFilter") == "yesterday" ? 'yesterday' : 'tomorrow')}&sort=terminal"
                   style="color: white">Terminal</a>
            </th>
            <th>
                <a href="info?directionFilter=${storage.get("directionFilter") == "leave" ? 'leave' : 'arrive'}&dateFilter=${storage.get("dateFilter") == "today" ? 'today' : (storage.get("dateFilter") == "yesterday" ? 'yesterday' : 'tomorrow')}&sort=gate"
                   style="color: white">Gate</a>
            </th>
        </tr>
    </thead>

    <c:forEach var="flight" items="${list}">
        <tr align="center">
            <td>${flight.time}</td>
            <td>${flight.flightNumber}</td>
            <td><b>${flight.waypoint}</b></td>
            <td>${flight.terminal}</td>
            <td>${flight.gate}</td>
        </tr>
    </c:forEach>
</table>

<hr>

<p align="center">
    <a href="admin" class="btn btn-default" style="width: 75pt">Admin page</a>
</p>

<hr>

</body>
</html>