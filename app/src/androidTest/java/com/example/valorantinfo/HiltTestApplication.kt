package com.example.valorantinfo

import android.app.Application
import dagger.hilt.android.testing.CustomTestApplication

// This is the proper way to define a custom test application with Hilt
// It will generate a HiltTestApplication_Application class for us
@CustomTestApplication(Application::class)
interface HiltTestApplication 