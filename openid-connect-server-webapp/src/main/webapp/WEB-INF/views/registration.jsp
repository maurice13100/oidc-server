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

    <div class="row-fluid">
        <div class="span3 custom-center ">
            <h1 class="banner"><a href="home">X-CONNECT</a></h1>
        </div>
    </div>

    <div class="row-fluid">
        <div class="text-center">
            <h3><spring:message code="register.register_user"/></h3>
        </div>
    </div>

    <c:if test="${ param.error != null }">
        <div class="alert alert-error"><spring:message code="register.error"/></div>
    </c:if>


    <div class="row-fluid">
        <div class="span5 well custom-card">
            <form:form modelAttribute="user" method="POST" enctype="utf8" cssClass="form-horizontal">
                <div class="control-group">
                    <label class="control-label custom-control-label" for="userName"><spring:message code="label.user.userName"></spring:message>*</label>
                    <div class="controls">
                        <form:input id="userName" path="userName" cssClass="input-block-level" placeholder="Enter your username" value=""/>
                        <form:errors path="userName" element="div"/>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label custom-control-label" for="givenName"><spring:message code="label.user.givenName"></spring:message></label>
                    <div class="controls">
                        <form:input id="givenName" path="givenName" cssClass="input-block-level" placeholder="" value=""/>
                        <form:errors path="givenName" element="div"/>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label custom-control-label" for="familyName"><spring:message code="label.user.familyName"></spring:message></label>
                    <div class="controls">
                        <form:input id="familyName" path="familyName" cssClass="input-block-level" placeholder="" value=""/>
                        <form:errors path="familyName" element="div"/>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label custom-control-label" for="middleName"><spring:message code="label.user.middleName"></spring:message></label>
                    <div class="controls">
                        <form:input id="middleName" path="middleName" cssClass="input-block-level" placeholder="" value=""/>
                        <form:errors path="middleName" element="div"/>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label custom-control-label" for="gender"><spring:message code="label.user.gender"></spring:message></label>
                    <div class="controls">
                        <form:input id="gender" path="gender" cssClass="input-block-level" placeholder="" value=""/>
                        <form:errors path="gender" element="div"/>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label custom-control-label" for="birthdate"><spring:message code="label.user.birthdate"></spring:message></label>
                    <div class="controls">
                        <form:input id="birthdate" path="birthdate" cssClass="input-block-level" placeholder="" value=""/>
                        <form:errors path="birthdate" element="div"/>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label custom-control-label" for="streetAddress"><spring:message code="label.user.streetAddress"></spring:message></label>
                    <div class="controls">
                        <form:input id="streetAddress" path="streetAddress" cssClass="input-block-level" placeholder="" value=""/>
                        <form:errors path="streetAddress" element="div"/>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label custom-control-label" for="locality"><spring:message code="label.user.locality"></spring:message></label>
                    <div class="controls">
                        <form:input id="locality" path="locality" cssClass="input-block-level" placeholder="" value=""/>
                        <form:errors path="locality" element="div"/>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label custom-control-label" for="region"><spring:message code="label.user.region"></spring:message></label>
                    <div class="controls">
                        <form:input id="region" path="region" cssClass="input-block-level" placeholder="" value=""/>
                        <form:errors path="region" element="div"/>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label custom-control-label" for="postalCode"><spring:message code="label.user.postalCode"></spring:message></label>
                    <div class="controls">
                        <form:input id="postalCode" path="postalCode" cssClass="input-block-level" placeholder="" value=""/>
                        <form:errors path="postalCode" element="div"/>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label custom-control-label" for="country"><spring:message code="label.user.country"></spring:message></label>
                    <div class="controls">
                        <form:input id="country" path="country" cssClass="input-block-level" placeholder="" value=""/>
                        <form:errors path="country" element="div"/>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label custom-control-label" for="email"><spring:message code="label.user.email"></spring:message>*</label>
                    <div class="controls">
                        <form:input id="email" path="email" cssClass="input-block-level" placeholder="example@email.com" value=""/>
                        <form:errors path="email" element="div"/>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label custom-control-label" for="phone"><spring:message code="label.user.phone"></spring:message>*</label>
                    <div class="controls">
                        <form:input id="phone" path="phone" cssClass="input-block-level" placeholder="+22123456789" value=""/>
                        <form:errors path="phone" element="div"/>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label custom-control-label" for="password"><spring:message code="label.user.password"></spring:message>*</label>
                    <div class="controls">
                        <form:input id="password" path="password" cssClass="input-block-level" type="password" placeholder="" value=""/>
                        <form:errors path="password" element="div"/>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label custom-control-label" for="password"><spring:message code="label.user.confirmPass"></spring:message>*</label>
                    <div class="controls">
                        <form:input id="matchingPassword" path="matchingPassword" type="password" cssClass="input-block-level" value=""/>
                        <form:errors path="matchingPassword" element="div"/>
                    </div>
                </div>
                <div class="row-fluid some-top">
                    <div class="span6 offset3">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                        <input type="submit" class="btn btn-block custom-button btn-large" value="<spring:message code="login.login-button"/>" name="submit" />
                    </div>
                </div>
            </form:form>
            <a href="<c:url value="login.html" />">
                <spring:message code="label.form.loginLink"></spring:message>
            </a>
        </div>
    </div>
</div>