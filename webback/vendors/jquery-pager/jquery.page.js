(function($){
	var pager = {
		init: function(pageRoot,args){
				pager.updateHtml(pageRoot,args);
				pager.bindEvent(pageRoot,args);
				if(args.data){
					pageRoot.data('post',args.data);
				}
		},
		//获取新的html并显示
		updateHtml: function(pageRoot,args){
				var pageString = '';
				//上一页
				if(args.currentPage > 1){
					pageString += '<a href="javascript:;" class="prevPage">上一页</a>';
				}else{
					pageString += '<span class="disabled">上一页</span>';
				}
				//中间页码
				if(args.currentPage != 1 && args.currentPage >= 4 && args.totalPages != 4){
					pageString += '<a href="javascript:;" class="pageNumber">'+1+'</a>';
				}
				if(args.currentPage-2 > 2 && args.currentPage <= args.totalPages && args.totalPages > 5){
					pageString += '<span>...</span>';
				}
				var start = args.currentPage -2,end = args.currentPage+2;
				if((start > 1 && args.currentPage < 4)||args.currentPage == 1){
					end++;
				}
				if(args.currentPage > args.totalPages-4 && args.currentPage >= args.totalPages){
					start--;
				}
				for (;start <= end; start++) {
					if(start <= args.totalPages && start >= 1){
						if(start != args.currentPage){
							pageString += '<a href="javascript:;" class="pageNumber">'+ start +'</a>';
						}else{
							pageString += '<span class="current">'+ start +'</span>';
						}
					}
				}
				if(args.currentPage + 2 < args.totalPages - 1 && args.currentPage >= 1 && args.totalPages > 5){
					pageString += '<span>...</span>';
				}
				if(args.currentPage != args.totalPages && args.currentPage < args.totalPages -2  && args.totalPages != 4){
					pageString += '<a href="javascript:;" class="pageNumber">'+args.totalPages+'</a>';
				}
				//下一页
				if(args.currentPage < args.totalPages){
					pageString += '<a href="javascript:;" class="nextPage">下一页</a>';
				}else{
					pageString += '<span class="disabled">下一页</span>';
				}
				pageRoot.html(pageString);
		},
		//绑定事件
		bindEvent: function(pageRoot,args){
			    pageRoot.off("click");
				pageRoot.on("click","a.pageNumber",function(){
					var current = Number($(this).text());
					pager.updateHtml(pageRoot,{currentPage:current,totalPages:args.totalPages});
					if(typeof args.callback ==='function'){
						args.callback(current);
					}
				});
				//上一页
				pageRoot.on("click","a.prevPage",function(){
					pageRoot.children("span.current").prev('a.pageNumber').click();
				});
				//下一页
				pageRoot.on("click","a.nextPage",function(){
					pageRoot.children("span.current").next('a.pageNumber').click();
				});
		}
	}
	$.fn.newPage = function(options){
		var args = $.extend({
				totalPages : 10,
				currentPage : 1,
				callback : function(){}
			},options);
		pager.init(this,args);
	}
})(jQuery);