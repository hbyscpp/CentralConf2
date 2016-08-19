<!-- Bread crumb is created dynamically -->
<!-- row -->
<html>
<div class="row">
	
	<!-- col -->
	<div class="col-xs-12 col-sm-7 col-md-7 col-lg-4">
		<h1 class="page-title txt-color-blueDark">
			
			<!-- PAGE HEADER -->
			<i class="fa-fw fa fa-table"></i> 
				APPS
				<span id="app_span">>  
					<a href="#toApp">应用</a>
				</span>
				<span id="env_span">>  
					<a href="#toEnv?app=${app!}">环境</a>
				</span>
				<span>>  
					配置项
				</span>  
		</h1>
	</div>
	
</div>
<!-- end row -->

<!-- widget grid -->
<section id="widget-grid" class="">

	<!-- row -->
	<div class="row">
		
		<!-- NEW WIDGET START -->
		<article class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
				<table id="jqgrid"></table>
				<div id="pjqgrid"></div>
				
		</article>
		<!-- WIDGET END -->
		
	</div>

	<!-- end row -->

</section>
<!-- end widget grid -->


<script type="text/javascript">
	 
	/* DO NOT REMOVE : GLOBAL FUNCTIONS!
	 *
	 * pageSetUp(); WILL CALL THE FOLLOWING FUNCTIONS
	 *
	 * // activate tooltips
	 * $("[rel=tooltip]").tooltip();
	 *
	 * // activate popovers
	 * $("[rel=popover]").popover();
	 *
	 * // activate popovers with hover states
	 * $("[rel=popover-hover]").popover({ trigger: "hover" });
	 *
	 * // activate inline charts
	 * runAllCharts();
	 *
	 * // setup widgets
	 * setup_widgets_desktop();
	 *
	 * // run form elements
	 * runAllForms();
	 *
	 ********************************
	 *
	 * pageSetUp() is needed whenever you load a page.
	 * It initializes and checks for all basic elements of the page
	 * and makes rendering easier.
	 *
	 */

	pageSetUp();
	
	/*
	 * ALL PAGE RELATED SCRIPTS CAN GO BELOW HERE
	 * eg alert("my home function");
	 * 
	 * var pagefunction = function() {
	 *   ...
	 * }
	 * loadScript("js/plugin/_PLUGIN_NAME_.js", pagefunction);
	 * 
	 */
	var params = {
			 app:'${app!}',
			 env:'${env!}'
	 }
	var pagefunction = function() {
		
		loadScript("js/plugin/jqgrid/jquery.jqGrid.min.js", run_jqgrid_function);

		function run_jqgrid_function() {

			jQuery("#jqgrid").jqGrid({
				url:"getConfItem",
				//data : jqgrid_data,
				postData:params,
				datatype : "json",
				mtype:"POST",
				height : 'auto',
				colNames : [ '键', '值','描述'],
				colModel : [
					{ name : 'key', index : 'key' , editable : true}, 
					{ name : 'value', index : 'value' , editable : true}, 
					{ name : 'desc', index : 'desc' , editable : true}
					//{ name : 'amount', index : 'amount', align : "right", editable : true }, 
					],
				rowNum : 'all',
				//rowList : [10, 20, 30],
				pager : '#pjqgrid',
				//sortname : 'key',
				toolbarfilter: true,
				pgbuttons:false,
				pginput :false,
				viewrecords : false,
				//sortorder : "asc",
				editurl : "updConfItem?app=${app!}&env=${env!}",
				caption : "配置项列表",
				gridComplete:function(){  //增加一个浏览选中文件的按钮
					//如果已經存在，則不需要再添加
					if(!$("#import_form").attr("method")){
						$("#import_json").before('<td><form id="import_form" enctype="multipart/form-data" method="post"><input type="file" name="file" id="file"/></form></td>');
					}
				},
				//multiselect : true,
				autowidth : true,

				});
				jQuery("#jqgrid").jqGrid('navGrid', "#pjqgrid", {
					search:false,
					flush:false,
					edit : true,
					add : true,
					del : true
				}).navButtonAdd('#pjqgrid',{
					caption:"",
					title:"导出json配置",   
					buttonicon:"upload",   
					id:"export_json",
					onClickButton: function(){
						location.href = "exportConfItem?app=${app!}&env=${env!}";
						/*$.post("exportConfItem",{app:"${app!}",env:"${env!}"},function(fileName){
							console.log(fileName);
							if(fileName){
								$.smartSuccess({content: "导出配置成功,导出路径为"+fileName});
							}else{
								$.smartFailure({content: "导出配置失败"});
							}
						});*/
					},   
					position:"last"
					}).navButtonAdd('#pjqgrid',{
					caption:"",
					title:"导入json配置",   
					buttonicon:"download",
					id:"import_json",
					onClickButton: function(){ 
						var fileName = $("#file").val();
						if(!fileName){
							$.smartFailure({content: "请选择json文件"});
							return;
						}
						var index = fileName.lastIndexOf(".");
						if(fileName.substr(index+1)!=="json"){
							$.smartFailure({content: "请选择json格式文件"});
							return;
						}
					//	$("#import_form").attr("action","importConfItem?app=${app!}&env=${env!}").submit();
						$('#import_form').ajaxSubmit({
						  dataType:'json',
							url: 'importConfItem', // 需要提交的 url
				            data:{app:"${app!}",env:"${env!}"},
						  success:function(json){
								if(json){
									$("#jqgrid").trigger("reloadGrid");  
									$.smartSuccess({content: "导入配置成功"});
								}else{
									$.smartFailure({content: "导入配置失败"});
								}
						  }
						});
					/*	$.post("importConfItem",{app:${app!},env:${env!}},function(isSuccess){
							if(isSuccess){
								$.smartSuccess({content: "导入配置成功"});
							}else{
								$.smartFailure({content: "导入配置失败"});
							}
						});*/
					},   
					position:"last"
					});
				//jQuery("#jqgrid").jqGrid('inlineNav', "#pjqgrid");
				/* Add tooltips */
				$('.navtable .ui-pg-button').tooltip({
					container : 'body'
				});


				jQuery("#m1").click(function() {
					var s;
					s = jQuery("#jqgrid").jqGrid('getGridParam', 'selarrrow');
					alert(s);
				});
				jQuery("#m1s").click(function() {
					jQuery("#jqgrid").jqGrid('setSelection', "13");
				});
				
				// remove classes
				$(".ui-jqgrid").removeClass("ui-widget ui-widget-content");
			    $(".ui-jqgrid-view").children().removeClass("ui-widget-header ui-state-default");
			    $(".ui-jqgrid-labels, .ui-search-toolbar").children().removeClass("ui-state-default ui-th-column ui-th-ltr");
			    $(".ui-jqgrid-pager").removeClass("ui-state-default");
			    $(".ui-jqgrid").removeClass("ui-widget-content");
			    
			    // add classes
			    $(".ui-jqgrid-htable").addClass("table table-bordered table-hover");
			    $(".ui-jqgrid-btable").addClass("table table-bordered table-striped");
			   
			   
			    $(".ui-pg-div").removeClass().addClass("btn btn-sm btn-primary");
			    $(".ui-icon.ui-icon-plus").removeClass().addClass("fa fa-plus");
			    $(".ui-icon.ui-icon-pencil").removeClass().addClass("fa fa-pencil");
			    $(".ui-icon.ui-icon-trash").removeClass().addClass("fa fa-trash-o");
			    $(".ui-icon.ui-icon-search").removeClass().addClass("fa fa-search");
			    $(".ui-icon.ui-icon-refresh").removeClass().addClass("fa fa-refresh");
			    $(".ui-icon.ui-icon-disk").removeClass().addClass("fa fa-save").parent(".btn-primary").removeClass("btn-primary").addClass("btn-success");
			    $(".ui-icon.ui-icon-cancel").removeClass().addClass("fa fa-times").parent(".btn-primary").removeClass("btn-primary").addClass("btn-danger");
			    //导入，导出json
			    $(".ui-icon.download").removeClass().addClass("fa fa-download");
			    $(".ui-icon.upload").removeClass().addClass("fa fa-upload");
			     
				$( ".ui-icon.ui-icon-seek-prev" ).wrap( "<div class='btn btn-sm btn-default'></div>" );
				$(".ui-icon.ui-icon-seek-prev").removeClass().addClass("fa fa-backward");
				
				$( ".ui-icon.ui-icon-seek-first" ).wrap( "<div class='btn btn-sm btn-default'></div>" );
			  	$(".ui-icon.ui-icon-seek-first").removeClass().addClass("fa fa-fast-backward");		  	
	
			  	$( ".ui-icon.ui-icon-seek-next" ).wrap( "<div class='btn btn-sm btn-default'></div>" );
			  	$(".ui-icon.ui-icon-seek-next").removeClass().addClass("fa fa-forward");
			  	
			  	$( ".ui-icon.ui-icon-seek-end" ).wrap( "<div class='btn btn-sm btn-default'></div>" );
			  	$(".ui-icon.ui-icon-seek-end").removeClass().addClass("fa fa-fast-forward");
			  	
			  	
			  
			  
			    // update buttons
			    
			    $(window).on('resize.jqGrid', function () {
					jQuery("#jqgrid").jqGrid( 'setGridWidth', $("#content").width() );
				})


			} // end function


	}
	
	loadScript("js/plugin/jqgrid/grid.locale-en.min.js", pagefunction);
	loadScript("js/plugin/jquery-form/jquery-form.min.js", pagefunction);
	
	function appDetail(appName){
		alert(appName);
		var j = $('#dialog-message');
		j.dialog('open');
		return false;
	}
	
	
	
	var modal_pagefunction = function() {
		$("#dialog-message").dialog({
			autoOpen : false,
			modal : true,
			title : "环境",
			buttons : [{
				html : "取消",
				"class" : "btn btn-default",
				click : function() {
					$(this).dialog("close");
				}
			}, {
				html : "<i class='fa fa-check'></i>&nbsp; OK",
				"class" : "btn btn-primary",
				click : function() {
					$(this).dialog("close");
				}
			}]
	
		});
	};
	modal_pagefunction();
</script>


	
</html>