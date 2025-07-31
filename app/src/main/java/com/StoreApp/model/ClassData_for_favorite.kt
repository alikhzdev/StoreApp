package com.StoreApp.model

data class ClassData_for_favorite(val test1:String, val test2: String, val background: Int, val list: List<Int>)


sealed class ListItem {
    data class ItemFavorite(val text1: String,val text2: String ,val buttonText: String) : ListItem()
    data class ItemFavorite2(val text1: String, val test2: String,val background: Int) : ListItem()
    data class ItemFavorite3(val text1: String,val test2: String ,val buttonText: String, val background: Int) : ListItem()
}


sealed class ListItem1 {
    data class ItemNotification1(val text1: String,val text2: String ,val buttonText: String,val background: Int) : ListItem1()
    data class ItemNotification2(val text1: String, val test2: String,val buttonText1: String,val buttonText2: String,val background: Int) : ListItem1()
    data class ItemNotification3(val text1: String,val test2: String ,val buttonText: String,val buttonText2: String) : ListItem1()
}

