<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>素材管理</title>
	<meta name="decorator" content="default"/>
	 <link href="${ctxStatic}/summernote/summernote.css" rel="stylesheet">
	 <link href="${ctxStatic}/summernote/summernote-bs3.css" rel="stylesheet">
	 <script src="${ctxStatic}/summernote/summernote.min.js"></script>
	 <script src="${ctxStatic}/summernote/summernote-zh-CN.js"></script>
	<script type="text/javascript">
		var validateForm;
		function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
		  if(validateForm.form()){
			  $("#content").val($("#rich_content").next().find(".note-editable").html());//取富文本的值
			  $("#inputForm").submit();
			  return true;
		  }
	
		  return false;
		}
		$(document).ready(function() {
			validateForm = $("#inputForm").validate({
				submitHandler: function(form){
					loading('正在提交，请稍等...');
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});
			
				//富文本初始化
			$('#rich_content').summernote({
                lang: 'zh-CN'
            });

			$("#rich_content").next().find(".note-editable").html(  $("#content").val());

			$("#rich_content").next().find(".note-editable").html(  $("#rich_content").next().find(".note-editable").text());
		});
	</script>
</head>
<body>
		<form:form id="inputForm" modelAttribute="fodder" action="${ctx}/bus/fodder/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>标题：</label></td>
					<td class="width-85" colspan="3">
						<form:input path="title" htmlEscape="false" maxlength="64" class="form-control required width-75"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">标题图片：</label></td>
					<td class="width-85" colspan="3">
						<form:hidden id="titleImage" path="titleImage" htmlEscape="false" maxlength="64" class="form-control"/>
						<sys:ckfinder input="titleImage" type="files" uploadPath="/bus/fodder" selectMultiple="false"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">素材内容：</label></td>
					<td class="width-85" colspan="3">
						<form:hidden path="content"/>
						<div id="rich_content">
                        </div>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">链接地址：</label></td>
					<td class="width-35">
						<form:input path="linkUrl" htmlEscape="false" maxlength="100" class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">书籍：</label></td>
					<td class="width-35">
						<form:input path="bookId" htmlEscape="false" maxlength="64" class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>来源机构：</label></td>
					<td class="width-35">
						<sys:treeselect id="office" name="office.id" value="${fodder.office.id}" labelName="office.name" labelValue="${fodder.office.name}"
							title="部门" url="/sys/office/treeData?type=2" cssClass="form-control required" allowClear="true" notAllowSelectParent="true"/>
					</td>
					<td class="width-15 active"><label class="pull-right">备注信息：</label></td>
					<td class="width-35">
						<form:textarea path="remarks" htmlEscape="false" rows="4" maxlength="255" class="form-control "/>
					</td>
				</tr>
		 	</tbody>
		</table>
	</form:form>
</body>
</html>