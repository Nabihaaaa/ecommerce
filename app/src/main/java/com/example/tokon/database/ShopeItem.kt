package com.example.tokon.database

import java.io.Serializable


data class ShopeItem(
    var id: String? = null,
    var namaToko: String? = null,
    var judul: String? = null,
    var deskripsi: String? = null,
    var harga: Long? = null,
    var category: String? = null
)