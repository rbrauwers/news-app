package com.rbrauwers.newsapp.data

import com.rbrauwers.newsapp.data.model.toEntity
import org.junit.Assert
import org.junit.Test

class ArticleMapperTests {

    @Test
    fun articleShouldMapToEntity() {
        val entity = article.toEntity()
        Assert.assertEquals(article.author, entity.author)
        Assert.assertEquals(article.content, entity.content)
        Assert.assertEquals(article.description, entity.description)
        Assert.assertEquals(article.publishedAt, entity.publishedAt)
        Assert.assertEquals(article.title, entity.title)
        Assert.assertEquals(article.url, entity.url)
        Assert.assertEquals(article.urlToImage, entity.urlToImage)
        Assert.assertEquals(article.id, entity.id)
    }

}