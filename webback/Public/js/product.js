$(document).ready(function(){
	$('.li_input').click(function(e){
		var _e = $(this).index();
		var _boxLeft = 130 + _e * 126;
		
		$('#gsBox').show();
		//$('#searchbar_arrow').show();
		//$('#searchbar_arrow').css({'left':_boxLeft});
		$('#searchbar_arrow').animate({'left':_boxLeft},300).show();
	});

	$('.close_btn').click(function(){
		$('#gsBox').hide();
		$('#searchbar_arrow').hide();
	})
})