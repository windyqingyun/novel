<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>素材管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
		});
	</script>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
	<div class="ibox">
	<div class="ibox-title">
		<h5>素材列表 </h5>
		<div class="ibox-tools">
			<a class="collapse-link">
				<i class="fa fa-chevron-up"></i>
			</a>
			<a class="dropdown-toggle" data-toggle="dropdown" href="#">
				<i class="fa fa-wrench"></i>
			</a>
			<ul class="dropdown-menu dropdown-user">
				<li><a href="#">选项1</a>
				</li>
				<li><a href="#">选项2</a>
				</li>
			</ul>
			<a class="close-link">
				<i class="fa fa-times"></i>
			</a>
		</div>
	</div>
    
    <div class="ibox-content">
	<sys:message content="${message}"/>
	
	<!--查询条件-->
	<div class="row">
	<div class="col-sm-12">
	<form:form id="searchForm" modelAttribute="fodder" action="${ctx}/bus/fodder/" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
		<div class="form-group">
			<span>标题：</span>
				<form:input path="title" htmlEscape="false" maxlength="64"  class=" form-control input-sm"/>
			<span>来源机构：</span>
				<sys:treeselect id="office" name="office.id" value="${fodder.office.id}" labelName="office.name" labelValue="${fodder.office.name}"
					title="部门" url="/sys/office/treeData?type=2" cssClass="form-control input-sm" allowClear="true" notAllowSelectParent="true"/>
		 </div>	
	</form:form>
	<br/>
	</div>
	</div>
	
	<!-- 工具栏 -->
	<div class="row">
	<div class="col-sm-12">
		<div class="pull-left">
			<shiro:hasPermission name="bus:fodder:add">
				<table:addRow url="${ctx}/bus/fodder/form" title="素材"></table:addRow><!-- 增加按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="bus:fodder:edit">
			    <table:editRow url="${ctx}/bus/fodder/form" title="素材" id="contentTable"></table:editRow><!-- 编辑按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="bus:fodder:del">
				<table:delRow url="${ctx}/bus/fodder/deleteAll" id="contentTable"></table:delRow><!-- 删除按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="bus:fodder:import">
				<table:importExcel url="${ctx}/bus/fodder/import"></table:importExcel><!-- 导入按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="bus:fodder:export">
	       		<table:exportExcel url="${ctx}/bus/fodder/export"></table:exportExcel><!-- 导出按钮 -->
	       	</shiro:hasPermission>
	       <button class="btn btn-white btn-sm " data-toggle="tooltip" data-placement="left" onclick="sortOrRefresh()" title="刷新"><i class="glyphicon glyphicon-repeat"></i> 刷新</button>
		
			</div>
		<div class="pull-right">
			<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="search()" ><i class="fa fa-search"></i> 查询</button>
			<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="reset()" ><i class="fa fa-refresh"></i> 重置</button>
		</div>
	</div>
	</div>
	
	<!-- 表格 -->
	<table id="contentTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
		<thead>
			<tr>
				<th><input type="checkbox" class="i-checks"></th>
				<th class="sort-column title">标题</th>
				<th class="titleImage">标题图片</th>
				<th class="content">素材内容</th>
				<th class="linkUrl">链接地址</th>
				<th class="sort-column bookId">对应书籍</th>
				<th class="sort-column office.name">来源机构</th>
				<th class="sort-column viewcount">浏览次数</th>
				<th class="sort-column createBy.id">创建者</th>
				<th class="sort-column createDate">创建时间</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="fodder">
			<tr>
				<td> <input type="checkbox" id="${fodder.id}" class="i-checks"></td>
				<td><a  href="#" onclick="openDialogView('查看素材', '${ctx}/bus/fodder/form?id=${fodder.id}','800px', '500px')">
					${fodder.title}
				</a></td>
				<td>
					${fodder.titleImage}
				</td>
				<td>
					${fns:unescapeHtml(fodder.content)}
				</td>
				<td>
					${fodder.linkUrl}
				</td>
				<td>
					${fodder.bookId}
				</td>
				<td>
					${fodder.office.name}
				</td>
				<td>
					${fodder.viewcount}
				</td>
				<td>
					${fodder.createBy.id}
				</td>
				<td>
					<fmt:formatDate value="${fodder.createDate}" pattern="yyyy-MM-dd"/>
				</td>
				<td>
					<shiro:hasPermission name="bus:fodder:view">
						<a href="#" onclick="openDialogView('查看素材', '${ctx}/bus/fodder/form?id=${fodder.id}','800px', '500px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="bus:fodder:edit">
    					<a href="#" onclick="openDialog('修改素材', '${ctx}/bus/fodder/form?id=${fodder.id}','800px', '500px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 修改</a>
    				</shiro:hasPermission>
    				<shiro:hasPermission name="bus:fodder:del">
						<a href="${ctx}/bus/fodder/delete?id=${fodder.id}" onclick="return confirmx('确认要删除该素材吗？', this.href)"   class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 删除</a>
					</shiro:hasPermission>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	
		<!-- 分页代码 -->
	<table:page page="${page}"></table:page>
	<br/>
	<br/>
	</div>
	</div>
</div>
</body>
</html>