package info.example.projectnewali.Roomdatabase2

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

// جدول دسته‌بندی‌ها
@Entity(tableName = "categories")
data class Category(
    @PrimaryKey val id: Int,
    val name: String
)

// جدول پک‌ها
@Entity(
    tableName = "emoji_packs",
    foreignKeys = [
        ForeignKey(
            entity = Category::class,
            parentColumns = ["id"],
            childColumns = ["primaryCategoryId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["primaryCategoryId"])]
)
data class EmojiPack(
    @PrimaryKey val id: Int,
    val name: String,
    val price: Double,
    val count: String, // نگه داشتیم به‌عنوان String (باید تو UI مدیریت بشه)
    val discountPrice: Double?, // null اگه تخفیف نداره
    val imageUrl: String,
    val primaryCategoryId: Int,
    val isSpecial: String // مثلاً "Yes" یا "No"
)

// جدول ایموجی‌ها
@Entity(
    tableName = "emojis",
    foreignKeys = [
        ForeignKey(
            entity = EmojiPack::class,
            parentColumns = ["id"],
            childColumns = ["packId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["packId"])]
)
data class Emoji(
    @PrimaryKey val id: Int,
    val unicode: String,
    val packId: Int
)

// جدول واسطه برای رابطه چند‌به‌چند
@Entity(
    tableName = "pack_category_cross_ref",
    primaryKeys = ["packId", "categoryId"],
    foreignKeys = [
        ForeignKey(
            entity = EmojiPack::class,
            parentColumns = ["id"],
            childColumns = ["packId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Category::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class PackCategoryCrossRef(
    val packId: Int,
    val categoryId: Int
)



@Entity(tableName = "cart_items")
data class CartItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val packId: Int, // رفرنس به ID پک تو جدول packs
    val quantity: Int = 1 // تعداد (اختیاری)
)


@Entity(tableName = "cart_mys")
data class CartMy(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val packId: Int, // رفرنس به ID پک تو جدول packs
    val quantity: Int = 1 // تعداد (اختیاری)
)

