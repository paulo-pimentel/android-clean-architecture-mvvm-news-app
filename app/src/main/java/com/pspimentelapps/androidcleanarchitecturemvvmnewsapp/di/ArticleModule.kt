package com.pspimentelapps.androidcleanarchitecturemvvmnewsapp.di

import com.pspimentelapps.androidcleanarchitecturemvvmnewsapp.features.articles.data.datasources.ArticleLocalDataSource
import com.pspimentelapps.androidcleanarchitecturemvvmnewsapp.features.articles.data.datasources.ArticleLocalDataSourceImpl
import com.pspimentelapps.androidcleanarchitecturemvvmnewsapp.features.articles.data.datasources.ArticleRemoteDataSource
import com.pspimentelapps.androidcleanarchitecturemvvmnewsapp.features.articles.data.datasources.ArticleRemoteDataSourceImpl
import com.pspimentelapps.androidcleanarchitecturemvvmnewsapp.features.articles.data.repositories.ArticleRepositoryImpl
import com.pspimentelapps.androidcleanarchitecturemvvmnewsapp.features.articles.domain.repositories.ArticleRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ArticleModule {
    @Binds
    @Singleton
    abstract fun bindArticleRepository(
        impl: ArticleRepositoryImpl
    ): ArticleRepository

    @Binds
    @Singleton
    abstract fun bindArticleRemoteDataSource(
        impl: ArticleRemoteDataSourceImpl
    ): ArticleRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindArticleLocalDataSource(
        impl: ArticleLocalDataSourceImpl
    ): ArticleLocalDataSource
}
