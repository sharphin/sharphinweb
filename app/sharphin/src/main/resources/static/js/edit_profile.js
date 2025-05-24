function dispLoading(){
  var innerMsg = '<div class="loadspinner"></div>';
  $("#main_contents").append(innerMsg);
}
/* ------------------------------
 表示ストップ用の関数
 ------------------------------ */
function removeLoading(){
  $(".loadspinner").remove();
}  
function fileChoose() {
    let file = document.getElementById("iconupload");
    file.click();
}
function fileUP() {
  dispLoading();
  var file = null; // 選択されるファイル
  var blob = null; // 画像(BLOBデータ)
  // ファイルを取得
  file = $("#iconupload").prop('files')[0];
  // 選択されたファイルが画像かどうか判定
  if (file.type != 'image/jpeg' && file.type != 'image/png') {
    // 画像でない場合は終了
    file = null;
    blob = null;
    return;
  }
  const file_load = fileResize(file,blob);
  file_load.then(function(blob) {
    file_submit(file,blob);
  }).then(result=> {
    location.reload();
  });
  removeLoading();
}
function fileResize(file,blob) {
  return new Promise(resolve => {
    const THUMBNAIL_WIDTH = 200; // 画像リサイズ後の横の長さの最大値
    const THUMBNAIL_HEIGHT = 200; // 画像リサイズ後の縦の長さの最大値  
    // 画像をリサイズする
    var image = new Image();
    var reader = new FileReader();
    reader.onload = function(e) {
      image.onload = function() {
        var width, height;
        if(image.width > image.height){
          // 横長の画像は横のサイズを指定値にあわせる
          var ratio = image.height/image.width;
          width = THUMBNAIL_WIDTH;
          height = THUMBNAIL_WIDTH * ratio;
        } else {
          // 縦長の画像は縦のサイズを指定値にあわせる
          var ratio = image.width/image.height;
          width = THUMBNAIL_HEIGHT * ratio;
          height = THUMBNAIL_HEIGHT;
        }
        // サムネ描画用canvasのサイズを上で算出した値に変更
        var canvas = $('#canvas')
                     .attr('width', width)
                     .attr('height', height);
        var ctx = canvas[0].getContext('2d');
        // canvasに既に描画されている画像をクリア
        ctx.clearRect(0,0,width,height);
        // canvasにサムネイルを描画
        ctx.drawImage(image,0,0,image.width,image.height,0,0,width,height);
  
        // canvasからbase64画像データを取得
        var base64 = canvas.get(0).toDataURL('image/jpeg');        
        // base64からBlobデータを作成
        var barr, bin, i, len;
        bin = atob(base64.split('base64,')[1]);
        len = bin.length;
        barr = new Uint8Array(len);
        i = 0;
        while (i < len) {
          barr[i] = bin.charCodeAt(i);
          i++;
        }
        blob = new Blob([barr], {type: 'image/jpeg'});
        console.log(blob);
        resolve(blob);
      }
      image.src = e.target.result;
    }
    reader.readAsDataURL(file);
  });
};
function file_submit(file,blob) {
  return new Promise(resolve => {
    let token = $("meta[name='_csrf']").attr("content"); 
    let header = $("meta[name='_csrf_header']").attr("content"); 
    
    // Ajax通信時に、リクエストヘッダにトークンを埋め込むよう記述
    $(document).ajaxSend(function(e, xhr, options){
        xhr.setRequestHeader(header, token);
    });
    // ファイルが指定されていなければ何も起こらない
    if(!file || !blob) {
      return;
    }
    let user_id = $('#userid').text()
    var name, fd = new FormData();
    fd.append('file', blob); // ファイルを添付する
  
    $.ajax({
      url: "/"+user_id+"/edit/iconsave", // 送信先
      type: 'POST',
      dataType: 'json',
      data: fd,
      processData: false,
      contentType: false
    }).done(function( data, textStatus, jqXHR ) {
    }).fail(function( jqXHR, textStatus, errorThrown ) {
    });
  });
}
function userupdate(user_id) {
  const result = userupdAjax(user_id.value);
  result.then(function(rr) {
    document.getElementById("profileback").submit();
  });
}
function userupdAjax(user_id) {
  return new Promise(resolve => {
    $.ajax({
      url: "/"+user_id+"/edit/save",
      type: 'POST',
      dataType: 'html',
      data: $("#userupd").serialize(),
    }).done(function(result) {
    }).fail(function(result) {
      alert("更新失敗");
    });
  });
}