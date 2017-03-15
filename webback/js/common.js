var verify_phone = /^(0|86|17951)?(13[0-9]|15[012356789]|17[4678]|18[0-9]|14[57])[0-9]{8}$/;  // 手机号码验证
var verify_id = /^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}([0-9]|X|x)$/;  //身份证验证
var countdown_count = 60; //倒计时默认时间

$(document).ready(function(){
  // 标签切换
  $('.tab-control').find('.tab-control-tag').on('click', '.item', function(){
    var $that = $(this);
    var i = $that.index();
    var $parent = $that.parents('.tab-control');
    var $tags = $parent.find('.tab-control-tag');
    var $content = $parent.find('.tab-content');

    $tags.find('li').removeClass('active');
    $that.addClass('active');
    $content.hide();
    $content.eq(i).show();
  });
});

//验证码按钮倒计时
function countdown_code(btn) {
  var my_countTime = window.setInterval(function(){
    $(btn).attr('disabled', true);
    $(btn).html(countdown_count + '秒后重新获取');
    countdown_count--;
    if (countdown_count == 0) {
      $(btn).attr('disabled', false);
      $(btn).html('获取验证码');
      window.clearInterval(my_countTime);
      countdown_count = 60;
    }
  },1000);
}

// 倒计时函数
function countDownTimer(countObj, intDiff) {
  window.setInterval(function() {
    var day = 0, hour = 0, minute = 0, second = 0;// 时间默认值
    if (intDiff > 0) {
      day = Math.floor(intDiff / (60 * 60 * 24));
      hour = Math.floor(intDiff / (60 * 60)) - (day * 24);
      minute = Math.floor(intDiff / 60) - (day * 24 * 60) - (hour * 60);
      second = Math.floor(intDiff) - (day * 24 * 60 * 60)
          - (hour * 60 * 60) - (minute * 60);
    }
    if (minute <= 9)
      minute = '0' + minute;
    if (second <= 9)
      second = '0' + second;
    countObj.find('.day-show').html(day);
    countObj.find('.hour-show').html(hour);
    countObj.find('.minute-show').html(minute);
    countObj.find('.second-show').html(second);
    intDiff--;
  }, 1000);
}
