
define(['jquery', 'bootstrap', 'backend', 'table', 'form'], function ($, undefined, Backend, Table, Form) {
    var Controller = {
        index: function () {
            // 初始化表格参数配置
            Table.api.init({
                extend: {
                    index_url: '/admin/homeworkrecord/datagrid?work_id='+$('#work_id').val(),
                    add_url: '/admin/homework/add',
                    edit_url: '/admin/homework/edit',
                    del_url: '/admin/homework/del',
                    table: 'page',
                }
            });
            var table = $("#table");

            // 初始化表格
            table.bootstrapTable({
                url: $.fn.bootstrapTable.defaults.extend.index_url,
                sortName: 'weigh',
                columns: [
                    [
                    	{field: 'state', checkbox: true},
                    	{field: 'id', title: __('ID'), operate: false},
                    	{field: 'student.name', title: __('学生姓名'), operate: 'LIKE %...%', placeholder: '标题', style: 'width:200px'},
                    	{field: 'student.id', title: __('学生Id'), visible: "true"},
                    	{field: 'work_id', title: __('下载链接'), operate: false ,formatter: Controller.api.formatter.url ,style: 'width:100px'} ,
                    /*	{field: 'content', title: __('描述'),events: Table.api.events.content, formatter: Table.api.formatter.content, operate: false,placeholder: '姓名', style: 'width:200px'},
                    	{field: 'type', title: __('分类'), formatter: function(value,row,index){
                    		return '<span class="label label-default">'+value+'</span>';
                    	}, operate: false},*/
                    	{field: 'create_time', title: __('创建时间'),  operate: false},
                    	{field: 'update_time', title: __('修改时间'),  operate: false},
                    	/*{field: 'operate',  title: __('操作'), events: Controller.api.events.operate1, formatter: Controller.api.formatter.operate1}*/
                    ]
                ],
                //禁用默认搜索
                search: false,
                //启用普通表单搜索
                commonSearch: true,
                //可以控制是否默认显示搜索单表,false则隐藏,默认为false
                searchFormVisible: true,
            });

            // 为表格绑定事件
            Table.api.bindevent(table);
        },
        add: function () {
            Controller.api.bindevent();
        },
        edit: function () {
            Controller.api.bindevent();
        },
        api: {
            bindevent: function () {
                Form.api.bindevent($("form[role=form]"));
            },
	        formatter: {//渲染的方法
	        	 operate1: function (value, row, index) {
       			  return   '<a class="btn btn-info btn-xs btn-detail">' + __('查看提交情况') + '</a> ';
       		  },
       		url: function (value, row, index) {
       			console.log(row);
                return '<div class="input-group input-group-sm"><span class="input-group-btn input-group-sm"><a href="/web/homework/download/' + row['work_id'] +'/'+row['student_id']+ '" target="_blank" class="btn btn-default btn-sm"><i class="fa fa-link"></i></a></span></div>';
            }
       		  
	        },
            events: {//绑定事件的方法
            	operate1: $.extend({
	        		'click .btn-detail': function (e, value, row, index) {
	        			Backend.api.open('/web/homeworkrecord/list/' + row['id'], __('查看学生作业'));
	        		},
	        	}, Table.api.events.operate)
            }
        }
    };
    return Controller;
});