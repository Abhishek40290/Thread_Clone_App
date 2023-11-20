package com.example.threadclone.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph
import androidx.navigation.NavHostController
import com.example.threadclone.itemvew.ThreadItem
import com.example.threadclone.itemvew.UserItem
import com.example.threadclone.ui.theme.dp16
import com.example.threadclone.ui.theme.dp20
import com.example.threadclone.ui.theme.sp24
import com.example.threadclone.viewModel.HomeViewModel
import com.example.threadclone.viewModel.SearchViewModel
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Search(navHostController: NavHostController) {


    val searchViewModel: SearchViewModel = viewModel()
    val userList by searchViewModel.userList.observeAsState()

    var search by remember {
        mutableStateOf("")
    }

    Column {

        Text(
            text = "Search",
            style = TextStyle(
                fontWeight = FontWeight.ExtraBold,
                fontSize = sp24
            ), modifier =Modifier.padding(top = dp16, start = dp16)
        )

        OutlinedTextField(value = search, onValueChange = {search = it}, label = {
            Text(text = "Search User")
        }, keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text
        ), singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(dp16),
            leadingIcon = {
             }
        )

        Box(modifier = Modifier.height(dp20))

        LazyColumn {

            if (userList != null && userList!!.isNotEmpty()) {
                val filteredItem =
                    userList!!.filter { it.name!!.contains(search, ignoreCase = true) }

                items(filteredItem) { pairs ->
                    UserItem(pairs, navHostController,)
                }
            }
        }

    }


}