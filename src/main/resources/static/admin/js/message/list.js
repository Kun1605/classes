define(['jquery', 'bootstrap', 'backend', 'table', 'form'], function ($, undefined, Backend, Table, Form) {

    var Controller = {
        index: function () {
            // 初始化表格参数配置
            Table.api.init({
                extend: {
                    index_url: '/admin/message/datagrid',
                    add_url: '/admin/message/add',
                    edit_url: '/admin/message/edit',
                    del_url: '/admin/message/del',
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
        {field: 'a', checkbox: true},
        {field: 'id', title: __('ID'), operate: false},
        {field: 'student.name', title: __('学生姓名'), operate: true},
        {field: 'title', title: __('标题'), operate: 'LIKE %...%', placeholder: '标题', style: 'width:200px'},
        {field: 'content', title: __('内容'), operate: true , events: Controller.api.events.browser, formatter: Controller.api.formatter.browser},
        {field: 'state', title: __('状态'), searchList: {
         	'1': __('未读的消息'),
         	'2': __('已读的消息'),
         	'3': __('删除的消息'),
         	 }},
        {field: 'create_time', title: __('创建时间'),  operate: false},
        {field: 'update_time', title: __('修改时间'),  operate: false},
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
        	formatter: {
      		  operate: function (value, row, index) {
      			  if($("#r_p_type").val() == 2){
	      			    return '<a class="btn btn-success btn-xs btn-addWX">' + __('设置金额(微信)') + '</a> '
	      			  + '<a class="btn btn-info btn-xs btn-detail">' + __('查看') + '</a> '
	      			+ Table.api.formatter.operate(value, row, index, $("#table"));
      			  }else if($("#r_p_type").val() == 1){
      				    return '<a class="btn btn-success btn-xs btn-add">' + __('设置金额(图文)') + '</a> '
      				  + '<a class="btn btn-info btn-xs btn-detail">' + __('查看') + '</a> '
      				  + Table.api.formatter.operate(value, row, index, $("#table"));
      			  }
      			   else if($("#r_p_type").val() == 3){
  				    return '<a class="btn btn-success btn-xs btn-add">' + __('设置金额(微博)') + '</a> '
  				  + '<a class="btn btn-info btn-xs btn-detail">' + __('查看') + '</a> '
  				  + Table.api.formatter.operate(value, row, index, $("#table"));
  			  }
      		  },
        browser: function (value, row, index) {
            //这里我们直接使用row的数据
        	if(value!=null){
            return '<a class="btn btn-xs btn-browser btn-danger ">' + value.substr(0,5) + '...</a>';
        	}
        },
      	  	},
	        events: {
        	  browser: {
                	'click .btn-browser': function (e, value, row, index) {
                		e.stopPropagation();
                        Layer.alert("该行数据为: <code>" + JSON.stringify(value) + "</code>");
                	}
                },
	        }
        }
    };
    return Controller;
});