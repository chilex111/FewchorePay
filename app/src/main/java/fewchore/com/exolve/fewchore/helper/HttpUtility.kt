package fewchore.com.exolve.fewchore.helper

import android.content.Context
import android.util.Log
import com.bumptech.glide.load.Key
import fewchore.com.exolve.fewchore.R
import org.json.JSONObject
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.nio.charset.Charset
import javax.net.ssl.HttpsURLConnection

object HttpUtility {

    private val TAG = "HttpUtility"

    @Throws(IOException::class)
     fun sendPostRequest(mUrl: String, params: HashMap<String, Any?>): String {
        var `in`: InputStream? = null
        var conn: HttpURLConnection? = null
        val charset = "UTF-8"
        val sbParams = StringBuilder()
        val parameters: String
        try {
            for ((i, key) in params.keys.withIndex()) {
                if (i != 0) {
                    sbParams.append("&")
                }
                sbParams.append(key).append("=")
                        .append(URLEncoder.encode(params[key].toString(), charset))

            }
            parameters = sbParams.toString()
            val postData = parameters.toByteArray(Charset.forName("UTF-8"))
            val postDataLength = postData.size
            val url = URL(mUrl)
            conn = url.openConnection() as HttpURLConnection
            conn.doOutput = true
            conn.setFixedLengthStreamingMode(postDataLength)//conn.setChunkedStreamingMode(0); for unknown length
            conn.readTimeout = 70000
            conn.connectTimeout = 70000
            conn.requestMethod = "POST"

            System.setProperty("http.keepAlive", "false")
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
            conn.setRequestProperty("charset", "utf-8")
            conn.setRequestProperty("Content-Length", Integer.toString(postDataLength))
            /*conn.addRequestProperty("Authorization",auth);
      conn.addRequestProperty("X-Api-Key",api_Key);*/
            //conn.addRequestProperty("content-type"," multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW");

            val writer = DataOutputStream(conn.outputStream)
            writer.write(postData)
            val response: String
            val responseCode = conn.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                `in` = BufferedInputStream(conn.inputStream)
                response = readStream(`in`)
                // return readStream(in);

            } else {
                `in` = BufferedInputStream(conn.errorStream)
                response = readStream(`in`)
                // return readStream(in);
            }

            Log.i("Response", response)
            return response
        }/*catch (e: Exception){}
        return null*/
        finally {
            `in`?.close()
            conn?.disconnect()
        }
    }

     fun sendPaystackPostRequest(mUrl: String, params: HashMap<String, Any?>, context:Context): String {
         var `in`: InputStream? = null
         var conn: HttpURLConnection? = null
         val charset = "UTF-8"
         val sbParams = StringBuilder()
         val parameters: String
         try {
             var i = 0
             for (key in params.keys) {
                 if (i != 0) {
                     sbParams.append("&")
                 }
                 sbParams.append(key).append("=")
                         .append(URLEncoder.encode(params[key].toString(), charset))

                 i++
             }
             parameters = sbParams.toString()
             val postData = parameters.toByteArray(Charset.forName("UTF-8"))
             val postDataLength = postData.size
             val url = URL(mUrl)
             conn = url.openConnection() as HttpURLConnection
             conn.doOutput = true
             conn.setFixedLengthStreamingMode(postDataLength)//conn.setChunkedStreamingMode(0); for unknown length
             conn.readTimeout = 70000
             conn.connectTimeout = 70000
             conn.requestMethod = "POST"

             conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
             conn.setRequestProperty("charset", "utf-8")
             conn.setRequestProperty("Content-Length", Integer.toString(postDataLength))
             val auth = "Bearer "+context.getString(R.string.secret_key)
             conn.addRequestProperty("Authorization",auth)

             /*conn.addRequestProperty("Authorization",auth);

      conn.addRequestProperty("X-Api-Key",api_Key);*/
             //conn.addRequestProperty("content-type"," multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW");

             val writer = DataOutputStream(conn.outputStream)
             writer.write(postData)
             val response: String
             val responseCode = conn.responseCode
             if (responseCode == HttpURLConnection.HTTP_OK) {
                 `in` = BufferedInputStream(conn.inputStream)
                 response = readStream(`in`)
                 // return readStream(in);

             } else {
                 `in` = BufferedInputStream(conn.errorStream)
                 response = readStream(`in`)
                 // return readStream(in);
             }

             Log.i("Response", response)
             return response
         } finally {
             if (`in` != null) {
                 `in`.close()
             }
             if (conn != null) {
                 conn.disconnect()
             }
         }
     }

     @Throws(IOException::class)
    private fun readStream(stream: InputStream?): String {
        val bReader = BufferedReader(InputStreamReader(stream, Key.STRING_CHARSET_NAME))
        val out = StringBuilder()
        while (true) {
            val line = bReader.readLine()
            if (line != null) {
                out.append(line)
            } else {
                Log.i(TAG, "HTTP RESPONSE" + out.toString())
                return out.toString()
            }
        }
    }

    @Throws(IOException::class)
    fun getRequest(myUrl: String): String {
        var `is`: InputStream? = null
        try {
            val conn = URL(myUrl).openConnection() as HttpURLConnection
            conn.readTimeout = 40000
            conn.connectTimeout = 35000
            conn.requestMethod = "GET"
            conn.doInput = true
            conn.connect()
            conn.responseCode
            `is` = conn.inputStream
            return readStream(`is`)
        } finally {
            `is`?.close()
        }
    }

    @Throws(IOException::class)
    fun postJsonAuth(mUrl: String, jsonParam: JSONObject,api_key: String): String {
        var conn: HttpsURLConnection? = null
        val printout: DataOutputStream
        var `in`: InputStream? = null
        try {
            val url = URL(mUrl)
            conn = url.openConnection() as HttpsURLConnection
            conn.doInput = true
            conn.doOutput = true
            conn.useCaches = false
            conn.readTimeout = 50000
            conn.connectTimeout = 40000
            conn.setRequestProperty("Content-Type", "application/json")
            conn.setRequestProperty("Authorization", "Bearer $api_key")
            conn.requestMethod = "POST"
            conn.setRequestProperty("charset", "utf-8")


            val postData = jsonParam.toString().toByteArray(Charset.forName("UTF-8"))

            printout = DataOutputStream(conn.outputStream)
            printout.write(postData)
            //printout.flush ();

            val responseCode = conn.responseCode

            if (responseCode == HttpURLConnection.HTTP_OK) {
                `in` = BufferedInputStream(conn.inputStream)
                return readStream(`in`)
            } else {
                `in` = BufferedInputStream(conn.errorStream)
                Log.i("TAG", `in`.toString())
                return readStream(`in`)

            }

        } finally {
            `in`?.close()
            conn?.disconnect()
        }
    }

    @Throws(IOException::class)
    fun postJson(mUrl: String, jsonParam: JSONObject): String {
        var conn: HttpURLConnection? = null
        val printout: DataOutputStream
        var `in`: InputStream? = null
        try {
            val url = URL(mUrl)
            conn = url.openConnection() as HttpURLConnection
            conn.doInput = true
            conn.doOutput = true
            conn.useCaches = false
            conn.readTimeout = 50000
            conn.connectTimeout = 40000
            conn.setRequestProperty("Content-Type", "application/json")
            conn.requestMethod = "POST"
            conn.setRequestProperty("charset", "utf-8")


            val postData = jsonParam.toString().toByteArray(Charset.forName("UTF-8"))

            printout = DataOutputStream(conn.outputStream)
            printout.write(postData)
            //printout.flush ();

            val responseCode = conn.responseCode

            if (responseCode == HttpURLConnection.HTTP_OK) {
                `in` = BufferedInputStream(conn.inputStream)
                return readStream(`in`)
            } else {
                `in` = BufferedInputStream(conn.errorStream)
                Log.i("TAG", `in`.toString())
                return readStream(`in`)

            }

        } finally {
            `in`?.close()
            conn?.disconnect()
        }
    }

}