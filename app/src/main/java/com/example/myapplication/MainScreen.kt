import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.rememberAsyncImagePainter
import com.example.myapplication.R
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

data class GridItem(
    val title: String,
    val imageRes: Int,
    val videoId: String,
    val description: String,
    val intensity: String,
    val duration: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Fitness Dashboard", color = Color.Black) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            NavigationBar(containerColor = Color.Black) {
                NavigationBarItem(
                    selected = true,
                    onClick = {},
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home", tint = Color.White) },
                    label = { Text("Home", color = Color.White) },
                    alwaysShowLabel = true
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate("workout/cardio") },
                    icon = { Icon(Icons.Default.DirectionsWalk, contentDescription = "Workout", tint = Color.White) },
                    label = { Text("Workout", color = Color.White) },
                    alwaysShowLabel = true
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate("diet") },
                    icon = { Icon(Icons.Default.Coffee, contentDescription = "Diet", tint = Color.White) },
                    label = { Text("Diet", color = Color.White) },
                    alwaysShowLabel = true
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate("profile") },
                    icon = { Icon(Icons.Default.Person, contentDescription = "Profile", tint = Color.White) },
                    label = { Text("Profile", color = Color.White) },
                    alwaysShowLabel = true
                )
            }
        },
        containerColor = Color.White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            SectionWithBackground(title = "Today's Weather", content = "22°C  |  Wind: 12 km/h")

            SectionWithBackground(title = "Fitness Tips", content = "Remember to warm up before your workout and stay hydrated throughout your training.")

            Text("Explore Workouts", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            WorkoutGrid(navController = navController)
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
fun SectionWithBackground(title: String, content: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFF2F2F2))
            .padding(16.dp)
    ) {
        Text(title, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        Spacer(modifier = Modifier.height(4.dp))
        Text(content, fontSize = 14.sp, color = Color.Black)
    }
}

@Composable
fun WorkoutGrid(navController: NavController) {
    val items = listOf(
        GridItem("Cardio", R.drawable.cardio, "ml6cT4AZdqI", "30-minute cardio session", "Medium", "30"),
        GridItem("Yoga", R.drawable.yoga, "v7AYKMP6rOE", "Yoga for beginners", "Easy", "20"),
        GridItem("HIIT", R.drawable.hiit, "ml6cT4AZdqI", "Full body HIIT", "Hard", "20"),
        GridItem("Strength", R.drawable.stretching, "UoC_O3HzsH0", "Strength building", "Medium", "30")
    )

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        for (row in items.chunked(2)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                row.forEach { item ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFE0E0E0))
                            .clickable {
                                val title = Uri.encode(item.title)
                                val videoId = Uri.encode(item.videoId)
                                val description = Uri.encode(item.description)
                                val intensity = Uri.encode(item.intensity)
                                val duration = item.duration
                                navController.navigate("workout/$title/$videoId/$description/$intensity/$duration")
                            }
                            .padding(8.dp)
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Image(
                                painter = painterResource(id = item.imageRes),
                                contentDescription = item.title,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(100.dp)
                                    .clip(RoundedCornerShape(8.dp))
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                item.title,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.Black
                            )
                        }
                    }
                }
            }
        }
    }
}





@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    MaterialTheme {
        val navController = rememberNavController()
        HomeScreen(navController = navController)
    }
}