package com.example.modules.learningkotlinjetpack

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

/**
 * Exercise 8: API Requests
 * 
 * This file demonstrates performing asynchronous network operations
 * using Ktor HTTP client and handling common networking tasks.
 */

/**
 * Data class representing a post from the API.
 */
@Serializable
data class PostDTO(
    val id: Int,
    val title: String,
    val body: String,
    val userId: Int
)

/**
 * Wrapper class for a list of posts that can be serialized.
 */
@Serializable
data class PostList(
    val posts: List<PostDTO>
)

/**
 * Service interface for API operations.
 */
interface ApiService {
    suspend fun getPosts(): List<PostDTO>
    suspend fun createPost(post: PostDTO): PostDTO?
}

/**
 * Implementation of ApiService using Ktor client.
 */
class ApiServiceImpl : ApiService {
    private val client = HttpClient(Android) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
    }
    
    override suspend fun getPosts(): List<PostDTO> {
        return client.get("https://jsonplaceholder.typicode.com/posts") {
            headers {
                append(HttpHeaders.CacheControl, "max-age=3600") // 1 hour cache
            }
        }.body<List<PostDTO>>()
    }
    
    override suspend fun createPost(post: PostDTO): PostDTO? {
        return try {
            client.post("https://jsonplaceholder.typicode.com/posts") {
                contentType(ContentType.Application.Json)
                setBody(post)
            }.body<PostDTO>()
        } catch (e: Exception) {
            null
        }
    }
}

/**
 * UI State for the API screen.
 */
data class ApiUiState(
    val posts: List<PostDTO> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val newPostTitle: String = "",
    val newPostBody: String = ""
)

/**
 * ViewModel for managing API operations and state.
 */
class ApiViewModel : ViewModel() {
    private val apiService: ApiService = ApiServiceImpl()
    
    private val _uiState = MutableStateFlow(ApiUiState())
    val uiState: StateFlow<ApiUiState> = _uiState.asStateFlow()
    
    init {
        loadPosts()
    }
    
    fun loadPosts() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)
                val posts = apiService.getPosts()
                _uiState.value = _uiState.value.copy(
                    posts = posts,
                    isLoading = false,
                    errorMessage = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Failed to load posts"
                )
            }
        }
    }
    
    fun updateNewPostTitle(title: String) {
        _uiState.value = _uiState.value.copy(newPostTitle = title)
    }
    
    fun updateNewPostBody(body: String) {
        _uiState.value = _uiState.value.copy(newPostBody = body)
    }
    
    fun createPost() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)
                val newPost = PostDTO(
                    id = 0, // API will assign real ID
                    title = _uiState.value.newPostTitle,
                    body = _uiState.value.newPostBody,
                    userId = 1 // Hardcoded for demo
                )
                
                val createdPost = apiService.createPost(newPost)
                if (createdPost != null) {
                    _uiState.value = _uiState.value.copy(
                        posts = _uiState.value.posts + createdPost,
                        newPostTitle = "",
                        newPostBody = "",
                        isLoading = false,
                        errorMessage = null
                    )
                } else {
                    throw Exception("Failed to create post")
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Failed to create post"
                )
            }
        }
    }
}

/**
 * Main screen composable for API operations.
 */
@Composable
fun ApiScreen() {
    val viewModel = remember { ApiViewModel() }
    val uiState by viewModel.uiState.collectAsState()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Create post section
        CreatePostSection(
            title = uiState.newPostTitle,
            body = uiState.newPostBody,
            onTitleChange = { viewModel.updateNewPostTitle(it) },
            onBodyChange = { viewModel.updateNewPostBody(it) },
            onCreatePost = { viewModel.createPost() }
        )
        
        HorizontalDivider()
        
        // Posts list section
        if (uiState.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        } else {
            PostsListSection(
                posts = uiState.posts,
                errorMessage = uiState.errorMessage,
                onRetry = { viewModel.loadPosts() }
            )
        }
    }
}

/**
 * Composable for creating new posts.
 */
@Composable
fun CreatePostSection(
    title: String,
    body: String,
    onTitleChange: (String) -> Unit,
    onBodyChange: (String) -> Unit,
    onCreatePost: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Create New Post",
            style = MaterialTheme.typography.titleLarge
        )
        
        OutlinedTextField(
            value = title,
            onValueChange = onTitleChange,
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth()
        )
        
        OutlinedTextField(
            value = body,
            onValueChange = onBodyChange,
            label = { Text("Body") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3
        )
        
        Button(
            onClick = onCreatePost,
            enabled = title.isNotBlank() && body.isNotBlank(),
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Create Post")
        }
    }
}

/**
 * Composable for displaying the list of posts.
 */
@Composable
fun PostsListSection(
    posts: List<PostDTO>,
    errorMessage: String?,
    onRetry: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Posts",
            style = MaterialTheme.typography.titleLarge
        )
        
        if (errorMessage != null) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyLarge
            )
            
            Button(
                onClick = onRetry,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Retry")
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(posts) { post ->
                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = post.title,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = post.body,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Preview for the ApiScreen composable.
 */
@Preview(
    name = "API Screen Preview",
    showBackground = true
)
@Composable
fun ApiScreenPreview() {
    ApiScreen()
}

/**
 * Preview for the CreatePostSection composable.
 */
@Preview(
    name = "Create Post Section Preview",
    showBackground = true
)
@Composable
fun CreatePostSectionPreview() {
    CreatePostSection(
        title = "",
        body = "",
        onTitleChange = {},
        onBodyChange = {},
        onCreatePost = {}
    )
}

/**
 * Preview for the PostsListSection composable.
 */
@Preview(
    name = "Posts List Section Preview",
    showBackground = true
)
@Composable
fun PostsListSectionPreview() {
    PostsListSection(
        posts = listOf(
            PostDTO(1, "Title 1", "Body 1", 1),
            PostDTO(2, "Title 2", "Body 2", 1)
        ),
        errorMessage = null,
        onRetry = {}
    )
} 