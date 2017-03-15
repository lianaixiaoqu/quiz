//加载对应
function loadTmpl(option){
	var tmp = '';
	asyncReq({
		url: 'tmpl/' + option.tmplUrl + '.tmpl'
	},function(tmpl){
		tmp += template.compile(tmpl)(option.data);
	});
}






















































