package info.example.projectnewali.Roomdatabase2.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation
import info.example.projectnewali.Roomdatabase2.Category
import info.example.projectnewali.Roomdatabase2.Emoji
import info.example.projectnewali.Roomdatabase2.EmojiPack
import info.example.projectnewali.Roomdatabase2.PackCategoryCrossRef

// لود پک‌ها با ایموجی‌ها
data class EmojiPackWithEmojis(
    @Embedded val emojiPack: EmojiPack,
    @Relation(parentColumn = "id", entityColumn = "packId")
    val emojis: List<Emoji>
)

// لود پک‌ها با دسته‌بندی‌ها (برای سرچ)
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
    val categories: List<Category1>
)

// مدل‌های JSON
data class JsonCategory(
    val id: Int,
    val name: String,
    val packs: List<JsonEmojiPack>
)

data class JsonEmojiPack(
    val id: Int,
    val name: String,
    val count: String,
    val price: Double,
    val discountPrice: Double?,
    val imageUrl: String,
    val categoryIds: List<Int>, // لیست IDهای دسته‌بندی‌ها
    val isSpecial: String,
    val emojis: List<JsonEmoji>
)

data class JsonEmoji(
    val id: Int,
    val unicode: String
)

// مدل برای نمایش تو UI
data class PackDisplayData(
    val id: Int,
    val name: String,
    val count: String,
    val originalPrice: Double, // قیمت اصلی
    val discountedPrice: Double?, // قیمت با تخفیف (null اگه تخفیف نداره)
    val discountPercentage: Int?, // درصد تخفیف (محاسبه‌شده)
    val imageUrl: String,
    val isSpecial: String,
    val emojis: List<Emoji>, // همیشه لیست معتبر (non-nullable)
    val categories: List<Category1>, // همیشه لیست معتبر (non-nullable)
    val cartQuantity: Int? = null
)

data class Category1(
    val id: Int,
    val name: String
) {
    override fun toString(): String {
        return name // مطمئن شو name حروف فارسی رو درست برمی‌گردونه
    }
}


data class CartItem1(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val packId: Int,
    val quantity: Int = 1
)