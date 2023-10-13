package com.example.login_phone

import android.graphics.drawable.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.login_phone.ui.theme.Login_phoneTheme
import com.example.login_phone.ui.theme.Purple80
import com.google.firebase.auth.FirebaseAuth

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
                    OTPScreen()

                }
            }
        }
    }
}
@Composable
fun OTPScreen(){
val context=LocalContext.current
    val otpVal:String?=null
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
                
            )
        )
        Spacer(modifier = Modifier.height(75.dp))

        Text (text="Enter the OTP",
            fontSize=20.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(10.dp))
        OTPTextFields(length = 4){
            getOpt ->otpVal

        }
        Spacer(modifier=Modifier.height(30.dp))
        Button(onClick={
            if(otpVal!=null){
                Toast.makeText(context,"Please enter Otp",Toast.LENGTH_SHORT).show()
            }
        },modifier= Modifier
            .fillMaxWidth(0.8f)
            .height(45.dp)
            .background(Purple80),
            shape= RoundedCornerShape(10.dp)
        ){
            Text(
                text ="Get Otp",
                fontSize=13.sp,
                color= Color.White
            )

        }

    }
}
}


