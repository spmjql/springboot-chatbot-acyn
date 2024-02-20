/**
 * 
 */
var stompClient=null; 
var key=new Date().getTime();

$(function(){
	$("#btn-bot").click(btnBotClicked);
});
function btnCloseClicked(){
	$("#bot-container").hide();
	$("#chat-content").html("");
	disconnect();
}
function btnBotClicked(){
	//1. 소켓 접속
	$("#bot-container").show();
	connect()
}

//1~9 -> 01~09 변환하는 함수
function fomatNumber(number){
	return number<10? '0'+number:''+number;
}

// 시간출력 ex) 오전 9:08
function formatTime(){
	var now=new Date();
	var ampm=(now.getHours()>11)?"오후":"오전";
	var hour=now.getHours()%12;
	if(hour==0)hour=12;
	return `${ampm} ${hour}:${fomatNumber(now.getMinutes())}`;
}

function userTag(text){
	var time=formatTime();
	return `
	<div class="msg user flex">
		<div class="message">
			<div class="part">
				<p>${text}</p>
			</div>
			<div class="time">${time}</div>
		</div>
	</div>
	`;
}

function botTag(text){
	var time=formatTime();
	return `
	<div class="msg bot flex">
		<div class="icon">
			<img src="/images/icon/robot-solid.svg">
		</div>
		<div class="message">
			<div class="part">
				<p>${text}</p>
			</div>
			<div class="time">${time}</div>
		</div>
	</div>
	`;
}
function showMessage(tag){
	$("#chat-content").append(tag);
	//스크롤이 제일 아래로
	$("#chat-content").scrollTop($("#chat-content").prop("scrollHeight"));

}

function connect(){
	//var socket=new SockJS("/green-bot")
	stompClient=Stomp.over(new SockJS("/green-bot"));
	stompClient.connect({},(frame)=>{
		//접속이 완료되면 인사말수신-구독
		stompClient.subscribe(`/topic/order/${key}`,(answerData)=>{
			//console.log(answerData.body);
			var message=JSON.parse(answerData.body);
			var text=message.content;
			/////////////////////////////
			var tag=botTag(text);
			/////////////////////////////
			showMessage(tag);
		})
		
		var data={
			key: key,
			name:"그린",
			content: "학원에있는영진이전화번호는?."
		}
		//인사말 보내줘
		stompClient.send("/message/bot",{},JSON.stringify(data));
	});
}
function disconnect() {
	stompClient.disconnect(function(){
		
	});
}
function checkEnterKey(event){
	var keyCode=event.keyCode;
	if(keyCode===13){
		btnMsgSendClicked();
	}
}

function btnMsgSendClicked() {
	var content=$("#question").val().trim();
	
	var data={
		key,
		name:"그린",
		content
	}
	
	stompClient.send("/message/bot",{},JSON.stringify(data))
	var tag=userTag(content);
	$("#question").val('');
	showMessage(tag);
	
}