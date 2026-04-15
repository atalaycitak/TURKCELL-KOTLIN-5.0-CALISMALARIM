package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.MyApplicationTheme

val CreamBackground = Color(0xFFFDFBF5)
val ElementBackground = Color(0xFFFFFFFF)
val RedPrimary = Color(0xFFD62828)
val TextDark = Color(0xFF1E1E1E)
val TextGray = Color(0xFF636363)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = CreamBackground
                ) { innerPadding ->
                    ProfileScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun ProfileScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CreamBackground)
            .padding(horizontal = 24.dp, vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        
        Box(
            modifier = Modifier
                .size(110.dp)
                .shadow(elevation = 8.dp, shape = CircleShape)
                .clip(CircleShape)
                .background(RedPrimary),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "AÇ",
                fontSize = 42.sp,
                color = ElementBackground,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 2.sp
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = "Atalay Çıtak",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = TextDark
        )
        
        Spacer(modifier = Modifier.height(6.dp))
        
        Text(
            text = "Bilgisayar Mühendisliği Öğrencisi",
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = RedPrimary
        )
        
        Spacer(modifier = Modifier.height(6.dp))
        
        Text(
            text = "Erciyes Üniversitesi",
            fontSize = 16.sp,
            color = TextGray
        )

        Spacer(modifier = Modifier.height(40.dp))
        
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
            Text(
                text = "Yetenekler",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))

        val skills = listOf("swiftUI/UIkit", "java", "unity", "c#")
        Column(modifier = Modifier.fillMaxWidth()) {
            skills.forEach { skill ->
                SkillItem(skillName = skill)
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun SkillItem(skillName: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation = 2.dp, shape = RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = ElementBackground)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .clip(CircleShape)
                    .background(RedPrimary)
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Text(
                text = skillName,
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextDark
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfilePreview() {
    MyApplicationTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = CreamBackground
        ) { innerPadding ->
            ProfileScreen(modifier = Modifier.padding(innerPadding))
        }
    }
}