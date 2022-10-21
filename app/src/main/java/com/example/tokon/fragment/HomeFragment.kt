package com.example.tokon.fragment

import android.content.ClipData
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Visibility
import com.example.tokon.R
import com.example.tokon.activity.DetailActivity
import com.example.tokon.activity.LoginActivity
import com.example.tokon.activity.TokoActivity
import com.example.tokon.adapter.ItemshopeAdapter
import com.example.tokon.database.ShopeItem
import com.example.tokon.database.User
import com.example.tokon.databinding.AdapterItemshopeBinding
import com.example.tokon.databinding.FragmentHomeBinding
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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupMoney()
        setupListener()
    }

    private fun setupMoney() {
        val useremail = auth.currentUser?.email
        db = FirebaseFirestore.getInstance()
        userRef = db.document("users/${useremail}")
        userRef
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Toast.makeText(requireActivity(), "Error", Toast.LENGTH_SHORT).show()
                }
                if (snapshot != null && snapshot.exists()) {
                    val money = snapshot.getLong("money")
                    val coin = snapshot.getLong("coin")
                    binding.textmoney.text = money.toString()
                    binding.coin.text = coin.toString()
                } else {
                    binding.textmoney.text = "0"
                    binding.coin.text = "0"
                }
            }
    }

    override fun onStart() {
        super.onStart()
        setupList()
    }

    private fun setupList() {
        itemRecyclerView = binding.itemShope
        itemArrayList = arrayListOf()
        getItemData()
        //EventhangeListener()
    }

    private fun getItemData() {
        dbref = FirebaseDatabase.getInstance().getReference("Item")
        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (itemSnapshot in snapshot.children) {
                        val item = itemSnapshot.getValue(ShopeItem::class.java)
                        itemArrayList.add(item!!)
                    }
                    itemRecyclerView.adapter = ItemshopeAdapter(itemArrayList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
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
                            startActivity(Intent(requireActivity(),TokoActivity::class.java))
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
            setItem(category)
        }
        binding.rectangle2.setOnClickListener {
            binding.rectangle2.setCardBackgroundColor(Color.parseColor("#F6DDCC"))
            binding.rectangle1.setCardBackgroundColor(Color.WHITE)
            binding.rectangle3.setCardBackgroundColor(Color.WHITE)
            binding.rectangle4.setCardBackgroundColor(Color.WHITE)
            category = "electronics"
            setItem(category)
        }
        binding.rectangle3.setOnClickListener {
            binding.rectangle3.setCardBackgroundColor(Color.parseColor("#F6DDCC"))
            binding.rectangle1.setCardBackgroundColor(Color.WHITE)
            binding.rectangle2.setCardBackgroundColor(Color.WHITE)
            binding.rectangle4.setCardBackgroundColor(Color.WHITE)
            category = "games"
            setItem(category)
        }
        binding.rectangle4.setOnClickListener {
            binding.rectangle4.setCardBackgroundColor(Color.parseColor("#F6DDCC"))
            binding.rectangle1.setCardBackgroundColor(Color.WHITE)
            binding.rectangle2.setCardBackgroundColor(Color.WHITE)
            binding.rectangle3.setCardBackgroundColor(Color.WHITE)
            category = "clothes"
            setItem(category)
        }

    }

    private fun setItem(category: String) {
        itemArrayList.clear()
        dbref = FirebaseDatabase.getInstance().getReference("Item")
        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (itemSnapshot in snapshot.children) {
                        val item = itemSnapshot.getValue(ShopeItem::class.java)
                        if (item != null) {
                            if (item.category == category)
                                itemArrayList.add(item)
                        }
                    }
                    itemRecyclerView.adapter = ItemshopeAdapter(itemArrayList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}

