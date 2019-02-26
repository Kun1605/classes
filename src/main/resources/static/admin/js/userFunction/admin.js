define(['jquery', 'bootstrap', 'backend', 'table', 'form'], function ($, undefined, Backend, Table, Form) {

    var Controller = {
        index: function () {
            // 初始化表格参数配置
            Table.api.init({
                extend: {
                    index_url: '/admin/userFunction/datagrid',
                    add_url: '/admin/userFunction/add',
                    edit_url: '/admin/userFunction/edit',
                    del_url: '/admin/userFunction/del',
                    multi_url: 'data/multi.json',
                }
            });

            var table = $("#table");

            // 初始化表格
            table.bootstrapTable({
                url: $.fn.bootstrapTable.defaults.extend.index_url,
                columns: [
                    [
                        {field: 'state', checkbox: true, },
                        {field: 'id', title: 'id', operate: false},
                        {field: 'student.name', title: __('学生姓名'), operate: 'LIKE %...%', placeholder: '模糊搜索', style: 'width:200px'},
                        {field: 'functionMenus', title: __('学生权限'), operate: false,  style: 'width:200px', events: Controller.api.events.browser1, formatter: Controller.api.formatter.browser},
                        
                        {field: 'operate', title: __('Operate'), events: Controller.api.events.operate, formatter: Controller.api.formatter.operate}
                    ]
                ],
                search: false,
                commonSearch: true,
                searchFormVisible: true
            });

            // 为表格绑定事件
            Table.api.bindevent(table);
            
            //指定搜索条件
            $(document).on("click", ".btn-singlesearch", function () {
                var options = table.bootstrapTable('getOptions');
                options.pageNumber = 1;
                options.queryParams = function (params) {
                    return {
                        search: params.search,
                        sort: params.sort,
                        order: params.order,
                        filter: JSON.stringify({admin_id: 1}),
                        op: JSON.stringify({admin_id: '='}),
                        offset: params.offset,
                        limit: params.limit,
                    };
                };
                table.bootstrapTable('refresh', {});
                Toastr.info("当前执行的是自定义搜索");
                return false;
            });
        },
        api: {
        	  formatter: {
        		  operate: function (value, row, index) {
        			  var html=[];
        			  html.push('<a class="btn btn-info btn-xs btn-detail"  >' + __('授权') + '</a>');
        			  html.push('<a href="javascript:;" class="btn btn-danger btn-delone btn-xs"><i class="fa fa-trash"></i></a>');
        			  return html.join(' ');
        		  },
        		  browser: function (value, row, index) {
                      //这里我们直接使用row的数据
                  	if(value!=null){
                  	  return '<a class="btn btn-xs btn-browser btn-danger ">点击查看用户权限<a>';
                  	}
                  }
        	  },
	        events: {
	        	browser1: {
	                	'click .btn-browser': function (e, value, row, index) {
	                		e.stopPropagation();
	                		var title="</br>";
	                		var obj=value;
	                		$.each(obj, function (n, value) {
	                            title=title+value.title+",</br>"
	                        });
	                        Layer.alert("用户的权限是: <code>" + title + "</code>");
	                	}
	                },
	        	operate: $.extend({
	        		'click .btn-detail': function (e, value, row, index) {
	        			Backend.api.open('/admin/userFunction/granklist/' + row['id'], __('用户授权'));
	        		}
	        	}, Table.api.events.operate)
	        	
	        },
        },
        add: function () {
            Form.api.bindevent($("form[role=form]"));
        },
        edit: function () {
            Form.api.bindevent($("form[role=form]"));
        }
    };
    return Controller;
});