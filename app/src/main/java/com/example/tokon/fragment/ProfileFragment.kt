package com.example.tokon.fragment

import android.app.Activity.RESULT_OK
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.tokon.R
import com.example.tokon.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException


class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var userRef: DocumentReference
    private lateinit var imageUri: Uri
    private lateinit var auth: FirebaseAuth
    private lateinit var storageReference: StorageReference
    private lateinit var progressDialog: ProgressDialog


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBinding()
        setupListener()
    }


    private fun setupListener() {
        val popupMenu = PopupMenu(requireActivity(), binding.camera)
        popupMenu.menuInflater.inflate(R.menu.img_profile, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_camera -> {
                    camera()
                    true
                }
                R.id.action_gallery -> {
                    binding.camera.setImageURI(null)
                    selectImage()
                    true
                }
                R.id.action_cancel -> {
                    true
                }
                else -> false
            }
        }
        binding.camera.setOnClickListener {
            popupMenu.show()
        }

    }

    private fun uploadImage() {
        try {
            progressDialog = ProgressDialog(requireActivity())
            progressDialog.setTitle("Uploading File....")
            progressDialog.show()

            val useremail = auth.currentUser?.email
            storageReference = FirebaseStorage.getInstance().reference
            val loc = storageReference.child("images/$useremail")
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
        } catch (e: Exception) {
            progressDialog.dismiss()
            Toast.makeText(requireActivity(), "Profile already saved", Toast.LENGTH_SHORT).show()
        }
    }

    private fun camera() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { intent ->
            activity?.packageManager?.let {
                intent.resolveActivity(it).also {
                    startActivityForResult(intent, 100)
                }
            }
        }
    }

    private fun selectImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, 100)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && data != null && data.data != null && resultCode == RESULT_OK) {
            imageUri = data.data!!
            binding.camera.setImageURI(imageUri)
            uploadImage()
        }

    }


    private fun setupBinding() {
        auth = Firebase.auth
        val useremail = auth.currentUser?.email
        //imageProfile
        storageReference = FirebaseStorage.getInstance().reference
        val loc = storageReference.child("images/$useremail")
        val localfile = File.createTempFile("tempImage", "jpeg")
        loc.getFile(localfile).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
            binding.camera.setImageBitmap(bitmap)
        }
        //Load data
        db = FirebaseFirestore.getInstance()
        userRef = db.document("users/${useremail}")
        userRef
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Toast.makeText(requireActivity(), "Error", Toast.LENGTH_SHORT).show()
                }
                if (snapshot != null && snapshot.exists()) {
                    val username = snapshot.getString("username")
                    val email = snapshot.getString("email")
                    binding.usernameEtext.text = username
                    binding.emailEtext.text = email
                } else {
                    binding.usernameEtext.text = ""
                    binding.emailEtext.text = ""
                }
            }


    }
}