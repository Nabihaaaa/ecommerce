package com.example.tokon.adapter

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tokon.database.ShopeItem
import com.example.tokon.databinding.AdapterTokoitemBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import java.io.File

class ItemtokoAdapter(
    private var items: ArrayList<ShopeItem>,
) : RecyclerView.Adapter<ItemtokoAdapter.ViewHolder>() {
    class ViewHolder(val binding: AdapterTokoitemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        AdapterTokoitemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.binding.tokoText.text = item.namaToko
        holder.binding.judulText.text = item.judul
        holder.binding.deskripsiText.text = item.deskripsi
        holder.binding.price.text = "IDR ${item.harga.toString()}"
        holder.binding.categoryText.text = item.category

        val auth = FirebaseAuth.getInstance()
        val useremail = auth.currentUser?.email.toString()
        val storageReference = FirebaseStorage.getInstance().reference
        val loc = storageReference.child("Toko/$useremail/${item.id}")
        val localfile = File.createTempFile("tempImage", "jpeg")
        loc.getFile(localfile).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
            holder.binding.image.setImageBitmap(bitmap)
        }

        /*holder.itemView.setOnClickListener{
             listener.onDetail(item)
         }*/

        /*holder.binding.bt0.setOnClickListener {
            listener.onUpdate(item)
        }*/
    }


    override fun getItemCount() = items.size


}

