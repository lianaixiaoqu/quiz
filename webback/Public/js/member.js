function tab_set(obj,id){
	$('.hover').removeClass('hover');
	$(obj).addClass('hover');
	$('.c_content').children('div').hide();
	$('#'+id).show();
}