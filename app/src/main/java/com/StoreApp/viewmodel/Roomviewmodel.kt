package info.example.projectnewali.Roomdatabase2.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import info.example.projectnewali.Roomdatabase2.Repository.EmojiStoreRepository
import info.example.projectnewali.Roomdatabase2.database.EmojiStoreDatabase
import info.example.projectnewali.Roomdatabase2.model.PackDisplayData
import info.example.projectnewali.Roomdatabase2.CartItem
import info.example.projectnewali.Roomdatabase2.CartMy
import kotlinx.coroutines.launch

class EmojiStoreViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: EmojiStoreRepository

    // مقداردهی repository تو سازنده
    init {
        val database = EmojiStoreDatabase.getDatabase(application)
        repository = EmojiStoreRepository(
            categoryDao = database.categoryDao(),
            emojiPackDao = database.emojiPackDao(),
            emojiDao = database.emojiDao(),
            packCategoryCrossRefDao = database.packCategoryCrossRefDao(),
            cartDao = database.cartDao(),
            cartMyDao = database.cartMy(),
            context = application
        )
    }

    private val _packDisplayList = MutableLiveData<List<PackDisplayData>>()
    val packDisplayList: LiveData<List<PackDisplayData>> get() = _packDisplayList
    private val _filteredPackList = MutableLiveData<List<PackDisplayData>?>()
    val filteredPackList: MutableLiveData<List<PackDisplayData>?> get() = _filteredPackList
    private val cartItems: LiveData<List<CartItem>> = repository.cartItems // اطمینان از اتصال
    private val catMys : LiveData<List<CartMy>> = repository.cartMys
    private val _cartDisplayItems = MediatorLiveData<List<PackDisplayData>>()
    val cartDisplayItems: LiveData<List<PackDisplayData>> get() = _cartDisplayItems
    private val _cartDisplayMys = MediatorLiveData<List<PackDisplayData>>()
    val cartDisplayMys: LiveData<List<PackDisplayData>> get() = _cartDisplayMys
    private val _totalCartPrice = MediatorLiveData<Double>()
    val totalCartPrice: LiveData<Double> get() = _totalCartPrice
    private val _totalDiscountPercentage = MediatorLiveData<Double>()
    val totalDiscountPercentage: LiveData<Double> get() = _totalDiscountPercentage
    private val _discountAmount = MediatorLiveData<Double>()
    val discountAmount: LiveData<Double> get() = _discountAmount

    init {
        viewModelScope.launch {
            repository.initializeDatabase()
        }

        // ترکیب allPacks و packsWithCategories
        val packsWithEmojis = repository.allPacks
        val packsWithCategories = repository.packsWithCategories

        packsWithEmojis.observeForever { emojiPacks ->
            packsWithCategories.observeForever { categoryPacks ->
                val packMap = categoryPacks.associateBy { it.emojiPack.id }
                val newList = emojiPacks.map { packWithEmojis ->
                    val pack = packWithEmojis.emojiPack
                    val emojis = packWithEmojis.emojis ?: emptyList()
                    val originalPrice = pack.price
                    val discountedPrice = pack.discountPrice
                    val isSpecial = pack.isSpecial
                    val discountPercentage = if (discountedPrice != null && originalPrice > 0) {
                        ((originalPrice - discountedPrice) / originalPrice * 100).toInt()
                    } else {
                        null
                    }
                    val categories = packMap[pack.id]?.categories ?: emptyList()
                    PackDisplayData(
                        id = pack.id,
                        name = pack.name,
                        count = pack.count,
                        discountedPrice = discountedPrice,
                        originalPrice = originalPrice,
                        discountPercentage = discountPercentage,
                        imageUrl = pack.imageUrl,
                        isSpecial = isSpecial,
                        emojis = emojis,
                        categories = categories
                    )
                }
                _packDisplayList.value = newList
                Log.d("EmojiStoreViewModel", "Updated packDisplayList with ${newList.size} items")
            }
        }

        // اتصال cartDisplayItems به cartItems و packDisplayList
        _cartDisplayItems.addSource(cartItems) { cartList ->
            updateCartDisplayItems()
        }

        _cartDisplayItems.addSource(_packDisplayList) { _ ->
            updateCartDisplayItems()
        }

        _cartDisplayMys.addSource(catMys) { cartList ->
            updateCartDisplayMys()
        }

        _cartDisplayMys.addSource(_packDisplayList) { _ ->
            updateCartDisplayMys()
        }


        _totalCartPrice.addSource(cartItems) { _ ->
            updateTotalCartPrice()
            updateDiscountAmount()
        }
        _totalCartPrice.addSource(_packDisplayList) { _ ->
            updateTotalCartPrice()
            updateDiscountAmount()
        }
        _totalDiscountPercentage.addSource(cartItems) { _ ->
            updateTotalDiscountPercentage()
            updateDiscountAmount()
        }
        _totalDiscountPercentage.addSource(_packDisplayList) { _ ->
            updateTotalDiscountPercentage()
            updateDiscountAmount()
        }


    }

    private fun updateCartDisplayItems() {
        val cartList = cartItems.value ?: emptyList()
        val allPacks = _packDisplayList.value ?: emptyList()
        val newList = cartList.mapNotNull { cartItem ->
            val pack = allPacks.find { it.id == cartItem.packId }
            if (pack == null) {
                Log.w("EmojiStoreViewModel", "No pack found for packId=${cartItem.packId}")
            }
            pack?.copy(cartQuantity = cartItem.quantity)
        }
        _cartDisplayItems.value = newList
        Log.d("EmojiStoreViewModel", "Updated cartDisplayItems with ${newList.size} items")
        updateTotalCartPrice()
        updateTotalDiscountPercentage()
        updateDiscountAmount()
    }

    private fun updateCartDisplayMys() {
        val cartList = catMys.value ?: emptyList()
        val allPacks = _packDisplayList.value ?: emptyList()
        val newList = cartList.mapNotNull { cartItem ->
            val pack = allPacks.find { it.id == cartItem.packId }
            if (pack == null) {
                Log.w("EmojiStoreViewModel", "No pack found for packId=${cartItem.packId}")
            }
            pack?.copy(cartQuantity = cartItem.quantity)
        }
        _cartDisplayMys.value = newList
        Log.d("EmojiStoreViewModel", "Updated cartDisplayItems with ${newList.size} items")
    }

    private fun updateTotalCartPrice() {
        val cartList = cartItems.value ?: emptyList()
        val allPacks = _packDisplayList.value ?: emptyList()
        val total = cartList.sumByDouble { cartItem ->
            val pack = allPacks.find { it.id == cartItem.packId }
            val price = pack?.discountedPrice?.toDouble() ?: pack?.originalPrice?.toDouble() ?: 0.0
            price * (cartItem.quantity ?: 1)
        }
        _totalCartPrice.value = total
        Log.d("EmojiStoreViewModel", "Updated totalCartPrice to $total")
    }

    private fun updateTotalDiscountPercentage() {
        val cartList = cartItems.value ?: emptyList()
        val allPacks = _packDisplayList.value ?: emptyList()
        val totalDiscount = cartList.sumByDouble { cartItem ->
            val pack = allPacks.find { it.id == cartItem.packId }
            (pack?.discountPercentage?.toDouble() ?: 0.0) * (cartItem.quantity ?: 1)
        }
        _totalDiscountPercentage.value = totalDiscount
        Log.d("EmojiStoreViewModel", "Updated totalDiscountPercentage to $totalDiscount%")
    }

    private fun updateDiscountAmount() {
        val totalPrice = _totalCartPrice.value ?: 0.0
        val discountPercent = _totalDiscountPercentage.value ?: 0.0
        val discountAmountValue = (totalPrice * (discountPercent / 100))
        _discountAmount.value = discountAmountValue
        Log.d("EmojiStoreViewModel", "Updated discountAmount to $discountAmountValue")
    }

    fun filterPacksByCategory(categoryName: String) {
        val filteredList = _packDisplayList.value?.filter { pack ->
            pack.categories.any { it.name.contains(categoryName, ignoreCase = true) }
        } ?: emptyList()
        _filteredPackList.postValue(filteredList)
    }

    fun addToCart(packId: Int) {
        viewModelScope.launch {
            try {
                repository.addToCart(packId)
                Log.d("EmojiStoreViewModel", "Added to cart: packId=$packId")
            } catch (e: Exception) {
                Log.e("EmojiStoreViewModel", "Error adding to cart: ${e.message}", e)
            }
        }
    }

    fun removeFromCart(packId: Int) {
        viewModelScope.launch {
            try {
                repository.removeFromCart(packId)
                updateCartDisplayItems()
                Log.d("EmojiStoreViewModel", "Removed from cart: packId=$packId")
            } catch (e: Exception) {
                Log.e("EmojiStoreViewModel", "Error removing from cart: ${e.message}", e)
            }
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            try {
                repository.clearCart()
                Log.d("EmojiStoreViewModel", "Cart cleared")
            } catch (e: Exception) {
                Log.e("EmojiStoreViewModel", "Error clearing cart: ${e.message}", e)
            }
        }
    }

    fun addToMy(packId: Int) {
        viewModelScope.launch {
            try {
                repository.addToMy(packId)
                Log.d("EmojiStoreViewModel123", "Added to cart: packId=$packId")
            } catch (e: Exception) {
                Log.e("EmojiStoreViewModel", "Error adding to cart: ${e.message}", e)
            }
        }
    }

    fun removeFromMy(packId: Int) {
        viewModelScope.launch {
            try {
                repository.removeFromMy(packId)
                updateCartDisplayMys()
                Log.d("EmojiStoreViewModel", "Removed from cart: packId=$packId")
            } catch (e: Exception) {
                Log.e("EmojiStoreViewModel", "Error removing from cart: ${e.message}", e)
            }
        }
    }

    fun clearMy() {
        viewModelScope.launch {
            try {
                repository.clearMy()
                Log.d("EmojiStoreViewModel", "Cart cleared")
            } catch (e: Exception) {
                Log.e("EmojiStoreViewModel", "Error clearing cart: ${e.message}", e)
            }
        }
    }
}