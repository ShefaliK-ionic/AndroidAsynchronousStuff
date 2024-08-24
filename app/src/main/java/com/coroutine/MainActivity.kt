package com.coroutine

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        introduceCoroutineBuilder()
//        introduceCoroutineResult()
//         sequentialFunViaLaunch()
//          parallelExecutionViaAsync()
//        launchJobFunctions()
//        dispatcherSupportList()
// supervisorScopeForExecuteOtherEvenExcptionForSibling()
//        coroutineExceptionHandler()
        deferredObjectAsync()
    }

    private fun deferredObjectAsync() {
        var deferredObj:Deferred<String>
        GlobalScope.launch {
            deferredObj= lifecycleScope.async(Dispatchers.IO) {
                println("Running on B4 Async: ${Thread.currentThread().name}")

                delay(1500)
                println("Running on thread Async: ${Thread.currentThread().name}")

                "Hi Shriti"


            }



            withContext(Dispatchers.Main,{
                delay(100)
                println("Switched to IO dispatcher on thread: ${Thread.currentThread().name}")

            })

            println("Switched to 999 dispatcher on thread: ${Thread.currentThread().name}")



            Log.d("222", "~~deferredObj~~" + deferredObj.await())
        }



    }

    private fun coroutineExceptionHandler() {
      val coroutineExceptionHandler= CoroutineExceptionHandler { coroutineContext, throwable ->
            Log.d("222","~~~CoroutineExceptionHandler~~~~~"+throwable.toString())
        }


        GlobalScope.launch(coroutineExceptionHandler) {

            var parentJob=            supervisorScope {

                var job1 = launch {

                        throw Exception("Sh excep")

                }

                var job2 = launch {
delay(1000)
                  Log.d("222","~~~~JOB2~~`")

                }
            }
        }


    }

    private fun supervisorScopeForExecuteOtherEvenExcptionForSibling() {
          //with superviosr
          lifecycleScope.launch {
            supervisorScope {

                    var job1 = launch {
                        try {
                            throw Exception("TestExce")
                        }catch (e:Exception){
                            Log.d("222", "~~~~supervisorScope~exc~complete~"+e)

                        }
                    }

                    var job2 = async {
                       Log.d("222", "~~~~job2~~~")
                        return@async "Shriti"
                    }

                    var job3 = async {
//                        Log.d("222", "~~~~job3~~~")
                        return@async "devoy"
                    }


                    Log.d("222", "~~~~supervisorScope~~complete~"+job2.await()+"~~"+job3.await())
                }

            }
         //with launch
//          lifecycleScope.launch {
//              var job1 = launch {
//                  try {
//                      throw Exception("TestLAUNCHExce")
//                  } catch (e: Exception) {
//                      Log.d("222", "~~~~launch~exc~complete~" + e)
//
//                  }
//              }
//
//
//              var job2 = launch {
//                  Log.d("222", "~~launch~~job2~~~")
//
//              }
//
//              var job3 = launch {
//                  Log.d("222", "~launch~~~job3~~~")
//
//              }
//
//          }


        lifecycleScope.launch(Dispatchers.IO) {

            supervisorScope {
                var job1 = async {
                    Log.d("222", "~~566~~")
                    throw Exception("566 exce")
                }

                var job12 = async {
                    Log.d("222", "~~566~~12")

                }


                try {
                    Log.d("222", "~~566~~" + job1.await())
                    Log.d("222", "~~566~~" + job12.await())
                } catch (e: Exception) {

                }
            }
        }



    }


    private fun dispatcherSupportList() {
        lifecycleScope.launch(Dispatchers.Default) {
            Log.d("222","~Dispatchers.Default~~${Thread.currentThread().name}")


        }

        lifecycleScope.launch(Dispatchers.Main) {
            Log.d("222","~Dispatchers.MAIN~~${Thread.currentThread().name}")


        }
        CoroutineScope(Dispatchers.IO).launch {
               delay(3000)
            Log.d("222","~Dispatchers.IO~~${Thread.currentThread().name}")


        }
        lifecycleScope.launch(Dispatchers.Unconfined) {
            Log.d("222","~Dispatchers.Unconfined~~${Thread.currentThread().name}")


        }

    }

    private fun launchJobFunctions() {
        lifecycleScope.launch {

            var job1=lifecycleScope.launch (Dispatchers.Unconfined){



            }


        }
    }

    private fun parallelExecutionViaAsync() {
        lifecycleScope.async {
            var id=getMyData()
            var id2=getMySecondData()
//time almost same as run parallely
            Log.d("222","~parallelExecutionViaAsync~~$id")
            Log.d("222","~parallelExecutionViaAsync~~id2~~~~$id2")
        }
    }

    private fun sequentialFunViaLaunch() {
//diff in time as sequential behaviour
        lifecycleScope.launch {
            var id=getMyData()
            var id2=getMySecondData()

            Log.d("222","~sequentialFunViaLaunch~~$id")
            Log.d("222","~sequentialFunViaLaunch~~id2~~~~$id2")
        }


    }

    private suspend fun getMyData(): Any {

        delay(2000)
        return 1
    }

    private suspend fun getMySecondData(): Any {

        delay(2000)
        return 2
    }

    private fun introduceCoroutineBuilder() {
        Log.d("222", "~~runBlocking~00" + Thread.currentThread().name)


        runBlocking {
            delay(2000)
            Log.d("222", "~~runBlocking~" + Thread.currentThread().name)

        }

        lifecycleScope.launch(Dispatchers.IO){

            Log.d("222","~lifecycleScope~launch~~~")


        }

    lifecycleScope.async {
            Log.d("222","~lifecycleScope~async~~~")


        }

        GlobalScope.async {
            Log.d("222","~GlobalScope~async~~~")

        }


        GlobalScope.launch(Dispatchers.Main) {
            Log.d("222","~GlobalScope~main~~~")

        }

        MainScope().launch(Dispatchers.Main){
            Log.d("222","~GlobalScope~main~~~")

        }

        CoroutineScope(Dispatchers.IO).launch {

            Log.d("222", "~~withContext result~555~" + Thread.currentThread().name)

            withContext(Dispatchers.Main) {
                Log.d("222", "~~withContext inside~" + Thread.currentThread().name)


            }
        }

    }

    private fun introduceCoroutineResult() {

        //both coroutine works as parallel
        //todo launch
        lifecycleScope.launch {

            var ab = GlobalScope.launch {
                Log.d("222","~~launch~22~")

                delay(3000)
                Log.d("222","~~launch~555~")

            }

            Log.d("222","~~launch~~"+ab.join())

        }

        //todo ASYNC
        lifecycleScope.launch {

          var synJob=  async {
              Log.d("222","~~async~22~")

              delay(3000)
              Log.d("222","~~async~555~")
                return@async "shriti"
            }

            var result=synJob.await()

            Log.d("222","~~async result~555~"+result)

        }

        //todo dispatcher switching
        CoroutineScope(Dispatchers.IO).launch {

              Log.d("222", "~~withContext result~555~" + Thread.currentThread().name)

                withContext(Dispatchers.Main) {
                    Log.d("222", "~~withContext inside~" + Thread.currentThread().name)


                }
            }

        MainScope().launch(Dispatchers.Main){
            Log.d("222", "~~MainScope().launch~" + Thread.currentThread().name)
        }






    }
}