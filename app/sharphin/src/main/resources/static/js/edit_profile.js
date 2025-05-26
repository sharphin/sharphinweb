function dispLoading(){
  var innerMsg = '<div class="loadspinner"></div>';
  $("#main_contents").append(innerMsg);
}

function removeLoading(){
  $(".loadspinner").remove();
}  
function fileChoose() {
    let file = document.getElementById("iconupload");
    file.click();
}
function fileUP() {
  dispLoading();
  var file = null; 
  var blob = null;
  // ファイルを取得
  file = $("#iconupload").prop('files')[0];
  if (file.type != 'image/jpeg' && file.type != 'image/png') {
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
    const THUMBNAIL_WIDTH = 200; 
    const THUMBNAIL_HEIGHT = 200; 

    var image = new Image();
    var reader = new FileReader();
    reader.onload = function(e) {
      image.onload = function() {
        var width, height;
        if(image.width > image.height){
          var ratio = image.height/image.width;
          width = THUMBNAIL_WIDTH;
          height = THUMBNAIL_WIDTH * ratio;
        } else {
          var ratio = image.width/image.height;
          width = THUMBNAIL_HEIGHT * ratio;
          height = THUMBNAIL_HEIGHT;
        }
        var canvas = $('#canvas')
                     .attr('width', width)
                     .attr('height', height);
        var ctx = canvas[0].getContext('2d');
        ctx.clearRect(0,0,width,height);
        ctx.drawImage(image,0,0,image.width,image.height,0,0,width,height);
  
        var base64 = canvas.get(0).toDataURL('image/jpeg');        
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
    
    $(document).ajaxSend(function(e, xhr, options){
        xhr.setRequestHeader(header, token);
    });
    if(!file || !blob) {
      return;
    }
    let user_id = $('#userid').text()
    var name, fd = new FormData();
    fd.append('file', blob);
  
    $.ajax({
      url: "/"+user_id+"/edit/iconsave",
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