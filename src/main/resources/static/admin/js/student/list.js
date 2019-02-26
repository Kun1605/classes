define(['jquery', 'bootstrap', 'backend', 'table', 'form'], function ($, undefined, Backend, Table, Form) {

    var Controller = {
        index: function () {
            // 初始化表格参数配置
            Table.api.init({
                extend: {
                    index_url: '/admin/student/datagrid',
                    add_url: '/admin/student/add',
                    edit_url: '/admin/student/edit',
                    del_url: '/admin/student/del',
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
                        {field: 'studentNo', title: __('学号'), operate: 'LIKE %...%', placeholder: '学号', style: 'width:200px'},
                        {field: 'name', title: __('姓名'), operate: 'LIKE %...%', placeholder: '姓名', style: 'width:200px'},
                        {field: 'classId', title: __('班集'), operate: false},
                        {field: 'phone', title: __('手机号'), operate: 'LIKE %...%', placeholder: '手机号', style: 'width:200px'},
                        {field: 'email', title: __('邮箱'), operate: false},
                        {field: 'status', title: __("Status"), searchList: {'enabled': __('启用的账号'), 'disabled': __('被禁的账号')}, formatter: Table.api.formatter.status},
                        {field: 'create_time', title: __('创建时间'),  operate: false},
                        {field: 'update_time', title: __('修改时间'),  operate: false},
                        {field: 'operate', title: __('Operate'), events: Table.api.events.operate, formatter: Table.api.formatter.operate}
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
            }
        }
    };
    return Controller;
});