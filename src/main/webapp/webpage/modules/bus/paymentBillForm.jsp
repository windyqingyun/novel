<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>支付单管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		var validateForm;
		function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
		  if(validateForm.form()){
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
		});
				
	</script>
</head>
<body>
		<form:form id="inputForm" modelAttribute="paymentBill" action="${ctx}/bus/paymentBill/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>用户：</label></td>
					<td class="width-35">
						<form:input path="user.name" htmlEscape="false" class="form-control required"/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>支付金额(元)：</label></td>
					<td class="width-35">
						<form:input path="price" htmlEscape="false" class="form-control required number"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>支付渠道：</label></td>
					<td class="width-35">
						<form:select path="payChannel" class="form-control required">
							<form:options items="${fns:getDictList('pay_channel')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>所属机构：</label></td>
					<td class="width-35">
						<sys:treeselect id="office" name="office.id" value="${paymentBill.office.id}" labelName="office.name" labelValue="${paymentBill.office.name}"
							title="部门" url="/sys/office/treeData?type=2" cssClass="form-control required" allowClear="true" notAllowSelectParent="true"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">充值阅读币：</label></td>
					<td class="width-35">
						<form:input path="resultCoin" htmlEscape="false" class="form-control  number"/>
					</td>
					<td class="width-15 active"><label class="pull-right">充值阅读券：</label></td>
					<td class="width-35">
						<form:input path="resultTicket" htmlEscape="false" class="form-control  number"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">支付时间：</label></td>
					<td class="width-35">
						<input id="payDate" name="payDate" type="text" maxlength="20" class="laydate-icon form-control layer-date "
							value="<fmt:formatDate value="${paymentBill.payDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
					</td>
					<td class="width-15 active"><label class="pull-right">支付成功时间：</label></td>
					<td class="width-35">
						<input id="successDate" name="successDate" type="text" maxlength="20" class="laydate-icon form-control layer-date "
							value="<fmt:formatDate value="${paymentBill.successDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>支付结果：</label></td>
					<td class="width-35">
						<form:select path="issuccess" class="form-control required">
							<form:options items="${fns:getDictList('is_success')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
					<td class="width-15 active"></td>
					<td class="width-35"></td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">异常信息：</label></td>
					<td class="width-85" colspan="3">
						<form:textarea path="errorMsg" htmlEscape="false" rows="3" maxlength="150" class="form-control "/>
					</td>
				</tr>
		 	</tbody>
		</table>
	</form:form>
</body>
</html>