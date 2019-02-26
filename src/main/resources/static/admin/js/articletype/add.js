define([ 'jquery', 'bootstrap', 'backend', 'table', 'form', 'template', 'jquery-ajax-file', 'upload', 'validator' ], function($, undefined, Backend, Table, Form, Template, FileUpload, Upload, Validator) {
	var Controller = {
		index : function() {
			// 初始化表格参数配置
			Table.api.init({
				extend : {
					index_url : 'data/page.json',
					add_url : 'page-edit.html',
					edit_url : 'page-edit.html',
					del_url : 'data/del.json',
					multi_url : 'data/multi.json',
					table : 'page',
				}
			});
			Form.api.bindevent($("form[role=form]"));
			var uploadBtn = $("#input_avatar");
			var file = $("input[type='file']");
			var img = $("input[name='logo']");
			uploadBtn.on('click', function() {
				file.trigger("click");

			});

			file.ajaxfileupload({
				action : '/qiniu/upload',
				method : 'post',
				valid_extensions : [ 'png', 'jpg', 'jpeg', 'gif', 'bmp', 'webp' ],
				params : {
					action : 'uploadimage'
				},
				onComplete : function(resp) {
					console.log(JSON.stringify(resp));
					$("#avatar-img").attr("src", resp.qiniu_path);
					img.attr("value", resp.qiniu_path);
				},
				onStart : function() {
					console.log('img upload start')
				},
				onCancel : function() {
					console.log('no file selected');
				}
			});



			function getSubStr(string) {
				var str = '';
				var len = 0;
				for (var i = 0; i < string.length; i++) {
					if (string[i].match(/[^\x00-\xff]/ig) != null) {
						len += 2;
					} else {
						len += 1;
					}
					if (len > 240) { /* 240为要截取的长度 */
						str += '...';
						break;
					}
					str += string[i];
				}
				return str;
			}
			// 获取没有mark的
			function getNoMarkupStr(markupStr) {
				/* markupStr 源码</> */
				//console.log(markupStr);
				var noMarkupStr = $("<div>").html(markupStr).text(); /* 得到可视文本(不含图片),将&nbsp;&lt;&gt;转为空字符串和<和>显示,同时去掉了换行,文本单行显示 */
				//console.log("1--S" + noMarkupStr + "E--");
				noMarkupStr = noMarkupStr.replace(/(\r\n|\n|\r)/gm, ""); /* 去掉可视文本中的换行,(没有用,上一步已经自动处理) */
				//console.log("2--S" + noMarkupStr + "E--");
				noMarkupStr = noMarkupStr.replace(/^\s+/g, ""); /* 替换开始位置一个或多个空格为一个空字符串 */
				//console.log("3--S" + noMarkupStr + "E--");
				noMarkupStr = noMarkupStr.replace(/\s+$/g, ""); /* 替换结束位置一个或多个空格为一个空字符串 */
				//console.log("4--S" + noMarkupStr + "E--");
				noMarkupStr = noMarkupStr.replace(/\s+/g, " "); /* 替换中间位置一个或多个空格为一个空格 */
				//console.log("5--S" + noMarkupStr + "E--");
				return noMarkupStr;
			}

			var table = $("#table");
			// 初始化表格
			table.bootstrapTable({
				url : $.fn.bootstrapTable.defaults.extend.index_url,
				sortName : 'weigh',
				columns : [
					[
						{
							field : 'state',
							checkbox : true
						},
						{
							field : 'id',
							title : __('Id'),
							operate : false
						},
						{
							field : 'category_id',
							title : __('Category_id'),
							operate : '='
						},
						{
							field : 'category.name',
							title : __('Category'),
							operate : '='
						},
						{
							field : 'title',
							title : __('Title'),
							operate : 'LIKE %...%',
							placeholder : '关键字，模糊搜索'
						},
						{
							field : 'flag',
							title : __('Flag'),
							formatter : Table.api.formatter.flag,
							operate : false
						},
						{
							field : 'image',
							title : __('Image'),
							formatter : Table.api.formatter.image,
							operate : false
						},
						{
							field : 'views',
							title : __('Views'),
							operate : false
						},
						{
							field : 'comments',
							title : __('Comments'),
							operate : false
						},
						{
							field : 'weigh',
							title : __('Weigh'),
							operate : false
						},
						{
							field : 'status',
							title : __('Status'),
							formatter : Table.api.formatter.status,
							searchList : {
								'normal' : __('Normal'),
								'hidden' : __('Hidden')
							},
							style : 'min-width:100px;'
						},
						{
							field : 'createtime',
							title : __('Create Time'),
							formatter : Table.api.formatter.datetime,
							operate : 'BETWEEN',
							type : 'datetime',
							addclass : 'datetimepicker',
							data : 'data-date-format="YYYY-MM-DD"'
						},
						{
							field : 'operate',
							title : __('Operate'),
							events : Table.api.events.operate,
							formatter : Table.api.formatter.operate
						}
					]
				],
				//普通搜索
				commonSearch : true,
				titleForm : '', //为空则不显示标题，不定义默认显示：普通搜索
			});

			// 为表格绑定事件
			Table.api.bindevent(table);
		},
		add : function() {
			Controller.api.bindevent();
		},
		edit : function() {
			Controller.api.bindevent();
		},
		api : {
			bindevent : function() {
				Form.api.bindevent($("form[role=form]"));
			}
		}
	};

	return Controller;
});