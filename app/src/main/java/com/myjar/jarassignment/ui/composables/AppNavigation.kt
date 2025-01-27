package com.myjar.jarassignment.ui.composables

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.myjar.jarassignment.data.model.ComputerItem
import com.myjar.jarassignment.ui.vm.JarViewModel

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    viewModel: JarViewModel,
) {
    val navController = rememberNavController()
    val navigate = remember { mutableStateOf<String>("") }

    NavHost(modifier = modifier, navController = navController, startDestination = "item_list") {
        composable("item_list") {
            ItemListScreen(
                viewModel = viewModel,
                onNavigateToDetail = { selectedItem -> navigate.value = selectedItem },
                navigate = navigate.value,
                navController = navController
            )
        }
        composable("item_detail/{itemId}") { backStackEntry ->
            val itemId = backStackEntry.arguments?.getString("itemId")
            ItemDetailScreen(itemId = itemId) {
                navigate.value = ""
                navController.popBackStack()
            }
        }
    }
}

@Composable
fun ItemListScreen(
    viewModel: JarViewModel,
    onNavigateToDetail: (String) -> Unit,
    navigate: String,
    navController: NavHostController
) {
    val productItems = viewModel.items.collectAsState()

    val query by viewModel.query.collectAsState()

    if (navigate.isNotBlank()) {
        Log.d("ItemListScreen", "ItemListScreen: ${navigate}")
        val currRoute = navController.currentDestination?.route.orEmpty()
        if (!currRoute.contains("item_detail")) {
            navController.navigate("item_detail/${navigate}")
        }
    }

    Column {
        OutlinedTextField(
            value = query,
            onValueChange = viewModel::updateQuery,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            placeholder = {
                Text("Enter query")
            })

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(productItems.value) { item ->
                ItemCard(
                    item = item,
                    onClick = { onNavigateToDetail(item.id) }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun ItemCard(item: ComputerItem, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() }
    ) {
        Text(text = item.name, fontWeight = FontWeight.Bold)
        item.data?.let {
            if (it.color != null) {
                ItemDescription("Color", it.color)
            }
            if (it.price != null) {
                ItemDescription("Price", it.price.toString())
            }
            if (it.capacity != null) {
                ItemDescription("Price", it.capacity)
            }
            if (it.description != null) {
                ItemDescription("Description", it.description)
            }
        }
    }
}

@Composable
fun ItemDescription(title: String, value: String) {
    Text("$title: $value")
}

@Composable
fun ItemDetailScreen(itemId: String?, onBack: () -> Unit) {
    // Fetch the item details based on the itemId
    // Here, you can fetch it from the ViewModel or repository
    Text(
        text = "Item Details for ID: $itemId",
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    )

    BackHandler {
        onBack()
    }
}
