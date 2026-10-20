package com.chat.ketukprojectmap.data.remote



import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.compose.auth.ComposeAuth
import io.github.jan.supabase.compose.auth.googleNativeLogin
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest

object SupabaseProvider {
    val client: SupabaseClient by lazy {
        createSupabaseClient(
            supabaseUrl = com.chat.ketukprojectmap.BuildConfig.SUPABASE_URL,
            supabaseKey = com.chat.ketukprojectmap.BuildConfig.SUPABASE_ANON_KEY
        ) {
            install(Auth)
            install(Postgrest)
            install(ComposeAuth) {
                googleNativeLogin(serverClientId = com.chat.ketukprojectmap.BuildConfig.GOOGLE_WEB_CLIENT_ID)
            }
        }
    }
}