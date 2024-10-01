package com.example.multipane

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalConfiguration
import androidx.activity.compose.BackHandler


// Define Product data class
data class Product(val name: String, val price: String, val description: String)

@Composable
fun MultiPaneShoppingApp(modifier: Modifier = Modifier) {
    // Define product list
    val products = listOf(
        Product("Product A", "$100", "This is a great product A with outstanding features."),
        Product("Product B", "$150", "Product B offers more functionalities at a reasonable price."),
        Product("Product C", "$200", "Premium Product C with top-notch materials."),
        Product("Product D", "$250", "High-quality Product D, perfect for daily use."),
        Product("Product E", "$300", "Luxury Product E with advanced features."),
        Product("Product F", "$350", "Product F is designed for professionals, offering robust performance."),
        Product("Product G", "$400", "Affordable and durable, Product G is a great choice."),
        Product("Product H", "$450", "High-end Product H with cutting-edge technology."),
        Product("Product I", "$500", "Product I offers exceptional design and build quality.")
    )

    var selectedProduct by remember { mutableStateOf<Product?>(null) }

    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == 2 // 2 represents landscape mode

    if (isLandscape) {
        // Landscape mode: Show both product list and details side by side
        Row(modifier = modifier.fillMaxSize()) {
            ProductList(
                products = products,
                onProductClick = { selectedProduct = it },
                selectedProduct = selectedProduct,
                modifier = Modifier.weight(1f)
            )
            ProductDetails(selectedProduct = selectedProduct, modifier = Modifier.weight(1f))
        }
    } else {
        // Portrait mode: Handle back press to navigate back to product list
        Column(modifier = modifier.fillMaxSize()) {
            // Capture back press and reset selected product when viewing details
            if (selectedProduct != null) {
                BackHandler {
                    selectedProduct = null // Reset selection when back is pressed
                }
            }

            if (selectedProduct == null) {
                // Show product list if no product is selected
                ProductList(
                    products = products,
                    onProductClick = { selectedProduct = it },
                    selectedProduct = selectedProduct,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                // Show product details if a product is selected
                ProductDetails(selectedProduct = selectedProduct, modifier = Modifier.fillMaxSize())
            }
        }
    }
}

@Composable
fun ProductList(
    products: List<Product>,
    onProductClick: (Product) -> Unit,
    selectedProduct: Product?,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier.padding(16.dp)) {
        items(products.size) { index ->
            val product = products[index]
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = if (selectedProduct == product) Color.LightGray else MaterialTheme.colorScheme.surface
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clickable { onProductClick(product) },
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = product.name,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Price: ${product.price}",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }
        }
    }
}

@Composable
fun ProductDetails(selectedProduct: Product?, modifier: Modifier = Modifier) {
    if (selectedProduct != null) {
        Surface(
            color = MaterialTheme.colorScheme.background,
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            tonalElevation = 4.dp
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Name: ${selectedProduct.name}",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Price: ${selectedProduct.price}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.secondary
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = selectedProduct.description,
                    fontSize = 18.sp,
                    lineHeight = 24.sp
                )
            }
        }
    } else {
        // Placeholder message when no product is selected
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                text = "Select a product to view details",
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMultiPaneShoppingApp() {
    MaterialTheme {
        MultiPaneShoppingApp()
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // Call MultiPaneShoppingApp and pass Modifier
                    MultiPaneShoppingApp(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
