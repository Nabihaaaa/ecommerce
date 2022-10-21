package com.example.tokon.fragment

import android.content.ContentValues.TAG
import android.os.Bundle
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.tokon.R
import com.example.tokon.database.User
import com.example.tokon.databinding.FragmentRegistersBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import jp.wasabeef.glide.transformations.BlurTransformation

class RegistersFragment : Fragment() {

    private lateinit var binding: FragmentRegistersBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var dataUser: User


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        auth = Firebase.auth
        binding = FragmentRegistersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setuBinding()
        setupData()
        setupListener()
    }

    private fun setuBinding() {
        Glide.with(this).load(R.drawable.ic_pojokkananbawah)
            .apply(RequestOptions.bitmapTransform(BlurTransformation(15, 1)))
            .into(binding.pojokBawah)

       Glide.with(this).load(R.drawable.ic_pojokatas)
            .apply(RequestOptions.bitmapTransform(BlurTransformation(15, 1)))
            .into(binding.pojokAtasKiri)
    }

    private fun setupData() {
        db = FirebaseFirestore.getInstance()
    }

    private fun setupListener() {
        binding.regButtton.setOnClickListener {
            val email: String = binding.emailEtext.text.toString()
            val name: String = binding.nameEtext.text.toString()
            val username: String = binding.usernameEtext.text.toString()
            val password: String = binding.passwordEtext.text.toString()

            if (name.isEmpty()) {
                binding.nameEtext.error = "name harus diisi"
            }
            if (email.isEmpty()) {
                binding.emailEtext.error = "email harus diisi"
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.emailEtext.error = "email tidak valid"
            }
            if (username.isEmpty()) {
                binding.usernameEtext.error = "username harus diisi"
            }
            if (password.isEmpty()) {
                binding.passwordEtext.error = "password harus diisi"
            }
            if (password.length < 5) {
                binding.passwordEtext.error = "password kurang dari 6 huruf"
            }
            if (name.isNotEmpty() && email.isNotEmpty() && username.isNotEmpty() && password.isNotEmpty()) {

                dataUser = User(email, name, username, password, 0, 0)
                db.document("users/$email").set(dataUser)

                RegisterFirebase(email, password)

                binding.emailEtext.text.clear()
                binding.nameEtext.text.clear()
                binding.usernameEtext.text.clear()
                binding.passwordEtext.text.clear()

            }
        }
        binding.regText.makeLinks(
            Pair("login", View.OnClickListener {
                findNavController().navigate(R.id.loginFragment)
            })
        )
    }

    private fun RegisterFirebase(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        requireActivity(), "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

    }

    fun TextView.makeLinks(vararg links: Pair<String, View.OnClickListener>) {
        val spannableString = SpannableString(this.text)
        var startIndexOfLink = -1
        for (link in links) {
            val clickableSpan = object : ClickableSpan() {
                override fun updateDrawState(textPaint: TextPaint) {
                    // use this to change the link color
                    textPaint.color = textPaint.linkColor
                    // toggle below value to enable/disable
                    // the underline shown below the clickable text
                    textPaint.isUnderlineText = true
                }

                override fun onClick(view: View) {
                    Selection.setSelection((view as TextView).text as Spannable, 0)
                    view.invalidate()
                    link.second.onClick(view)
                }
            }
            startIndexOfLink = this.text.toString().indexOf(link.first, startIndexOfLink + 1)
//      if(startIndexOfLink == -1) continue // todo if you want to verify your texts contains links text
            spannableString.setSpan(
                clickableSpan, startIndexOfLink, startIndexOfLink + link.first.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        this.movementMethod =
            LinkMovementMethod.getInstance() // without LinkMovementMethod, link can not click
        this.setText(spannableString, TextView.BufferType.SPANNABLE)

    }


}