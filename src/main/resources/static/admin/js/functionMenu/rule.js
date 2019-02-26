define(['jquery', 'bootstrap', 'backend', 'table', 'form', 'template'], function ($, undefined, Backend, Table, Form, Template) {
   
	Form.api.bindevent($("form[role=form]"));
	$(document).on('click', ".btn-search-icon", function () {
    	  var iconlist = [];
          Form.api.bindevent($("form[role=form]"));
    	  if (iconlist.length == 0) {
              $.get(Config.site.cdnurl + "/admin/assets/libs/font-awesome/less/variables.less", function (ret) {
                  var exp = /fa-var-(.*):/ig;
                  var result;
                  while ((result = exp.exec(ret)) != null) {
                      iconlist.push(result[1]);
                  }
                  Layer.open({
                      type: 1,
                      area: ['460px', '300px'], //宽高
                      content: Template('chooseicontpl', {iconlist: iconlist})
                  });
              });
          } else {
              Layer.open({
                  type: 1,
                  area: ['460px', '300px'], //宽高
                  content: Template('chooseicontpl', {iconlist: iconlist})
              });
          }
    });
    $(document).on('click', '#chooseicon ul li', function () {
        $("input[name='icon']").val('fa fa-' + $(this).data("font"));
        Layer.closeAll();
    });
    $(document).on('keyup', 'input.js-icon-search', function () {
        $("#chooseicon ul li").show();
        if ($(this).val() != '') {
            $("#chooseicon ul li:not([data-font*='" + $(this).val() + "'])").hide();
        }
    });
    $(document).on('change', '#c-menu', function () {
        $("#allonemenu").hide();
        if ($(this).val() == "0") {
        	$("#allonemenu").show();
        }
    });
    var Controller = {
        index: function () {
            // 初始化表格参数配置
            Table.api.init({
                extend: {
                    index_url: '/admin/functionMenu/datagrid',
                    add_url: '/admin/functionMenu/add',
                    edit_url: '/admin/functionMenu/edit',
                    del_url: '/admin/functionMenu/del',
                }
            });

            var table = $("#table");

            // 初始化表格
            table.bootstrapTable({
                url: $.fn.bootstrapTable.defaults.extend.index_url,
                escape:false, 
                columns: [
                    [
                        {field: 'state', checkbox: true, },
                        {field: 'id', title: 'ID' ,operate: false},
                        {field: 'title', title: __('Title'), operate: true},
                        {field: 'icon', title: __('Icon'), formatter: Controller.api.formatter.icon,operate: false},
                        {field: 'url', title: __('Url'), operate: false},
                        {field: 'onemenu', title: __('一级菜单'), searchList: {
                        	'1': __('一级菜单'),
                        	'0': __('二级菜单'),
                        }, operate: true},
                        {field: 'pid', title: __('二级菜单'), operate: false},
                        {field: 'weight', title: __('Weigh') ,operate: false},
                        {field: 'create_time', title: __('Create time'), operate: false},
                        {field: 'update_time', title: __('Update time'), operate: false},
                        {field: 'operate', title: __('Operate'), events: Controller.api.events.operate, formatter: function (value, row, index) {
                            return Table.api.formatter.operate.call(this, value, row, index, table);
                        }}
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
            Table.api.bindevent(table);//当内容渲染完成后

            
        },
        add: function () {
        	alert("1");
            Controller.api.bindevent();
        },
        edit: function () {
            Controller.api.bindevent();
        },
        api: {
            formatter: {
                icon: function (value, row, index) {
                    return '<i class="' + value + '"></i>';
                }
            },
            bindevent: function () {
                var iconlist = [];
                Form.api.bindevent($("form[role=form]"));
                $(document).on('click', ".btn-search-icon", function () {
                    if (iconlist.length == 0) {
                        $.get(Config.site.cdnurl + "/admin/assets/libs/font-awesome/less/variables.less", function (ret) {
                            var exp = /fa-var-(.*):/ig;
                            var result;
                            while ((result = exp.exec(ret)) != null) {
                                iconlist.push(result[1]);
                            }
                            Layer.open({
                                type: 1,
                                area: ['460px', '300px'], //宽高
                                content: Template('chooseicontpl', {iconlist: iconlist})
                            });
                        });
                    } else {
                        Layer.open({
                            type: 1,
                            area: ['460px', '300px'], //宽高
                            content: Template('chooseicontpl', {iconlist: iconlist})
                        });
                    }
                });
                $(document).on('click', '#chooseicon ul li', function () {
                    $("input[name='icon']").val('fa fa-' + $(this).data("font"));
                    Layer.closeAll();
                });
                $(document).on('keyup', 'input.js-icon-search', function () {
                    $("#chooseicon ul li").show();
                    if ($(this).val() != '') {
                        $("#chooseicon ul li:not([data-font*='" + $(this).val() + "'])").hide();
                    }
                });
                $(document).on('change', '#c-menu', function () {
                    $("#allonemenu").hide();
                    if ($(this).val() == "0") {
                    	$("#allonemenu").show();
                    }
                });
            },
            events: {//绑定事件的方法
                operate: $.extend({
                    'click .btn-deloneC': function (e, value, row, index) {
                    	e.stopPropagation();
                        var that = this;
                        var top = $(that).offset().top - $(window).scrollTop();
                        var left = $(that).offset().left - $(window).scrollLeft() - 260;
                        if (top + 154 > $(window).height()) {
                            top = top - 154;
                        }
                        if ($(window).width() < 480) {
                            top = left = undefined;
                        }
                        $.post("/s/f/auth/rule/getUserFunction",{id:row.id},function(result){
                        	if(result.code == 200){
                        		var index = Layer.confirm(
                                    __('该菜单已授权给用户,是否删除?'),
                                    {icon: 3, title: __('Warning'), offset: [top, left], shadeClose: true},
                                    function () {
                                        var table = $(that).closest('table');
                                        var options = {url: '/s/f/auth/rule/del', data: {ids: row.id}};
                                        Fast.api.ajax(options, function (data) {
                                            Toastr.success(__('Operation completed'));
                                            table.bootstrapTable('refresh');
                                        });
                                        Layer.close(index);
                                    }
                                );
                        	}else{
                        		var index = Layer.confirm(
                                    __('Are you sure you want to delete this item?'),
                                    {icon: 3, title: __('Warning'), offset: [top, left], shadeClose: true},
                                    function () {
                                        var table = $(that).closest('table');
                                        var options = {url: '/s/f/auth/rule/del', data: {ids: row.id}};
                                        Fast.api.ajax(options, function (data) {
                                            Toastr.success(__('Operation completed'));
                                            table.bootstrapTable('refresh');
                                        });
                                        Layer.close(index);
                                    }
                                );
                        	}
                        },"json"); 
                    }
                }, Table.api.events.operate)
            }
        }
    };
    return Controller;
});