<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="o" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<%@ page session="false" %>

<o:header title="Registration complete"/>

<o:topbar/>
<div class="container-fluid main">
    <div class="row-fluid">
        <div class="span6 well custom-card">
            <h1 class="text-center">Registration successful</h1>
            <p class="text-center">Username : ${user.userName}</p>
            <p class="text-center">Email : ${user.email}</p>
            <p class="text-center">Phone : ${user.phone}</p>
        </div>
    </div>
</div>

