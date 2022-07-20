function iamport(){
	//가맹점 식별코드
	IMP.init('imp13557790');
	IMP.request_pay({
	    pg : 'kakaopay',
	    pay_method : 'kakaopay',
	    merchant_uid : 'merchant_' + new Date().getTime(),
	    name : '상품1' , //결제창에서 보여질 이름
	    amount : 10, //실제 결제되는 가격
	    buyer_email : 'winner_loser@naver.com',
	    buyer_name : 'KING',
	    buyer_tel : '010-6666-4444',
	    buyer_addr : '사랑시 고백구 행복동',
	    buyer_postcode : '123-456'
	}, function(rsp) {
		console.log(rsp);
	    if ( rsp.success ) {
	    	console.log(rsp.imp_uid);
	    	var msg = '결제가 완료되었습니다.';
	        msg += '고유ID : ' + rsp.imp_uid;
	        msg += '상점 거래ID : ' + rsp.merchant_uid;
	        msg += '결제 금액 : ' + rsp.paid_amount;
	        msg += '카드 승인번호 : ' + rsp.apply_num;
	    } else {
	    	 var msg = '결제에 실패하였습니다.';
	         msg += '에러내용 : ' + rsp.error_msg;
	    }
	    alert(msg);
	});
}
function cancelPay() {
	
	var jobj = JSON.stringify({
			muid: 'merchant_' + new Date().getTime(),
			camount: 10,
			reason: '테스트 결제 환불'
		});
	console.log(jobj);
	
	var objuid = {'muid': 'merchant_' + new Date().getTime()};
	
	$.ajax({
		url: 'cancelproc',
		type: 'post',
		//contentType: 'application/json',
		data: objuid,
		//dataType:'json',
		success: function(result){
			alert("환불 성공");
		},
		error: function(error){
			alert("환불 실패");
		}
	});
	
}
 