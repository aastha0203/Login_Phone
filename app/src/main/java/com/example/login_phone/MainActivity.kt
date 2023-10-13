package com.example.login_phone


import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.login_phone.ui.theme.Login_phoneTheme
import com.example.login_phone.ui.theme.Purple80
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {
    private val mAuth = FirebaseAuth.getInstance()
    var verificationOtp =""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Login_phoneTheme {

                Surface(
                    color = MaterialTheme.colorScheme.background
                ) {
                    OTPScreen{mobileNum,otp->
                        if(mobileNum.isNotEmpty()){
                            send(mobileNum)
                        }
                        if(otp.isNotEmpty()){
                            otpVerification(otp)
                        }
                    }

                }
            }
        }
    }
    val turnOffPhoneVerify=FirebaseAuth.getInstance().firebaseAuthSettings
        .setAppVerificationDisabledForTesting(false)
    private fun send(mobileNum:String){
        val options = PhoneAuthOptions.newBuilder(mAuth)
            .setPhoneNumber("+91$mobileNum")
            .setTimeout(60L,TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(object:
            PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
                override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                    Toast.makeText(applicationContext,"Verification Completed",Toast.LENGTH_SHORT).show()
                }

                override fun onVerificationFailed(p0: FirebaseException) {
                    Toast.makeText(applicationContext,"Verification Failed",Toast.LENGTH_SHORT).show()
                }

                override fun onCodeSent(otp: String, p1: PhoneAuthProvider.ForceResendingToken) {
                    super.onCodeSent(otp, p1)
                    verificationOtp=otp
                    Toast.makeText(applicationContext,"Otp Send Successfully",Toast.LENGTH_SHORT).show()
                }
            }).build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }
    private fun otpVerification(otp: String){
        val credential=PhoneAuthProvider.getCredential(verificationOtp,otp)
            FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(this){task->
                    if(task.isSuccessful){
                        Toast.makeText(applicationContext,"Verification Successful",Toast.LENGTH_SHORT).show()
                    }
                    else{
                        Toast.makeText(applicationContext,"Wrong Otp",Toast.LENGTH_SHORT).show()
                    }

                }
    }

}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OTPScreen(
    onClick:(mobileNum:String,otp:String)-> Unit
){

val context=LocalContext.current
    var otpVal:String?=null
    val phoneNumber=remember{ mutableStateOf("") }

Column(
    modifier=Modifier
        .fillMaxSize()
){
    Column(
        modifier=Modifier
            .fillMaxWidth(),
        horizontalAlignment =Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
  Text(text="OTP Screen",
      fontSize=20.sp,
      fontWeight = FontWeight.Bold
  )
    }
    Column(
        modifier=Modifier
        .fillMaxSize(),
        horizontalAlignment=Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    )
    {
        Image(painter = painterResource(id = R.drawable.baseline_phone_android_24),
            contentDescription ="otp image" ,
            modifier= Modifier
                .width(100.dp)
                .height(100.dp)
        )
        Spacer(modifier = Modifier.height(50.dp))
        OutlinedTextField(value = phoneNumber.value, onValueChange =
        {phoneNumber.value=it}, label = {Text(text="Phone Number")},
            placeholder = {Text(text="Phone Number")},
            leadingIcon = { Icon(Icons.Filled.Phone,contentDescription = "Phone Number") },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
                
            ),
        )
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = {
               onClick(phoneNumber.value,"")
            },
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(45.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Purple80)) {
            Text(text = "Send Otp", fontSize = 15.sp,color=Color.White)
            
        }
        Spacer(modifier = Modifier.height(40.dp))

        Text (text="Enter the OTP",
            fontSize=20.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(10.dp))
        OTPTextFields(length = 6){
            getOpt ->
            otpVal = getOpt

        }
        Spacer(modifier=Modifier.height(30.dp))
        Button(onClick={
            if(otpVal!=null){
                onClick("",otpVal!!)
            }
        },modifier= Modifier
            .fillMaxWidth(0.8f)
            .height(45.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Purple80),
            shape= RoundedCornerShape(10.dp)
        ){
            Text(
                text ="Otp Verify",
                fontSize=13.sp,
                color= Color.White
            )

        }

    }
}
}


