package com.lifecycle.binding.server

import com.koushikdutta.async.http.server.AsyncHttpServer
import com.koushikdutta.async.http.server.AsyncHttpServerRequest
import com.koushikdutta.async.http.server.AsyncHttpServerResponse
import com.koushikdutta.async.http.server.HttpServerRequestCallback
import com.lifecycle.binding.life.AppLifecycle
import com.lifecycle.binding.util.assertToFile
import java.io.File


class LocalServer(private val json:String = "json",private val port:Int = 8888) : HttpServerRequestCallback {

    private val path by lazy { AppLifecycle.application.getExternalFilesDir(null)!!
            .also {
                    it.deleteRecursively()
                    assertToFile(AppLifecycle.application, json, it)
            }
    }

    private val server = AsyncHttpServer()

    fun start() {
        server.addAction("OPTIONS", "[\\d\\D]*", this)
        server.get("[\\d\\D]*", this)
        server.post("[\\d\\D]*", this)
        server.listen(port)
    }

    override fun onRequest(request: AsyncHttpServerRequest, response: AsyncHttpServerResponse) {
        File(path,  json+request.path).also { if (it.exists()) response.sendFile(it) }
    }

    fun stop() {
        server.stop()
    }
}