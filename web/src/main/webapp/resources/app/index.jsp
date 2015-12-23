<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta http-equiv="Content-Language" content="uk,en,ru">
    <title>Виконавець послуг</title>
    <link href="resources/assets/bower_components/ng-table/ng-table.css" rel="stylesheet">
    <link href="resources/assets/bower_components/angular-loading-bar/build/loading-bar.css" rel="stylesheet">
    <link href="resources/assets/bower_components/bootstrap/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="resources/assets/bower_components/font-awesome/css/font-awesome.min.css" rel="stylesheet">
    <link href="resources/assets/bower_components/chosen/chosen.min.css" rel="stylesheet">
    <link href="resources/assets/bower_components/bootstrap-daterangepicker/daterangepicker-bs3.css" rel="stylesheet">

    <link href="resources/assets/bower_components/ui-select/dist/select.min.css" rel="stylesheet">
    <link href="resources/assets/bower_components/select2/select2.css" rel="stylesheet">
    <link href="resources/assets/bower_components/select2-bootstrap-css/select2-bootstrap.min.css" rel="stylesheet">

    <link href="resources/assets/bower_components/angularjs-toaster/toaster.min.css" rel="stylesheet">

    <link rel="stylesheet" type="text/css" href="resources/assets/bower_components/semantic/dist/semantic.min.css">

    <link href="resources/assets/css/application-form-organization.css" rel="stylesheet">
    <link href="resources/assets/css/provider.css" rel="stylesheet">
    <link href="resources/assets/css/globalSearch.css" rel="stylesheet">
    <link href="resources/assets/css/calibrator.css" rel="stylesheet">
    <%--<link href="resources/assets/css/sb-admin-2.css" rel="stylesheet">--%>
    <%--<link href="resources/assets/css/timeline.css" rel="stylesheet">--%>


    <link href='http://fonts.googleapis.com/css?family=Roboto' rel='stylesheet' type='text/css'>
    <link href="resources/assets/bower_components/bootstrap-switch/dist/css/bootstrap3/bootstrap-switch.css"
          rel="stylesheet">
    <link rel="stylesheet"
          href="resources/assets/winmarkltd-BootstrapFormHelpers-0d89ab4/dist/css/bootstrap-formhelpers.min.css">
    <%--<link rel="stylesheet" href="resources/assets/css/loader.css">--%>

    <style>
        [ng\:cloak]
		[ng-cloak],
        [data-ng-cloak],
        [x-ng-cloak],
        .ng-cloak,
        .x-ng-cloak {
            display: none !important;
        }
    </style>

</head>

<body id="employeeModule">

<div id="content" class="wrapper ng-cloak" ng-controller="CommonController as common">

    <!-- Navigation -->
    <nav class="navbar navbar-default navbar-static-top" id="dark" role="navigation"
         ng-controller="TopNavBarControllerProvider">
        <ul class="nav navbar-nav" id="menubuttonelem">
            <li>
                <button class="btn btn-default navbar-btn"
                        ng-class="{'toggle_button' : common.menuState}"
                        ng-init="common.menuState = true"
                        ng-click="common.menuState = !common.menuState"
                        type="button" id="menubutton">
                    <i class="fa fa-bars" style="padding-right: 0"></i></button>
            </li>
            </ul>
        <div class="navbar-header">
            <a class="navbar-brand" translate="HEAD_TITLE"></a>
        </div>


        <div ng-controller="InternationalizationController">
            <ul class="nav navbar-nav">

                <li class="dropdown" dropdown on-toggle="toggled(open)">
                    <a class="dropdown-toggle" dropdown-toggle translate="LANG">
                        <span class="caret"></span>
                    </a>

                    <ul class="dropdown-menu">
                        <li ng-repeat="lang in languages">
                            <a ng-click="changeLanguage(lang.key)">{{ lang.name }}</a>
                        </li>
                    </ul>
                </li>
            </ul>
        </div>

        <!-- Nav bar top right links -->
        <ul class="nav navbar-top-links navbar-right">
            <li class="dropdown" dropdown>
                <a class="dropdown-toggle" style="width:250px; padding: 10px 15px; text-align: right" dropdown-toggle>
                    <div>
                        <label  translate="{{employee.firstName}} {{employee.lastName}}"></label>
                        <i class="fa fa-user fa-fw"></i><i class="fa fa-caret-down"></i>
                    </div>
                </a>

                <ul class="dropdown-menu dropdown-user">
                    <div class="ui card" style="width:250px">
                        <div class="ui image center aligned">
                            <img src="resources/assets/AdminLTE-master/img/User_icon.png"
                                 style="width: 250px;"
                                 alt="User Image"/>
                        </div>
                        <div class="content" style="padding:0;">
                            <div class="ui top attached secondary segment" style="padding:10px;">
                                <label class="userlabel" translate="{{employee.firstName}} {{employee.lastName}} {{employee.middleName}} <br/> ({{employee.username}})"></label>
                            </div>
                            <div class="ui vertical menu" style="margin-top:0">
                                <div ui-sref="profile-info" class="link item middle center aligned">
                                    <p translate="PROFILE"></p>
                                </div>
                                <div ui-sref="settings-provider" class="link item middle center aligned">
                                    <p translate="SETTINGS"></p>
                                </div>
                                <div ng-click="logout()" class="link item middle center aligned">
                                    <p translate="LOG_OUT"> </p>
                                </div>
                            </div>
                        </div>
                    </div>
                </ul>
            </li>
        </ul>
    </nav>
    <!-- Sidebar -->

    <div class="row ng-cloak">
        <div id="sidebar-wrapper" ng-class="{'sidebar-toggle-close' : common.menuState}">
            <div class="ui visible sticky visible very wide sidebar" role="navigation">
                <ul class="nav ui vertical menu" id="sidemenu">
                    <sec:authorize url="/provider">
                        <li ui-sref-active="active">
                            <a ui-sref="main-panel-provider"><i class="fa fa-home fa-fw"></i> <span translate="MAIN_PANEL_PROVIDER"></span></a>
                        </li>
                    </sec:authorize>

                    <sec:authorize url="/calibrator">
                        <li ui-sref-active="active">
                            <a ui-sref="main-panel-calibrator"><i class="fa fa-home fa-fw"></i>
                                <span translate="MAIN_PANEL_CALIBRATOR"> </span></a>
                        </li>
                    </sec:authorize>

                    <sec:authorize url="/verificator">
                        <li ui-sref-active="active">
                            <a ui-sref="main-panel-verificator"><i class="fa fa-home fa-fw"></i>
                                <span translate="MAIN_PANEL_STATE_VERIFICATOR"></span></a>
                        </li>
                    </sec:authorize>


                    <sec:authorize url="/provider">
                        <li ui-sref-active="active" ng-controller="NotificationsControllerProvider">
                            <a ui-sref="new-verifications-provider" ng-click="reloadVerifications()">
                                <i class="fa fa-list-alt fa-fw"></i> <span translate="NEW_VERIFICATIONS"></span>
                              <span class="ui teal label" ng-bind="countOfUnreadVerifications"
                                    ng-show="countOfUnreadVerifications>0" ng-cloak>
                              </span>
                            </a>
                        </li>
                    </sec:authorize>

                    <sec:authorize url="/calibrator">
                        <li ui-sref-active="active" ng-controller="NotificationsControllerCalibrator">
                            <a ui-sref="new-verifications-calibrator" ng-click="reloadVerifications()"><i
                                    class="fa fa-list-alt fa-fw"></i> <span translate="NEW_VERIFICATIONS_CALIBRATOR"> </span>
                           		<span class="ui teal label" ng-bind="countOfUnreadVerifications"
                                      ng-show="countOfUnreadVerifications>0" ng-cloak>
                              	</span>
                            </a>
                        </li>
                        <li ui-sref-active="active" ng-controller="DigitalVerificationProtocolsControllerCalibrator">
                        <a ui-sref="verifications-protocols-calibrator" ng-click="onTableHandling()">
                            <i class="fa fa-file-powerpoint-o"></i>
                            <span translate="DIGITAL_PROTOCOLS"></span>
                            </a>
                         </li>

                        <li ui-sref-active="active">
                            <a ui-sref="planning-task-calibrator"><i class="fa fa-tasks"></i>
                                <span translate="TASK_PLANNING"></span>
                            </a>
                        </li>
                        <li ui-sref-active="active">
                            <a ui-sref="task-for-calibration-module"><i class="fa fa-thumb-tack"></i>
                                <span translate="TASKS_FOR_STATIONS"></span>
                            </a>
                        </li>
                        <li ui-sref-active="active">
                            <a><i class="fa fa-thumb-tack"></i></i> <span translate="TASKS_FOR_BRIGADES"></span>
                            </a>
                        </li>


                    </sec:authorize>


                    <sec:authorize url="/verificator">
                        <li ui-sref-active="active" ng-controller="NotificationsControllerVerificator">
                            <a ui-sref="new-verifications-verificator" ng-click="reloadVerifications()"><i
                                    class="fa fa-list-alt fa-fw"></i> <span translate="VERIFICATION_PROTOCOLS"></span>
                                <span class="ui teal label" ng-bind="countOfUnreadVerifications"
                                      ng-show="countOfUnreadVerifications>0" ng-cloak>
                              	</span>
                            </a>
                        </li>
                    </sec:authorize>

                    <sec:authorize url="/provider/admin/">
                        <li ui-sref-active="active">
                            <a ui-sref="employee-show-provider"><i class="fa fa-users"></i> <span
                                    translate="EMPLOYEE"></span></a>
                        </li>
                    </sec:authorize>

                    <sec:authorize url="/calibrator/admin/">
                        <li ui-sref-active="active">
                            <a ui-sref="employee-show-calibrator"><i class="fa fa-users"></i> <span
                                    translate="EMPLOYEE"></span></a>
                        </li>

                        <li ui-sref-active="active">
                            <a ui-sref="disassembly-team-calibrator">
                                <i class="fa fa-desktop"></i><span translate="DISASSEMBLING_BRIGADES_LISTING"></span>
                            </a>
                        </li>

                    </sec:authorize>

                    <sec:authorize url="/verificator/admin/">
                        <li ui-sref-active="active">
                            <a ui-sref="employee-show-verificator"><i class="fa fa-users"></i> <span
                                    translate="EMPLOYEE"></span></a>
                        </li>
                    </sec:authorize>

                    <sec:authorize url="/provider">
                        <li ui-sref-active="active">
                            <a ui-sref="verifications-archive-provider"><i class="fa fa-archive fa-fw"></i><span
                                    translate="VERIFICATIONS_ARCHIVE">
                           </span></a>
                        </li>
                        <li ui-sref-active="active">
                            <a ui-sref="reports-provider"><i class="fa fa-file-excel-o"></i><span
                                    translate="REPORTS">
                           </span></a>
                        </li>
                    </sec:authorize>
                    <sec:authorize url="/calibrator">
                        <li ui-sref-active="active">
                            <a ui-sref="verifications-archive-calibrator"><i class="fa fa-archive fa-fw"></i> <span translate="VERIFICATIONS_ARCHIVE"></span></a>
                        </li>
                    </sec:authorize>
                    <sec:authorize url="/verificator">
                        <li ui-sref-active="active">
                            <a ui-sref="verifications-archive-verificator"><i class="fa fa-archive fa-fw"></i> <span translate="VERIFICATIONS_ARCHIVE"></span></a>
                        </li>
                    </sec:authorize>
                    <sec:authorize url="/provider/admin/">
                        <li ui-sref-active="active">
                            <a ui-sref="statistic-show-providerEmployee"><i class="fa fa-bar-chart"></i>
                                <span translate="STATISTIC_OF_EMPLOYEE_CAPACITY"> </span>
                            </a>
                        </li>
                    </sec:authorize>
                </ul>
            </div>
        </div>
        <div ng-class="{'col-md-11 sidebar-close' : !common.menuState, 'col-md-12 sidebar-open' : common.menuState}">
            <div ui-view></div>
        </div>
    </div>

</div>
<div id="loader-wrapper">
    <div id="loader" class="ui large loader"></div>
</div>
<toaster-container
        toaster-options="{'time-out': 3000, 'close-button':true, 'animation-class': 'toast-top-right'}"></toaster-container>
<script type="text/javascript" data-main="resources/app/runApp"
        src="resources/assets/bower_components/requirejs/require.js"></script>
<script src="resources/assets/bower_components/jquery/dist/jquery.js" type="text/javascript"></script>
<script src="resources/assets/bower_components/chosen/chosen.jquery.min.js"
        type="text/javascript"></script>
<script src="resources/assets/pdfjs/build/pdf.js" type="text/javascript"></script>
<script type="text/javascript">
    $(".chzn-select").chosen();
    $(".chzn-select-deselect").chosen({
        allow_single_deselect: true
    });
    $("#states").chosen();
</script>

<script type="text/javascript">
    $("#menubutton").click(function() {
        $("#sidebar-wrapper").find("a span").toggle();
    });
</script>

<script src="resources/assets/js/loader-employee.js"></script>


</body>
