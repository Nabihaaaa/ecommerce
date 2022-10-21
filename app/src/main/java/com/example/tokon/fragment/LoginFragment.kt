package com.example.tokon.fragment

import android.content.ContentValues
import android.content.Intent
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
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.tokon.R
import com.example.tokon.activity.HomeActivity
import com.example.tokon.activity.LoginActivity
import com.example.tokon.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.ktx.Firebase
import jp.wasabeef.glide.transformations.BlurTransformation


class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var email: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        auth = Firebase.auth
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListener()
        setuBinding()
    }

    private fun setuBinding() {
        Glide.with(this).load(R.drawable.ic_pojokkananbawah)
            .apply(RequestOptions.bitmapTransform(BlurTransformation(15, 1)))
            .into(binding.pojokBawah)
        Glide.with(this).load(R.drawable.ic_pojokatas)
            .apply(RequestOptions.bitmapTransform(BlurTransformation(15, 1)))
            .into(binding.pojokAtas)
    }

    private fun setupListener() {
        binding.regText.makeLinks(
            Pair("create", View.OnClickListener {
                findNavController().navigate(R.id.action_loginFragment2_to_registrasiFragment)
            })
        )
        binding.forgotPasswordText.makeLinks(
            Pair("Forgot Password?", View.OnClickListener {

            })
        )

        binding.loginButtton.setOnClickListener {

            email = binding.emailEtext.text.toString()
            val password: String = binding.passwordEtext.text.toString()

            if (email.isEmpty()) {
                binding.emailEtext.error = "email harus diisi"
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.emailEtext.error = "email tidak valid"
            }

            if (password.isEmpty()) {
                binding.passwordEtext.error = "password harus diisi"
            } else if (password.length < 5) {
                binding.passwordEtext.error = "password kurang dari 6 huruf"
            }

            if (email.isNotEmpty() && password.isNotEmpty()
                && Patterns.EMAIL_ADDRESS.matcher(email).matches() &&
                password.length >= 5
            ) {
                val user: MutableMap<String, Any> = HashMap()
                user["email"] = email
                user["password"] = password
                LoginFirebase(email, password)
                binding.emailEtext.text.clear()
                binding.passwordEtext.text.clear()
            }
        }

    }


    private fun LoginFirebase(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    startActivity(Intent(requireActivity(), HomeActivity::class.java).also {
                        it.putExtra("email", email)
                    })
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(ContentValues.TAG, "Login:failure", task.exception)
                    Toast.makeText(
                        requireActivity(), "Login failed.",
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