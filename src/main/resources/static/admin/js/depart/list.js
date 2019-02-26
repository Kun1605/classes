define(['jquery', 'bootstrap', 'backend', 'table', 'form', 'template'], function ($, undefined, Backend, Table, Form, Template) {

    var Controller = {
        index: function () {
            // 初始化表格参数配置
            Table.api.init({
                extend: {
                    index_url: '/admin/depart/datagrid',
                    add_url: '/admin/depart/add',
                    edit_url: '/admin/depart/edit',
                    del_url: '/admin/depart/del',
                    multi_url: '',
                }
            });

            var table = $("#table");

            Template.helper("Moment", Moment);
            
        	function formatCurrency(num) {
        	    num = num.toString().replace(/\$|\,/g,'');
        	    if(isNaN(num))
        	    num = "0";
        	    sign = (num == (num = Math.abs(num)));
        	    num = Math.floor(num*100+0.50000000001);
        	    cents = num%100;
        	    num = Math.floor(num/100).toString();
        	    if(cents<10)
        	    cents = "0" + cents;
        	    for (var i = 0; i < Math.floor((num.length-(1+i))/3); i++)
        	    num = num.substring(0,num.length-(4*i+3))+','+
        	    num.substring(num.length-(4*i+3));
        	    return (((sign)?'':'-') + num + '.' + cents);
        	}

            // 初始化表格
            table.bootstrapTable({
                url: $.fn.bootstrapTable.defaults.extend.index_url,
                templateView: true,
                columns: [
                    [
                        {field: 'state', checkbox: true, },
                        {field: 'id', title: 'ID', operate: false},
                        //直接响应搜索
                        {field: 'name', title: __('名称'), operate: 'LIKE %...%', placeholder: '模糊搜索'},
                        //通过Ajax渲染searchList
                        {field: 'logo', title: __('班级logo'), operate:false, align: 'center', formatter: Table.api.formatter.image},
                        //点击IP时同时执行搜索此IP,同时普通搜索使用下拉列表的形式
                        {field: 'balance', title: __('班费余额'), operate: false ,formatter:function (value, row, index) {
                        	return "<code>￥"+formatCurrency(value/100)+"</code>";
                        }},
                        //browser是一个不存在的字段
                        //通过formatter来渲染数据,同时为它添加上事件
                        {field: 'weibo', title: __('班集微博'), operate: false,  formatter: Controller.api.formatter.url},
                        //启用时间段搜索
                        {field: 'create_time', title: __('Create time'), operate:false, type: 'datetime', addclass: 'datetimepicker', data: 'data-date-format="YYYY-MM-DD"'},
                        //我们向操作栏额外添加上一个详情按钮,并保留已有的编辑和删除控制,同时为这个按钮添加上点击事件
                        {field: 'operate', title: __('Operate'), events: Controller.api.events.operate, formatter: Controller.api.formatter.operate}
                    ],
                ],
                //禁用默认搜索
                search: false,
                //启用普通表单搜索
                commonSearch: true,
                //可以控制是否默认显示搜索单表,false则隐藏,默认为false
                searchFormVisible: true,
                //分页大小
                pageSize: 6
            });

            // 为表格绑定事件
            Table.api.bindevent(table);

            //指定搜索条件
            $(document).on("click", ".btn-toggle-view", function () {
                var options = table.bootstrapTable('getOptions');
                table.bootstrapTable('refreshOptions', {templateView: !options.templateView});
            });

            //点击详情
            $(document).on("click", ".btn-detail[data-id]", function () {
                Backend.api.open('example/bootstraptable/detail/ids/' + $(this).data('id'), __('Detail'));
            });

            //获取选中项
            $(document).on("click", ".btn-selected", function () {
                //在templateView的模式下不能调用table.bootstrapTable('getSelections')来获取选中的ID,只能通过下面的Table.api.selectedids来获取
                Layer.alert(JSON.stringify(Table.api.selectedids(table)));
            });
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
            formatter: {
                url: function (value, row, index) {
                    return '<div class="input-group input-group-sm" style="width:250px;"><input type="text" class="form-control input-sm" value="' + value + '"><span class="input-group-btn input-group-sm"><a href="' + value + '" target="_blank" class="btn btn-default btn-sm"><i class="fa fa-link"></i></a></span></div>';
                },
                ip: function (value, row, index) {
                    return '<a class="btn btn-xs btn-ip bg-success"><i class="fa fa-map-marker"></i> ' + value + '</a>';
                },
                browser: function (value, row, index) {
                    //这里我们直接使用row的数据
                    return '<a class="btn btn-xs btn-browser">' + row.useragent.split(" ")[0] + '</a>';
                },
                operate: function (value, row, index) {
                    //返回字符串加上Table.api.formatter.operate的结果
                    //默认需要按需显示排序/编辑/删除按钮,则需要在Table.api.formatter.operate将table传入
                    //传入了table以后如果edit_url为空则不显示编辑按钮,如果del_url为空则不显显删除按钮
                    return  Table.api.formatter.operate(value, row, index, $("#table"));
                },
            },
            events: {
                ip: {
                    'click .btn-ip': function (e, value, row, index) {
                        var options = $("#table").bootstrapTable('getOptions');
                        //这里我们手动将数据填充到表单然后提交
                        $("#commonSearchContent_" + options.idTable + " form [name='ip']").val(value);
                        $("#commonSearchContent_" + options.idTable + " form").trigger('submit');
                        Toastr.info("执行了自定义搜索操作");
                    }
                },
                browser: {
                    'click .btn-browser': function (e, value, row, index) {
                        Layer.alert("该行数据为: <code>" + JSON.stringify(row) + "</code>");
                    }
                },
                operate: $.extend({
                    'click .btn-detail': function (e, value, row, index) {
                        Backend.api.open('example/tabletemplate/detail/ids/' + row['id'], __('Detail'));
                    }
                }, Table.api.events.operate)
            }
        }
    };
    return Controller;
});