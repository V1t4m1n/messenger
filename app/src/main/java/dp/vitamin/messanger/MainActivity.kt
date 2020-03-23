package dp.vitamin.messanger

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import dp.vitamin.messanger.messenger.dialog.MessengerActivity
import dp.vitamin.messanger.utils.User
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    lateinit var loginButtonMainActivity: Button
    lateinit var registrationButtonMainActivity: Button
    lateinit var forgotButtonMainActivity: Button

    lateinit var auth: FirebaseAuth
    lateinit var db: FirebaseDatabase
    lateinit var users: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        auth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance()
        users = db.getReference("UsersNew")

        //auth.signOut()

        if (FirebaseAuth.getInstance().currentUser != null) {
            var intent = Intent(this, MessengerActivity::class.java)
            startActivity(intent)
        } else {

            loginButtonMainActivity = findViewById(R.id.loginButtonMainMainActivity)
            registrationButtonMainActivity = findViewById(R.id.registerButtonMainActivity)
            forgotButtonMainActivity = findViewById(R.id.forgotButtonMainActivity)

            loginButtonMainActivity.setOnClickListener {
                ShowSignInAlert()
            }

            forgotButtonMainActivity.setOnClickListener {

                ShowForgotPassword()
                //startActivity(Intent(this, ForgotPasswordActivity::class.java))
            }

            registrationButtonMainActivity.setOnClickListener {

                ShowRegisterAlert()
                //startActivity(Intent(this, RegistrationActivity::class.java))
            }
        }
    }

    private fun ShowSignInAlert() {

        var dialog: AlertDialog.Builder = AlertDialog.Builder(this)
        dialog.setTitle("Sign In")
        dialog.setMessage("Enter login and password!")

        val inflater = LayoutInflater.from(this)
        val view: View = inflater.inflate(R.layout.activity_login, null)
        dialog.setView(view)

        val loginUser: EditText = view.findViewById(R.id.loginEditTextLoginActivity)
        val passwordUser: EditText = view.findViewById(R.id.passwordEditTextLoginActivity)


        dialog.setNegativeButton("Cancel") { dialogInterface, _ ->
            Snackbar.make(root, "Sign in canceled!", Snackbar.LENGTH_LONG).show()
            dialogInterface.dismiss()
        }

        dialog.setPositiveButton("Sign In", DialogInterface.OnClickListener { _, _ ->
            if (!Patterns.EMAIL_ADDRESS.matcher(loginUser.text.toString()).matches()) {
                Snackbar.make(root, "Enter you login!", Snackbar.LENGTH_LONG).show()
                return@OnClickListener
            }
            if (TextUtils.isEmpty(passwordUser.text.toString()) || passwordUser.text.length < 5) {
                Snackbar.make(root, "Enter you password or password length < 5!", Snackbar.LENGTH_LONG).show()
                return@OnClickListener
            }

            auth.signInWithEmailAndPassword(loginUser.text.toString(), passwordUser.text.toString())
                .addOnSuccessListener {
                    startActivity(Intent(this, MessengerActivity::class.java))
                    finish()
                }
                .addOnFailureListener(OnFailureListener {
                    Snackbar.make(root, "Sign in failure! \n ${it.localizedMessage}", Snackbar.LENGTH_LONG).show()
                })
        })
        dialog.show()
    }

    private fun ShowRegisterAlert() {

        var dialog: AlertDialog.Builder = AlertDialog.Builder(this)
        dialog.setTitle("Registration")
        dialog.setMessage("Enter personal information!")

        val inflater = LayoutInflater.from(this)
        val view: View = inflater.inflate(R.layout.activity_registration, null)
        dialog.setView(view)

        val loginUser: EditText = view.findViewById(R.id.loginEditTextRegistrationActivity)
        val passwordUser: EditText = view.findViewById(R.id.passwordEditTextRegistrationActivity)
        val nameUser: EditText = view.findViewById(R.id.nameEditTextRegistrationActivity)


        dialog.setNegativeButton("Cancel") { dialogInterface, _ ->
            Snackbar.make(root, "Registration canceled!", Snackbar.LENGTH_LONG).show()
            dialogInterface.dismiss()
        }

        dialog.setPositiveButton("Registration", DialogInterface.OnClickListener { _, _ ->
            if (!Patterns.EMAIL_ADDRESS.matcher(loginUser.text.toString()).matches()) {
                Snackbar.make(root, "Enter you login!", Snackbar.LENGTH_LONG).show()
                return@OnClickListener
            }
            if (TextUtils.isEmpty(passwordUser.text.toString()) || passwordUser.text.length < 6) {
                Snackbar.make(root, "Enter you password or password length < 6!", Snackbar.LENGTH_LONG).show()
                return@OnClickListener
            }
            if (TextUtils.isEmpty(nameUser.text.toString())) {
                Snackbar.make(root, "Enter you Name!", Snackbar.LENGTH_LONG).show()
                return@OnClickListener
            }

            auth.createUserWithEmailAndPassword(loginUser.text.toString(), passwordUser.text.toString())
                .addOnSuccessListener {
                    val user = User()

                    user.login = loginUser.text.toString()
                    user.password = passwordUser.text.toString()
                    user.name = nameUser.text.toString()

                    users.child(FirebaseAuth.getInstance().currentUser!!.uid)
                        .setValue(user)
                        .addOnSuccessListener {

                        }
                    Snackbar.make(root, "Registration complete!", Snackbar.LENGTH_LONG).show()
                }
                .addOnFailureListener {
                    Snackbar.make(root, "Registration reject! \nThis user already exists.", Snackbar.LENGTH_LONG).show()
                }
        })
        dialog.show()
    }

    private fun ShowForgotPassword() {

        var dialog: AlertDialog.Builder = AlertDialog.Builder(this)
        dialog.setTitle("Forgot password")
        dialog.setMessage("Enter login for reset password!")

        val inflater = LayoutInflater.from(this)
        val view: View = inflater.inflate(R.layout.activity_forgot, null)
        dialog.setView(view)

        val loginUser: EditText = view.findViewById(R.id.loginEditTextForgotActivity)


        dialog.setNegativeButton("Cancel") { dialogInterface, _ ->
            Snackbar.make(root, "forgot password in canceled!", Snackbar.LENGTH_LONG).show()
            dialogInterface.dismiss()
        }

        dialog.setPositiveButton("Forgot password", DialogInterface.OnClickListener { _, _ ->
            if (!Patterns.EMAIL_ADDRESS.matcher(loginUser.text.toString()).matches()) {
                Snackbar.make(root, "Enter you login!", Snackbar.LENGTH_LONG).show()
                return@OnClickListener
            }

            auth.sendPasswordResetEmail(loginUser.text.toString())
                .addOnSuccessListener {
                    Snackbar.make(root, "Reset password complete! Check you email!", Snackbar.LENGTH_LONG).show()
                    return@addOnSuccessListener
                }
                .addOnFailureListener(OnFailureListener {
                    Snackbar.make(root, "Sign in failure! \n ${it.localizedMessage}", Snackbar.LENGTH_LONG).show()
                })
        })
        dialog.show()
    }
}
