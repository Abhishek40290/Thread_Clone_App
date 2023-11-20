package com.example.threadclone.itemvew

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.threadclone.model.UserModel
import com.example.threadclone.navigation.Routes
import com.example.threadclone.ui.theme.dp1
import com.example.threadclone.ui.theme.dp12
import com.example.threadclone.ui.theme.dp16
import com.example.threadclone.ui.theme.dp36
import com.example.threadclone.ui.theme.dp4
import com.example.threadclone.ui.theme.dp8
import com.example.threadclone.ui.theme.sp18
import com.example.threadclone.ui.theme.sp20

@Composable
fun UserItem(
    users: UserModel,
    navHostController: NavHostController,
){

    Column {

        ConstraintLayout(modifier = Modifier
            .fillMaxWidth()
            .padding(dp16).clickable {
                val routes = Routes.OtherUsers.routes.replace("{data}", users.uid)
                navHostController.navigate(routes)
            }
        ) {

            val (userImage, userName, data, time, title, image) = createRefs()

            Image(painter = rememberAsyncImagePainter(model = users.imageUrl),
                contentDescription = "close",
                modifier = Modifier
                    .constrainAs(userImage) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    }
                    .size(dp36)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Text(
                text = users.userMame, style = TextStyle(
                    fontSize = sp20
                ), modifier = Modifier.constrainAs(userName) {
                    top.linkTo(userImage.top)
                    start.linkTo(userImage.end, margin = dp12)
                }
            )

            Text(
                text = users.name, style = TextStyle(
                    fontSize = sp18
                ), modifier = Modifier.constrainAs(title) {
                    top.linkTo(userName.bottom, margin = dp4)
                    start.linkTo(userName.start)
                }
            )

        }

        Divider(color = Color.LightGray, thickness = dp1)

    }

}