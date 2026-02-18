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

/**
 * Hilt module providing article feature dependencies.
 *
 * Binds interfaces to their implementations for dependency injection.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class ArticleModule {

    /**
     * Binds [ArticleRepositoryImpl] to [ArticleRepository] interface.
     *
     * @param impl The repository implementation.
     * @return [ArticleRepository] interface.
     */
    @Binds
    @Singleton
    abstract fun bindArticleRepository(
        impl: ArticleRepositoryImpl
    ): ArticleRepository

    /**
     * Binds [ArticleRemoteDataSourceImpl] to [ArticleRemoteDataSource] interface.
     *
     * @param impl The remote data source implementation.
     * @return [ArticleRemoteDataSource] interface.
     */
    @Binds
    @Singleton
    abstract fun bindArticleRemoteDataSource(
        impl: ArticleRemoteDataSourceImpl
    ): ArticleRemoteDataSource

    /**
     * Binds [ArticleLocalDataSourceImpl] to [ArticleLocalDataSource] interface.
     *
     * @param impl The local data source implementation.
     * @return [ArticleLocalDataSource] interface.
     */
    @Binds
    @Singleton
    abstract fun bindArticleLocalDataSource(
        impl: ArticleLocalDataSourceImpl
    ): ArticleLocalDataSource
}
