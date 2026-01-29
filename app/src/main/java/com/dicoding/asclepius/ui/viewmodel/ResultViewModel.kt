package com.dicoding.asclepius.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.asclepius.data.remote.dto.ArticleDto
import com.dicoding.asclepius.data.repository.HealthRepository
import com.dicoding.asclepius.data.Result
import com.dicoding.asclepius.data.remote.response.NewsResponse
import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject

class ResultViewModel(private val repository: HealthRepository) : ViewModel() {
    private val _listArticle = MutableLiveData<Result<List<ArticleDto>?>>()
    val listArticle: LiveData<Result<List<ArticleDto>?>> = _listArticle

    init {
        getAllUser()
    }

    private fun getAllUser() {
        viewModelScope.launch {
            _listArticle.value = Result.Loading

            try {
                val response = repository.getNews()
                _listArticle.value = handleApiResponse(response)
            } catch (e: IOException) {
                _listArticle.value = Result.Error("Network error: ${e.message}")
            } catch (e: HttpException) {
                _listArticle.value = Result.Error(parseHttpException(e))
            } catch (e: Exception) {
                _listArticle.value = Result.Error("Unexpected error: ${e.message}")
            }
        }
    }

    private fun handleApiResponse(response: Response<NewsResponse>): Result<List<ArticleDto>?> {
        return if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                Result.Success(body.articles)
            } else {
                Result.Error("Response body is null")
            }
        } else {
            Result.Error(parseErrorResponse(response.errorBody()))
        }
    }

    private fun parseErrorResponse(errorBody: ResponseBody?): String {
        return try {
            errorBody?.string()?.let { bodyString ->
                JSONObject(bodyString).optString("status_message", "Unknown error")
            } ?: "No error body"
        } catch (e: JSONException) {
            "Error parsing error response: ${e.message}"
        }
    }

    private fun parseHttpException(httpException: HttpException): String {
        return parseErrorResponse(httpException.response()?.errorBody())
    }
}
