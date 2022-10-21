package com.example.tokon.database

import java.io.Serializable


class ShopeItem (
    var img : String ?= null,
    var namaToko: String ?= null,
    var judul: String ?= null,
    var deskripsi: String ?= null,
    var harga: Long ?= null,
    var category: String ?= null
)