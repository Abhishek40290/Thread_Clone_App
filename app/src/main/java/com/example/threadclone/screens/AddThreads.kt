package com.example.threadclone.screens

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
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
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.threadclone.R
import com.example.threadclone.navigation.Routes
import com.example.threadclone.ui.theme.dp1
import com.example.threadclone.ui.theme.dp12
import com.example.threadclone.ui.theme.dp14
import com.example.threadclone.ui.theme.dp250
import com.example.threadclone.ui.theme.dp36
import com.example.threadclone.ui.theme.dp8
import com.example.threadclone.ui.theme.sp20
import com.example.threadclone.ui.theme.sp24
import com.example.threadclone.utils.SharedPref
import com.example.threadclone.viewModel.AddThreadsViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun AddThreads(navHostController: NavHostController) {

    val context = LocalContext.current

    val threadViewModel: AddThreadsViewModel = viewModel()
    val isposted by threadViewModel.isPosted.observeAsState(false)

    var thread by remember {
        mutableStateOf("")
    }

    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    val permissionToRequest = if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.TIRAMISU){
        Manifest.permission.READ_MEDIA_IMAGES
    } else {
        Manifest.permission.READ_EXTERNAL_STORAGE
    }

    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) {
            uri: Uri? ->
        imageUri = uri
    }

    val permissionLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) {

                isGranted: Boolean ->
            if (isGranted) {

            } else {

            }
        }

    LaunchedEffect(isposted) {
        if (isposted!!){
            thread = ""
            imageUri = null
            Toast.makeText(context, "Thread added", Toast.LENGTH_SHORT).show()

            navHostController.navigate(Routes.Home.routes) {
                popUpTo(Routes.AddThread.routes){
                    inclusive = true
                }
            }
        }
    }

    ConstraintLayout(modifier = Modifier
        .fillMaxSize()
        .padding(dp14)
    )
    {

        val (crossPic, text, logo, userName, editText, attachMedia,
            replyText, button, imageBox) = createRefs()

        Image(painter = painterResource(id = R.drawable.baseline_close_24),
            contentDescription = "close",
            modifier = Modifier
                .constrainAs(crossPic) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }
                .clickable {
                    navHostController.navigate(Routes.Home.routes) {
                        popUpTo(Routes.AddThread.routes){
                            inclusive = true
                        }
                    }
                }
            )

        Text(
            text = "Add Thread", style = TextStyle(
                fontWeight = FontWeight.ExtraBold,
                fontSize = sp24
            ), modifier = Modifier.constrainAs(text) {
                top.linkTo(crossPic.top)
                start.linkTo(crossPic.end, margin = dp12)
                bottom.linkTo(crossPic.bottom)
            }
        )

        Image(painter = rememberAsyncImagePainter(model = SharedPref.getImage(context)),
            contentDescription = "close",
            modifier = Modifier
                .constrainAs(logo) {
                    top.linkTo(text.bottom)
                    start.linkTo(parent.start)
                }
                .size(dp36)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )

        Text(
            text = SharedPref.getUserName(context), style = TextStyle(
                fontSize = sp20
            ), modifier = Modifier.constrainAs(userName) {
                top.linkTo(logo.top)
                start.linkTo(logo.end, margin = dp12)
                bottom.linkTo(logo.bottom)
            }
        )

        BasicTextFieldWithHint(
            hint = "Start a thread..." ,
            value = thread,
            onValueChange = {thread = it},
            modifier = Modifier
                .constrainAs(editText) {
                    top.linkTo(userName.bottom)
                    start.linkTo(userName.start)
                    end.linkTo(parent.end)
                }
                .padding(horizontal = dp8, vertical = dp8)
                .fillMaxWidth()
        )

        if(imageUri == null){
            Image(painter = painterResource(id = R.drawable.attach),
                contentDescription = "close",
                modifier = Modifier
                    .constrainAs(attachMedia) {
                        top.linkTo(editText.bottom)
                        start.linkTo(editText.start)
                    }
                    .clickable {
                        val isGranted = ContextCompat.checkSelfPermission(
                            context, permissionToRequest
                        ) == PackageManager.PERMISSION_GRANTED

                        if (isGranted) {
                            launcher.launch("image/*")
                        } else {
                            permissionLauncher.launch(permissionToRequest)
                        }
                    }
            )
        } else{
            Box(modifier = Modifier
                .background(Color.Gray)
                .padding(dp1)
                .constrainAs(imageBox) {
                    top.linkTo(editText.bottom)
                    start.linkTo(editText.start)
                    end.linkTo(parent.end)
                }
                .height(dp250)
            ) {
                Image(painter = rememberAsyncImagePainter(model = imageUri),
                    contentDescription = "close",
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    contentScale = ContentScale.Crop
                )
                Icon(imageVector = Icons.Default.Close, contentDescription = "Remove image",
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .clickable {
                            imageUri = null
                        })
            }
        }
        Text(
            text = "Anyone can reply", style = TextStyle(
                fontSize = sp20
            ), modifier = Modifier.constrainAs(replyText) {
                start.linkTo(parent.start, margin = dp12)
                bottom.linkTo(parent.bottom, margin = dp12)
            }
        )

        TextButton(onClick = {
                             if(imageUri == null){
                                 threadViewModel.saveData(thread, FirebaseAuth.getInstance().currentUser!!.uid, "")
                             } else{
                                 threadViewModel.saveImage(thread, FirebaseAuth.getInstance().currentUser!!.uid, imageUri!!)
                             }
                             }, modifier = Modifier.constrainAs(button){
            end.linkTo(parent.end, margin = dp12)
            bottom.linkTo(parent.bottom, margin = dp12)
        }) {
            Text(
                text = "Post", style = TextStyle(
                    fontSize = sp20
                ),
            )
        }

    }
}

@Composable
fun BasicTextFieldWithHint(hint:String, value:String, onValueChange: (String) -> Unit, modifier: Modifier) {

    Box(modifier = modifier) {
        if (value.isEmpty()) {
            Text(text = hint, color = Color.Gray)
        }
        BasicTextField(value = value, onValueChange = onValueChange,
            textStyle = TextStyle.Default.copy(color = Color.Black),
            modifier = Modifier.fillMaxWidth()
        )
    }
}