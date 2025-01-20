// 生成台灣縣市二聯式選單
new TwCitySelector() + '<p />';
		
//圖片物件及條件
let file =document.querySelector('#file');
let imgErrMsgArea = document.querySelector('#img_errmsg');
let imgPreArea = document.querySelector('#img_area');
let maxSize = 5 * 1024 * 1024; // 5MB
let minWidth = 1000;
let imgstr = '';
let imgErrMsg = '';

  //負責檢查上傳檔案
function chkfile(obj){
issubmit = true;
imgErrMsgArea.innerHTML = '';
imgPreArea.innerHTML = '';
imgstr = '';
imgErrMsg = '';

let filesLength = obj.files.length;
console.log(filesLength);

if (filesLength > 1){
     alert('只能選一張圖片!');
     issubmit = false;
  } else {
   Object.values(obj.files).forEach(chkfile2);
  }
}
  
function chkfile2(item, index){
    let reader = new FileReader();

reader.onload = function (e) {
    //(1)預覽影像
    let data = e.target.result;
    imgstr += '<img src="' + data + '"width="300" class="">';
    imgPreArea.innerHTML = imgstr;
    //(2)檢查檔案容量
    let fileSize = item['size'];
    if (fileSize > maxSize){
      issubmit = false;
      imgErrMsg += '<br>' + item['name'] + '檔案容量太大';
      imgErrMsgArea.innerHTML = imgErrMsg;
    }
    //(3)檢查圖片寬度
    let image = new Image();
    image.onload = function (){
      let fileWidth = image.width;
      if(fileWidth<minWidth){
        issubmit = false;
        imgErrMsg =+ '<br>' + item['name'] + '檔案寬度太小';
        imgErrMsgArea.innerHTML = imgErrMsg;
      }
    }
    image.src = data;
  }
  reader.readAsDataURL(item);
}

flatpickr(".datetime", {
 enableTime: true,  // 启用时间选择
 dateFormat: "Y-m-d H:i",  // 设置日期时间格式
});

// 當表單提交時進行檢查
document.getElementById("eventForm").addEventListener("submit", function(event) {
    // 取得活動日期和售票日期的值
    const eventDate = new Date(document.getElementById("eventDate").value);
    const sellDate = new Date(document.getElementById("sellDate").value);

    // 檢查活動日期是否大於售票日期
    if (eventDate <= sellDate) {
        alert("活動日期必須大於售票日期！");
        event.preventDefault(); // 阻止表單提交
    }
});

