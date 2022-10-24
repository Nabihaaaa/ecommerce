package com.example.tokon.fragment

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.tokon.R
import com.example.tokon.database.ShopeItem
import com.example.tokon.databinding.FragmentSellBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

class SellFragment : Fragment() {

    private lateinit var binding: FragmentSellBinding
    private lateinit var itemArrayList: ArrayList<ShopeItem>
    private lateinit var DbRef: DatabaseReference
    private lateinit var imageUri: Uri
    private lateinit var auth: FirebaseAuth
    private lateinit var item: ShopeItem


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSellBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        item = ShopeItem()
        setupListener()
    }

    private fun SetupData() {

        var Useremail = auth.currentUser?.email.toString()
        Useremail = Useremail.replace(".", "")
        DbRef = FirebaseDatabase.getInstance().reference.child("Toko/$Useremail")

        item.namaToko = binding.namaTokoEtext.text.toString()
        item.judul = binding.JudulEtext.text.toString()
        item.deskripsi = binding.deskripsiEtext.text.toString()
        item.harga = binding.hargaEtext.text.toString().toLong()
        item.category = binding.categoryEtext.text.toString()
        item.id = DbRef.key

        itemArrayList = arrayListOf()
        itemArrayList.add(item)

        DbRef.push().setValue(itemArrayList)
            .addOnSuccessListener {
                Toast.makeText(
                    requireActivity(), "Data Saved",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .addOnFailureListener {
                Toast.makeText(
                    requireActivity(), "Data Failed  Saved",
                    Toast.LENGTH_SHORT
                ).show()
            }
        uploadImage()
    }

    private fun setupListener() {
        binding.saveButton.setOnClickListener {
            SetupData()
            //findNavController().navigate(R.id.tokoFragment)
        }
        /*binding.uploadImgFile.setOnClickListener {
            selectImage()
        }*/
    }

    private fun selectImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && data != null && data.data != null && resultCode == Activity.RESULT_OK) {
            imageUri = data.data!!
            binding.camera.setImageURI(imageUri)

        }

    }

    private fun uploadImage() {
        val progressDialog = ProgressDialog(requireActivity())
        progressDialog.setTitle("Uploading File....")
        progressDialog.show()

        val useremail = auth.currentUser?.email
        val storageReference = FirebaseStorage.getInstance().reference
        val loc = storageReference.child("Toko/$useremail/${item.id}")
        val uploadTask = loc.putFile(imageUri)

        uploadTask.addOnSuccessListener {
            Toast.makeText(requireActivity(), "Successfully Upload", Toast.LENGTH_SHORT).show()
            if (progressDialog.isShowing) {
                progressDialog.dismiss()
            }

        }.addOnFailureListener {
            Toast.makeText(requireActivity(), "Failed to Upload", Toast.LENGTH_SHORT).show()
            if (progressDialog.isShowing) {
                progressDialog.dismiss()
            }
        }
    }
}