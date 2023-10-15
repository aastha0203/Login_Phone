package com.example.login_phone

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusOrder
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusOrder
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.login_phone.ui.theme.PurpleGrey40
import com.example.login_phone.ui.theme.PurpleGrey80


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OTPTextField1(
    modifier: Modifier = Modifier,
    length: Int,
    onFilled: (code: String) -> Unit
) {
    var code: List<Char> by remember { mutableStateOf(listOf()) }
    val focusRequesters: List<FocusRequester> = remember {
        val temp = mutableListOf<FocusRequester>()
        repeat(length) {
            temp.add(FocusRequester())
        }
        temp
    }
    Row(modifier = Modifier.height(50.dp)) {
        (0 until length).forEach { index ->
            OutlinedTextField(
                modifier = Modifier
                    .width(50.dp)
                    .height(50.dp)
//                    .focusProperties {
//                        focusRequesters[index]
//                        focusRequesters[index + 1].requestFocus()
//                        this.next = focusRequesters[index+1]
//                    },

                    .focusRequester(focusRequester = focusRequesters[index])
                    .focusProperties {
                        next = if (index == length - 1)
                            focusRequesters[0]
                        else
                            focusRequesters[index + 1]
//                        focusRequesters[index + 1].requestFocus()
                    }
                    .focusable()

//                    .focusOrder(focusRequester = focusRequesters[index]) {
//                        focusRequesters[index + 1].requestFocus()
//                    }
                ,
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    textAlign = TextAlign.Center,
                    color = Color.Black
                ),
                singleLine = true,
                value = code.getOrNull(index = index)?.takeIf {
                    it.isDigit()
                }?.toString() ?: "",
                onValueChange = { value: String ->
                    if (focusRequesters[index].freeFocus()) {
                        val temp = code.toMutableList()
                        if (value == "") {
                            if (temp.size > index) {
                                temp.removeAt(index = index)
                                code = temp
                                focusRequesters.getOrNull(index - 1)?.requestFocus()
                            }
                        } else {
                            if (code.size > index) {
                                temp[index] = value.getOrNull(0) ?: ' '
                            } else {
                                temp.add(value.getOrNull(0) ?: ' ')
                                code = temp
                                focusRequesters.getOrNull(index + 1)?.requestFocus() ?: onFilled(
                                    code.joinToString(separator = "")
                                )
                            }

                        }
                    }
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                visualTransformation = PasswordVisualTransformation()

            )
            Spacer(modifier = Modifier.width(15.dp))

        }

    }
}

@Composable
fun OTPTextFields(
    modifier: Modifier = Modifier,
    length: Int,
    onFilled: (code: String) -> Unit
) {
    var otpValue by remember {
        mutableStateOf("")
    }
    BasicTextField(
        value = otpValue,
        onValueChange = {
            if (it.length <= 6)
                otpValue = it;
            if(it.length == 6) {
                onFilled(otpValue)
//                Log.d("otp", otpValue)
            }
        },
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.NumberPassword,
            imeAction = ImeAction.Next
        ),
        decorationBox = {
            Row(horizontalArrangement = Arrangement.Center) {
                repeat(length) { index ->
                    val char = when {
                        index >= otpValue.length -> ""
                        else -> otpValue[index].toString()
                    }
                    val isFocused = otpValue.length == index
                    Text(
                        modifier = Modifier
                            .width(40.dp)
                            .border(
                                if(isFocused) 1.5.dp
                                else 1.dp,
                                if(isFocused) PurpleGrey40
                                else PurpleGrey80,
                                RoundedCornerShape(8.dp)
                            )
                            .padding(2.dp),
                        text = char,
                        style = MaterialTheme.typography.headlineMedium,
                        color = PurpleGrey40,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun OTPTextFieldsPreview() {
    Column {
        OTPTextFields(length = 6, onFilled = {})
        OTPTextField1(length = 6, onFilled = {})
        val s = remember { mutableStateOf("") }
        OutlinedTextField(value = s.value, onValueChange = {
            s.value = it
        })
    }

}