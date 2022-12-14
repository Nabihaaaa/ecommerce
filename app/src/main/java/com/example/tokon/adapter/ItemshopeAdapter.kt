package com.example.tokon.adapter

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.tokon.database.ShopeItem
import com.example.tokon.databinding.AdapterItemshopeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import java.io.File


class ItemshopeAdapter(
    private var items: ArrayList<ShopeItem>,
) : RecyclerView.Adapter<ItemshopeAdapter.ViewHolder>() {
    class ViewHolder(val binding: AdapterItemshopeBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        AdapterItemshopeBinding.inflate(
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

        val storageReference = FirebaseStorage.getInstance().reference
        val loc = storageReference.child("keyboard.jfif")
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

    /*fun  addList(list: List<ShopeItem>){
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }*/

    /* interface AdapterListener{
         //fun onUpdate(taskModel: ShopeItem)
         fun onDetail(itemModel: ShopeItem)
     }*/
}


