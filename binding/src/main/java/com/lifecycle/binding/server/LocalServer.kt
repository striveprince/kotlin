package com.lifecycle.binding.server

import com.koushikdutta.async.http.server.AsyncHttpServer
import com.koushikdutta.async.http.server.AsyncHttpServerRequest
import com.koushikdutta.async.http.server.AsyncHttpServerResponse
import com.koushikdutta.async.http.server.HttpServerRequestCallback
import com.lifecycle.binding.life.AppLifecycle
import com.lifecycle.binding.util.assertToFile
import java.io.File


class LocalServer(
    private val json: String = "json",
    private val port: Int = 8888,
    private val update: Boolean = true
) : HttpServerRequestCallback {

    private val server = AsyncHttpServer()
    private val path by lazy {
        AppLifecycle.application.getExternalFilesDir(null)!!
            .also {
                if (update) {
                    it.deleteRecursively()
                    assertToFile(AppLifecycle.application, json, it)
                } else if (!File(it, json).isDirectory){
                    assertToFile(AppLifecycle.application, json, it)
                }
            }
    }

    fun start() {
        server.addAction("OPTIONS", "[\\d\\D]*", this)
        server.get("[\\d\\D]*", this)
        server.post("[\\d\\D]*", this)
        server.listen(port)
    }

    override fun onRequest(request: AsyncHttpServerRequest, response: AsyncHttpServerResponse) {
        File(path, json + request.path).also { if (it.exists()) response.sendFile(it) }
    }

    fun stop() {
        server.stop()
    }

}