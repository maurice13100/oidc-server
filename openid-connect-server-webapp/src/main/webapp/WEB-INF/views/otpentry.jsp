<%@ page session="true"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="authz" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="o" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<o:header title="Please enter the token" />
<script type="text/javascript">
    <!--

    $(document).ready(function() {
        // select the appropriate field based on context
        $('#otptoken').focus();
    });

    //-->
</script>
<o:topbar />
<div class="container-fluid main">

    <h1><spring:message code="login.login_with_otp"/></h1>

    <c:if test="${ param.error != null }">
        <div class="alert alert-error"><spring:message code="login.error"/></div>
    </c:if>

    <div class="row-fluid">
        <div class="span6 offset1 well">
            <p>You will receive your password shortly. Please enter it to finalize authentication.</p>
            <form name="login" method="post" action="validate">
                <div>
                    <div class="input-prepend input-block-level">
                        <span class="add-on"><i class="icon-lock"></i></span>
                        <input type="text" placeholder="<spring:message code="login.otptoken"/>" autocorrect="off" autocapitalize="off" autocomplete="off" spellcheck="false" id="otptoken" name="otptoken" />
                    </div>
                </div>
                <div>
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                    <input type="submit" class="btn" value="<spring:message code="login.submit"/>" name="Submit token">
                </div>
            </form>
        </div>
    </div>
</div>

<o:footer/>
