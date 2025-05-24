let stompClient = null;
function send_user_decide(thisElement,chatroom_id) {
  $.ajax({
    url: "message/d/"+chatroom_id,
    type: 'POST',
    data: $(thisElement).find(".choose_user").serialize(),
  }).done(function(data) {
    let send_user = data.sendUser;
    let icon = '<img src="/geticon?name='+send_user.icon_path+'">';
    if(send_user.icon_path == null) {
      icon = '<img src="/img/account.png"></img>';      
    }
    $(".chatt").html(
      '<li class ="userrr">\
        <div class="iconimageee">'+icon+'</div>\
        <div class="username2">'+send_user.user_name+'</div>\
      </li>\
      <div id="message">'+make_message_history(data.message)
      +'</div>\
      <div class="form-inline">\
        <div class="form-group">\
          <input type="text" id="name"  value = "'+send_user.user_id+'" hidden>\
        </div>\
        <div class="form-group">\
          <input type="text" id="statement" placeholder="Your Message">\
        </div>\
        <button id="send" onclick = "sendMessage('+"'"+send_user.chatroom_id+"'"+')">Send</button>\
      </div>');
    disconnect();
    connect();
  }).fail(function() {
  });
}
function make_message_history(messages) {
  let mess ="";
  for(let i = 0; i< messages.length;i++) {
      mess += "<li>" + messages[i].main_message+ "</li>";
  }
  return mess;
}

function connect() {
  let touser_id = $(".userid").text();
  $("#name").val(touser_id);
  let socket = new SockJS('/chat'); 
  stompClient = Stomp.over(socket);
  stompClient.connect({}, function (frame) {
    console.log('Connected: ' + frame);
    stompClient.subscribe('/topic/messages/'+$("#fromuser").text(), function (response) {
      showMessage(response.body);
    });
  });
}

function disconnect() {
  if (stompClient !== null) {
    stompClient.disconnect();
  }
  console.log("Disconnected");
}
function sendMessage(chatroom_id) {
    let id = $("#name").attr("value");
  stompClient.send("/app/chat/"+chatroom_id+"/"+id, {},$("#statement").val());
  showMessage($("#statement").val());
  $("#statement").val('');
}

function showMessage(message) {
  $("#message").append("<li>" + message+ "</li>");
}

$(function () {
  $("#connect").click(function () {
    connect();
  });
  $("#disconnect").click(function () {
    disconnect();
  });
});