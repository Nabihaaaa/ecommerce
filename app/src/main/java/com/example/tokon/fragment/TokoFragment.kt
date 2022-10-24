package com.example.tokon.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.tokon.R
import com.example.tokon.adapter.ItemshopeAdapter
import com.example.tokon.adapter.ItemtokoAdapter
import com.example.tokon.database.ShopeItem
import com.example.tokon.databinding.FragmentTokoBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class TokoFragment : Fragment() {

    private lateinit var binding: FragmentTokoBinding
    private lateinit var itemArrayList: ArrayList<ShopeItem>
    private lateinit var dbref: DatabaseReference
    private lateinit var itemRecyclerView: RecyclerView
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTokoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        SetupData()
        SetupListener()
    }

    private fun SetupData() {
        itemArrayList = arrayListOf()
        itemRecyclerView = binding.itemJualan
        var useremail = auth.currentUser?.email.toString()
        useremail = useremail.replace(".", "")
        dbref = FirebaseDatabase.getInstance().getReference("Toko/$useremail")
        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (itemSnapshot in snapshot.children) {
                        val item = itemSnapshot.getValue(ShopeItem::class.java)
                        itemArrayList.add(item!!)
                    }
                    itemRecyclerView.adapter = ItemtokoAdapter(itemArrayList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun SetupListener() {
        binding.buttonTambah.setOnClickListener {
            findNavController().navigate(R.id.action_tokoFragment_to_sellFragment)
        }
    }


}