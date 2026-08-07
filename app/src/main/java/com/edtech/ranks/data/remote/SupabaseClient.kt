package com.edtech.ranks.data.remote

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage

// Replace these placeholders with your actual Supabase project credentials.
const val SUPABASE_URL = "https://yhhbtrgmgpwtrnoyuxea.supabase.co"
const val SUPABASE_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InloaGJ0cmdtZ3B3dHJub3l1eGVhIiwicm9sZSI6ImFub24iLCJpYXQiOjE3ODQ5MDY3MDUsImV4cCI6MjEwMDQ4MjcwNX0.aBpXcKd9uJ7KE2v0up2axChk1Zq_0NUto-SSI_RYShQ"

val supabase = createSupabaseClient(
    supabaseUrl = SUPABASE_URL,
    supabaseKey = SUPABASE_KEY
) {
    install(Auth) {
        scheme = "ranks"
        host = "login"
    }
    install(Postgrest)
    install(Storage)
}
