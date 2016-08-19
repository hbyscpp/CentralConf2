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
			<span>>  
				环境
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
	 
	pageSetUp();
	
	var param = {app:'${app!}'}
	var pagefunction = function() {
		loadScript("js/plugin/jqgrid/jquery.jqGrid.min.js", run_jqgrid_function);
		
		function run_jqgrid_function() {

			jQuery("#jqgrid").jqGrid({
				url:"getEnv",
				datatype : "json",
				postData:param,
				mtype:"POST",
				height : 'auto',
				colNames : [ '环境名', '操作'],
				colModel : [
					{ name : 'envName', index : 'envName' , editable : true}, 
					{ name : 'act', index:'act', sortable:false }
					],
				rowNum : 'all',
				pager : '#pjqgrid',
				pgbuttons:false,
				pginput :false,
				sortname : '环境名',
				toolbarfilter: true,
				viewrecords : false,
				sortorder : "asc",
				gridComplete: function(){
					var ids = jQuery("#jqgrid").jqGrid('getDataIDs');
					for(var i=0;i < ids.length;i++){
						var cl = ids[i];
						var rowData = jQuery("#jqgrid").jqGrid('getRowData',ids[i]);
						var envName=rowData.envName;
						var appName='${app!}';
						var ca = "<a class='ww btn btn-info' aria-describedby='jqgrid_operator'  style='text-decoration: none; color: #fff;' data-original-title='配置项'title='配置项' href=\"#toConfItemDetail?app="+appName+"&env="+envName+"\"><i class='fa fa-exclamation-sign'></i>配置项</a>";
						var be = "<button class='btn btn-info' data-original-title='复制' title='复制' onclick=\"copyEnv('"+appName+"','"+envName+"')\"><i class='fa fa-exclamation-sign'></i>复制</button>"; 
						var resource = "<button class='btn btn-info' data-original-title='资源' style='text-decoration: none; color: #fff;' title='资源' onclick=\"chooseRsc('"+envName+"')\"><i class='fa fa-exclamation-sign'></i>资源</button>"; 
						jQuery("#jqgrid").jqGrid('setRowData',ids[i],{act:ca+resource+be});
					}	
				},
				editurl : "updEnv?app=${app!}",
				caption : "环境列表",
				//multiselect : true,
				autowidth : true
				});
				jQuery("#jqgrid").jqGrid('navGrid', "#pjqgrid", {
					search:false,
					edit : false,
					add : true,
					del : true
				}).navButtonAdd('#pjqgrid',{
					caption:"",
					title:"公共环境配置",   
					buttonicon:"cloud",   
					id:"export_json",
					onClickButton: function(){
						location.href = "#toConfItem?app=${app!}&env=_common";
					},   
					position:"first"
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
			   
			   //公共资源配置图标
			   $(".ui-icon.cloud").removeClass().addClass("fa fa-cloud");
			   
			    $(".ui-pg-div").removeClass().addClass("btn btn-sm btn-primary");
			    $(".ui-icon.ui-icon-plus").removeClass().addClass("fa fa-plus");
			    $(".ui-icon.ui-icon-pencil").removeClass().addClass("fa fa-pencil");
			    $(".ui-icon.ui-icon-trash").removeClass().addClass("fa fa-trash-o");
			    $(".ui-icon.ui-icon-search").removeClass().addClass("fa fa-search");
			    $(".ui-icon.ui-icon-refresh").removeClass().addClass("fa fa-refresh");
			    $(".ui-icon.ui-icon-disk").removeClass().addClass("fa fa-save").parent(".btn-primary").removeClass("btn-primary").addClass("btn-success");
			    $(".ui-icon.ui-icon-cancel").removeClass().addClass("fa fa-times").parent(".btn-primary").removeClass("btn-primary").addClass("btn-danger");
			  
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
	
	function copyEnv(appName,envName){
		$("#dialog-chooseEnv").dialog('open');
		
		//$("#isOverried-y").attr("checked",true);
		//$("#isOverried-n").removeAttr("checked");
		
		$("#dstEnv").val('');
		$("#srcEnv").val(envName);
 	}

	 var modal_function = function() {
		$("#dialog-chooseEnv").dialog({
			autoOpen : false,
			modal : true,
			title : "复制环境", 
			buttons : [{
				html : "取消",
				"class" : "btn btn-default",
				click : function() {
					$(this).dialog("close");
				}
			}, {
				html : "<i class='fa fa-check'></i>&nbsp; 确定",
				"class" : "btn btn-primary",
				click : function() {
					var isOverried = $("input:radio[name=isOverried]:checked").val();
					var dstEnv = $('#dstEnv').val();
					
					if(dstEnv==null ||dstEnv=='undefined'||dstEnv==''){
						$.smartAlert({content: '目的环境不能为空'});
						return;
					}
					
					
					var srcEnv = $('#srcEnv').val();
					var app='${app!}';
					var params = {};
					var isOverrie = isOverried==1?true:false;
					params.app=app;
					params.srcEnv=srcEnv;
					params.dstEnv=dstEnv;
					params.isOverrie=isOverrie;
					$.ajax({
						url: "copyEnv",
						type: "get",
						data: params,
						dataType: "json",
						async: true,
						success: function(obj) {
							 if(obj.success){
								 $.smartSuccess({content: obj.msg});
								 $('#jqgrid').jqGrid().trigger("reloadGrid");
							} else{
								$.smartFailure({content: obj.msg});
							} 
						},
						error:function(){
							$.smartFailure({content: "操作失败，网络异常！"});
						}
					});
					$(this).dialog("close");
				}
			}]
	
		});
	};
	modal_function();  
	
	//全局变量
	var rsc_env = "";
			// Modal Link
		function chooseRsc(envName) {
			$.post("/centralconf/getRscList",function(json){
				var html = "";
				var length = json.length;
				for(var i=0;length&&i<length;i++){
					html += '<label class="checkbox"><input name="checkbox-inline" type="checkbox"><i></i>'+json[i]+'</label>';
				}
				$("#dialog_inline_group").html(html);
				rsc_env = envName;
				//显示已经关联的公共资源
				$.post("/centralconf/getEnvRscShow",{app:'${app!}',env:rsc_env},function(jsonStr){
					var len = jsonStr.length;
					for(var i=0;len&&i<len;i++){
						var showValue = jsonStr[i].key;
					//	console.log("showValue="+showValue);
						var labels = $("#dialog_inline_group input");
						var labelLen = labels.length;
						for(var j=0;labelLen&&j<labelLen;j++){
							var $label = $(labels[j]);
							var labelValue = $label.parent("label").text();
							if(labelValue==showValue){
								$label.attr("checked",true);
							}
						}
					}
					$('#dialog-chooseRsc').dialog('open');
				},"json");
				

			},"json");
			
			return false;
		};
	
			$("#dialog-chooseRsc").dialog({
				autoOpen : false,
				modal : true,
				height: 300,
	     		width: 550,
				title : "选择公共资源",
				buttons : [{
					html : "取消",
					"class" : "btn btn-default",
					click : function() {
						$(this).dialog("close");
					}
				}, {
					html : "<i class='fa fa-check'></i>&nbsp; 确定",
					"class" : "btn btn-primary",
					click : function() {
						//获得当前的选中的值
						var labels = $("#dialog_inline_group input");
						var len = labels.length;
						var checkList = [];
						for(var j=0;len&&j<len;j++){
							var cv = {};
							cv.value = $(labels[j]).parent("label").text();
							cv.desc = $(labels[j]).is(":checked")?"1":"0";
							checkList.push(cv);
						}
						$.post("/centralconf/addRelativeRsc",{app:'${app!}',env:rsc_env,checks:JSON.stringify(checkList)},function(isSuccess){
							if(isSuccess){
								$.smartSuccess({content: "操作公共资源成功"});
							}else{
								$.smartFailure({content: "操作公共资源失败"});
							}
						});
						$(this).dialog("close");
					}
				}]
		
			});
			
</script>

<div id="dialog-chooseRsc" title="选择公共资源" style="overflow:-Scroll;overflow-x:hidden">
	<input type="text" id="srcRsc" value="" hidden/>
	<div class="widget-body no-padding">
		<form action="" class="smart-form">
			<fieldset>
				<section>
					<label class="label">资源名称</label>
					<div class="inline-group" id="dialog_inline_group">
						<label class="checkbox">
							<input name="checkbox-inline" type="checkbox">
							<i></i>Alice</label>
					</div>
				</section>
			</fieldset>
		</form>
	</div>
</div>

<div id="dialog-chooseEnv" title="复制配置项" style="overflow:-Scroll;overflow-x:hidden">
	<input type="text" id="srcEnv" value="" hidden/>
	<div class="widget-body no-padding">
		<form action="" class="smart-form">
			<fieldset>
				<section>
					<label class="label">是否覆盖</label>
					<div class="row">
						<div class="col col-4">
							<label class="radio"> <input type="radio" id="isOverried-y" name="isOverried" value="1" checked="checked">
								 <i></i>是
							</label> 
							
							<label class="radio"> <input type="radio" id="isOverried-n" name="isOverried" value="2">
								<i></i>否
							</label>
						</div>
					</div>
				</section>
				<section>
					<label class="label">目的环境</label>
					<div class="row">
						<div class="col col-8" id="dstEnv-div">
							<label class="input">
								<input type="text" id="dstEnv">
							</label>
						</div>
					</div>
				</section>
			</fieldset>
		</form>
	</div>
</div>	

</html>