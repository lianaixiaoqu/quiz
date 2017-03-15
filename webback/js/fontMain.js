require.config({
  baseUrl : '../vendors',
   shim : {   
       // 'jquery-ui' : ['jquery'],
               'bs' : ['jquery'],
         'validate' : ['jquery'],
      'validate-zh' : ['jquery','validate'],
              'bbx' : ['bs'],
         'fullPage' : ['jquery'],
         /*  'upload' : ['jquery'],
           'upload' : ['jquery'],
               'ue' : ['ue-config']*/
  },
  paths : {
           'jquery' : 'jquery/dist/jquery-1.11.3.min',
        //'jquery-ui' : 'jquery-ui/jquery-ui.min',  
               'bs' : 'bootstrap/dist/js/bootstrap.min',
         'validate' : 'validator/jquery.validate.min',
      'validate-zh' : 'validator/messages_cn',
              'bbx' : 'bootbox/bootbox.min',
         'fullPage' : 'http://cdn.bootcss.com/fullPage.js/2.8.2/jquery.fullPage'
           /*'upload' : 'ajaxUpload/ajaxfileupload',*/
  }
});

define(['jquery','bbx','validate-zh'],function($,bootbox){

         var basePath = '/happy_shop_web/front';

         var dataUrl = 'http://10.249.181.188:8080/happyshop';


    return {
      $: $,
      basePath: basePath,
      dataUrl: dataUrl,
      bootbox: bootbox
    }       

});

