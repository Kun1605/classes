define(['jquery', 'bootstrap', 'backend', 'table', 'form'], function ($, undefined, Backend, Table, Form) {

    var Controller = {
        index: function () {
            // 初始化表格参数配置
            Table.api.init({
                extend: {
	                 index_url: '/admin/article/datagrid',
	            	 add_url: '/admin/article/add',
	                 edit_url: '/admin/article/edit',
	                 del_url: '/admin/article/del',
                }
            });

            var table = $("#table");

            // 初始化表格
            table.bootstrapTable({
                url: $.fn.bootstrapTable.defaults.extend.index_url,
                columns: [
                    [
                        //该列为复选框字段,如果后台的返回state值将会默认选中
                        {field: 'state', checkbox: true, },
                       
                        {field: 'id', title: 'TaskID', operate: false},
                        
                        {field: 'title', title: __('文章名称'), operate: 'LIKE %...%', placeholder: '模糊搜索', style: 'width:200px'},
                        
                        {field: 'id', title: __('本平台链接'), operate: false, formatter: Controller.api.formatter.url},
                        
                        {field: 'create_time', title: __('Create time'), operate: false},
                        
                        {field: 'update_time', title: __('Update time'), operate: false},
                        
                    ],
                ],
                //禁用默认搜索
                search: false,
                //启用普通表单搜索
                commonSearch: true,
                //可以控制是否默认显示搜索单表,false则隐藏,默认为false
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
        		url: function (value, row, index) {
                    return '<div class="input-group input-group-sm"><span class="input-group-btn input-group-sm"><a href="/web/article/' + value + '" target="_blank" class="btn btn-default btn-sm"><i class="fa fa-link"></i></a></span></div>';
                }
      	  	},
	        bindevent: function () {
	        	Form.api.bindevent($("form[role=form]"));
            }
        }
    };
    return Controller;
});