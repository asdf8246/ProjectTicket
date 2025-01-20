// 確認退票操作的函數
function confirmCancel(orderId, orderDate) {
    // 使用 date-fns 解析日期字符串，並轉換為 JavaScript Date 物件
    const endTime = dateFns.parse(orderDate, 'yyyy-MM-dd HH:mm:ss', new Date());
    let endtimems = endTime.getTime() + 259200000;  // 取得結束時間的毫秒數
    let nowms = new Date().getTime();  // 取得當前時間的毫秒數
    
    // 檢查當前時間是否超過了結束時間
    if (nowms > endtimems) {
        alert("已超過退票時限!");
        return false;  // 如果超過時限，阻止後續操作
    }

    // 顯示確認對話框
    const confirmation = confirm("確定要退票嗎？");

    if (confirmation) {
        // 使用 AJAX 發送刪除請求
        var xhr = new XMLHttpRequest();
        xhr.open("GET", '/ticket/order/cancel?orderId=' + orderId, true);
        
        // 當請求完成後的回調函數
        xhr.onload = function() {
            if (xhr.status === 200) {
                alert("已完成退票");
                location.reload();  // 若成功，重新加載頁面
            } else {
                alert("退票操作失敗！");
            }
        };
        xhr.send();  // 發送請求
    }

    // 如果用戶選擇「取消」，則返回 false，阻止鏈接的默認行為（避免跳轉）
    return false;
}

// 確認刪除操作的函數
function confirmDelete(orderId) {
    // 顯示確認對話框
    const confirmation = confirm("確定要取消訂單嗎？取消後將不會保留。");

    if (confirmation) {
        // 使用 AJAX 發送刪除請求
        var xhr = new XMLHttpRequest();
        xhr.open("GET", '/ticket/order/delete?orderId=' + orderId, true);
        
        // 當請求完成後的回調函數
        xhr.onload = function() {
            if (xhr.status === 200) {
                alert("訂單已取消");
                location.reload();  // 若成功，重新加載頁面
            } else {
                alert("取消訂單失敗！");
            }
        };
        xhr.send();  // 發送請求
    }

    // 如果用戶選擇「取消」，則返回 false，阻止鏈接的默認行為（避免跳轉）
    return false;
}


// 確認是否刪除用戶
function deleteUser(userId) {
    // 顯示確認對話框
    const confirmation = confirm("確定要註銷帳號嗎？註銷後用戶資料將全部刪除。");

    if (confirmation) {
        // 使用 AJAX 發送刪除請求
        var xhr = new XMLHttpRequest();
        xhr.open("GET", '/ticket/user/delete?userId=' + userId, true);
        
        // 當請求完成後的回調函數
        xhr.onload = function() {
            if (xhr.status === 200) {
                alert("帳號已註銷");
                window.location.href = '/ticket/home';
            } else {
                alert("帳號註銷失敗！");
            }
        };
        xhr.send();  // 發送請求
    }

    // 如果用戶選擇「取消」，則返回 false，阻止鏈接的默認行為（避免跳轉）
    return false;
}