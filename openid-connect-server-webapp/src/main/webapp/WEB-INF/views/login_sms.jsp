<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="authz" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="o" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<o:header title="Log In with your phone" />
<script type="text/javascript">
    <!--

    $(document).ready(function() {
        // select the appropriate field based on context
        $('#j_phone_number').focus();
    });

    //-->
</script>
<div class="container-fluid main">

    <div class="row-fluid">
        <div class="text-center">
            <h1 style="font-size: 45px;color:rgb(214, 43, 21) !important;"><spring:message code="login.login_with_phone"/></h1>
        </div>
    </div>

    <c:if test="${ param.error != null }">
        <div class="alert alert-error"><spring:message code="login.error"/></div>
    </c:if>


    <div class="row-fluid">
        <div class="span3 custom-card loginmodal-container">

            <form action="${ config.issuer }${ config.issuer.endsWith('/') ? '' : '/' }j_spring_security_check?connectionType=SMS" method="POST">
                <div>
                    <div class="input-prepend input-block-level custom-field">
                        <input type="text" placeholder="<spring:message code="login.phone"/>" autocorrect="off" autocapitalize="off" autocomplete="off" spellcheck="false" value="<c:out value="${ login_hint }" />" id="j_phone_number" name="j_phone_number">
                    </div>
                </div>
                <div>
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                    <input type="submit" class="login loginmodal-submit" value="<spring:message code="login.login-button"/>" name="submit">
                </div>
                <div class="row-fluid">
        <div class="span10 custom-center text-center">
            <p><strong style="font-size: 18px;color : #606060   ">Enter the secret code on your mobile</strong></p>
        </div>
    </div>

            </form>               
        </div>
    </div>
           <div class="row-fluid">
        <div class="span10 custom-center text-center create-account">
            <a href="http://www.xconnectafrica.com" style="font-size: 18px;color: black;">What's XConnect?</a>
        </div>
    </div>        
</div>
