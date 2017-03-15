function changeCategory(cid,id,url){
    url = url+'/cid/'+cid;
    getData(url,id,'aid','area');
}

function changeCategoryAndArea(cid,url){
    url = url+'/cid/'+cid;
    $.ajax({
        type:'GET',
        url:url,
        dataType:'json',
        beforeSend:function(){},
        success:function(result){
            if(result.status){
                var data = result.area;
                var str = '';
                for(var i in data){
                    str = str + '<option value="'+data[i].aid+'">'+data[i].area+'</option>'
                }
                $('#aid').html(str);
                var server = result.server;
                str = '';
                for(var y in server){
                    str = str + '<option value="'+server[y].sid+'">'+server[y].server+'</option>';
                }
                $('#sid').html(str);
            }
        },
        error:function(){}
    });
}

function changeArea(aid,id,url){
    url = url+'/aid/'+aid;
    getData(url,id,'sid','server');
}

function getData(url,id,k,v){
    $.ajax({
        type:'GET',
        url:url,
        dataType:'json',
        beforeSend:function(){},
        success:function(result){
            if(result.status){
                var data = result.data;
                var str = '';
                for(var i in data){
                    str = str + '<option value="'+data[i][k]+'">'+data[i][v]+'</option>'
                }
                $('#'+id).html(str);
            }
        },
        error:function(){}
    });
}