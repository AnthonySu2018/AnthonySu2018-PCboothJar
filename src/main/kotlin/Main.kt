import java.io.IOException
import java.net.InetAddress

import java.util.concurrent.TimeUnit
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.client.*
import io.ktor.client.engine.cio.*


val ipAddress = "192.168.10.2"
var isPingSuccess = false

var falseCounter = 0
val client = HttpClient(CIO)
suspend fun main(args: Array<String>) {
    println("Author: Anthony")
    println("Version: 1.1")
    println("Please don't close this windows.")


    do{
        ping(ipAddress)
        isPingSuccess = ping(ipAddress)
        if(isPingSuccess)
        {

            TimeUnit.SECONDS.sleep(5);
            println("Connected to host")
            println("Getting task from host")

            val response: HttpResponse = client.get("http://192.168.10.2:8080/task")
            val respondStr = response.bodyAsText()
            println(respondStr)
            if(respondStr =="shutdown"){
                println("Start to shutdown")
                try {
                    val command = "shutdown.exe -p" // -r 表示重启，-t 0 表示无延迟立即重启
                    val process = Runtime.getRuntime().exec(command)
                    process.waitFor()
                } catch (e: IOException) {
                    e.printStackTrace()
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }

            }
        }
        else{
            println("Ping $ipAddress: $isPingSuccess")
            TimeUnit.SECONDS.sleep(4)
            falseCounter ++
            println("failed to connect to host，the $falseCounter time")
            if(falseCounter >10)
            {
                try {
                    val command = "shutdown.exe -r -t 4" // -r 表示重启，-t 0 表示无延迟立即重启
                    val process = Runtime.getRuntime().exec(command)
                    process.waitFor()
                } catch (e: IOException) {
                    e.printStackTrace()
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }
        println("Please don't close this windows.")
    } while (true)
}


fun ping(ipAddress: String): Boolean {
    return try {
        val inet = InetAddress.getByName(ipAddress)
        inet.isReachable(1000) // 设置超时时间为5秒
    } catch (ex: Exception) {
        false
    }
}


