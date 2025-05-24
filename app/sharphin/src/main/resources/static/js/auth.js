function userCheck(id) {
    var form_element = document.getElementById(id);
    form_element.action = "sighin";
    if(id == "register") {
        form_element.username.value = form_element.user_id.value;
    }
    form_element.submit();
}
function userRegister() {
    var form_element = document.getElementById("register");
    let formData = new FormData(form_element);
  
    $.ajax({
        url : "/sighup",
        dataType: "html",
        type : "post",
        data : formData,
        processData: false,
        contentType: false
    }).done(function(resData) {
        if(parseInt(resData) == -1){
            alert("このユーザIDは使用済みです");
        } else if(parseInt(resData) == 1){
            alert("このユーザIDが未入力です");
        } else if(parseInt(resData) == 2){
            alert("パスワードは10文字以上にしてください");
        } else if(parseInt(resData) == 3) {
            alert("ユーザー名が未入力です");
        } else if(parseInt(resData) == 4) {
            alert("メールアドレスが無効です");
        } else {
            const myModal = new bootstrap.Modal(document.getElementById("sighup_success"));
            myModal.show();
        }
    }).fail(function() {
    });
  }