<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="authz" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="o" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<o:header title="Log In" />
<script type="text/javascript">
<!--

$(document).ready(function() {
	// select the appropriate field based on context
	$('#<c:out value="${ login_hint != null ? 'j_password' : 'j_username' }" />').focus();
});

//-->
</script>
<div class="container-fluid main">

	<div class="row-fluid">
		<div class="span3 custom-center ">
			<h1 class="banner"><a href="home">XConnect</a></h1>
		</div>
	</div>

	<div class="row-fluid">
		<div class="text-center">
			<h3><spring:message code="login.login_with_username_and_password"/></h3>
		</div>
	</div>

	<c:if test="${ param.error != null }">
		<div class="row-fluid">
			<div class="alert alert-error"><spring:message code="login.error"/></div>
		</div>
	</c:if>

	<div class="row-fluid">
		<div class="span3 well custom-card ">
			<img src="resources/images/user.jpg" class="img-circle custom-image">

			<form action="${ config.issuer }${ config.issuer.endsWith('/') ? '' : '/' }j_spring_security_check" method="POST">
				<div>
					<div class="input-prepend input-block-level custom-field">
						<span class="add-on"><i class="icon-user"></i></span>
						<input type="text" placeholder="<spring:message code="login.username"/>" autocorrect="off" autocapitalize="off" autocomplete="off" spellcheck="false" value="<c:out value="${ login_hint }" />" id="j_username" name="j_username">
					</div>
				</div>
				<div>
					<div class="input-prepend input-block-level custom-field">
						<span class="add-on"><i class="icon-lock"></i></span>
						<input type="password" placeholder="<spring:message code="login.password"/>" autocorrect="off" autocapitalize="off" autocomplete="off" spellcheck="false" id="j_password" name="j_password">
					</div>
				</div>
				<div>
					<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
					<input type="submit" class="btn btn-block custom-button" value="<spring:message code="login.login-button"/>" name="submit">
				</div>
			</form>
		</div>
	</div>

	<div class="row-fluid">
		<div class="span2 custom-center text-center create-account">
			<a href="/registration">Create account</a>
		</div>
	</div>
</div>
