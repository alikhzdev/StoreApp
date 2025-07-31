package info.example.projectnewali.Roomdatabase2.Repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import info.example.projectnewali.Roomdatabase2.Category
import info.example.projectnewali.Roomdatabase2.Emoji
import info.example.projectnewali.Roomdatabase2.EmojiPack
import info.example.projectnewali.Roomdatabase2.PackCategoryCrossRef
import info.example.projectnewali.Roomdatabase2.CartItem
import info.example.projectnewali.Roomdatabase2.CartMy
import info.example.projectnewali.Roomdatabase2.dao.CategoryDao
import info.example.projectnewali.Roomdatabase2.dao.EmojiDao
import info.example.projectnewali.Roomdatabase2.dao.EmojiPackDao
import info.example.projectnewali.Roomdatabase2.dao.PackCategoryCrossRefDao
import info.example.projectnewali.Roomdatabase2.dao.CartDao
import info.example.projectnewali.Roomdatabase2.dao.CartMyDao
import info.example.projectnewali.Roomdatabase2.model.EmojiPackWithEmojis
import info.example.projectnewali.Roomdatabase2.model.JsonCategory
import info.example.projectnewali.Roomdatabase2.model.PackWithCategories
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStreamReader

class EmojiStoreRepository(
    private val categoryDao: CategoryDao,
    private val emojiPackDao: EmojiPackDao,
    private val emojiDao: EmojiDao,
    private val packCategoryCrossRefDao: PackCategoryCrossRefDao,
    private val cartDao: CartDao,
    private val cartMyDao: CartMyDao,
    private val context: Context
) {
    val allPacks: LiveData<List<EmojiPackWithEmojis>> = emojiPackDao.getAllPacks()
    val packsWithCategories: LiveData<List<PackWithCategories>> = emojiPackDao.getPacksWithCategories()
    val cartItems: LiveData<List<CartItem>> = cartDao.getAllCartItems() // استفاده مستقیم از LiveData DAO
    val cartMys :  LiveData<List<CartMy>> = cartMyDao.getAllCartItems() // استفاده مستقیم از LiveData DAO

    suspend fun initializeDatabase() {
        withContext(Dispatchers.IO) {
            if (categoryDao.getAllCategories().value.isNullOrEmpty()) {
                Log.d("Repository", "Database is empty, starting initialization")

                val inputStream = context.assets.open("emojis2.json")
                val jsonString = InputStreamReader(inputStream).use { it.readText() }
                val gson = Gson()
                val type = object : TypeToken<List<JsonCategory>>() {}.type
                val jsonCategories: List<JsonCategory> = gson.fromJson(jsonString, type)

                val categories = jsonCategories.map { Category(id = it.id, name = it.name) }
                Log.d("Repository", "Inserting categories: $categories")
                categoryDao.insertAll(categories)

                val packs = jsonCategories.flatMap { it.packs }.map { jsonPack ->
                    EmojiPack(
                        id = jsonPack.id,
                        name = jsonPack.name,
                        price = jsonPack.price,
                        discountPrice = jsonPack.discountPrice,
                        imageUrl = jsonPack.imageUrl,
                        primaryCategoryId = jsonPack.categoryIds.first(),
                        isSpecial = jsonPack.isSpecial,
                        count = jsonPack.count
                    )
                }
                Log.d("Repository", "Inserting packs: $packs")
                emojiPackDao.insertAll(packs)

                val emojis = jsonCategories.flatMap { it.packs }.flatMap { jsonPack ->
                    jsonPack.emojis.map { jsonEmoji ->
                        Emoji(id = jsonEmoji.id, unicode = jsonEmoji.unicode, packId = jsonPack.id)
                    }
                }
                Log.d("Repository", "Inserting emojis: ${emojis.size} items")
                emojiDao.insertAll(emojis)

                val crossRefs = jsonCategories.flatMap { it.packs }.flatMap { jsonPack ->
                    jsonPack.categoryIds.map { categoryId ->
                        PackCategoryCrossRef(packId = jsonPack.id, categoryId = categoryId)
                    }
                }
                Log.d("Repository", "Inserting crossRefs: $crossRefs")
                try {
                    packCategoryCrossRefDao.insertAll(crossRefs)
                    Log.d("Repository", "CrossRefs inserted successfully")
                } catch (e: Exception) {
                    Log.e("Repository", "Error inserting crossRefs: ${e.message}", e)
                    throw e
                }

                Log.d("Repository", "Database initialization completed")
            } else {
                Log.d("Repository", "Database already initialized, skipping")
            }
        }
    }

    suspend fun addToCart(packId: Int) {
        withContext(Dispatchers.IO) {
            val cartItem = CartItem(packId = packId)
            cartDao.insertCartItem(cartItem)
            Log.d("Repository", "Added to cart: packId=$packId")
        }
    }

    suspend fun removeFromCart(packId: Int) {
        withContext(Dispatchers.IO) {
            Log.d("Repository", "Attempting to remove cart item with packId=$packId")
            try {
                val cartItem = cartDao.getCartItemByPackId(packId)
                cartItem?.let {
                    cartDao.deleteCartItem(it)
                    Log.d("Repository", "Removed from cart: packId=$packId")
                } ?: Log.w("Repository", "CartItem not found for packId=$packId")
            } catch (e: Exception) {
                Log.e("Repository", "Error removing from cart: ${e.message}", e)
            }
        }
    }


    suspend fun clearCart() {
        withContext(Dispatchers.IO) {
            cartDao.clearCart()
            Log.d("Repository", "Cart cleared")
        }
    }


    suspend fun addToMy(packId: Int) {
        withContext(Dispatchers.IO) {
            val cartItem = CartMy(packId = packId)
            cartMyDao.insertCartItem(cartItem)
            Log.d("Repository", "Added to cart: packId=$packId")
        }
    }

    suspend fun removeFromMy(packId: Int) {
        withContext(Dispatchers.IO) {
            Log.d("Repository", "Attempting to remove cart item with packId=$packId")
            try {
                val cartItem = cartMyDao.getCartItemByPackId(packId)
                cartItem?.let {
                    cartMyDao.deleteCartItem(it)
                    Log.d("Repository", "Removed from cart: packId=$packId")
                } ?: Log.w("Repository", "CartItem not found for packId=$packId")
            } catch (e: Exception) {
                Log.e("Repository", "Error removing from cart: ${e.message}", e)
            }
        }
    }


    suspend fun clearMy() {
        withContext(Dispatchers.IO) {
            cartMyDao.clearCart()
            Log.d("Repository", "Cart cleared")
        }
    }
}