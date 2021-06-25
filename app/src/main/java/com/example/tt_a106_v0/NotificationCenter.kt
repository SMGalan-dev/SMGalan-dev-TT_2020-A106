package com.example.tt_a106_v0

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.view.marginLeft
import androidx.core.view.setPadding
import androidx.core.view.updatePaddingRelative
import com.example.tt_a106_v0.utils.CurrentUser
import com.example.tt_a106_v0.utils.MessageTypes
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import java.util.ArrayList

class NotificationCenter : AppCompatActivity() {
    private val doctorRef = FirebaseFirestore.getInstance().collection("persons").document(CurrentUser.id)
    private val personsRef = FirebaseFirestore.getInstance().collection("persons")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification_center)
        val mainLinearLayout = findViewById(R.id.cardContainer) as LinearLayout
        CurrentUser.notifications.forEach {
            val cardView = createCard(it as HashMap<String, Any>)
            mainLinearLayout.addView(cardView, 0)
        }
    }

    fun createCard(notification: HashMap<String, Any>) : CardView{
        val cardView = createCardView()
        val cardLinearLayout = LinearLayout(this)
        cardLinearLayout.orientation = LinearLayout.VERTICAL
        val message = createMessage(notification["message"] as String)
        cardLinearLayout.addView(message)
        if( (notification["type"] as Long).toInt() == MessageTypes.AddPatient.ordinal)
            cardLinearLayout.addView(createPatientRequestButton(notification))
        cardView.addView(cardLinearLayout)
        return cardView
    }

    @SuppressLint("ResourceAsColor")
    private fun createPatientRequestButton(notification: HashMap<String, Any>): LinearLayout {
        val accept = Button(this)
        val denied = Button(this)
        val buttonsLayout = LinearLayout(this)
        val params = RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT)
        val space = Space(this)
        space.layoutParams = RelativeLayout.LayoutParams(
            36,
            ViewGroup.LayoutParams.WRAP_CONTENT)
        accept.setBackgroundColor(Color.parseColor("#057801") )
        denied.setBackgroundColor(Color.parseColor("#B00020") )
        accept.layoutParams = params
        denied.layoutParams = params
        accept.text = "aceptar"
        denied.text = "rechazar"
        accept.setOnClickListener{
            doctorRef.update("notifications", FieldValue.arrayRemove(notification))
            .addOnCompleteListener {
                doctorRef.update("patients", FieldValue.arrayUnion(personsRef.document(notification["from"] as String)))
                Toast.makeText(this, "Solicitud aceptada", Toast.LENGTH_SHORT).show()
                Handler(Looper.getMainLooper()).postDelayed( {
                    finish();
                    startActivity(intent);
                }, 700)
            }.addOnFailureListener {
                Toast.makeText(this, "Hubo un error favor de intentar mas tarde", Toast.LENGTH_SHORT).show()
            }
        }
        denied.setOnClickListener{
            doctorRef.update("notifications", FieldValue.arrayRemove(notification))
                .addOnCompleteListener {
                    Toast.makeText(this, "Solicitud rechazada", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    Toast.makeText(this, "Hubo un error favor de intentar mas tarde", Toast.LENGTH_SHORT).show()
                }
        }
        buttonsLayout.setPadding(0,36,0,36)
        buttonsLayout.orientation = LinearLayout.HORIZONTAL
        buttonsLayout.addView(accept)
        buttonsLayout.addView(space)
        buttonsLayout.addView(denied)
        return buttonsLayout
    }

    fun createCardView():CardView{
        val cardView = CardView(this)
        val params = RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT)
        params.setMargins(16,16,16,16)
        cardView.radius = 15f
        cardView.setCardBackgroundColor(Color.parseColor("#f5f5f5"))
        cardView.setContentPadding(36,36,36,36)
        cardView.layoutParams = params
        cardView.cardElevation = 30f
        return cardView
    }
    fun createMessage(message:String):TextView{
        val messageV = TextView(this)
        messageV.text = message
        messageV.textSize = 24f
        messageV.setTextColor(Color.BLACK)
        messageV.setTypeface(Typeface.SANS_SERIF,Typeface.NORMAL)
        return  messageV
    }
}