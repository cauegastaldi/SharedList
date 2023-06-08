package br.edu.ifsp.sharedlist.model

import android.util.Log
import br.edu.ifsp.sharedlist.view.LoginActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlin.reflect.typeOf

class UserDaoRtDbFb(private val loginActivity: LoginActivity) {

    private val USER_LIST_ROOT_NODE = "userList"
    private val LAST_SIGNED_USER_NODE = "lastSignedUser"
    private val userRtDbFbReference = Firebase.database.getReference(USER_LIST_ROOT_NODE)

    fun registerLastSignedUser(email: String) {
        userRtDbFbReference.child(LAST_SIGNED_USER_NODE).setValue(email)
    }

    fun retrieveLastSignedUserEmail() {
        Thread {
            userRtDbFbReference.child(LAST_SIGNED_USER_NODE)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val lastSignedUserEmail = snapshot.value as String
                        loginActivity.runOnUiThread {
                            loginActivity.updateEmailField(lastSignedUserEmail)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })
        }.start()
    }
}