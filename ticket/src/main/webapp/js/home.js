document.getElementById("searchForm").addEventListener("submit", function(event) {
    // 獲取搜尋欄位的值
    var searchValue = document.getElementById("searchInput").value.trim();
    // 如果搜尋欄位為空，則阻止表單提交並顯示提示
    if (searchValue === "") {
        event.preventDefault(); // 阻止表單提交
        //alert("請輸入搜尋關鍵字"); // 顯示提示
    }
});
