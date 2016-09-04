<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="o" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<%@ page session="false" %>

<o:header title="Registration complete"/>

<o:topbar/>
<div class="container-fluid main">
    <div class="row-fluid">
        <div class="span10">
            <div class="hero-unit">
                <h1>Registration successful</h1>
                <ul>
                    <li>Username : ${user.userName}</li>
                    <li>Email : ${user.email}</li>
                    <li>Phone : ${user.phone}</li>
                </ul>
            </div>
        </div>
    </div>
</div>

<o:footer/>
