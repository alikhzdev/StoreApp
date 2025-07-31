package info.example.projectnewali.Roomdatabase2.dao

import androidx.room.Transaction
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Embedded
import androidx.room.Insert
import androidx.room.Junction
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Relation
import info.example.projectnewali.Roomdatabase2.CartItem
import info.example.projectnewali.Roomdatabase2.CartMy
import info.example.projectnewali.Roomdatabase2.Category
import info.example.projectnewali.Roomdatabase2.Emoji
import info.example.projectnewali.Roomdatabase2.EmojiPack
import info.example.projectnewali.Roomdatabase2.PackCategoryCrossRef
import info.example.projectnewali.Roomdatabase2.model.EmojiPackWithEmojis
import info.example.projectnewali.Roomdatabase2.model.PackWithCategories

@Dao
interface CategoryDao {
    @Query("SELECT * FROM categories")
    fun getAllCategories(): LiveData<List<Category>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(categories: List<Category>)

    @Transaction
    @Query("SELECT * FROM categories")
    fun getCategoriesWithPacks(): LiveData<List<CategoryWithPacks>>
}

data class CategoryWithPacks(
    @Embedded val category: Category,
    @Relation(
        parentColumn = "id", // id دسته‌بندی
        entity = EmojiPack::class,
        entityColumn = "id", // id پک
        associateBy = Junction(
            value = PackCategoryCrossRef::class,
            parentColumn = "categoryId", // ستون تو PackCategoryCrossRef
            entityColumn = "packId" // ستون تو PackCategoryCrossRef
        )
    )
    val packs: List<EmojiPack>
)

@Dao
interface EmojiPackDao {
    @Transaction
    @Query("SELECT * FROM emoji_packs")
    fun getAllPacks(): LiveData<List<EmojiPackWithEmojis>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(packs: List<EmojiPack>)

    @Transaction
    @Query("SELECT * FROM emoji_packs")
    fun getPacksWithCategories(): LiveData<List<PackWithCategories>>
}

data class PackWithCategories(
    @Embedded val emojiPack: EmojiPack,
    @Relation(
        parentColumn = "id", // id پک
        entity = Category::class,
        entityColumn = "id", // id دسته‌بندی
        associateBy = Junction(
            value = PackCategoryCrossRef::class,
            parentColumn = "packId", // ستون تو PackCategoryCrossRef
            entityColumn = "categoryId" // ستون تو PackCategoryCrossRef
        )
    )
    val categories: List<Category>
)

@Dao
interface EmojiDao {
    @Query("SELECT * FROM emojis WHERE packId = :packId")
    fun getEmojisForPack(packId: Int): LiveData<List<Emoji>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(emojis: List<Emoji>)
}

@Dao
interface PackCategoryCrossRefDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(crossRefs: List<PackCategoryCrossRef>)

    @Query("DELETE FROM pack_category_cross_ref")
    suspend fun deleteAll()
}


@Dao
interface CartDao {
    @Insert
    suspend fun insertCartItem(cartItem: CartItem)

    @Query("SELECT * FROM cart_items")
    fun getAllCartItems(): LiveData<List<CartItem>>

    @Delete
    suspend fun deleteCartItem(cartItem: CartItem)

    @Query("DELETE FROM cart_items WHERE packId = :packId")
    suspend fun deleteCartItemByPackId(packId: Int)

    @Query("DELETE FROM cart_items")
    suspend fun clearCart()

    @Query("SELECT * FROM cart_items WHERE packId = :packId LIMIT 1")
    suspend fun getCartItemByPackId(packId: Int): CartItem?
}


@Dao
interface CartMyDao {
    @Insert
    suspend fun insertCartItem(cartItem: CartMy)

    @Query("SELECT * FROM cart_mys")
    fun getAllCartItems(): LiveData<List<CartMy>>

    @Delete
    suspend fun deleteCartItem(cartItem: CartMy)

    @Query("DELETE FROM cart_mys WHERE packId = :packId")
    suspend fun deleteCartItemByPackId(packId: Int)

    @Query("DELETE FROM cart_mys")
    suspend fun clearCart()

    @Query("SELECT * FROM cart_mys WHERE packId = :packId LIMIT 1")
    suspend fun getCartItemByPackId(packId: Int): CartMy?
}