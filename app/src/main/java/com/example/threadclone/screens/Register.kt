package com.example.threadclone.screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.threadclone.R
import com.example.threadclone.navigation.Routes
import com.example.threadclone.ui.theme.dp24
import com.example.threadclone.ui.theme.dp25
import com.example.threadclone.ui.theme.dp30
import com.example.threadclone.ui.theme.dp6
import com.example.threadclone.ui.theme.dp96
import com.example.threadclone.ui.theme.sp16
import com.example.threadclone.ui.theme.sp20
import com.example.threadclone.ui.theme.sp24
import com.example.threadclone.viewModel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Register(navHostController: NavHostController) {

    val authViewModel : AuthViewModel = viewModel()
    val firebaseUser by authViewModel.firebaseUser.observeAsState(null)


    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    var name by remember {
        mutableStateOf("")
    }

    var userName by remember {
        mutableStateOf("")
    }

    var bio by remember {
        mutableStateOf("")
    }

    var imageUrl by remember {
        mutableStateOf<Uri?>(null)
    }

    val permissionToRequest = if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.TIRAMISU){
        Manifest.permission.READ_MEDIA_IMAGES
    } else {
        Manifest.permission.READ_EXTERNAL_STORAGE
    }

    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) {
        uri: Uri? ->
        imageUrl = uri
    }

    val permissionLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) {

        isGranted: Boolean ->
        if (isGranted) {

        } else {

        }
    }

    LaunchedEffect(firebaseUser){
        if (firebaseUser!=null){
            navHostController.navigate(Routes.BottomNav.routes) {
                popUpTo(navHostController.graph.startDestinationId)
                launchSingleTop = true
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(dp24),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {

        Text(
            text = "Register here ",
            style = TextStyle(
                fontWeight = FontWeight.ExtraBold,
                fontSize = sp24
            )
        )

        Box(modifier = Modifier.height(dp25))

        Image(
            painter = if (imageUrl == null) painterResource(id = R.drawable.user)
            else rememberAsyncImagePainter(model = imageUrl),
            contentDescription = "person",
            modifier = Modifier
                .size(dp96)
                .clip(CircleShape)
                .background(Color.Gray)
                .clickable {

                    val isGranted = ContextCompat.checkSelfPermission(
                        context, permissionToRequest
                    ) == PackageManager.PERMISSION_GRANTED

                    if (isGranted) {
                        launcher.launch("image/*")
                    } else {
                        permissionLauncher.launch(permissionToRequest)
                    }
                }, contentScale = ContentScale.Crop
        )

        Box(modifier = Modifier.height(dp25))

        OutlinedTextField(value = name, onValueChange = {name = it}, label = {
            Text(text = "Name")
        }, keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text
        ), singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(value = userName, onValueChange = {userName = it}, label = {
            Text(text = "UserName")
        }, keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text
        ), singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(value = bio, onValueChange = {bio = it}, label = {
            Text(text = "Bio")
        }, keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text
        ), singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(value = email, onValueChange = {email = it}, label = {
            Text(text = "Email")
        }, keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email
        ), singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(value = password, onValueChange = {password = it}, label = {
            Text(text = "Password")
        }, keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password
        ), singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Box(modifier = Modifier.height(dp30))

        ElevatedButton(onClick = {

                      if(name.isEmpty()||email.isEmpty()||bio.isEmpty()||password.isEmpty()||imageUrl == null) {
                          Toast.makeText(context, "Please fill all details", Toast.LENGTH_SHORT).show()
                      }else{
                          authViewModel.register(email, password, name, bio, userName, imageUrl!!, context)
                      }

        }, modifier = Modifier.fillMaxWidth()) {
            Text(
                //TODO ADD LOADER WHEN CLICKED ON REGISTER
                text = "Register Here",
                style = TextStyle(
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = sp20
                ), modifier = Modifier.padding(vertical = dp6)
            )
        }

        TextButton(onClick = {
                             navHostController.navigate(Routes.Login.routes) {
                                 popUpTo(navHostController.graph.startDestinationId)
                                 launchSingleTop = true
                             }
        }, modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Already registered? Login here",
                style = TextStyle(
                    fontSize = sp16
                )
            )
        }

    }

}

//@Preview(showBackground = true)
//@Composable
//fun reg(){
//    Register()
//}