package com.example.tokon.fragment

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.tokon.R
import com.example.tokon.activity.LoginActivity
import com.example.tokon.activity.TokoActivity
import com.example.tokon.adapter.ItemshopeAdapter
import com.example.tokon.database.ShopeItem
import com.example.tokon.databinding.FragmentHomeBinding
import com.example.tokon.util.UtilFirestore
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var itemRecyclerView: RecyclerView
    private lateinit var itemArrayList: ArrayList<ShopeItem>
    private lateinit var dbref: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var userRef: DocumentReference
    private lateinit var db: FirebaseFirestore


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        auth = Firebase.auth
        db = FirebaseFirestore.getInstance()
        itemRecyclerView = binding.itemShope
        itemArrayList = arrayListOf()
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        setupList()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupMoney()
        setupListener()
    }

    private fun setupMoney() {
        val useremail = auth.currentUser?.email
        db.collection("users").document(useremail.toString())
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    binding.textmoney.text = document.getLong("money").toString()
                    binding.coin.text = document.getLong("coin").toString()
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(
                    context, "Failed Load Data",
                    Toast.LENGTH_SHORT
                ).show()
            }

    }

    private fun setupList() {
        itemRecyclerView.adapter = ItemshopeAdapter(itemArrayList)
        db.collection("Items")
            .get()
            .addOnSuccessListener { result ->
                for (document in result.documents) {
                    val item = document.toObject(ShopeItem::class.java)
                    itemArrayList.add(item!!)
                }
                itemRecyclerView.adapter = ItemshopeAdapter(itemArrayList)
            }
            .addOnFailureListener { exception ->
                Toast.makeText(
                    context, "Failed Load Data",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun getItemData(category: String) {
        itemArrayList.clear()
        db.collection("Items")
            .get()
            .addOnSuccessListener { result ->
                for (document in result.documents) {
                    val item = document.toObject(ShopeItem::class.java)
                    if (item?.category == category) {
                        itemArrayList.add(item)
                    }
                }
                itemRecyclerView.adapter = ItemshopeAdapter(itemArrayList)
            }
            .addOnFailureListener { exception ->
                Toast.makeText(
                    context, "Failed Load Data",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun setupListener() {
        var category: String
        binding.menu.setOnClickListener {
            PopupMenu(requireActivity(), it).apply {
                setOnMenuItemClickListener { item ->
                    when (item?.itemId) {
                        R.id.action_logout -> {
                            startActivity(Intent(requireActivity(), LoginActivity::class.java))
                            auth.signOut()
                            true
                        }
                        R.id.buka_toko -> {
                            startActivity(Intent(requireActivity(), TokoActivity::class.java))
                            true
                        }
                        else -> false
                    }
                }
                inflate(R.menu.menu_home)
                show()
            }
        }
        binding.rectangle1.setOnClickListener {
            binding.rectangle1.setCardBackgroundColor(Color.parseColor("#F6DDCC"))
            binding.rectangle2.setCardBackgroundColor(Color.WHITE)
            binding.rectangle3.setCardBackgroundColor(Color.WHITE)
            binding.rectangle4.setCardBackgroundColor(Color.WHITE)
            category = "cosmetics"
            getItemData(category)
        }
        binding.rectangle2.setOnClickListener {
            binding.rectangle2.setCardBackgroundColor(Color.parseColor("#F6DDCC"))
            binding.rectangle1.setCardBackgroundColor(Color.WHITE)
            binding.rectangle3.setCardBackgroundColor(Color.WHITE)
            binding.rectangle4.setCardBackgroundColor(Color.WHITE)
            category = "electronics"
            getItemData(category)
        }
        binding.rectangle3.setOnClickListener {
            binding.rectangle3.setCardBackgroundColor(Color.parseColor("#F6DDCC"))
            binding.rectangle1.setCardBackgroundColor(Color.WHITE)
            binding.rectangle2.setCardBackgroundColor(Color.WHITE)
            binding.rectangle4.setCardBackgroundColor(Color.WHITE)
            category = "games"
            getItemData(category)
        }
        binding.rectangle4.setOnClickListener {
            binding.rectangle4.setCardBackgroundColor(Color.parseColor("#F6DDCC"))
            binding.rectangle1.setCardBackgroundColor(Color.WHITE)
            binding.rectangle2.setCardBackgroundColor(Color.WHITE)
            binding.rectangle3.setCardBackgroundColor(Color.WHITE)
            category = "clothes"
            getItemData(category)
        }
    }
}

