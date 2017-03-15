function requestError(xhr,status) {
	alert('请求失败！');
}

//发送请求
function asyncReq(option,success) {

	var defaultOption = {
		type: 'get',
		cache: false,
		headers: { Accept: '*/*',/*'X-Session': readSession()*/ }
	}

	option = $.extend(defaultOption,option);

	return $.ajax(defaultOption).then(success).fail(requestError);
}

/**
 * 表单验证
 * @param option.form      表单选择器
 * @param option.item      input元素验证规则的json数组
 * @param option.errorCallback  验证失败的回调
 * @param option.errorCallback  验证失败的回调
 */

function submitForm (option) {
	var mValidate = new Mvalidate(option.form),
		validateItem = option.item;
	//设置默认回调
	if(!(option.errorCallback && typeof option.errorCallback === 'function')) {
		option.callback = function(el, errorEl){
			el.focus();
		}
	}
	if (validateItem) {
		validateItem.forEach(function(item,key){
			if(!(item.callback && typeof item.callback === 'function')) {
				item.callback = option.callback;
			}
			mValidate.add(item);
		});
	}
	
	document.querySelector(option.form).addEventListener('submit',function(e){
	    if(mValidate.valid()){
	        //提交
	        asyncReq({
				type: 'post',
				url: '',
				data: $(option.form).serialize(),
			}).then(function(){
				location.assign(option.action);
			});
	    }
	    e.preventDefault();
	})
}





// var dataUrl = 'http://139.219.226.16:8443';
var dataUrl = 'http://88.88.88.2:8443';


//点击按钮出现模态框
function modal_div(){
	$('[data-toggle="modal"]').click(modalShow);
}
//把模态框显示出来
function showModal(modal,data){ 
	modal.on({
	  'show.bs.modal':function(){
	  	if( data.lock == '0' ){
	  		$('#submit').html('<div class="modal-dialog turntable-modal"><div class="modal-inside">\
    	<p class="prompt-language">' + data.condition + '</p>\
    	<img src="images/2.3.1.1微信答题抽奖_03.png" alt="加载失败">\
    	<p class="words">再接再厉！</p>\
    	<div>\
    		<a href="knowledge-answer.html" class="back-index">下次再答</a>\
    		<button class="btn-modal-show" data-dismiss="modal">确定</button>\
    	</div>\
    </div>\
  </div>');
	  	} else if ( data.lock == '3') {
	  		$('#submit').html('<div class="modal-dialog turntable-modal">\
    <div class="modal-inside">\
    	<p>总星数: <span class="number-star">'+ data.currentTotalStars +'</span></p>\
    	<p class="once-congratulations">恭喜英雄获得一次抽奖机会</p>\
    	<img src="images/2.3.1.1微信答题抽奖_03.png" alt="加载失败">\
    	<p class="words">恭喜您！</p>\
    	<div>\
    		<a href="knowledge-answer.html" class="back-index">任性放弃</a>\
    		<button class="btn-modal-show" data-dismiss="modal">试试手气</button>\
    	</div>\
    </div>\
  </div>');
	  	}else if( data.lock == '4' ){
	  		$('#submit').html('<div class="modal-dialog modal-dialog-outside turntable-modal">\
    <div class="modal-inside">\
    	<p class="once-congratulations clearance-awards">恭喜英雄打擂成功，您将获得江湖大礼！</p>\
    	<img src="images/2.3.1.1微信答题抽奖_03.png" alt="加载失败">\
    	<p class="words">果然是高手~</p>\
    	<div class="return-page clearfix">\
    		<a href="personal_ranking.html" class="back_index">功成名就</a>\
    	</div>\
    </div>\
  </div>');
	  	}
	  	$('.back-index').click(function(e){
	  		e.preventDefault();
		  	$.ajax({
				type:'GET',
				dataType:'json',
				url: dataUrl+'/prize?sectionId='+localStorage.sectionId+'&userId='+ localStorage.userId +'&activityId=1',
			}).then(function(data){
				if(data.code == "success"){
					location.href = "knowledge-answer.html";
				}
			})
	  	})

	  },
	  'hidden.bs.modal': function(){
	  	if( data.lock == '0' ){
	  		$('#submit').hide();
	  		$('.start-through1').html('');
	  		$('.start-through1').html('<a href="knowledge-answer.html" class="continue-btn-next">重新闯关</a>')
	  	}else if( data.lock == '3' ){
	  		$('.start-through1').html('');
	  		$('.start-through1').html('<a href="disk-play1.html" class="luck-draw">去抽奖</a><a class="luck-draw luck-continue">继续闯关</a><a class="relax-one">休息一下</a>')
	  		$('#submit').hide();
	  		$('.luck-continue').click(function(e){
			  	$.ajax({
					type:'GET',
					dataType:'json',
					url: dataUrl+'/prize?sectionId='+localStorage.sectionId+'&userId='+ localStorage.userId +'&activityId=1',
				}).then(function(data){
					if(data.code == "success"){
						location.href = "knowledge-answer.html";
					}
				})
		  		e.preventDefault();
		  	})
		  	$('.relax-one').click(function(e){
			  	$.ajax({
					type:'GET',
					dataType:'json',
					url: dataUrl+'/prize?sectionId='+localStorage.sectionId+'&userId='+ localStorage.userId +'&activityId=1',
				}).then(function(data){
					if(data.code == "success"){
						location.href = "knowledge-answer.html";
					}
				})
		  		e.preventDefault();
		  	})
	  	}else if( data.lock == '4' ){
	  		$('.start-through1').html('');
	  		$('.start-through1').html('<a href="knowledge-answer.html" class="continue-btn-next">功成名就</a>')
	  	}
	    modal.remove();
	  }
	}).modal('show');
}

function modalShow(e){
var _this = $(this),
    modalName = _this.attr('modal-name'),
    cache = _this.data();
if(!cache.modal){
  asyncReq({
    url: 'tmpl/modal/' + modalName.substring(1) + '.tmpl'
  },function(data){
    $('body').append(data);
    setTimeout(function(){
      cache.modal = $(modalName);
      showModal(cache.modal,e);
    });
  })
}
}




























