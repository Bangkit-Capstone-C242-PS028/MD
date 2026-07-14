package com.bangkit.dermascan.ui.main.home

//import com.bangkit.dermascan.dataArticles.AccesToken
//import com.bangkit.dermascan.dataArticles.signIn
//import androidx.compose.foundation.layout.FlowColumnScopeInstance.align
import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Divider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.recyclerview.widget.LinearLayoutManager
import coil3.compose.AsyncImage
import com.bangkit.dermascan.data.model.response.SkinLesionItem
import com.bangkit.dermascan.databinding.ActivityArticleBinding
import com.bangkit.dermascan.ui.ViewModelFactory
import com.bangkit.dermascan.ui.article.ArticleActivity
import com.bangkit.dermascan.ui.article.ArticleAdapter
import com.bangkit.dermascan.ui.article.ArticleViewModel
import com.bangkit.dermascan.ui.articleAdd.ArticleAddActivity
import com.bangkit.dermascan.ui.main.feeds.ComposeArticleScreen
import com.bangkit.dermascan.ui.main.feeds.FeedsScreen
import com.bangkit.dermascan.ui.theme.Blue
import com.bangkit.dermascan.ui.theme.LightBlue
import com.bangkit.dermascan.ui.theme.Typography
import com.bangkit.dermascan.ui.theme.White
import com.bangkit.dermascan.ui.main.scan.upload.SkinLesionViewModel
import com.bangkit.dermascan.util.Result
import com.bangkit.dermascan.util.formatTimestampToGMT
//import com.bangkit.dermascan.util.formatTimestampToGMT
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

//@Preview(showBackground = true)
@Composable
fun HomeScreen(navController: NavController, roles: String?) {
    val context = LocalContext.current
    val skinLesionViewModel: SkinLesionViewModel = hiltViewModel()
    val skinLesionsResult by skinLesionViewModel.skinLesions.collectAsState()

    LaunchedEffect(Unit) {
        skinLesionViewModel.fetchSkinLesions()
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .verticalScroll(rememberScrollState())  // Menambahkan scroll vertikal
            .padding(16.dp)
    ) {
        if(roles != "DOCTOR"){
            when (val result = skinLesionsResult) {
                is Result.Loading -> {
                    // Tampilkan shimmer loading untuk daftar skin lesion
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
//                        modifier = Modifier.padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.CenterHorizontally),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "History:",
                                style = Typography.titleLarge
                            )

                            // Tetap tampilkan "See All" saat loading
                            Text(
                                text = "See All",
                                style = Typography.bodyMedium.copy(color = Blue),
                                modifier = Modifier.clickable {
                                    navController.navigate("skinLesionHistory")
                                }
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        LazyRow(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            // Tampilkan 3-5 item shimmer
                            items(5) {
                                SkinLesionItemWithShimmer(
                                    isLoading = true,
                                    modifier = Modifier.padding(end = 8.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(32.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.CenterHorizontally),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Article:",
                                style = Typography.titleLarge
                            )

                            // Tetap tampilkan "See All" saat loading
                            Text(
                                text = "See More",
                                style = Typography.bodyMedium.copy(color = Blue),
                                modifier = Modifier.clickable {
                                    navController.navigate("skinLesionHistory")
                                }
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        LazyRow(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            // Tampilkan 3-5 item shimmer
                            items(5) {
                                SkinLesionItemWithShimmer(
                                    isLoading = true,
                                    modifier = Modifier.padding(end = 8.dp)
                                )
                            }
                        }
                    }
                }
                is Result.Success -> {
                    val lesions = result.data
                    Column(
                        horizontalAlignment = Alignment.Start,
//                        modifier = Modifier.padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "History:",
                                style = Typography.titleLarge
                            )

                            Text(
                                text = "See All",
                                style = Typography.bodyMedium.copy(color = Blue),
                                modifier = Modifier.clickable {
                                    navController.navigate("skinLesionHistory")
                                }
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        LazyRow(
                            modifier = Modifier
                                .wrapContentSize()
                        ) {
                            items(lesions.take(2)) { skinLesion ->
                                SkinLesionItemWithShimmer(
                                    isLoading = false,
                                    skinLesion = skinLesion,
                                    modifier = Modifier.padding(end = 8.dp).align(Alignment.CenterHorizontally)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp)) // Memberikan jarak antara History dan Artikel
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Article:",
                                style = Typography.titleLarge
                            )
                            Text(
                                text = "See More",
                                style = Typography.bodyMedium.copy(color = Blue),
                                modifier = Modifier.clickable {
                                    navController.navigate("article")
                                }
                            )
                        }


                    }


                }
                is Result.Error -> {
                    Log.e("SkinLesionHistoryScreen", "Error: ${result.message}")
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "History:",
                            style = Typography.titleLarge,
                            modifier = Modifier.align(Alignment.Start)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = result.message,
                            color = Color.Red,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Result.Idle -> {}
            }


            Spacer(modifier = Modifier.height(0.dp))
            val viewModel : ArticleViewModel = hiltViewModel()
            AndroidView(
                modifier = Modifier.fillMaxWidth().height(340.dp),
                factory = { _ ->
                    // Inflate layout XML yang sudah ada
                    val binding = ActivityArticleBinding.inflate(LayoutInflater.from(context))
                    val view = binding.root

                    // Configurasi ViewModel


                    // Setup RecyclerView
                    val adapter = ArticleAdapter()
                    binding.rvArticles.adapter = adapter
                    binding.rvArticles.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)


                    // Observasi perubahan artikel
                    viewModel.listArticle.observe(context as LifecycleOwner) { articleResponse ->
                        adapter.submitList(articleResponse.take(3))
                    }

                    // Setup loading
//                            viewModel.isLoading.observe(context as LifecycleOwner) { isLoading ->
//                                binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.INVISIBLE
//                            }

                    // Memuat artikel
                    viewModel.showArticles()

                    if (roles != "DOCTOR" || roles != "PAATIENT") {
                        binding.fabAddarticle.visibility = View.GONE
                    }
                    // Setup FloatingActionButton
                    binding.fabAddarticle.setOnClickListener {
//                val intent = Intent(context, ArticleAddActivity::class.java)
//                context.startActivity(intent)
                        navController.navigate("articleAdd")
                    }

                    view
                },
                update = { _ ->
                    // Misalnya, me-refresh data artikel
                    viewModel.showArticles()

                    // Atau melakukan operasi update lainnya sesuai kebutuhan
                }

            )
        }else{
            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .fillMaxWidth() // Untuk memenuhi lebar layar
                    .fillMaxHeight() // Untuk memenuhi tinggi layar
//                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp), // Memberikan jarak sedikit di bawah Row
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Article:",
                        style = Typography.titleLarge
                    )
                    Text(
                        text = "See More",
                        style = Typography.bodyMedium.copy(color = Blue),
                        modifier = Modifier.clickable {
                            navController.navigate("article")
//                            val intent = Intent(context, ArticleActivity::class.java)
//                            context.startActivity(intent)
                        }

                    )
                }

                // RecyclerView via AndroidView
                val viewModel: ArticleViewModel = hiltViewModel()
                AndroidView(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(340.dp)
                        .padding(top = 8.dp),
                    factory = { _ ->
                        val binding = ActivityArticleBinding.inflate(LayoutInflater.from(context))
                        val view = binding.root

                        // Setup RecyclerView
                        val adapter = ArticleAdapter()
                        binding.rvArticles.adapter = adapter
                        binding.rvArticles.layoutManager =
                            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

                        // Observasi ViewModel
                        viewModel.listArticle.observe(context as LifecycleOwner) { articleResponse ->
                            adapter.submitList(articleResponse.take(3))
                        }

                        viewModel.isLoading.observe(context as LifecycleOwner) { isLoading ->
                            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.INVISIBLE
                        }

                        // Memuat artikel
                        viewModel.showArticles()

                        // Setup FloatingActionButton
                        if (roles != "DOCTOR" || roles != "PATIENT") {
                            binding.fabAddarticle.visibility = View.GONE
                        }
                        binding.fabAddarticle.setOnClickListener {
                            navController.navigate("articleAdd")
                        }

                        view
                    },
                    update = { _ ->
                        viewModel.showArticles()
                    }
                )
            }




        }
    }
}

@Composable
@SuppressLint("ModifierParameter")
fun SkinLesionItemWithShimmer(
    isLoading: Boolean,
    skinLesion: SkinLesionItem? = null,
    modifier: Modifier = Modifier
) {
    if (isLoading) {
        Card(
            modifier = modifier
                .width(320.dp)
                .clip(RoundedCornerShape(16.dp)),
            colors = CardDefaults.cardColors(
                containerColor = LightBlue.copy(alpha = 0.5f)
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    ShimmerEffect(
                        modifier = Modifier
                            .size(90.dp)
                            .background(Color.LightGray.copy(alpha = 0.3f), shape = RoundedCornerShape(12.dp))
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        ShimmerEffect(
                            modifier = Modifier
                                .fillMaxWidth(0.8f)
                                .height(20.dp)
                                .background(Color.LightGray.copy(alpha = 0.3f), shape = RoundedCornerShape(8.dp))
                        )
                        ShimmerEffect(
                            modifier = Modifier
                                .width(60.dp)
                                .height(16.dp)
                                .background(Color.LightGray.copy(alpha = 0.3f), shape = RoundedCornerShape(8.dp))
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                ShimmerEffect(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(14.dp)
                        .background(Color.LightGray.copy(alpha = 0.3f), shape = RoundedCornerShape(8.dp))
                )
                Spacer(modifier = Modifier.height(12.dp))
                Divider(color = Blue.copy(alpha = 0.1f))
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    ShimmerEffect(
                        modifier = Modifier
                            .width(80.dp)
                            .height(16.dp)
                            .background(Color.LightGray.copy(alpha = 0.3f), shape = RoundedCornerShape(8.dp))
                    )
                    ShimmerEffect(
                        modifier = Modifier
                            .width(80.dp)
                            .height(16.dp)
                            .background(Color.LightGray.copy(alpha = 0.3f), shape = RoundedCornerShape(8.dp))
                    )
                }
            }
        }
    } else {
        Card(
            modifier = modifier
                .width(320.dp)
                .clip(RoundedCornerShape(16.dp)),
            colors = CardDefaults.cardColors(
                containerColor = LightBlue
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val imageUrl = skinLesion?.processedImageUrl.takeIf { it?.isNotEmpty() ?: false }
                        ?: skinLesion?.originalImageUrl

                    AsyncImage(
                        model = imageUrl,
                        contentDescription = "Skin Lesion Image",
                        modifier = Modifier
                            .size(90.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = skinLesion?.classification ?: "Classification: Unknown",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Blue,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        // Status Badge
                        Surface(
                            color = when (skinLesion?.status?.uppercase()) {
                                "SUCCESS" -> Color(0xFFE8F5E9)
                                "PENDING" -> Color(0xFFFFF3E0)
                                else -> Color(0xFFFFEBEE)
                            },
                            contentColor = when (skinLesion?.status?.uppercase()) {
                                "SUCCESS" -> Color(0xFF2E7D32)
                                "PENDING" -> Color(0xFFE65100)
                                else -> Color(0xFFC62828)
                            },
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = skinLesion?.status ?: "PENDING",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }

                if (skinLesion?.description != null && skinLesion.description.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = skinLesion.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.DarkGray,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))
                Divider(color = Blue.copy(alpha = 0.1f))
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        Text(
                            text = "Created At",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.Gray
                        )
                        Text(
                            text = skinLesion?.createdAt?.let { formatTimestampToGMT(it) } ?: "-",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Medium,
                            color = Blue
                        )
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "Processed At",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.Gray
                        )
                        Text(
                            text = skinLesion?.processedAt?.let { formatTimestampToGMT(it) } ?: "-",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Medium,
                            color = Blue
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SkinLesionItem(
    skinLesion: SkinLesionItem,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .width(400.dp)  // Tetapkan lebar jika diperlukan
            // .height(250.dp)  // Hapus baris ini agar tinggi menyesuaikan konten
            .padding(16.dp)
            .background(
                color = LightBlue,
                shape = RoundedCornerShape(12.dp)
            ).padding(16.dp),
        verticalAlignment = Alignment.CenterVertically // Tetap memusatkan konten secara vertikal
    ){
        // Display processed image if available, otherwise original image
        val imageUrl = skinLesion.processedImageUrl.takeIf { it.isNotEmpty() }
            ?: skinLesion.originalImageUrl

        // Image using Glide with AndroidView
        AsyncImage(
            model = imageUrl,
            contentDescription = null,
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(8.dp))
        )

        Spacer(modifier = Modifier.width(16.dp)) // Spacer horizontal

        // Informasi Skin Lesion
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.weight(1f) // Mengisi sisa ruang horizontal
        ) {
            // Classification and Status
            Text(
                text = "Classification: ${skinLesion?.classification ?: "Loading..."}",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .background(
                        color = Blue.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Status: ${skinLesion.status}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .background(
                        color = Blue.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Dates
            Text(
                text = "Created At: ${formatTimestampToGMT(skinLesion.createdAt)}",
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Start
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Processed At: ${formatTimestampToGMT(skinLesion.processedAt)}",
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Start
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Additional Information
            Text(
                text = "Patient ID: ${skinLesion.patientUid}",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                textAlign = TextAlign.Start
            )
        }
    }
}



@Composable
fun ShimmerEffect(
    modifier: Modifier,
    widthOfShadowBrush: Int = 500,
    angleOfAxisY: Float = 270f,
    durationMillis: Int = 1000,
    content: @Composable () -> Unit = {}
) {


    val shimmerColors = listOf(
        Color.White.copy(alpha = 0.3f),
        Color.White.copy(alpha = 0.5f),
        Color.White.copy(alpha = 1.0f),
        Color.White.copy(alpha = 0.5f),
        Color.White.copy(alpha = 0.3f),
    )

    val transition = rememberInfiniteTransition(label = "")

    val translateAnimation = transition.animateFloat(
        initialValue = 0f,
        targetValue = (durationMillis + widthOfShadowBrush).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = durationMillis,
                easing = LinearEasing,
            ),
            repeatMode = RepeatMode.Restart,
        ),
        label = "Shimmer loading animation",
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(x = translateAnimation.value - widthOfShadowBrush, y = 0.0f),
        end = Offset(x = translateAnimation.value, y = angleOfAxisY),
    )

    Box(
        modifier = modifier
    ) {
        Spacer(
            modifier = Modifier
                .matchParentSize()
                .background(brush, shape = RoundedCornerShape(18.dp))
        )
    }


}
