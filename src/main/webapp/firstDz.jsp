<%@ page import="java.util.LinkedHashMap" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
  LinkedHashMap<String, Integer> arr = new LinkedHashMap<>();
  arr.put("Product1", 250);
  arr.put("Product2", 350);
  arr.put("Product3", 150);
  arr.put("Product4", 100);
  arr.put("Product5", 50);
%>

<style>
  table {
    border-collapse: collapse;
    width: 25%;
  }
  th, td {
    border: 1px solid black;
    padding: 8px;
    text-align: left;
  }
  th {
    background-color: azure;
  }
</style>

<table>
  <tbody>
  <% for (String key : arr.keySet()) { %>
  <tr>
    <td><%= key %></td>
    <td><%= arr.get(key)%> $</td>
  </tr>
  <% } %>
  </tbody>
</table>
