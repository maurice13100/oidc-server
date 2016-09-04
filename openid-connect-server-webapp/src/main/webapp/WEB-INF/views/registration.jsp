<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="o" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page session="false" %>

<o:header title="Register"/>

<script type="text/javascript">
    <!--

    $(document).ready(function () {
        // select the appropriate field based on context
        $('#<c:out value="${ login_hint != null ? 'j_password' : 'j_username' }" />').focus();
    });

    //-->
</script>
<o:topbar/>
<div class="container-fluid main">

    <h1><spring:message code="register.register_user"/></h1>

    <c:if test="${ param.error != null }">
        <div class="alert alert-error"><spring:message code="register.error"/></div>
    </c:if>


    <div class="row-fluid">
        <div class="span6 offset1 well">
            <form:form modelAttribute="user" method="POST" enctype="utf8">
                <br>
                <tr>
                    <td>
                        <label>
                            <spring:message code="label.user.userName"></spring:message>
                        </label>
                    </td>
                    <td><form:input path="userName" value=""/></td>
                    <form:errors path="userName" element="div"/>
                </tr>
                <tr>
                    <td>
                        <label>
                            <spring:message code="label.user.email"></spring:message>
                        </label>
                    </td>
                    <td><form:input path="email" value=""/></td>
                    <form:errors path="email" element="div"/>
                </tr>
                <tr>
                    <td>
                        <label>
                            <spring:message code="label.user.phone"></spring:message>
                        </label>
                    </td>
                    <td><form:input path="phone" value=""/></td>
                    <form:errors path="phone" element="div"/>
                </tr>
                <tr>
                    <td>
                        <label>
                            <spring:message code="label.user.password"></spring:message>
                        </label>
                    </td>
                    <td>
                        <form:input path="password" value="" type="password"/></td>
                    <form:errors path="password" element="div"/>
                </tr>
                <tr>
                    <td>
                        <label>
                            <spring:message code="label.user.confirmPass"></spring:message>
                        </label>
                    </td>
                    <td><form:input path="matchingPassword" value="" type="password"/></td>
                    <form:errors element="div"/>
                </tr>
                <div>
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                    <input type="submit" class="btn" value="<spring:message code="login.login-button"/>" name="submit">
                </div>
            </form:form>
            <br>
            <a href="<c:url value="login.html" />">
                <spring:message code="label.form.loginLink"></spring:message>
            </a>
        </div>
    </div>
</div>

<o:footer/>
