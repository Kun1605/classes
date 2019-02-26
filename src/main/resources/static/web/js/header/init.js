/**
 * 获取用户未读消息
 */
function getUserMessage(){
	$.post("/web/message/unreadmessage", {}, function (result) {
		console.log(result)
		if(result.code == 200){
			if(result.unreadCount > 0 && result.unreadCount <= 99){
				$(".userMailNumber").html(result.unreadCount);		
				$(".userMailNumber").show();
			}else if(result.unreadCount > 99){
				$(".userMailNumber").html('99<span>+</span>');
				$(".userMailNumber").show();
			}else{
				$(".userMailNumber").html('');
				$(".userMailNumber").hide();
			}
			
		}
	}, "json");
}

$(function(){
	//获取用户未读消息
	getUserMessage();	
});

