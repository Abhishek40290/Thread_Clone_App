package com.example.threadclone.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.threadclone.itemvew.ThreadItem
import com.example.threadclone.model.UserModel
import com.example.threadclone.navigation.Routes
import com.example.threadclone.ui.theme.dp120
import com.example.threadclone.ui.theme.dp14
import com.example.threadclone.ui.theme.sp20
import com.example.threadclone.ui.theme.sp24
import com.example.threadclone.utils.SharedPref
import com.example.threadclone.viewModel.AuthViewModel
import com.example.threadclone.viewModel.UserViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun OtherUsers(navHostController: NavHostController, uid: String) {
    val authViewModel: AuthViewModel = viewModel()
    val firebaseUser by authViewModel.firebaseUser.observeAsState(null)
    val context = LocalContext.current

    val userViewModel: UserViewModel = viewModel()
    val threads by userViewModel.threads.observeAsState(null)
    val users by userViewModel.users.observeAsState(null)
    val followerList by userViewModel.followerList.observeAsState(null)
    val followingList by userViewModel.followingList.observeAsState(null)



    userViewModel.fetchThreads(uid)
    userViewModel.fetchUser(uid)
    userViewModel.getFollowers(uid)
    userViewModel.getFollowing(uid)

    var currentUserId = ""
    if(FirebaseAuth.getInstance().currentUser!=null)
        currentUserId = FirebaseAuth.getInstance().currentUser!!.uid


    LaunchedEffect(firebaseUser){
        if (firebaseUser==null){
            navHostController.navigate(Routes.Login.routes) {
                popUpTo(navHostController.graph.startDestinationId)
                launchSingleTop = true
            }
        }
    }


    LazyColumn() {
        item {
            ConstraintLayout(modifier = Modifier
                .fillMaxSize()
                .padding(dp14)
            )
            {

                val (
                    text, logo, userName, bio, followers, following,button,
                ) = createRefs()

                Text(
                    text = users!!.name, style = TextStyle(
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = sp24
                    ), modifier = Modifier.constrainAs(text) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    }
                )

                Image(painter = rememberAsyncImagePainter(model = users!!.imageUrl),
                    contentDescription = "close",
                    modifier = Modifier
                        .constrainAs(logo) {
                            top.linkTo(parent.top)
                            end.linkTo(
                                parent.end
                            )
                        }
                        .size(dp120)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )

                Text(
                    text = users!!.userMame, style = TextStyle(
                        fontSize = sp20
                    ), modifier = Modifier.constrainAs(userName) {
                        top.linkTo(text.bottom)
                        start.linkTo(parent.start)
                    })

                Text(
                    text = users!!.bio, style = TextStyle(
                        fontSize = sp20
                    ), modifier = Modifier.constrainAs(bio) {
                        top.linkTo(userName.bottom)
                        start.linkTo(parent.start)
                    })

                Text(
                    text = "${followerList?.size} Followers", style = TextStyle(
                        fontSize = sp20
                    ), modifier = Modifier.constrainAs(followers) {
                        top.linkTo(bio.bottom)
                        start.linkTo(parent.start)
                    })

                Text(
                    text = "${followingList?.size} Following", style = TextStyle(
                        fontSize = sp20
                    ), modifier = Modifier.constrainAs(following) {
                        top.linkTo(followers.bottom)
                        start.linkTo(parent.start)
                    })

                ElevatedButton(onClick = {
                    if(currentUserId!= "")
                                         userViewModel.followUsers(uid, currentUserId)
                }, modifier = Modifier.constrainAs(button) {
                    top.linkTo(following.bottom)
                    start.linkTo(parent.start)
                }
                ) {
                    Text(text = if (followerList!=null && followerList!!.isNotEmpty() && followerList!!.contains(currentUserId)) "Following" else "Follow")
                }



            }
        }
        if(threads!= null && users!=null){
        items(threads ?: emptyList()) { pair ->
            ThreadItem(
                thread = pair,
                users = users!!,
                navHostController = navHostController,
                userId = SharedPref.getUserName(context)
            )
        }
        }


    }


}