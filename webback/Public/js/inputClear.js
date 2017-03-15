(function($){
    $.fn.extend({
        inputClear:function(selector){
            var node = $($.parseHTML('<i class="fa fa-times-circle"></i>')[0]),
                tagHeight = parseFloat(node.css('height'));
            node.on('click touchend',function(){
                $(this).next().val('').keyup();     
            });
            function tagPosition(nodeClone) {
                var _this = $(this),
                    offset = _this.offset(),
                    right =$(window).width() - offset.left - parseFloat(_this.css('width')) + 20,
                    top = offset.top + parseFloat(_this.css('height'))/2 ;
                    nodeClone.css({
                        position: 'fixed', 
                        zIndex: '10', 
                        right: right, 
                        fontSize: '18px',
                        color: '#ddd'
                    }).css({top: top - parseFloat(nodeClone.css('height'))/2});
            }
            this
            .on('keyup change',function(){
                if($(this).val()){
                    $(this).prev('.fa.fa-times-circle').show();
                    tagPosition.call(this,$(this).prev('.fa.fa-times-circle'));
                } else {
                    $(this).prev('.fa.fa-times-circle').hide();
                }

            })
            .each(function(){
                var nodeClone = node.clone(true);
                $(this).before(nodeClone);
                tagPosition.call(this,nodeClone);
            }).keyup();
            return this;
        }
    });
}(jQuery));