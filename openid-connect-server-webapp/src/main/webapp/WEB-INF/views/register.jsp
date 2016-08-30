<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="o" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page session="false" %>

<o:header title="Register"/>

<script type="text/javascript">
    <!--

    $(document).ready(function() {
        // select the appropriate field based on context
        $('#<c:out value="${ login_hint != null ? 'j_password' : 'j_username' }" />').focus();
    });

    //-->
</script>
<o:topbar />
<div class="container-fluid main">

    <h1><spring:message code="register.register_user"/></h1>

    <c:if test="${ param.error != null }">
        <div class="alert alert-error"><spring:message code="register.error"/></div>
    </c:if>


    <div class="row-fluid">
        <div class="span6 offset1 well">
            <form:form modelAttribute="user" method="POST" enctype="utf8">
                <br>
                <tr><td>
                    <label>
                        <spring:message code="label.user.firstName"></spring:message>
                    </label>
                </td>
                    <td><form:input path="firstName" value="" /></td>
                    <form:errors path="firstName" element="div"/>
                </tr>
                <tr><td>
                    <label>
                        <spring:message code="label.user.lastName"></spring:message>
                    </label>
                </td>
                    <td><form:input path="lastName" value="" /></td>
                    <form:errors path="lastName" element="div" />
                </tr>
                <tr><td>
                    <label>
                        <spring:message code="label.user.email"></spring:message>
                    </label>
                </td>
                    <td><form:input path="email" value="" /></td>
                    <form:errors path="email" element="div" />
                </tr>
                <tr><td>
                    <label>
                        <spring:message code="label.user.password"></spring:message>
                    </label>
                </td>
                    <td>
                        <form:input path="password" value="" type="password" /></td>
                    <form:errors path="password" element="div" />
                </tr>
                <tr><td>
                    <label>
                        <spring:message code="label.user.confirmPass"></spring:message>
                    </label>
                </td>
                    <td><form:input path="matchingPassword" value="" type="password" /></td>
                    <form:errors element="div" />
                </tr>
                <button type="submit">
                    <spring:message code="label.form.submit"></spring:message>
                </button>
            </form:form>
            <br>
            <a href="<c:url value="login.html" />">
                <spring:message code="label.form.loginLink"></spring:message>
            </a>

            <%--<form action="${ config.issuer }${ config.issuer.endsWith('/') ? '' : '/' }j_spring_security_check" method="POST">--%>
                <%--<div>--%>
                    <%--<div class="input-prepend input-block-level">--%>
                        <%--<span class="add-on"><i class="icon-user"></i></span>--%>
                        <%--<input type="text" placeholder="<spring:message code="login.username"/>" autocorrect="off" autocapitalize="off" autocomplete="off" spellcheck="false" value="<c:out value="${ login_hint }" />" id="j_username" name="j_username">--%>
                    <%--</div>--%>
                <%--</div>--%>
                <%--<div>--%>
                    <%--<div class="input-prepend input-block-level">--%>
                        <%--<span class="add-on"><i class="icon-lock"></i></span>--%>
                        <%--<input type="password" placeholder="<spring:message code="login.password"/>" autocorrect="off" autocapitalize="off" autocomplete="off" spellcheck="false" id="j_password" name="j_password">--%>
                    <%--</div>--%>
                <%--</div>--%>
                <%--<div>--%>
                    <%--<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />--%>
                    <%--<input type="submit" class="btn" value="<spring:message code="login.login-button"/>" name="submit">--%>
                <%--</div>--%>
            <%--</form>--%>
        </div>
    </div>
</div>

<o:footer/>
