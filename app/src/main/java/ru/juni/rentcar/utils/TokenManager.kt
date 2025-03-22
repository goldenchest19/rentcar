package ru.juni.rentcar.utils

import android.content.Context
import android.content.SharedPreferences

/**
 * Менеджер для управления токенами авторизации в приложении.
 * Реализует паттерн Singleton для обеспечения единой точки доступа к управлению токенами.
 * Хранит токен и время его истечения в SharedPreferences.
 */
class TokenManager private constructor(context: Context) {
    /**
     * SharedPreferences для хранения данных авторизации.
     * Использует приватный режим доступа для обеспечения безопасности.
     */
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    companion object {
        /**
         * Имя файла SharedPreferences для хранения данных авторизации.
         */
        private const val PREFS_NAME = "auth_prefs"
        
        /**
         * Ключ для хранения токена доступа в SharedPreferences.
         */
        private const val KEY_ACCESS_TOKEN = "access_token"
        
        /**
         * Ключ для хранения времени истечения токена в SharedPreferences.
         */
        private const val KEY_TOKEN_EXPIRY = "token_expiry"

        /**
         * Экземпляр TokenManager, реализующий паттерн Singleton.
         * Аннотация @Volatile обеспечивает видимость изменений между потоками.
         */
        @Volatile
        private var instance: TokenManager? = null

        /**
         * Получает единственный экземпляр TokenManager.
         * Реализует потокобезопасный паттерн Singleton с двойной проверкой блокировки.
         * 
         * @param context Контекст приложения
         * @return Экземпляр TokenManager
         */
        fun getInstance(context: Context): TokenManager {
            return instance ?: synchronized(this) {
                instance ?: TokenManager(context.applicationContext).also { instance = it }
            }
        }
    }

    /**
     * Сохраняет токен авторизации и время его истечения.
     * 
     * @param token Токен авторизации для сохранения
     * @param expiryTime Время истечения токена в миллисекундах
     */
    fun saveToken(token: String, expiryTime: Long) {
        sharedPreferences.edit()
            .putString(KEY_ACCESS_TOKEN, token)
            .putLong(KEY_TOKEN_EXPIRY, expiryTime)
            .apply()
    }

    /**
     * Получает сохраненный токен авторизации.
     * 
     * @return Токен авторизации или null, если токен не сохранен
     */
    fun getToken(): String? {
        return sharedPreferences.getString(KEY_ACCESS_TOKEN, null)
    }

    /**
     * Проверяет валидность текущего токена.
     * Токен считается валидным, если он существует и не истек срок его действия.
     * 
     * @return true если токен валиден, false в противном случае
     */
    fun isTokenValid(): Boolean {
        val token = getToken() ?: return false
        val expiryTime = sharedPreferences.getLong(KEY_TOKEN_EXPIRY, 0)
        return token.isNotEmpty() && System.currentTimeMillis() < expiryTime
    }

    /**
     * Удаляет сохраненный токен и время его истечения.
     * Используется при выходе пользователя из системы.
     */
    fun clearToken() {
        sharedPreferences.edit()
            .remove(KEY_ACCESS_TOKEN)
            .remove(KEY_TOKEN_EXPIRY)
            .apply()
    }
} 