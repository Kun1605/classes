define(['jquery', 'bootstrap', 'backend', 'table', 'form'], function ($, undefined, Backend, Table, Form) {

    var Controller = {
        index: function () {
            // 初始化表格参数配置
            Table.api.init({
                extend: {
                    index_url: '/admin/moneyRecord/datagrid',
                    add_url: '/admin/moneyRecord/add',
                    edit_url: '/admin/moneyRecord/edit',
                    del_url: '/admin/moneyRecord/del',
                    multi_url: 'data/multi.json',
                }
            });
            Form.api.bindevent($("form[role=form]"));
            var table = $("#table");
            var amount_a = $("#amount_a");
            var amount = $("#amount");
            amount_a.blur(function() {
            	var fen=parseFloat($(this).val())*100;
            	console.log(fen);
            	amount.val(fen);
            });
            
            
            
            
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
                columns: [
                    [
                        {field: 'state', checkbox: true, },
                        {field: 'id', title: 'id', operate: false},
                        {field: 'depart.name', title: __('班级'), operate: false},
                        {field: 'reason', title: __('花费原因'), operate: false,  style: 'width:200px', events: Controller.api.events.browser1, formatter: Controller.api.formatter.browser},
                        {field: 'amount', title: '金额', operate: false ,formatter:function (value, row, index) {
                        	return "<code>￥"+formatCurrency(value/100)+"</code>";
                        }},
                        /*{field: 'operate', title: __('Operate'), events: Controller.api.events.operate, formatter: Controller.api.formatter.operate}*/
                    ]
                ],
                search: false,
                commonSearch: false,
                searchFormVisible: false
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
                  		var str=value.substring(0, 6);
                  	  return '<a class="btn btn-xs btn-browser btn-danger ">'+str+'<a>';
                  	}
                  }
        	  },
	        events: {
	        	browser1: {
	                	'click .btn-browser': function (e, value, row, index) {
	                		e.stopPropagation();
	                        Layer.alert("原因是<code>" + value + "</code>");
	                	}
	                },
	        	operate: $.extend({
	        		'click .btn-detail': function (e, value, row, index) {
	        			Backend.api.open('/admin/moneyRecord/granklist/' + row['id'], __('用户授权'));
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