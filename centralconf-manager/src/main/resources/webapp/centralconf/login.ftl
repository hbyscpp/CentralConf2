<html>
	<head>
		<meta charset="utf-8">
		<title> 统一配置  </title>
		<meta name="description" content="">
		<meta name="author" content="">
		<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
		
		<#include "layout.ftl">
	
		<!-- #FAVICONS -->
		<link rel="shortcut icon" href="img/favicon/favicon.ico" type="image/x-icon">
		<link rel="icon" href="img/favicon/favicon.ico" type="image/x-icon">


		<!-- #APP SCREEN / ICONS -->
		<!-- Specifying a Webpage Icon for Web Clip 
			 Ref: https://developer.apple.com/library/ios/documentation/AppleApplications/Reference/SafariWebContent/ConfiguringWebApplications/ConfiguringWebApplications.html -->
		<link rel="apple-touch-icon" href="img/splash/sptouch-icon-iphone.png">
		<link rel="apple-touch-icon" sizes="76x76" href="img/splash/touch-icon-ipad.png">
		<link rel="apple-touch-icon" sizes="120x120" href="img/splash/touch-icon-iphone-retina.png">
		<link rel="apple-touch-icon" sizes="152x152" href="img/splash/touch-icon-ipad-retina.png">
		
		<!-- iOS web-app metas : hides Safari UI Components and Changes Status Bar Appearance -->
		<meta name="apple-mobile-web-app-capable" content="yes">
		<meta name="apple-mobile-web-app-status-bar-style" content="black">
		
		<!-- Startup image for web apps -->
		<link rel="apple-touch-startup-image" href="img/splash/ipad-landscape.png" media="screen and (min-device-width: 481px) and (max-device-width: 1024px) and (orientation:landscape)">
		<link rel="apple-touch-startup-image" href="img/splash/ipad-portrait.png" media="screen and (min-device-width: 481px) and (max-device-width: 1024px) and (orientation:portrait)">
		<link rel="apple-touch-startup-image" href="img/splash/iphone.png" media="screen and (max-device-width: 320px)">

	</head>
	
	<body class="animated fadeInDown">

		<header id="header">

			<div id="logo-group">
				<span id="logo" style="font-weight:bold;font-size: 20px; width:300px;color:#686868"> 统一配置 </span>
			</div>

		</header>

		<div id="main" role="main">

			<!-- MAIN CONTENT -->
			<div id="content" class="container">

				<div class="row">
					<div class="col-xs-12 col-sm-12 col-md-7 col-lg-8 hidden-xs hidden-sm">
					
					</div>
					<div class="col-xs-12 col-sm-12 col-md-5 col-lg-4">
						<div class="well no-padding">
							<form action="/centralconf/login" id="login-form"  method ="post"  class="smart-form client-form">
								<header>
									登录
								</header>

								<fieldset>
									
									<section>
										<label class="label">账号</label>
										<label class="input"> <i class="icon-append fa fa-user"></i>
											<input type="text" name="name" required="required" >
											<b class="tooltip tooltip-top-right"><i class="fa fa-user txt-color-teal"></i> 请输入账号</b></label>
									</section>

									<section>
										<label class="label">密码</label>
										<label class="input"> <i class="icon-append fa fa-lock"></i>
											<input type="password" name="password">
											<b class="tooltip tooltip-top-right"><i class="fa fa-lock txt-color-teal"></i> 请输入密码</b> </label>
									</section>
									
									<section>
										<div id="errorMsg">
											<p style="color: red;">${msg!}</p>
										</div>
									</section>

									<!-- <section>
										<label class="checkbox">
											<input type="checkbox" name="remember" checked="">
											<i></i>保持登录</label>
									</section> -->
								</fieldset>
								<footer>
									<button type="submit" class="btn btn-primary">
										登录
									</button>
								</footer>
							</form>

						</div>
						
					</div>
				</div>
			</div>

		</div>


		<script type="text/javascript">
			runAllForms();

			$(function() {
				
				$('#main').keydown(function(e) {
					if (e.keyCode == 13) {
						login();
					}
				});
				
				// Validation
				$("#login-form").validate({
					// Rules for form validation
					rules : { 
						name : {
							required : true
						},  
						password : {
							required : true,
							minlength : 3,
							maxlength : 20
						}
					},

					// Messages for form validation
					messages : {
						name : {
							required : '请输入您的账号'
						}, 
						password : {
							required : '请输入您的密码'
						}
					},

					// Do not change code below
					errorPlacement : function(error, element) {
						error.insertAfter(element.parent());
					}
				});
			});
			
			function login() {
				$("#errorMsg").html("");
				$('#login-form').submit();
				
			};
		</script>

	</body>
</html>