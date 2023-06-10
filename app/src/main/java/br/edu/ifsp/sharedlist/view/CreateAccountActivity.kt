package br.edu.ifsp.sharedlist.view

import android.os.Bundle
import android.widget.Toast
import br.edu.ifsp.sharedlist.databinding.ActivityCreateAccountBinding
import br.edu.ifsp.sharedlist.validator.CreateAccountValidator
import com.google.firebase.auth.FirebaseAuth

class CreateAccountActivity: BaseActivity() {

    private val acab: ActivityCreateAccountBinding by lazy {
        ActivityCreateAccountBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(acab.root)

        acab.createAccountBt.setOnClickListener {

            val formErrors = CreateAccountValidator.getFormErrors(acab)
            if (formErrors.isEmpty()) {
                // Cria a conta no Firebase
                val email = acab.emailEt.text.toString()
                val password = acab.passwordEt.text.toString()

                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnSuccessListener {
                    Toast.makeText(
                        this@CreateAccountActivity,
                        "Usuário $email criado com sucesso!",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                }.addOnFailureListener {
                    Toast.makeText(
                        this@CreateAccountActivity,
                        it.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            else {
                Toast.makeText(this, formErrors, Toast.LENGTH_SHORT).show()
            }

        }
    }
}