<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta charset="UTF-8">
        <title></title>
        <meta content='width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no' name='viewport'>
        <link href="<%=basePath %>/AdminLTE/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
        <link href="<%=basePath %>/AdminLTE/css/font-awesome.min.css" rel="stylesheet" type="text/css" />
        <link href="<%=basePath %>/AdminLTE/css/ionicons.min.css" rel="stylesheet" type="text/css" />
        <link href="<%=basePath %>/AdminLTE/css/morris/morris.css" rel="stylesheet" type="text/css" />
        <link href="<%=basePath %>/AdminLTE/css/jvectormap/jquery-jvectormap-1.2.2.css" rel="stylesheet" type="text/css" />
        <link href="<%=basePath %>/AdminLTE/css/fullcalendar/fullcalendar.css" rel="stylesheet" type="text/css" />
        <link href="<%=basePath %>/AdminLTE/css/daterangepicker/daterangepicker-bs3.css" rel="stylesheet" type="text/css" />
        <link href="<%=basePath %>/AdminLTE/css/bootstrap-wysihtml5/bootstrap3-wysihtml5.min.css" rel="stylesheet" type="text/css" />
        <link href="<%=basePath %>/AdminLTE/css/AdminLTE.css" rel="stylesheet" type="text/css" />
    </head>
    <body class="skin-black">
        <header class="header">
           <!--  <a href="index.html" class="logo">
                AdminLTE
            </a> -->
            <nav class="navbar navbar-static-top" role="navigation">
                <!-- Sidebar toggle button-->
                <a href="#" class="navbar-btn sidebar-toggle" data-toggle="offcanvas" role="button">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </a>
                
            </nav>
        </header>
        <div class="wrapper row-offcanvas row-offcanvas-left">
            <aside class="left-side sidebar-offcanvas">                
                <section class="sidebar">
                    <ul class="sidebar-menu">
                        <li class="active">
                            <a href="#">
                                <i class="fa fa-dashboard"></i> <span>新增配置</span>
                            </a>
                        </li>
                        <li class="treeview">
                            <a href="#" >
                                <i class="fa fa-bar-chart-o"></i>
                                <span>配置管理</span>
                                <i class="fa fa-angle-left pull-right"></i>
                            </a>
                            <ul class="treeview-menu">
                                <li><a href="index.html"><i class="fa fa-angle-double-right"></i> 查询配置</a></li>
                                <li><a href="../user/login.do" ><i class="fa fa-angle-double-right"></i>修改配置</a></li>
                            </ul>
                        </li>
                    </ul>
                </section>
                <!-- /.sidebar -->
            </aside>

            <!-- Right side column. Contains the navbar and content of the page -->
            <aside class="right-side">                
                <!-- Content Header (Page header) -->
                <section class="content-header">
                    <h1>
                        Blank page
                        <small>Control panel</small>
                    </h1>
                    <ol class="breadcrumb">
                        <li><a href="#"><i class="fa fa-dashboard"></i> Home</a></li>
                        <li class="active">Blank page</li>
                    </ol>
                </section>

                <!-- Main content -->
                <!-- <section class="content">


                </section>/.content -->
                 <section class="content">
					 <div class="row">
                        <div class="col-xs-12">
                            <div class="box">
                            <iframe  id="menuFrame"  name="menuFrame" src="<%=basePath %>/user/login.do" scrolling="yes" frameborder="no" height="800px" width="100%"></iframe>
                                </div>
                                </div>
                        </div>
                        </section>
            </aside><!-- /.right-side -->
        </div><!-- ./wrapper -->


        <!-- jQuery 2.0.2 -->
        <script src="http://code.jquery.com/jquery-2.1.1.min.js"></script>
        <!-- jQuery UI 1.10.3 -->
        <script src="<%=basePath %>/AdminLTE/js/jquery-ui-1.10.3.min.js" type="text/javascript"></script>
        <!-- Bootstrap -->
        <script src="<%=basePath %>/AdminLTE/js/bootstrap.min.js" type="text/javascript"></script>
        <!-- Morris.js charts -->
       <script src="<%=basePath %>/AdminLTE/js/raphael-min.js"></script>
        <%-- <script src="<%=basePath %>/AdminLTE/js/plugins/morris/morris.min.js" type="text/javascript"></script> --%>
        <!-- Sparkline -->
        <script src="<%=basePath %>/AdminLTE/js/plugins/sparkline/jquery.sparkline.min.js" type="text/javascript"></script>
        <!-- jvectormap -->
        <script src="<%=basePath %>/AdminLTE/js/plugins/jvectormap/jquery-jvectormap-1.2.2.min.js" type="text/javascript"></script>
        <script src="<%=basePath %>/AdminLTE/js/plugins/jvectormap/jquery-jvectormap-world-mill-en.js" type="text/javascript"></script>
        <!-- fullCalendar -->
        <script src="<%=basePath %>/AdminLTE/js/plugins/fullcalendar/fullcalendar.min.js" type="text/javascript"></script>
        <!-- jQuery Knob Chart -->
        <script src="<%=basePath %>/AdminLTE/js/plugins/jqueryKnob/jquery.knob.js" type="text/javascript"></script>
        <!-- daterangepicker -->
        <script src="<%=basePath %>/AdminLTE/js/plugins/daterangepicker/daterangepicker.js" type="text/javascript"></script>
        <!-- Bootstrap WYSIHTML5 -->
        <script src="<%=basePath %>/AdminLTE/js/plugins/bootstrap-wysihtml5/bootstrap3-wysihtml5.all.min.js" type="text/javascript"></script>
        <!-- iCheck -->
        <script src="<%=basePath %>/AdminLTE/js/plugins/iCheck/icheck.min.js" type="text/javascript"></script> 

        <!-- AdminLTE App -->
        <script src="<%=basePath %>/AdminLTE/js/AdminLTE/app.js" type="text/javascript"></script>
        
        <!-- AdminLTE dashboard demo (This is only for demo purposes) -->
      <%-- <script src="<%=basePath %>/AdminLTE/js/AdminLTE/dashboard.js" type="text/javascript"></script>    --%>   

    </body>
</html>