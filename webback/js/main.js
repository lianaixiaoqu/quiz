require.config({
  baseUrl : '../../vendors',
	 shim : {   
       // 'jquery-ui' : ['jquery'],
               'bs' : ['jquery'],
         'validate' : ['jquery'],
      'validate-zh' : ['jquery','validate'],
       'datepicker' : ['jquery',],
    'datepicker-zh' : ['jquery','datepicker'],
           'custom' : ['jquery'],
              'bbx' : ['bs'],
           'upload' : ['jquery'],
           'upload' : ['jquery'],
        'ue-config' : ['validate-zh','bbx','upload','datepicker-zh'],
               'ue' : ['ue-config'],
            'pager' : ['jquery'],
     },
	paths : {
           'jquery' : 'jquery/dist/jquery-1.11.3.min',
        //'jquery-ui' : 'jquery-ui/jquery-ui.min',  
               'bs' : 'bootstrap/dist/js/bootstrap.min',
         'validate' : 'validator/jquery.validate.min',
      'validate-zh' : 'validator/messages_cn',
       'datepicker' : 'bootstrap-datepicker/bootstrap-datepicker.min', 
    'datepicker-zh' : 'bootstrap-datepicker/bootstrap-datepicker.zh-CN.min',
           'custom' : '../js/custom',
              'bbx' : 'bootbox/bootbox.min',
        //'ue-config' : 'http://10.249.181.194:8080/happyshop/uEditor/ueditor.config',
        'ue-config' : 'uEditor/ueditor.config',
              // 'ue' : 'http://10.249.181.194:8080/happyshop/uEditor/ueditor.all.min',
                'ue' : 'uEditor/ueditor.all.min',
           'upload' : 'ajaxUpload/ajaxfileupload',
            'pager' : 'jquery-pager/jquery.page'
	}
});
require(['jquery','bbx','validate-zh','custom','ue','pager'],function($,bootbox){
    $(function(){

        //获取工程名，具体是否需要，还需要看上线之后是否保留
        var src = $('[data-main]')[0].src;
        var basePath = src.substring(0,src.indexOf('/vendors')) + '/back'


        //取数据的基础url
        var dataUrl = decodeURIComponent(window.name);
        if( dataUrl === '' ||dataUrl === 'null' ||dataUrl === 'undefined' ){
            alert('请先登录');
            location.replace('../../login.html');
        }

         //bootbox默认参数
         bootbox.setDefaults({
          size:'small',
          locale: 'zh_CN'
        });
         //导入exel
         $(document).on('submit','#excel-form',function(e){
          var _this = this;
          var formData = new FormData(_this);
          $.ajax({
            url: dataUrl + '/upload',
            type: 'post',
            data: formData,
            contentType: false,
            processData: false,
            statusCode: {
              500: function() {
                bootbox.alert("导入失败");
              }
            }
          })
          .done(function(data) {
              console.log(data);
              var data=JSON.parse(data);
              if(data.code=='error'){
                  bootbox.alert(data.msg);
                  return false;
              }
            formData = null;
            _this.reset();
            
            bootbox.confirm({
              message: "导入成功",
              callback: function(tag){
                if(tag){
                  $.ajax({
                      
                  })
                  .then(function(data){
                    history.go(0);
                  })
                  .fail(requestError);
                }
              }
            });
            
          });
          e.preventDefault();
         });

        //创建分页
        function readyForPager(context,data,pageRoot){
            createPager(data,pageRoot,function(pageNumber){
                requestData(context.dataPath + '?' + $.param($.extend(data.condition,{page: pageNumber})))
                    .then(function(data){
                        context.showData(data);
                    });
            });
        }
        function createPager(data,pageRoot,callback){
            pageRoot = pageRoot || '.pagePageCode';
            $(pageRoot).newPage({
                totalPages: data.totalPages,
                currentPage: 1,
                data: {},
                callback:function(pageNumber){
                    callback(pageNumber-1);
                }
            });
        }

        //请求失败时执行，暂时只做简单处理，同样需要进行session判断
        function requestError(xhr,status,error) {
            bootbox.alert('请求失败');
        }

        //用于后退
        function backward(data) {
          if( data.code == 'success' ) {
                history.back(); 
            } else {
                bootbox.alert(data.msg);
            }
        }
        //用于文本过多时省略显示
        function textEllipsis(selector) {
            var arr = selector.split(",");
            for(var i = 0; i < arr.length ; i++) {
                $(arr[i]).each(function(){
                    $(this).attr('title', $(this).text());
                    if(20 < $(this).text().length) {
                      $(this).html($(this).text().substring(0,20) + "...");
                    }
                });
            }
        }

        /**
         * [用于表单提交]
         * @param  {args.form} jquery对象 [需要提交的表单元素]
         * @param  {args.otherValidate} function [补充一些验证插件无法验证的验证规则]
         * @param  {args.redirectUrl} boolean [基本不用，留空]
         * @param  {args.submitUrl} string [表单的提交地址]
         * @param  {args.getFormData} function [需要提交的表单数据]
         */
        function formSubmit(args) {
            //参数默认值
            var arg = {
                form: $('#submit-form'),
                otherValidate: function() { return true; },
                redirectUrl: false,//这个参数基本不用
                submitUrl: '',
                getFormData: function() {
                    return this.form.serialize();
                }
            };
            arg = $.extend(arg,args);

            arg.form.validate({
                submitHandler : function(){

                    //执行附加验证，若返回false则不进行表单提交
                    if(!arg.otherValidate()) {
                        //在阻止提交前可进行部分其他操作
                        
                        return;
                    };
                    bootbox.confirm({
                      message: "确定要提交数据吗？",
                      callback: function(tag){
                        if(tag){
                          $.ajax({
                              type: "POST",
                              url: arg.submitUrl,
                              dataType: "json",
                              data: arg.getFormData()
                          })
                          .then(function(data){
                              if( typeof arg.submitSuccess === 'function' ){
                                arg.submitSuccess(data);
                              } else {
                                backward(data);
                                bootbox.alert('保存成功')
                              }
                             
                          })
                          .fail(requestError);
                        }
                      }
                    });
                }
            });

        }

    	//ajax配置
    	$.ajaxSetup({cache: false,headers: {Accept: '*/*'},xhrFields:{withCredentials:true}});
    	$(document)
    	.ajaxStart(function(){
			//过渡动画
    		$('.loading-modal').addClass('load');
    	})
    	.ajaxStop(function(){
    		$('.loading-modal').removeClass('load');
    	});

      //验证登陆
      requestData(dataUrl + '/test');
      

      //调用路由
      $(window).on('hashchange',function(e){
          var tplPath =  location.hash.substring(1);
          if(router[tplPath]) {
              router[tplPath].handle(tplPath);
          }
      });

      //加载模板
      function showTpl(tplPath) {
         return $.get(basePath + tplPath)
              .then(function(data){
                //bootbox.alert(data);
              cleanAndShow(data);
              })
              .fail(requestError);
      }

      //模板填充前清除数据
      function cleanAndShow(html,container) {
      var container = container? $(container) : $('.real-content');
          var html = $($.parseHTML(html)).filter(function(){
            return this.nodeType === 1;
          })
          container.empty().html(html);
      }

      //请求数据
      function requestData(url) {
        return $.ajax({
            dataType:'JSON',
            url: url,
            type: 'get'
          })
          .then(function(data){
            if(data.code === 'notLogin'){
              bootbox.alert({message:data.msg,callback:function(){
                location.replace('../../login.html');
              }});
            }
            return data;
          });
      }

      //删除数据
      function deleteData(option){
        if(option.deletable==='false'){
          bootbox.alert('不可删除');
          return;
        }
        var data = {}; 
        data[option.param.key] = option.param.value;
        return $.ajax({
            url: option.url,
            type: 'POST',
            data: data
          })
          //.then(badSession)
          .then(function(data){
              var data=JSON.parse(data);
              $(window).trigger('hashchange');
              if(data.code=='error'){
                  bootbox.alert(data.msg);
              }else{
                  bootbox.alert('删除成功');
              }

          })
          .fail(requestError);      
      }

      var showData,searchPath;
      //初始化页面
      function initPage(options) {
          showData = options.showData;
          searchPath = options.dataPath;
          showTpl(options.tplPath)
          .then(function(){
            //拿数据
            if(!options.dataPath){
              return false;
            }
              return requestData(options.dataPath)
              .then(function(data,status,xhr){
                  sessionStorage.pagerData = JSON.stringify(data);
                  return options.showData(data,status,xhr);
              })
              .fail(requestError);
          })
          .then(function(){
              options.bindEvent();
              if(options.submitParam) {
                formSubmit(options.submitParam);
              }
          });
      }

      //获取数据的接口地址 有接口地址时再行解注
      function getDataPath(url,queryObject){
        if(!url){
            return false;
        }
        if (queryObject){
          return dataUrl + url + '?' + $.param(queryObject);
        } else {
          return dataUrl + url;
        }
      }
      var router = {
          //试题管理
          '/question_bank/question_manage/index.html':{
              dataPath: '/questionbank/show',
              handle: function (tplPath) {
                  initPage({
                      tplPath: tplPath,
                      showData: this.showData,
                      dataPath: getDataPath(this.dataPath),
                      bindEvent: this.bindEvent,
                      submitParam: {
                        otherValidate: function(){

                          return true;
                        },
                        submitUrl: '',
                        getFormData: function() {
                            return this.form.serialize();
                        }
                      }
                  });
              },
              showData: function(data) {
                  //这里填写每个页面如何处理拿到的数据

                  var data = data.data.questionbank.content;
                  var trs = '';
                  for(var i in data){
                     var tds="<tr>";
                      tds += "<td>"+(Number(i)+1)+"</td>";
                      tds += "<td>"+data[i].categoryName+"</td>";
                      tds += "<td>"+data[i].questionContent+"</td>";
                      tds += "<td>"+data[i].serialNumber+"</td>"
                      tds += "<td>"+data[i].typeName+"</td>";
                      tds += "<td>"+data[i].knowledgeName+"</td>";
                      tds += "<td>"+data[i].difficultyName+"</td>";
                      tds += "<td class='text-ellipsis'>\
                            <a href= '#/question_bank/question_manage/modify.html' class='fa fa-lg fa-pencil-square-o' data-role='edit' data-edit-id='"+data[i].id+"' title='编辑'></a>  \
                            <a href='javascript:void(0);' data-role='delete' class='fa fa-trash-o trash' data-key='id' data-delete-id='"+data[i].id+"' data-delete-path='/questionbank/d/" + data[i].id + "' title='删除'></a>\
                             </td>";
                    tds+='</tr>';
                    trs+=tds;
                  }
                $('table.table-striped tbody').html(trs);
              },
            bindEvent: function() {
                //这里填写每个页面需要绑定的一些事件
                var _this = this;
                var data = JSON.parse(sessionStorage.pagerData);
                readyForPager(_this,data.data.questionbank);

                //请求页面上的其他数据
                requestData(getDataPath('/questionbank/item'))
                    .then(function(data){
                        loadSelectOPtion({data:data.data.categories,selector: '[name="category"]',value: 'id',text: 'name'});
                        loadSelectOPtion({data:data.data.knowledge,selector: '[name="knowledge"]',value: 'id',text: 'knowledgeName'});
                    });
                $('#search-form').data({ context: _this, key: 'questionbank' });
            }
        },
          //试题管理编辑
          '/question_bank/question_manage/modify.html': {
              //每个页面对应的数据获取地址
              dataPath: '/questionbank/showlines',
              //页面加载之后需要做的事
              handle: function (tplPath) {
                  initPage({
                      tplPath: tplPath,
                      showData: this.showData,
                      dataPath: getDataPath(this.dataPath,{id:sessionStorage.getItem('editId')}),
                      bindEvent: this.bindEvent,
                      submitParam: {
                         otherValidate: function(){

                          return true;
                        },
                        submitUrl: dataUrl + '/questionbank/update',
                        getFormData: function() {
                            return this.form.serialize();
                        }
                      }
                  });
               },
              showData: function(data) {
                //var dataStatus = data.data.status_load;
                var data = data.data.questionbank;
                $('#phone').val(data[0][0].questionContent);
                $('#middle-name').val(data[0][0].knowledgeName);
                $('.member_sex ').each(function(k,v){
                  $(this).find('option[value="'+ data[0][0].difficulty +'"]').prop('selected',true);
                });
                var type = '';
                for(var i = 0;i<data[1].length;i++){
                  type += '<option value="' + data[1][i].id + '">' + data[1][i].name + '</option>';
                }
                $('.literature_type').append(type);
                $('[name="categoryId"]').each(function(k,v){
                  $(this).find('option[value="'+ data[0][0].categoryId +'"]').prop('selected',true);
                });
                $('[name="type"]').each(function(k,v){
                  $(this).find('option[value="'+ data[0][0].type +'"]').prop('selected',true);
                });

                $('[name="id"]').val(data[0][0].id);
                $('[name="rightAnswer"]').val(data[0][0].rightAnswer);
                $('[name="knowledgeName"]').val(data[0][0].knowledgeName);
                $('[name="serialNumber"]').val(data[0][0].serialNumber);
              },
              bindEvent: function() {
                  //这里填写每个页面需要绑定的一些事件
                  $.ajax({
                    dataType:'JSON',
                    url: dataUrl + '/questionbank/answer' ,
                    type: 'get',
                    data:{id: $('[name="id"]').val()}
                  }).then(function(data){
                    var data = data.data.questionbank[0];
                    $('[name="a"]').val(data.A);
                    $('[name="b"]').val(data.B);
                    $('[name="c"]').val(data.C);
                    $('[name="d"]').val(data.D);
                    $('[name="e"]').val(data.E);
                    $('[name="f"]').val(data.F);
                  })
            }
          },
           //试题管理新增
          '/question_bank/question_manage/insert.html': {
              //每个页面对应的数据获取地址
              dataPath: '/questionbank/item',
              //页面加载之后需要做的事
              handle: function (tplPath) {
                  initPage({
                      tplPath: tplPath,
                      showData: this.showData,
                      dataPath: getDataPath(this.dataPath),
                      bindEvent: this.bindEvent,
                      submitParam: {
                         otherValidate: function(){

                          return true;
                        },
                        submitSuccess: function (data){
                          if(data.data.questionbank.status == "该编号已存在"){
                            bootbox.alert("该编号已存在");
                          }else{
                            history.go(-1);
                            bootbox.alert('保存成功');
                          }
                          
                        },
                        submitUrl:dataUrl+'/questionbank/category',
                        getFormData: function() {
                            return this.form.serialize();
                        }
                      }
                  });
               },
              showData: function(data) {
                var dataStatus = data.data.categories;
                var dataknowledge = data.data.knowledge;
                var type = '';
                for(var i = 0;i<dataStatus.length;i++){
                  type += '<option value="' + dataStatus[i].id + '">' + dataStatus[i].name + '</option>';
                }
                $('.literature_type').append(type);
                var knowledge = '';
                for(var j = 0;j<dataknowledge.length;j++){
                  knowledge += '<option value="' + dataknowledge[j].id + '">' + dataknowledge[j].knowledgeName + '</option>';
                } 
                $('.knowledge').append(knowledge);    
              },
              bindEvent: function() {
                  //这里填写每个页面需要绑定的一些事件 
            }
          },
          //试题分类管理
          '/question_bank/classific_manage/index.html': {
              //每个页面对应的数据获取地址
              dataPath: '/category/show',
              //页面加载之后需要做的事
              handle: function (tplPath) {
                  initPage({
                      tplPath: tplPath,
                      showData: this.showData,
                      dataPath: getDataPath(this.dataPath),
                      bindEvent: this.bindEvent,
                      submitParam: {
                         otherValidate: function(){

                          return true;
                        },
                       // submitUrl:dataUrl+'',
                        getFormData: function() {
                            return this.form.serialize();
                        }
                      }
                  });
               },
              showData: function(data) {
                //var dataStatus = data.data.status_load;
                var data = data.data.category.content;
                  var trs = '';
                  for(var i in data){
                     var tds="<tr>";
                      tds += "<td>"+(Number(i)+1)+"</td>";
                      tds += "<td>"+data[i].name+"</td>";
                      tds += "<td class='text-ellipsis'>\
                            <a href= '#/question_bank/classific_manage/modify.html' class='fa fa-lg fa-pencil-square-o' data-role='edit' data-edit-id='"+data[i].id+"' title='编辑'></a>  \
                            <a href='javascript:void(0);' class='fa fa-trash-o trash delete-zr' data-key='id' data-delete-id='"+data[i].id+"' title='删除'></a>\
                             </td>";
                    tds+='</tr>';
                    trs+=tds;
                  }
                $('table.table-striped tbody').html(trs);

                $('.delete-zr').click(function(){
                  var delete_activoty = $(this).attr('data-delete-id');
                    bootbox.confirm({
                      message: "确定删除吗？",
                      callback: function(tag){
                        if(tag){
                          $.ajax({
                             dataType:'JSON',
                             url: dataUrl +'/category/d/'+ delete_activoty +'',
                             type: 'POST',
                          }).then(function(data){
                            if(data.data.delete == "failure"){
                              bootbox.alert('该分类已被使用，无法删除！');
                            }else{
                              history.go(0);
                            }
                            
                          })
                          .fail(requestError);
                        }
                      }
                    });
                 })
              },
              bindEvent: function() {
                //这里填写每个页面需要绑定的一些事件
                  var _this = this;
                  var data = JSON.parse(sessionStorage.pagerData);
                  readyForPager(_this,data.data.category);

                  //请求页面上的其他数据
                  requestData(getDataPath('/category/showAll'))
                      .then(function(data){
                          loadSelectOPtion({data:data.data.all,selector: '[name="category"]',value: 'id',text: 'name'});
                      });
                  $('#search-form').data({ context: _this, key: 'category' });
            }
          },
          //分类管理编辑
          '/question_bank/classific_manage/modify.html': {
              //每个页面对应的数据获取地址
              dataPath: '/category/showlines',
              //页面加载之后需要做的事
              handle: function (tplPath) {
                  initPage({
                      tplPath: tplPath,
                      showData: this.showData,
                      dataPath: getDataPath(this.dataPath, {id: sessionStorage.getItem('editId')}),
                      bindEvent: this.bindEvent,
                      submitParam: {
                         otherValidate: function(){

                          return true;
                        },
                        submitUrl: dataUrl + '/category/update',
                        getFormData: function() {
                            return this.form.serialize();
                        }
                      }
                  });
               },
              showData: function(data) {
                  var dataId = data.data.id.name;
                  $('[name="categoryName"]').val(dataId.name);
                  $('[name="oldId"]').val(dataId.id);
              },
              bindEvent: function() {
                  //这里填写每个页面需要绑定的一些事件

            }
          },
          //分类管理新增
          '/question_bank/classific_manage/insert.html': {
              //每个页面对应的数据获取地址
              dataPath: '',
              //页面加载之后需要做的事
              handle: function (tplPath) {
                  initPage({
                      tplPath: tplPath,
                      showData: this.showData,
                      dataPath: getDataPath(this.dataPath),
                      bindEvent: this.bindEvent,
                      submitParam: {
                         otherValidate: function(){

                          return true;
                        },
                        submitUrl: dataUrl + '/category',
                        getFormData: function() {
                            return this.form.serialize();
                        }
                      }
                  });
               },
              showData: function(data) {
                
              },
              bindEvent: function() {
                  //这里填写每个页面需要绑定的一些事件 
            }
          },
          //知识点管理
          '/question_bank/knowledge_manage/index.html': {
              //每个页面对应的数据获取地址
              dataPath: '/knowledge/show',
              //页面加载之后需要做的事
              handle: function (tplPath) {
                  initPage({
                      tplPath: tplPath,
                      showData: this.showData,
                      dataPath: getDataPath(this.dataPath),
                      bindEvent: this.bindEvent,
                      submitParam: {
                         otherValidate: function(){

                          return true;
                        },
                       // submitUrl:dataUrl+'',
                        getFormData: function() {
                            return this.form.serialize();
                        }
                      }
                  });
               },
              showData: function(data) {
                  if(data.code!='success'){
                      bootbox.alert(data.msg);
                      return false;
                  }
                var data = data.data.knowledge.content;

                  var trs = '';
                  for(var i in data){
                     var tds="<tr>";
                      tds += "<td>"+(Number(i)+1)+"</td>";
                      tds += "<td>"+data[i].knowledgeName+"</td>";
                      tds += "<td>"+data[i].categoryName+"</td>";
                      tds += "<td class='text-ellipsis'>\
                            <a href= '#/question_bank/knowledge_manage/modify.html' class='fa fa-lg fa-pencil-square-o' data-role='edit' data-edit-id='"+data[i].id+"' title='编辑'></a>  \
                            <a href='javascript:void(0);' data-role='delete' class='fa fa-trash-o trash' data-key='id' data-delete-id='"+data[i].id+"' data-delete-path='/knowledge/d/" + data[i].id + "' title='删除'></a>\
                             </td>";
                    tds+='</tr>';
                    trs+=tds;
                  }
                $('table.table-striped tbody').html(trs);
                 
              },
              bindEvent: function() {
                  //这里填写每个页面需要绑定的一些事件
                  var _this = this;
                  var data = JSON.parse(sessionStorage.pagerData);
                  readyForPager(_this,data.data.knowledge);

                  //请求页面上的其他数据
                  requestData(getDataPath('/questionbank/item'))
                      .then(function(data){
                          loadSelectOPtion({data:data.data.knowledge,selector: '[name="knowledge"]',value: 'id',text: 'knowledgeName'});
                          loadSelectOPtion({data:data.data.categories,selector: '[name="category"]',value: 'id',text: 'name'});
                      });
                  $('#search-form').data({ context: _this, key: 'category' });
            }
          },
          //知识点管理编辑
          '/question_bank/knowledge_manage/modify.html': {
              //每个页面对应的数据获取地址
              dataPath: '/knowledge/showlines',
              //页面加载之后需要做的事
              handle: function (tplPath) {
                  initPage({
                      tplPath: tplPath,
                      showData: this.showData,
                      dataPath: getDataPath(this.dataPath,{id: sessionStorage.getItem('editId')}),
                      bindEvent: this.bindEvent,
                      submitParam: {
                         otherValidate: function(){

                          return true;
                        },
                        submitUrl: dataUrl + '/knowledge/update',
                        getFormData: function() {
                            return this.form.serialize();
                        }
                      }
                  });
               },
              showData: function(data) {
                var dataStatus = data.data.organization[0];
                $('#middle-name').val(dataStatus.knowledgeName);

                var data = data.data.organization[1];
                var category = '';
                  for(var j = 0;j<data.length;j++){
                    category += '<option value="' + data[j].categoryId + '">' + data[j].categoryName + '</option>';
                  } 
                  $('.category').append(category);
                  $('.member_sex').each(function(k,v){
                    $(this).find('option[value="'+ dataStatus.categoryId +'"]').prop('selected',true);
                   });
                  $('[name="oldId"]').val(dataStatus.id);
              },
              bindEvent: function() {
                  //这里填写每个页面需要绑定的一些事件 
            }
          },
          //知识点管理新增
          '/question_bank/knowledge_manage/insert.html': {
              //每个页面对应的数据获取地址
              dataPath: '/category/showAll',
              //页面加载之后需要做的事
              handle: function (tplPath) {
                  initPage({
                      tplPath: tplPath,
                      showData: this.showData,
                      dataPath: getDataPath(this.dataPath),
                      bindEvent: this.bindEvent,
                      submitParam: {
                         otherValidate: function(){

                          return true;
                        },
                        submitUrl: dataUrl + '/knowledge',
                        getFormData: function() {
                            return this.form.serialize();
                        }
                      }
                  });
               },
              showData: function(data) {
                var data = data.data.all;
                var category = '';
                for(var j = 0;j<data.length;j++){
                  category += '<option value="' + data[j].name + '">' + data[j].name + '</option>';
                } 
                $('.literature_type').append(category);
                  
              },
              bindEvent: function() {
                  //这里填写每个页面需要绑定的一些事件 
            }
          },
          //员工管理
          '/staff_bank/staff_management/index.html': {
              //每个页面对应的数据获取地址
              dataPath: '/organizationUser/show',
              //页面加载之后需要做的事
              handle: function (tplPath) {
                  initPage({
                      tplPath: tplPath,
                      showData: this.showData,
                      dataPath: getDataPath(this.dataPath),
                      bindEvent: this.bindEvent,
                      submitParam: {
                         otherValidate: function(){

                          return true;
                        },
                       // submitUrl:dataUrl+'',
                        getFormData: function() {
                            return this.form.serialize();
                        }
                      }
                  });
               },
              showData: function(data) {
                var data = data.data.organizationUser.content;
                var trs = '';
                  for(var i in data){
                     var tds="<tr>";
                      tds += "<td>"+(Number(i)+1)+"</td>";
                      tds += "<td>"+data[i].jobNumber+"</td>";
                      tds += "<td>"+data[i].name+"</td>";
                      tds += "<td>"+data[i].position+"</td>";
                      tds += "<td>"+data[i].organizationId+"</td>";
                      tds += "<td class='text-ellipsis'>\
                            <a href= '#/staff_bank/staff_management/modify.html' class='fa fa-lg fa-pencil-square-o' data-role='edit' data-edit-id='"+data[i].id+"' title='编辑'></a>  \
                            <a href='javascript:void(0);' data-role='delete' class='fa fa-trash-o trash' data-key='id' data-delete-id='"+data[i].id+"' data-delete-path='/organizationUser/d/" + data[i].id + "' title='删除'></a>\
                             </td>";
                    tds+='</tr>';
                    trs+=tds;
                  }
                $('table.table-striped tbody').html(trs);
              },
              bindEvent: function() {
                  //这里填写每个页面需要绑定的一些事件
                  var _this = this;
                  var data = JSON.parse(sessionStorage.pagerData);
                  readyForPager(_this,data.data.organizationUser);
                  $('#search-form').data({ context: _this, key: 'organizationUser' });
            }
          },
          //员工管理编辑
          '/staff_bank/staff_management/modify.html': {
              //每个页面对应的数据获取地址
              dataPath: '/organizationUser/showlines',
              //页面加载之后需要做的事
              handle: function (tplPath) {
                  initPage({
                      tplPath: tplPath,
                      showData: this.showData,
                      dataPath: getDataPath(this.dataPath, {id: sessionStorage.getItem('editId')}),
                      bindEvent: this.bindEvent,
                      submitParam: {
                         otherValidate: function(){

                          return true;
                        },
                        submitUrl: dataUrl + '/organizationUser/update',
                        getFormData: function() {
                            return this.form.serialize();
                        }
                      }
                  });
               },
              showData: function(data) {
                var dataorganization = data.data.organizationUser[0];
                $.each(dataorganization,function(key,value){
                  $('.' + key).val(value);
                });
                var dataStatus = data.data.organizationUser[1];
                var len = dataStatus.length;
                var organizationName = '';
                for(var i=0; i<len;i++){
                  organizationName += '<option value="' + dataStatus[i].organizationId + '">' + dataStatus[i].organizationName + '</option>';
                }
                $('.organization').html(organizationName);
                $('.member_sex').each(function(k,v){
                    $(this).find('option[value="'+ dataorganization.organizationId +'"]').prop('selected',true);
                });
                $('[name="id"]').val(dataorganization.id);
                  
              },
              bindEvent: function() {
                  //这里填写每个页面需要绑定的一些事件 
            }
          },
          //员工管理新增
          '/staff_bank/staff_management/insert.html': {
              //每个页面对应的数据获取地址
              dataPath: '/organizationUser/showall',
              //页面加载之后需要做的事
              handle: function (tplPath) {
                  initPage({
                      tplPath: tplPath,
                      showData: this.showData,
                      dataPath: getDataPath(this.dataPath),
                      bindEvent: this.bindEvent,
                      submitParam: {
                         otherValidate: function(){
                          
                          return true;
                        },
                        submitSuccess: function (data){
                            if(data.data.organizationUser == "fail"){
                              bootbox.alert("工号已存在，请重新输入工号！");
                            }else{
                              history.go(-1);
                              bootbox.alert('保存成功');
                            }
                        },
                        submitUrl: dataUrl + '/organizationUser/create',
                        getFormData: function() {
                            return this.form.serialize();
                        }
                      }
                  });
               },
              showData: function(data) {
                var dataStatus = data.data.organizationUser;
                var organizationName = '';
                var len = dataStatus.length;
                for(var i=0; i<len;i++){
                  organizationName += '<option value="' + dataStatus[i].organizationId + '">' + dataStatus[i].organizationName + '</option>';
                }
                $('.organization').html(organizationName);
                  
              },
              bindEvent: function() {
                  //这里填写每个页面需要绑定的一些事件 
            }
          },
          //机构管理
          '/mechanism_bank/organizational_management/index.html': {
              //每个页面对应的数据获取地址
              dataPath: '/organization/show',
              //页面加载之后需要做的事
              handle: function (tplPath) {
                  initPage({
                      tplPath: tplPath,
                      showData: this.showData,
                      dataPath: getDataPath(this.dataPath),
                      bindEvent: this.bindEvent,
                      submitParam: {
                         otherValidate: function(){

                          return true;
                        },
                       // submitUrl:dataUrl+'',
                        getFormData: function() {
                            return this.form.serialize();
                        }
                      }
                  });
               },
              showData: function(data) {
                var data = data.data.organization.content;
                var trs = '';
                  for(var i in data){
                     var tds="<tr>";
                      tds += "<td>"+(Number(i)+1)+"</td>";
                      tds += "<td>"+data[i].organizationId+"</td>";
                      tds += "<td>"+data[i].organizationName+"</td>";
                      tds += "<td>"+data[i].organizationLevel+"</td>";
                      tds += "<td>"+data[i].organizationLevelName+"</td>";
                      tds += "<td>"+data[i].parentId+"</td>";
                      tds += "<td class='text-ellipsis'>\
                            <a href= '#/mechanism_bank/organizational_management/modify.html' class='fa fa-lg fa-pencil-square-o' data-role='edit' data-edit-id='"+data[i].id+"' title='编辑'></a>  \
                            <a href='javascript:void(0);' data-role='delete' class='fa fa-trash-o trash' data-key='id' data-delete-id='"+data[i].id+"' data-delete-path='/organization/d/" + data[i].id + "' title='删除'></a>\
                             </td>";
                    tds+='</tr>';
                    trs+=tds;
                  }
                $('table.table-striped tbody').html(trs);
                 
              },
              bindEvent: function() {
                  //这里填写每个页面需要绑定的一些事件
                  var _this = this;
                  var data = JSON.parse(sessionStorage.pagerData);
                  readyForPager(_this,data.data.organization);
                  $('#search-form').data({ context: _this, key: 'organization' });
            }
          },
          //机构管理编辑
          '/mechanism_bank/organizational_management/modify.html': {
              //每个页面对应的数据获取地址
              dataPath: '/organization/showlines',
              //页面加载之后需要做的事
              handle: function (tplPath) {
                  initPage({
                      tplPath: tplPath,
                      showData: this.showData,
                      dataPath: getDataPath(this.dataPath, {id: sessionStorage.getItem('editId')}),
                      bindEvent: this.bindEvent,
                      submitParam: {
                         otherValidate: function(){
                          return true;
                        },
                        submitUrl: dataUrl + '/organization/update',
                        getFormData: function() {
                            return this.form.serialize();
                        }
                      }
                  });
               },
              showData: function(data) {
                var dataStatus = data.data.organization[1];
                var data= data.data.organization[0];
                var len = dataStatus.length;
                var organizationParent = '';
                for(var j=0; j<len;j++){
                  organizationParent += '<option value="' + dataStatus[j].parentId + '">' + dataStatus[j].parentId + '</option>';
                }
                $('.organization_parent').html(organizationParent); 
                $('.organization_parent').each(function(k,v){
                    $(this).find('option[value="'+ data.parentId +'"]').prop('selected',true);
                });

                $('.organizationId').val(data.organizationId);
                $('.organizationLevelName').val(data.organizationLevelName);
                $('.organizationLevel').val(data.organizationLevel);
                $('.organizationName').val(data.organizationName);
                $('.organizationPath').val(data.organizationPath);
                $('[name="id"]').val(data.id);


              },
              bindEvent: function() {
                  //这里填写每个页面需要绑定的一些事件 
            }
          },
          //机构管理新增
          '/mechanism_bank/organizational_management/insert.html': {
              //每个页面对应的数据获取地址
              dataPath: '/organization/showAll',
              //页面加载之后需要做的事
              handle: function (tplPath) {
                  initPage({
                      tplPath: tplPath,
                      showData: this.showData,
                      dataPath: getDataPath(this.dataPath),
                      bindEvent: this.bindEvent,
                      submitParam: {
                         otherValidate: function(){
                             
                             return true;
                        },
                        submitSuccess: function (data){
                          if(data.data.organization.status == "1"){
                            bootbox.alert("组织ID已存在，请重新输入组织ID！");
                          }else{
                            history.go(-1);
                            bootbox.alert('保存成功');
                          }
                          
                        },
                        submitUrl: dataUrl + '/organization/create',
                        getFormData: function() {
                            return this.form.serialize();

                        }
                      }
                  });
               },
              showData: function(data) {
                var dataStatus = data.data.organization;
                var len = dataStatus.length;
                var organizationName = '';
                for(var i=0; i<len;i++){
                  organizationName += '<option value="' + dataStatus[i].organizationId + '">' + dataStatus[i].organizationName + '</option>';
                }
                $('.organization').html(organizationName);
                var organizationParent = '';
                for(var j=0; j<len;j++){
                  organizationParent += '<option value="' + dataStatus[j].parentId + '">' + dataStatus[j].parentId + '</option>';
                }
                $('.organization_parent').html(organizationParent); 
                  
              },
              bindEvent: function() {
                  //这里填写每个页面需要绑定的一些事件 
            }
          },
          //活动管理
          '/activity_bank/activity_management/index.html': {
              //每个页面对应的数据获取地址
              dataPath: '/activity/showActivity',
              //页面加载之后需要做的事
              handle: function (tplPath) {
                  initPage({
                      tplPath: tplPath,
                      showData: this.showData,
                      dataPath: getDataPath(this.dataPath),
                      bindEvent: this.bindEvent,
                      submitParam: {
                         otherValidate: function(){
                          return true;
                        },
                       // submitUrl:dataUrl+'',
                        getFormData: function() {
                            return this.form.serialize();
                        }
                      }
                  });
               },
              showData: function(data) {
                var data = data.data.activityName.content;
                var trs = '';
                  for(var i in data){
                     var tds="<tr>";
                      tds += "<td>"+(Number(i)+1)+"</td>";
                      tds += "<td>"+data[i].name+"</td>";
                      tds += "<td>"+data[i].description+"</td>";
                      tds += "<td>"+data[i].startTime+"</td>";
                      tds += "<td>"+data[i].endTime+"</td>";
                      tds += "<td class='text-ellipsis'>\
                            <a href= '#/activity_bank/activity_management/modify.html' class='fa fa-lg fa-pencil-square-o' data-role='edit' data-edit-id='"+data[i].id+"' title='编辑'></a>\
                            <a href='javascript:void(0);' class='fa fa-trash-o trash delete-zr' data-key='id' data-delete-id='"+data[i].id+"'  title='删除'></a>\
                            </td>";
                    tds+='</tr>';
                    trs+=tds;
                  }
                $('table.table-striped tbody').html(trs); 


                $('.delete-zr').click(function(){
                  var delete_activoty = $(this).attr('data-delete-id');
                    bootbox.confirm({
                      message: "确定删除吗？",
                      callback: function(tag){
                        if(tag){
                          $.ajax({
                            dataType:'JSON',
                            url: dataUrl +'/activity/d/'+ delete_activoty +'',
                            type: 'POST', 
                          }).then(function(data){
                            if(data.data.delete.status == "failure"){
                              bootbox.alert('该活动为当前活动！不可删除');
                            }else{
                              history.go(0);
                            }
                          })
                          .fail(requestError);
                        }
                      }
                    });
                })
              },
              bindEvent: function() {
                //这里填写每个页面需要绑定的一些事件
                var _this = this;
                var data = JSON.parse(sessionStorage.pagerData);
                readyForPager(_this,data.data.activityName);
                $('#search-form').data({ context: _this, key: 'activityName' });



            }
          },
          //活动管理编辑
          '/activity_bank/activity_management/modify.html': {
              //每个页面对应的数据获取地址
              dataPath: '/activity/showlines',
              //页面加载之后需要做的事
              handle: function (tplPath) {
                  initPage({
                      tplPath: tplPath,
                      showData: this.showData,
                      dataPath: getDataPath(this.dataPath, {id: sessionStorage.getItem('editId')}),
                      bindEvent: this.bindEvent,
                      submitParam: {
                         otherValidate: function(){
                          return true;
                        },
                        submitUrl: dataUrl + '/activity/update',
                        getFormData: function() {
                            return this.form.serialize();
                        }
                      }
                  });
               },
              showData: function(data) {
                var dataValue = data.data.activity;
                $.each(dataValue,function(key,value){
                  $('.' + key).val(value);
                });
                $('[name="isCurrent"]').each(function(k,v){
                  $(this).find('option[value="'+ dataValue.isCurrent +'"]').prop('selected',true);
                });
                var dataStatus = data.data.categories;
                var dataknowledge = data.data.knowledge;
                var type = '';
                for(var i = 0;i<dataStatus.length;i++){
                  type += '<option value="' + dataStatus[i].id + '">' + dataStatus[i].name + '</option>';
                }

                $('.literature_type').append(type);
                var knowledge = '';
                for(var j = 0;j<dataknowledge.length;j++){
                  knowledge += '<option value="' + dataknowledge[j].categoryId + '">' + dataknowledge[j].knowledgeName + '</option>';
                } 
                $('.knowledge').append(knowledge);
                $('[name="id"]').val(dataValue.id); 

                if( data.data.lock == "1" ){
                  $('#zong-chapter').attr('disabled','disabled');
                  $('#every-chapter').attr('disabled','disabled');
                  $('#zong-question').attr('disabled','disabled');
                  $('#zong-time').attr('disabled','disabled');
                  $('.literature_type').attr('disabled','disabled');
                  $('#cate').attr('disabled','disabled');
                  $('.knowledge').attr('disabled','disabled');
                }
                  
              },
              bindEvent: function() {
                //这里填写每个页面需要绑定的一些事件
                $('[name="isCurrent"]').change(function(){
                  if($('[name="isCurrent"]').val() == "1"){
                    bootbox.alert('如果设置本活动为当前活动，那么现在的当前活动将会失效');
                  }
                })



                $.ajax({
                  dataType:'JSON',
                  url: dataUrl +'/activity/showCategory',
                  type: 'get',
                  data:{id:$('[name="id"]').val()}
                }).then(function(data){
                  $('#classic').each(function(k,v){
                    $(this).find('option[value="'+ data.data.activity.id +'"]').prop('selected',true);
                  });
                });




                        var text='';
                          $('#sel_cate').on('change',function(){
                              text+=' '+$('#sel_cate>option:selected').text();
                              $('#cate').val(text);
                          });

                          $(".datepicker").datepicker({
                              language: "zh-CN",
                              autoclose: true,//选中之后自动隐藏日期选择框
                              clearBtn: true,//清除按钮
                              todayBtn: true,//今日按钮
                              format: "yyyymmdd"//日期格式，详见 http://bootstrap-datepicker.readthedocs.org/en/release/options.html#format
                          });

                          $('#endTime').blur(function(){//截至时间必须大于开始时间
                              setTimeout(function(){
                                  var endTime=$('#endTime').val();
                          var startTime=$('#startTime').val();
                          var differData=parseInt(endTime)-parseInt(startTime);
                          if(differData<0){
                              $('#endTime').val('截至时间必须大于等于开始时间');
                          }
                      },500);
                  });


                  $('#classic').on('change',function(){
                      var val=$('#classic option:selected').val();//选中的文本
                      $.ajax({
                          dataType:'JSON',
                          url: dataUrl +'/activity/showKnowledge',
                          type: 'get',
                          data:{categerId:val}
                      }).then(function(data){
                          var code=data.code;
                          if(code=='success'){
                              var activity=data.data.activity;
                              var html='';
                              if(activity.length) {
                                  for (var i = 0; i < activity.length; i++) {
                                      var knowledgeName = activity[i].knowledgeName;
                                      html += '<option data-infor="'+knowledgeName+'" value="' + activity[i].categoryId + '">' + knowledgeName + '</option>';
                                  }
                                  $('.knowledge').html(html);
                              }else{
                                  $('.knowledge').html(' ');
                              }
                          }
                      })
                  });
            }
          },
          //活动管理新增
          '/activity_bank/activity_management/insert.html': {
              //每个页面对应的数据获取地址
              dataPath: '/questionbank/item',
              //页面加载之后需要做的事
              handle: function (tplPath) {
                  initPage({
                      tplPath: tplPath,
                      showData: this.showData,
                      dataPath: getDataPath(this.dataPath),
                      bindEvent: this.bindEvent,
                      submitParam: {
                        otherValidate: function(){

                          return true;
                        },
                        submitUrl: dataUrl + '/activity',
                        getFormData: function() {
                            return this.form.serialize();
                        }
                      }
                  });
               },
              showData: function(data) {
                var dataStatus = data.data.categories;
                var dataknowledge = data.data.knowledge;
                var type = '';
                for(var i = 0;i<dataStatus.length;i++){
                  type += '<option value="' + dataStatus[i].id + '">' + dataStatus[i].name + '</option>';
                }
                $('.literature_type').append(type);
                var knowledge = '';
                for(var j = 0;j<dataknowledge.length;j++){
                  knowledge += '<option value="' + dataknowledge[j].categoryId + '">' + dataknowledge[j].knowledgeName + '</option>';
                }
                // $('.knowledge').append(knowledge);
                  
              },
              bindEvent: function() {
                  //这里填写每个页面需要绑定的一些事件
                $('[name="isCurrent"]').change(function(){
                  if($('[name="isCurrent"]').val() == "1"){
                    bootbox.alert('如果设置本活动为当前活动，那么现在的当前活动将会失效');
                  }
                })




                  var text='';
                  $('#sel_cate').on('change',function(){
                      text+=' '+$('#sel_cate>option:selected').text();
                      $('#cate').val(text);
                  });

                  $(".datepicker").datepicker({
                      language: "zh-CN",
                      autoclose: true,//选中之后自动隐藏日期选择框
                      clearBtn: true,//清除按钮
                      todayBtn: true,//今日按钮
                      format: "yyyymmdd"//日期格式，详见 http://bootstrap-datepicker.readthedocs.org/en/release/options.html#format
                  });

                  $('#endTime').blur(function(){//截至时间必须大于开始时间
                      setTimeout(function(){
                          var endTime=$('#endTime').val();
                          var startTime=$('#startTime').val();
                          var differData=parseInt(endTime)-parseInt(startTime);
                          if(differData<0){
                              $('#endTime').val('截至时间必须大于等于开始时间');
                          }
                      },500);
                  });


                  $('#classic').on('change',function(){
                      var val=$('#classic option:selected').val();//选中的文本
                      $.ajax({
                          dataType:'JSON',
                          url: dataUrl +'/activity/showKnowledge',
                          type: 'get',
                          data:{categerId:val}
                      }).then(function(data){
                          var code=data.code;
                          if(code=='success'){
                              var activity=data.data.activity;
                              var html='';
                              if(activity.length) {
                                  for (var i = 0; i < activity.length; i++) {
                                      var knowledgeName = activity[i].knowledgeName;
                                      html += '<option data-infor="'+knowledgeName+'" value="' + activity[i].categoryId + '">' + knowledgeName + '</option>';
                                  }
                                  $('.knowledge').html(html);
                              }else{
                                  $('.knowledge').html(' ');
                              }
                          }
                      })
                  });
            }
          },
          //关卡配置
          '/level_configuration/level_management/detaile.html': {
              //每个页面对应的数据获取地址
              dataPath: '/activity/showAll',
              //页面加载之后需要做的事
              handle: function (tplPath) {
                  initPage({
                      tplPath: tplPath,
                      showData: this.showData,
                      dataPath: getDataPath(this.dataPath),
                      bindEvent: this.bindEvent,
                      submitParam: {
                         otherValidate: function(){
                          var hard = $('[name="hardPerSection"]').val();
                          var normal = $('[name="normalPerSection"]').val();
                          var easy = $('[name="easyPerSection"]').val();
                          var sum = $('[name="questionsPerSection"]').val();
                          if(Number(normal)+Number(easy)+Number(hard) != Number(sum)){
                            bootbox.confirm({
                              message: "每关难中易题数总和需要等于每关总题数！",
                              callback: function(tag){
                                if(tag){
                                  $.ajax({
                                      
                                  })
                                  .then(function(data){
                                      
                                  })
                                  .fail(requestError);
                                }
                              }
                            });
                            return false;
                          }
                          return true;
                        },
                        submitSuccess: function (data){
                          if(data.code != "success"){
                            bootbox.confirm({
                              message: data.msg,
                              callback: function(tag){
                                if(tag){
                                  $.ajax({
                                      
                                  })
                                  .then(function(data){
                                      
                                  })
                                  .fail(requestError);
                                }
                              }
                            });
                            return false;
                          }else{
                            history.go(0);
                          }
                        },
                        submitUrl: dataUrl + '/questionsheet/generate',
                        getFormData: function() {
                            return this.form.serialize();
                        }
                      }
                  });
               },
              showData: function(data) {
                 var dataactivity = data.data.activity;
                 var dataactivities = data.data.activities;
                 $.each(dataactivity,function(key,value){
                  $('.' + key ).val(value);
                 });
                 var activities = '';
                  for(var i = 0;i<dataactivities.length;i++){
                    activities += '<option value="' + dataactivities[i].id + '">' + dataactivities[i].name + '</option>';
                  }
                  $('[name="activityId"]').append(activities);
                 $('[name="activityId"]').each(function(k,v){
                    $(this).find('option[value="'+ dataactivity.id +'"]').prop('selected',true);
                  });
                 $('[name="activityId"]').change(function(){
                  $.ajax({
                    dataType:'JSON',
                    url: dataUrl +'/activity/showAll',
                    type: 'get',
                    data:{activityId:$('[name="activityId"]').val()}
                  }).then(function(data){
                    $('[name="activityId"]').html('');
                   var dataactivity1 = data.data.activity;
                   var dataactivities1 = data.data.activities;
                   $.each(dataactivity1,function(key,value){
                    $('.' + key ).val(value);
                   });
                   var activities = '';
                    for(var i = 0;i<dataactivities1.length;i++){
                      activities += '<option value="' + dataactivities1[i].id + '">' + dataactivities1[i].name + '</option>';
                    }
                    $('[name="activityId"]').append(activities);
                   $('[name="activityId"]').each(function(k,v){
                      $(this).find('option[value="'+ dataactivity1.id +'"]').prop('selected',true);
                    });
                  })
                 })
                  
              },
              bindEvent: function() {
                //这里填写每个页面需要绑定的一些事件
                $.ajax({
                  dataType:'JSON',
                  url: dataUrl +'/questionsheet/show',
                  type: 'get',
                  data:{activityId:$('[name="activityId"]').val()}
                }).then(function(data){
                  var data = data.data.show;
                  if(data.status == "0"){
                    $('[name="normalPerSection"]').val('');
                    $('[name="easyPerSection"]').val('');
                    $('[name="hardPerSection"]').val('');
                    $('[name="scaleScore"]').html('');
                  }else{
                    $('[name="normalPerSection"]').val(data.normalPerSection);
                    $('[name="easyPerSection"]').val(data.easyPerSection);
                    $('[name="hardPerSection"]').val(data.hardPerSection);
                    $('[name="scaleScore"]').html('<option value="'+ data.sorce +'">'+ data.sorce +'</option>');
                  } 
                })

                $('[name="activityId"]').change(function(){
                  var _this = $(this);
                  $.ajax({
                    dataType:'JSON',
                    url: dataUrl +'/questionsheet/show',
                    type: 'get',
                    data:{activityId:_this.val()}
                  }).then(function(data){
                    var data = data.data.show;
                    if(data.status == "0"){
                      $('[name="normalPerSection"]').val('');
                      $('[name="easyPerSection"]').val('');
                      $('[name="hardPerSection"]').val('');
                      $('[name="scaleScore"]').html('');
                    }else{
                      $('[name="normalPerSection"]').val(data.normalPerSection);
                      $('[name="easyPerSection"]').val(data.easyPerSection);
                      $('[name="hardPerSection"]').val(data.hardPerSection);
                      $('[name="scaleScore"]').html('<option value="'+ data.sorce +'">'+ data.sorce +'</option>');
                    } 
                  })
                })




                  var a=[];
                  var i=0;
                  var questionNum=$('#questionName').val();
                  $('.question-how').blur(function(){
                      if($(this).val()){
                         var id=$(this).attr('id');
                              for( key in a){
                                  if(key==id){
                                      i--;
                                      a[key]=$(this).val();
                                  }
                              }
                          a[id]=$(this).val();
                          i++;
                      }
                        if(i==3){
                            $.ajax({
                                dataType:'JSON',
                                url: dataUrl + '/activity/check',
                                type: 'get',
                                data:{difficult:$('#difficult-name').val(),normal:$('#normal-name').val(),easy:$('#easy-name').val()},
                            }).then(function(data){
                                var code=data.code;
                                $('select#selectEasyDifficult').html('');
                                if(code=="success"){
                                    var activitys=data.data.activitys;
                                    var html='';
                                    for(var key in activitys){
                                        var value=activitys[key].join(',');
                                        html += '<option value="' + value + '">' + value + '</option>';
                                    }
                                    $('select#selectEasyDifficult').append(html);
                                }
                            })
                        }
                  });
                  var data={};
                  var totalPass=$('#totalPass').val();
                  var containPass=$('#containPass').val();
                  data={
                      totalPass:totalPass,
                      containPass:containPass
                  }
                  sessionStorage.setItem(data,data);
            }
          },
          //过关条件配置 activity/showOne
          '/condition_configuration/condition_configuration/index.html': {
              //每个页面对应的数据获取地址
              dataPath: '/activity/showSum',
              //页面加载之后需要做的事
              handle: function (tplPath) {
                  initPage({
                      tplPath: tplPath,
                      showData: this.showData,
                      dataPath: getDataPath(this.dataPath),
                      bindEvent: this.bindEvent,
                      submitParam: {
                          otherValidate: function(){
                            
                            return true;
                          },
                          submitSuccess: function (data){
                              history.go(0);
                          },
                          submitUrl:  dataUrl +'/chapter/setChapter',
                          getFormData: function() {
                              // return this.form.serialize();
                             var data= this.form.serialize();
                              var dataArr=data.split('&');
                              var saveData0=dataArr[0].split('=');
                                                            console.log(data);

                              var a=[];
                              var arr=[];
                              for(var i=2;i<dataArr.length-1;i+=4){
                                  var average=dataArr[i].split('=');
                                  var sum=dataArr[i+1].split('=');
                                  var under=dataArr[i+2].split('=');
                                  var less=dataArr[i+3].split('=');
                                  console.log(average);
                                  var saveData1={
                                      'averageStart':average[1],
                                      'sumStart':sum[1],
                                      'underStart':under[1],
                                      'lessChapter':less[1]
                                  };
                                  a.push(JSON.stringify(saveData1));
                              }
                              a.unshift(saveData0[1]);
                              var arr={'arr':a.join('&'),'activityId':$('#activity_value').attr('data-target')};
                              return arr;
                          }
                      }
                  });
              },
              showData: function(data) {
                  var activity = data.data.activity;
                  var chapters=activity.chapters;
                  var id=activity.id;
                  var html='';
                  var i=0;
                  while(i<chapters){
                      html+='<tr class="table'+ i +'">';
                      html+='<td class="condi_serial_number">序号 '+(Number(i)+1)+'</td>';
                      html+='<td class="condi_secction"><input  number="number" name="chapterId" value="'+(Number(i)+1)+'" disabled ></td>';
                      html+='<td class="condi_average_stars"><input  number="number" name="averageStart" style="text-align: center;"></td>';
                      html+='<td class="condi_total_stars"><input number="number" name="sumStart" style="text-align: center;"></td>';
                      html+='<td class="condi_low_stars"><input number="number" name="underStart" style="text-align: center;"></td>';
                      html+='<td class="condi_close"><input number="number" name="lessChapter" style="text-align: center;"></td>';
                      // html+='<td class="condi_prise_confi" data-i="'+i+'"><a href="#/price_configuration/price_configuration/detail.html" class="fa fa-cog setting"></a></td>';
                      // html+='<td class="condi_close"><button type="submit" class="btn btn-success"><i class="fa fa-floppy-o" aria-hidden="true"></i>保存</button></td>';
                      html+='</tr>';
                      i++;
                  }
                  $('[name="chapterSome"]').val(chapters);
                  $('#activity_value').attr('data-target',id);
                  $('.confi_tbody').html(html);

                  var dataArr = data.data.activitys;
                  var html = '';
                  for(var j=0; j<dataArr.length;j++){
                    html += '<option value="' + dataArr[j].id + '">' + dataArr[j].name + '</option>';
                  }
                  $('[name="activityId"]').html(html);
                  $('[name="activityId"]').each(function(k,v){
                    $(this).find('option[value="'+ activity.id +'"]').prop('selected',true);
                  });
              },
              bindEvent: function() {
                //这里填写每个页面需要绑定的一些事件
                $.ajax({
                  dataType:'JSON',
                  url:dataUrl + '/chapter/showAll',
                  type: 'get',
                }).then(function(data){
                  var data = data.data.chapter;
                  for(var i=0;i<data.length;i++ ){
                      $('.table' + i + ' .condi_average_stars input').val(data[i].avgStar);
                      $('.table' + i + ' .condi_total_stars input').val(data[i].sumStar);
                      $('.table' + i + ' .condi_low_stars input').val(data[i].lessStar);
                      $('.table' + i + ' .condi_close input').val(data[i].section);
                  }
                })
                $('[name="activityId"]').change(function(){
                  var _this = $(this);
                  $.ajax({
                    dataType:'JSON',
                    url:dataUrl + '/activity/showSum',
                    type: 'get',
                    data:{activityId:_this.val()}
                  }).then(function(data){
                    var activity = data.data.activity;
                    var chapters = activity.chapters;
                    var id=activity.id;
                    $('.confi_tbody').html('');
                    var html='';
                    var i=0;  
                    while(i<chapters){
                        html+='<tr class="table'+ i +'">';
                        html+='<td class="condi_serial_number">序号 '+(Number(i)+1)+'</td>';
                        html+='<td class="condi_secction"><input  number="number" name="chapterId" value="'+(Number(i)+1)+'" disabled ></td>';
                        html+='<td class="condi_average_stars"><input number="number" name="averageStart" style="text-align: center;"></td>';
                        html+='<td class="condi_total_stars"><input class="table'+ i +'" number="number" name="sumStart" style="text-align: center;"></td>';
                        html+='<td class="condi_low_stars"><input class="table'+ i +'" number="number" name="underStart" style="text-align: center;"></td>';
                        html+='<td class="condi_close"><input class="table'+ i +'" number="number" name="lessChapter" style="text-align: center;"></td>';
                        // html+='<td class="condi_prise_confi" data-i="'+i+'"><a href="#/price_configuration/price_configuration/detail.html" class="fa fa-cog setting"></a></td>';
                        // html+='<td class="condi_close"><button type="submit" class="btn btn-success"><i class="fa fa-floppy-o" aria-hidden="true"></i>保存</button></td>';
                        html+='</tr>';
                        i++;
                    }
                    $('[name="chapterSome"]').val(chapters);
                    $('#activity_value').attr('data-target',id);
                    $('.confi_tbody').html(html);

                    var dataArr = data.data.activitys;
                    var html = '';
                    for(var j=0; j<dataArr.length;j++){
                      html += '<option value="' + dataArr[j].id + '">' + dataArr[j].name + '</option>';
                    }
                    $('[name="activityId"]').html(html);
                    $('[name="activityId"]').each(function(k,v){
                      $(this).find('option[value="'+ activity.id +'"]').prop('selected',true);
                    });
                    return $.ajax({
                              dataType:'JSON',
                              url:dataUrl + '/chapter/showAll',
                              type: 'get',
                              data:{activityId:_this.val()}
                            })
                  }).then(function(data){
                    var dataChepter = data.data.chapter;
                    for(var i=0;i<dataChepter.length;i++ ){
                      $('.table' + i + ' .condi_average_stars input').val(dataChepter[i].avgStar);
                      $('.table' + i + ' .condi_total_stars input').val(dataChepter[i].sumStar);
                      $('.table' + i + ' .condi_low_stars input').val(dataChepter[i].lessStar);
                      $('.table' + i + ' .condi_close input').val(dataChepter[i].section);
                    }
                  });
                });
              }
          },
          //奖品设置
          '/price_configuration/price_configuration/detail.html': {
              //每个页面对应的数据获取地址
              dataPath: '/activity/showOne',
              //页面加载之后需要做的事
              handle: function (tplPath) {
                  initPage({
                      tplPath: tplPath,
                      showData: this.showData,
                      dataPath: getDataPath(this.dataPath),
                      bindEvent: this.bindEvent,
                      submitParam: {
                          otherValidate: function(){

                              return true;
                          },
                          submitSuccess: function (data){
                            history.go(0);
                          },
                          submitUrl: dataUrl + '/prize/insert',
                          getFormData: function() {
                              return this.form.serialize();
                          }
                      }
                  });
              },
              showData: function(data) {
                var activities = data.data.activities;
                var dataactivityEntity = data.data.activityEntity;
                  var html = '';
                  for(var j=0; j<dataactivityEntity.chapters;j++){
                    html += '<option value="' + (Number(j)+1) + '">' + (Number(j)+1) + '</option>';
                  }
                $('#chapter_section').html(html);
                var activity = '' ;
                for(var i=0; i<activities.length;i++){
                    activity += '<option value="' + activities[i].id + '">' + activities[i].name + '</option>';
                }
                $('[name="activityId"]').append(activity);
                $('#activity_section').each(function(k,v){
                  $(this).find('option[value="'+ dataactivityEntity.id +'"]').prop('selected',true);
                });
                $('[name="activityId"]').change(function(){
                  $.ajax({
                    dataType:'JSON',
                    url:dataUrl + '/activity/showOne',
                    type: 'get',
                    data:{activityId:$('[name="activityId"]').val()}
                  }).then(function(data){
                    $('[name="activityId"]').html('');
                    var activities1 = data.data.activities;
                    var dataactivityEntity = data.data.activityEntity;
                    var html = '';
                    for(var j=0; j<dataactivityEntity.chapters;j++){
                      html += '<option value="' + (Number(j)+1) + '">' + (Number(j)+1) + '</option>';
                    }
                    $('#chapter_section').html(html);
                    var activity1 = '' ;
                    for(var i=0; i<activities1.length;i++){
                        activity1 += '<option value="' + activities1[i].id + '">' + activities1[i].name + '</option>';
                    }
                    $('[name="activityId"]').append(activity1);
                    $('#activity_section').each(function(k,v){
                      $(this).find('option[value="'+ dataactivityEntity.id +'"]').prop('selected',true);
                    });
                    return $.ajax({
                                dataType:'JSON',
                                url:dataUrl + '/prize/showlines',
                                type: 'get',
                                data:{chapterId:$('#chapter_section').val(),activityId:$('[name="activityId"]').val()}
                              })
                  }).then(function(data){
                    var dataArr = data.data.prize;
                      if(!dataArr.length){
                        $('.first-prize').val('');
                        $('[name="first_id"]').val('');
                        $('[name="second_id"]').val('');
                        $('[name="third_id"]').val('');
                      }else{
                        for( var i=0;i<dataArr.length;i++ ){
                            $('.table'+ i).val(dataArr[i].content);
                            $('.input'+ i).val(dataArr[i].totalAmount);
                            $('[name="first_id"]').val(dataArr[0].id);
                            $('[name="second_id"]').val(dataArr[1].id);
                            $('[name="third_id"]').val(dataArr[2].id);
                        }
                      }
                  });
                })
              },
              bindEvent: function() {
                //这里填写每个页面需要绑定的一些事件
                $.ajax({
                  dataType:'JSON',
                  url:dataUrl + '/prize/showlines',
                  type: 'get',
                  data:{chapterId:$('#chapter_section').val(),activityId:$('[name="activityId"]').val()}
                }).then(function(data){
                  var dataVlaue = data.data.prize;
                  for( var i=0;i<dataVlaue.length;i++ ){
                    if(dataVlaue.length == 0 ){
                      $('.first-prize').val('');
                      $('[name="first_id"]').val('');
                      $('[name="second_id"]').val('');
                      $('[name="third_id"]').val('');
                    }else{
                      $('.table'+ i).val(dataVlaue[i].content);
                      $('.input'+ i).val(dataVlaue[i].totalAmount);
                      $('[name="first_id"]').val(dataVlaue[0].id);
                      $('[name="second_id"]').val(dataVlaue[1].id);
                      $('[name="third_id"]').val(dataVlaue[2].id);
                    }
                  }
                });
                $('#chapter_section').change(function(){
                    $.ajax({
                      dataType:'JSON',
                      url:dataUrl + '/prize/showlines',
                      type: 'get',
                      data:{chapterId:$('#chapter_section').val(),activityId:$('[name="activityId"]').val()}
                    }).then(function(data){
                      var data = data.data.prize;
                      for(var i=0;i<=data.length;i++){
                        if(data.length == 0 ){
                          $('.first-prize').val('');
                          $('[name="first_id"]').val('');
                          $('[name="second_id"]').val('');
                          $('[name="third_id"]').val('');
                        }else{
                          $('.table'+ i).val(data[i].content);
                          $('.input'+ i).val(data[i].totalAmount);
                          $('[name="first_id"]').val(data[0].id);
                          $('[name="second_id"]').val(data[1].id);
                          $('[name="third_id"]').val(data[2].id);
                        }
                      }
                    })
                })  
              }
          },
          //答题管理
          '/answer_management/answer_manage/index.html': {
              //每个页面对应的数据获取地址
              dataPath: '/usersection/show',
              //页面加载之后需要做的事
              handle: function (tplPath) {
                  initPage({
                      tplPath: tplPath,
                      showData: this.showData,
                      dataPath: getDataPath(this.dataPath),
                      bindEvent: this.bindEvent,
                      submitParam: {
                         otherValidate: function(){

                          return true;
                        },
                        
                       // submitUrl:dataUrl+'',
                        getFormData: function() {
                          return this.form.serialize();
                        }
                      }
                  });
               },
              showData: function(data) {
                var data = data.data.userSection.content;
                var trs = '';
                  for(var i in data){
                     var tds="<tr>";
                      tds += "<td>"+(Number(i)+1)+"</td>";
                      tds += "<td>"+data[i].userName+"</td>";
                      tds += "<td>"+data[i].jobNumber+"</td>";
                      tds += "<td>"+data[i].buMen+"</td>";
                      tds += "<td>"+data[i].departmentName+"</td>";
                      tds += "<td>"+data[i].correctCount+"</td>";
                      /*tds += "<td>"+data[i].correctRate+"%</td>";*/
                      tds += "<td>"+data[i].sections+"</td>";
                      tds += "<td>"+data[i].stars+"</td>";
                      tds += "<td>"+data[i].submitCount+"</td>";
                      tds += "<td>"+(data[i].submitSeconds)/1000+"</td>";
                    tds+='</tr>';
                    trs+=tds;
                  }
                $('table.table-striped tbody').html(trs); 
              },
              bindEvent: function() {
                  //这里填写每个页面需要绑定的一些事件
                  var _this = this;
                  var data = JSON.parse(sessionStorage.pagerData);
                  readyForPager(_this,data.data.userSection);
                  $('#search-form').data({ context: _this, key: 'userSection' }); 
            }
          },
          //个人排名
          '/ranking_management/personranking_manage/index.html': {
              //每个页面对应的数据获取地址
              dataPath: '/rank/personSituation',
              //页面加载之后需要做的事
              handle: function (tplPath) {
                  initPage({
                      tplPath: tplPath,
                      showData: this.showData,
                      dataPath: getDataPath(this.dataPath),
                      bindEvent: this.bindEvent,
                      submitParam: {
                         otherValidate: function(){

                          return true;
                        },
                       // submitUrl:dataUrl+'',
                        getFormData: function() {
                            return this.form.serialize();
                        }
                      }
                  });
               },
              showData: function(data) {
                var data = data.data.rank;
                var trs = '';
                  for(var i in data){
                     var tds="<tr>";
                      tds += "<td>"+(Number(i)+1)+"</td>";
                      tds += "<td>"+data[i].departmentname+"</td>";
                      tds += "<td>"+data[i].username+"</td>";
                      tds += "<td>"+data[i].sectioncnt+"</td>";
                      tds += "<td>"+data[i].stars+"</td>";
                      tds += "<td>"+data[i].like+"</td>";
                    tds+='</tr>';
                    trs+=tds;
                  }
                $('table.table-striped tbody').html(trs); 
              },
              bindEvent: function() {
                  //这里填写每个页面需要绑定的一些事件 
            }
          },
          //中心排名
          '/ranking_management/centerranking_manage/index.html': {
              //每个页面对应的数据获取地址
              dataPath: '/rank/departmentSituation',
              //页面加载之后需要做的事
              handle: function (tplPath) {
                  initPage({
                      tplPath: tplPath,
                      showData: this.showData,
                      dataPath: getDataPath(this.dataPath),
                      bindEvent: this.bindEvent,
                      submitParam: {
                         otherValidate: function(){

                          return true;
                        },
                       // submitUrl:dataUrl+'',
                        getFormData: function() {
                            return this.form.serialize();
                        }
                      }
                  });
               },
              showData: function(data) {
                var data = data.data.rank;
                var trs = '';
                  for(var i in data){
                     var tds="<tr>";
                      tds += "<td>"+(Number(i)+1)+"</td>";
                      tds += "<td>"+data[i].departmentname+"</td>";
                      tds += "<td>"+data[i].joinRate+"%</td>";
                      tds += "<td>"+data[i].avgSection+"</td>";
                      tds += "<td>"+data[i].avgScore+"</td>";
                      tds += "<td>"+data[i].like+"</td>"
                    tds+='</tr>';
                    trs+=tds;
                  }
                $('table.table-striped tbody').html(trs);
              },
              bindEvent: function() {
                  //这里填写每个页面需要绑定的一些事件 
            }
          },
          //用户管理
          '/user_management/user_manage/index.html': {
              //每个页面对应的数据获取地址
              dataPath: '/backUser/showAll',
              //页面加载之后需要做的事
              handle: function (tplPath) {
                  initPage({
                      tplPath: tplPath,
                      showData: this.showData,
                      dataPath: getDataPath(this.dataPath),
                      bindEvent: this.bindEvent,
                      submitParam: {
                         otherValidate: function(){

                          return true;
                        },
                       // submitUrl:dataUrl+'',
                        getFormData: function() {
                            return this.form.serialize();
                        }
                      }
                  });
               },
              showData: function(data) {
                var data = data.data.backUser;
                var trs = '';
                  for(var i in data){
                     var tds="<tr>";
                      tds += "<td>"+(Number(i)+1)+"</td>";
                      tds += "<td>"+data[i].loginName+"</td>";
                      tds += "<td class='text-ellipsis'>\
                            <a href= '#/user_management/user_manage/modify.html' class='fa fa-lg fa-pencil-square-o' data-role='edit' data-edit-id='"+data[i].id+"' title='编辑'></a>  \
                            <a href='javascript:void(0);' data-role='delete' class='fa fa-trash-o trash' data-key='id' data-delete-id='"+data[i].id+"' data-delete-path='/backUser/d/" + data[i].id + "' title='删除'></a>\
                             </td>";
                    tds+='</tr>';
                    trs+=tds;
                  }
                $('table.table-striped tbody').html(trs); 
              },
              bindEvent: function() {
                  //这里填写每个页面需要绑定的一些事件 
            }
          },
          //用户管理新增
          '/user_management/user_manage/insert.html': {
              //每个页面对应的数据获取地址
              dataPath: '',
              //页面加载之后需要做的事
              handle: function (tplPath) {
                  initPage({
                      tplPath: tplPath,
                      showData: this.showData,
                      dataPath: getDataPath(this.dataPath),
                      bindEvent: this.bindEvent,
                      submitParam: {
                         otherValidate: function(){

                          return true;
                        },
                        submitSuccess: function (data){
                            console.log(data.data.backUser);
                          if(data.data.backUser != "success"){
                            bootbox.confirm({
                              message: "两次密码不一致",
                              callback: function(tag){
                                if(tag){
                                  $.ajax({
                                      
                                  })
                                  .then(function(data){
                                      
                                  })
                                  .fail(requestError);
                                }
                              }
                            });
                            return false;
                          }else{
                              bootbox.alert('保存成功');
                              window.history.go(-1);
                          }
                          return true;
                        },
                        submitUrl: dataUrl + '/backUser/create',
                        getFormData: function() {
                            return this.form.serialize();
                        }
                      }
                  });
               },
              showData: function(data) {
                
              },
              bindEvent: function() {
                  //这里填写每个页面需要绑定的一些事件 
            }
          },
          //用户管理编辑
          '/user_management/user_manage/modify.html': {
              //每个页面对应的数据获取地址
              dataPath: '/backUser/updateLoad',
              //页面加载之后需要做的事
              handle: function (tplPath) {
                  initPage({
                      tplPath: tplPath,
                      showData: this.showData,
                      dataPath: getDataPath(this.dataPath,{id:sessionStorage.getItem('editId')}),
                      bindEvent: this.bindEvent,
                      submitParam: {
                         otherValidate: function(){

                          return true;
                        },
                        submitSuccess: function (data){
                          if(data.data.backUser != "success"){
                            bootbox.confirm({
                              message: "两次密码不一致",
                              callback: function(tag){
                                if(tag){
                                  $.ajax({
                                      
                                  })
                                  .then(function(data){
                                      
                                  })
                                  .fail(requestError);
                                }
                              }
                            });
                            return false;
                          }else{
                            bootbox.alert('修改成功');
                            window.history.go(-1);
                          }
                          return true;
                        },
                        submitUrl: dataUrl + '/backUser/update',
                        getFormData: function() {
                            return this.form.serialize();
                        }
                      }
                  });
               },
              showData: function(data) {
                var data = data.data.backUser;
                $('.userName').val(data.loginName);
                $('[name="id"]').val(data.id);
              },
              bindEvent: function() {
                //这里填写每个页面需要绑定的一些事件

            }
          },
          //获奖名单
          '/have_prize/przie_have/index.html': {
              //每个页面对应的数据获取地址
              dataPath: '/prize/show',
              //页面加载之后需要做的事
              handle: function (tplPath) {
                  initPage({
                      tplPath: tplPath,
                      showData: this.showData,
                      dataPath: getDataPath(this.dataPath),
                      bindEvent: this.bindEvent,
                      submitParam: {
                         otherValidate: function(){

                          return true;
                        },
                        
                       // submitUrl:dataUrl+'',
                        getFormData: function() {
                            return this.form.serialize();
                        }
                      }
                  });
               },
              showData: function(data) {
                var data = data.data.prize.content;
                var trs = '';
                  for(var i in data){
                     var tds="<tr>";
                      tds += "<td>"+(Number(i)+1)+"</td>";
                      tds += "<td>"+data[i].userName+"</td>";
                      tds += "<td>"+data[i].createTime+"</td>";
                      tds += "<td>"+data[i].content+"</td>";
                    tds+='</tr>';
                    trs+=tds;
                  }
                $('table.table-striped tbody').html(trs); 
              },
              bindEvent: function() {
                  //这里填写每个页面需要绑定的一些事件
                  var _this = this;
                  var data = JSON.parse(sessionStorage.pagerData);
                  readyForPager(_this,data.data.prize);
                  $('#search-form').data({ context: _this, key: 'prize' }); 
            }
          },

      };
        //填充select
      function loadSelectOPtion(option){
          var method = option.method || 'append',
          optionStr = '';
          for(var i = 0,len = option.data.length; i < len; i++){
              optionStr += '<option value="'+ option.data[i][option.value] +'">' + option.data[i][option.text] + '</option>';
          }
          $(option.selector)[method](optionStr);
      }

      window.load_checkbox = function(data,param_nam,param_id,param_name,selector){
            var opts = '';
            for(var i = 1,len = data.length; i <= len; i++){
                opts += '<label class="col-xs-4"><input class="user-read" type="checkbox" name="'+ param_nam +'"  value="' + data[i-1][param_id] + '" required style="margin-right:2px;">' + data[i-1][param_name] + '</label>';
                if(i % 5 == 0) {
                    opts += '<br>';
                }
            }
            $(selector).append(opts);
        };


      function datePicker(){
         $('.datepicker').datepicker({
            format:'yyyy-mm-dd',
            todayHighlight: true,
            autoclose:true,
            language:'zh-CN'
        });
      }

      function setInput(dataInput){
        if($.isArray(dataInput)){
          $.each(dataInput,function(k,v){
             $('input').not('select,[type="checkbox"],[type="radio"]').each(function(){
              this.value = v[this.name];
            });
          });
        } else if($.isPlainObject(dataInput)) {
          $('input').not('select,[type="checkbox"],[type="radio"]').each(function(){
              this.value = dataInput[this.name];
          });
        }
      }

      window.cadleo_data = function(dataPath){
          !!location.hash.substring(1) && $.get(dataUrl + encodeURI(dataPath), function(data) {
             data.code == 0 && !showData(data) || (data.message && bootbox.alert(data.message)||bootbox.alert('请求出错，请重试'));
          });
      };

      /**
       * [fillInput 填充checkbox radio]
       * @param  {options.arr} 数组 [需要循环的数据的集合]
       * @param  {options.type} 类型 [input的类型]
       * @param  {options.arr[i].name} 类型 [input的name]
       * @param  {options.arr[i].value} string [input的value值]
       * @param  {options.arr[i].text} string [label中的文本]
       * @param  {options.required} boolean [是否必选，默认为false]
       * @param  {options.selector} string [需要填充的父容器的选择器]
       */
      fillInput = function(options){
          var opts = '';
          for(var i = 0,len = options.arr.length; i < len; i++){
              opts += '<label><input type="'+ options.type +'" name="'+ options.arr[i].name +'" value="' + options.arr[i].value + (options.required?'" required>':'" >') + options.arr[i].text + '</label>';
              if(i % 5 == 0) {
                  opts += '<br>';
              }
          }
          $(options.selector).append(opts);
      };

		  //编辑
	    $(document).on('click', '[data-role="edit"]', function(e){
          sessionStorage.setItem('editId', $(this).attr('data-edit-id'));
	    	  sessionStorage.setItem('typeName', $(this).attr('data-type-name'));
		  });

      //菜单点击加载页面
      $(document).on('click', '[data-hash="href"]', function(e){
        $(this).parent('li').siblings().removeClass('active current-page');
        if($(this).attr('href')==location.hash){
          $(window).trigger('hashchange');
        }
      });

      //返回按钮
      $(document).on('click', '[data-history="back"]', function(e){
        history.back();
        e.preventDefault();
      });

      //删除
  		$(document).on('click', '[data-role="delete"]', function(){
  			var _this = $(this),key = _this.attr('data-key');
        bootbox.confirm({
          message: '确认要删除这条数据吗?',
          callback: function(arg){
            if(arg){
        			deleteData({url: dataUrl + _this.attr('data-delete-path'),param: {key: key,value: _this.attr('data-delete-id')}});
            }
          }
        })
  		});

		  //详情页
   	  $(document).on('click','[data-role="detail"]',function(e){
        var _this = $(this)
        sessionStorage.setItem('detailId', _this.attr('data-detail-id'));
        _this.attr('data-detail-path') && $.ajax({
                                            url: _this.attr('data-detail-path'),
                                            type: 'GET',
                                        })
                                        .then(function(data){
                                            //$('#wj-main-content').html(data);
                                        })
                                        .fail(requestError);

   	  });
      

    //checkbox全选单选 
      function initTableCheckbox() {  
          var thr = $('#check-all');  
          var tbr = $('.check-single');   
          var checkAll = thr.find('input');  
            checkAll.click(function(event){  
              /*将所有行的选中状态设成全选框的选中状态*/  
              tbr.find('input').prop('checked',$(this).prop('checked'));  
              /*阻止向上冒泡，以防再次触发点击操作*/  
              event.stopPropagation();  
           });  
          /*点击全选框所在单元格时也触发全选框的点击操作*/  
          thr.click(function(){  
              $(this).find('input').click();  
          });
         
          tbr.find('input').click(function(event){  
              checkAll.prop('checked',tbr.find('input:checked').length == tbr.length ? true : false);  
              /*阻止向上冒泡，以防再次触发点击操作*/  
              event.stopPropagation();  
          });  
          /*点击每一行时也触发该行的选中操作*/  
          tbr.click(function(){  
              $(this).find('input').click();  
          }); 
      }  
       
      //checkbox全选单选结束


    	//搜索
    	$(document).on('submit','#search-form',function(){
            var _this = $(this),
                context = _this.data('context');
            requestData(context.dataPath + '?' + _this.serialize())
                .then(function(data){
                    if(data.code == 'success'){
                        context.showData(data);
                        readyForPager(context,data.data[_this.data('key')]);
                    }
                })
                .fail(requestError);
            return false;//阻止表单提交
    	});


    	$(document).on('change','#fileToUpload',function(){
    		var fileName = '';
    		if($(this).val().lastIndexOf('\\') !=-1){
    			fileName = $(this).val().substring($(this).val().lastIndexOf('\\')+1);
    		} else {
    			fileName = $(this).val().substring($(this).val().lastIndexOf('/')+1);
    		}
    		$(this).parent().siblings('.file-path').text('您选择的文件为：'+fileName).attr('title',fileName);
    	});

    	$(document).on('click','.upload-btn',function() {
    		var parent = $(this).parent();
    		if(!$(this).siblings().find('#fileToUpload').val()){
    			parent.find('.file-path').text('请先选择图片');
    			return ;
    		}
    		var val = $(this).siblings().find('#fileToUpload').val();
    		if(!/^(.JPEG|.jpeg|.JPG|.jpg|.GIF|.gif|.BMP|.bmp|.PNG|.png)$/.test(val.substring(val.lastIndexOf('.')))){
    			$(this).siblings().find('#fileToUpload').val('');
    			$(this).siblings('.file-path').text('只能上传后缀名为".jpg"，".jpeg"，".png"，".gif"，".bmp"的文件');
    			return ;
    		}
    		var path = $(this).siblings('.file-path');
        	$.ajaxFileUpload({
     	    	 url : dataUrl+'/upload.htmls',
     	    	 secureuri : false,
     	    	 fileElementId : 'fileToUpload',
     	    	 parentNode: parent,
     	    	 dataType : 'json',
     	    	 success : function(data, status) {
              //parent.parent('.view-img-pane').attr('src', dataUrl+'/showPic.htmls?pdfUrl='+data.picUrl).removeClass('collapse');
     	    		//parent.find().find('.view-img-shop ').attr('src', dataUrl+'/showPic.htmls?pdfUrl='+ data.data).removeClass('collapse');
              //parent.parent().find('#submit-file').val(data.picUrl);
     	    	 },
     	    	 error : function(xhr, status, error) {
     	    		path.text('服务器错误，上传出错，请重试');
     	    	 }
       		});
        	path.text('');
        });

        //登出
    	/*$(document).on('click','#un-login',function(){
            var str = dataUrl + $(this).data('path');
            $.ajax({
                 type: 'get',
                 url:dataUrl+'/destory.htmls',
                 dataType: 'html',
            })
            .then(function(data){
              if(!$.isPlainObject(data)){
                var data = $.parseJSON(data);
              }
              if(data.code == 'E10013'){
            	  location.href = '/happy_shop_web/login.html' || '/';
              }
            })
            .fail(requestError);
    	});*/

      // $.ajax({
      //     type: 'get',
      //     url: basePath + '/init.htmls',
      // })
      // .then(function(data){
      //     if(data.code == '0'){

      //     } else {
      //         location.href = basePath || '/';
      //     }
      // })
      // .fail(requestError);

      //F5刷新时加载数据
      setTimeout(function(){
      	$(window).trigger('hashchange');
      });

      function light_cad(lightCad,time){
          lightCad.css({left:-680}).animate({left:'120%'},time,function(){
            lightCad.css({left:-680});
            light_cad(lightCad,time);
          });
      }
      if(document.body.style.transition===undefined){
        var lightCad = $('<div class="light-cad"></div>').appendTo('.welcome-content').css({height:$('html')[0].offsetHeight});
        lightCad.css({opacity: 0}).animate({left:'120%'},300,function(){
          lightCad.css({opacity: 1});
          light_cad(lightCad,2000);
        });
      }

	});
});

