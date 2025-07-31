package info.example.projectnewali.Roomdatabase2.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import info.example.projectnewali.Roomdatabase2.CartItem
import info.example.projectnewali.Roomdatabase2.CartMy
import info.example.projectnewali.Roomdatabase2.Category
import info.example.projectnewali.Roomdatabase2.Emoji
import info.example.projectnewali.Roomdatabase2.EmojiPack
import info.example.projectnewali.Roomdatabase2.PackCategoryCrossRef
import info.example.projectnewali.Roomdatabase2.dao.CartDao
import info.example.projectnewali.Roomdatabase2.dao.CartMyDao
import info.example.projectnewali.Roomdatabase2.dao.CategoryDao
import info.example.projectnewali.Roomdatabase2.dao.EmojiDao
import info.example.projectnewali.Roomdatabase2.dao.EmojiPackDao
import info.example.projectnewali.Roomdatabase2.dao.PackCategoryCrossRefDao // اضافه شده

@Database(
    entities = [Category::class, EmojiPack::class, Emoji::class, PackCategoryCrossRef::class, CartItem::class,CartMy::class],
    version = 6,
    exportSchema = false
)
abstract class EmojiStoreDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun emojiPackDao(): EmojiPackDao
    abstract fun emojiDao(): EmojiDao
    abstract fun packCategoryCrossRefDao(): PackCategoryCrossRefDao // اضافه شده
    abstract fun cartDao() : CartDao
    abstract fun cartMy() : CartMyDao

    companion object {
        @Volatile
        private var INSTANCE: EmojiStoreDatabase? = null

        fun getDatabase(context: Context): EmojiStoreDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    EmojiStoreDatabase::class.java,
                    "emoji_store_database"
                ).fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}