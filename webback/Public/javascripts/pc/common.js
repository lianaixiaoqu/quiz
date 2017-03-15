var verify_phone = /^(0|86|17951)?(13[0-9]|15[012356789]|17[4678]|18[0-9]|14[57])[0-9]{8}$/; // 验证手机号码
var verify_id = /^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}([0-9]|X|x)$/;  //身份证验证
var countdown_count = 60; // 倒计时默认时间

$(document).ready(function(){
  // 限制字符串字数
  limitStr();
});

// 验证码按钮倒计时
function countdownCode(btn) {
  var my_countTime = window.setInterval(function() {
    $(btn).attr('disabled', true);
    $(btn).addClass('disabled');
    $(btn).html(countdown_count + '秒后重新获取');
    countdown_count--;
    if (countdown_count == 0) {
      $(btn).attr('disabled', false);
      $(btn).removeClass('disabled');
      $(btn).html('获取验证码');
      window.clearInterval(my_countTime);
      countdown_count = 60;
    }
  }, 1000);
}

// 转换64位
function convertBase64UrlToBlob(urlData){
  var bytes=window.atob(urlData.split(',')[1]); //去掉url的头，并转换为byte
  //处理异常,将ascii码小于0的转换为大于0
  var ab = new ArrayBuffer(bytes.length);
  var ia = new Uint8Array(ab);
  for (var i = 0; i < bytes.length; i++) {
    ia[i] = bytes.charCodeAt(i);
  }
  return new Blob( [ab] , {type : 'image/png'});
}

// 字符串限制字数
function limitStr(){
  $('.limit-str-text').each(function(){
    var $that = $(this);
    var text = $that.html();
    var min_str = $that.data('minstr');
    var new_str = cutStrForNum(text, min_str);

    $that.html(new_str);
  });
}

function cutStrForNum(str, len) {
  var str_length = 0;
  var str_cut = new String();
  var result=str;

  for (var i = 0; i < str.length; i++) {
    a = str.charAt(i);
    str_length ++;
    if (escape(a).length > 4) {
      //中文字符的长度经编码之后大于4
      str_length ++;
    }
    str_cut = str_cut.concat(a);

    if (str_length >= len) {
        str_cut = str_cut.concat('...');
        result=str_cut.toString();
        break;
    }
  }
  return result;
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
