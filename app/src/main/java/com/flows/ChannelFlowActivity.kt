package com.flows

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.helper.widget.Flow
import androidx.lifecycle.lifecycleScope
import com.coroutine.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onEmpty
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.reduce
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toCollection
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch

class ChannelFlowActivity : AppCompatActivity() {

    var channelSingly = Channel<String>()
    var channelMulti = Channel<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_channel_flow)

//        channelProducer()
//        channelReceiver()
//        ChannelType()
//        flowProduce()
//        flowReceive()
//        flowOperators()
//        produceSharedFlow()
//   receiveSharedFlow()
//    produceStateFlow()
        receiveStateFlow()
    }

    private fun receiveStateFlow() {
        lifecycleScope.launch {

            var myStateFlow=produceStateFlow()
//            delay(6000)
            Log.d("222","~myStateFlow~~~"+myStateFlow.value)

//            myStateFlow.collect{
//
//                Log.d("222","~myStateFlow~~~"+it)
//
//            }

        }
    }

    private fun produceStateFlow() :kotlinx.coroutines.flow.StateFlow<Int>{
        val mutableStateFlow=MutableStateFlow<Int>(10)
        lifecycleScope.launch {

            var list= listOf(21,23,25,27)
            list.forEach{

                mutableStateFlow.emit(it)
                Log.d("222","~~mutableStateFlow~EMIT~~~"+it)
            }

        }
        return mutableStateFlow
    }

    private fun receiveSharedFlow() {
        lifecycleScope.launch(Dispatchers.IO) {

            var mutableSharedFlow=produceSharedFlow()

            mutableSharedFlow.collect{

                Log.d("222","~FIRST~mutableSharedFlow~~~~"+it)
            }

        }


        lifecycleScope.launch(Dispatchers.IO) {

            var mutableSharedFlowSecond=produceSharedFlow()
 delay(1500)
            mutableSharedFlowSecond.collect{

                Log.d("222","~SECOND~mutableSharedFlowSecond~~~~"+it)
            }

        }
    }

    private fun produceSharedFlow()
    :kotlinx.coroutines.flow.Flow<Int> {
        val mutableSharedFlow = MutableSharedFlow<Int>(2)

        lifecycleScope.launch {

            val list = listOf(1, 7, 8,88)
            list.forEach() {
                mutableSharedFlow.emit(it)
                delay(1000)
            }}
            return mutableSharedFlow
        }




    private fun flowOperators() {

        var filterOp = lifecycleScope.launch {
            var flowOp = flowProduce().onStart {
                Log.d("222", "~onStart~~")

            }
                .onCompletion {
                    Log.d("222", "~onCompletion~~")

                }
                .onEach {
                    Log.d("222", "~onEach~~")

                }
                .onEmpty {
                    Log.d("222", "~onEmpty~~")

                }

            flowOp.map {
//               Log.d("222","~~Filter~Map~~~~"+it)

                it * 4
            }.filter {
                it == 20
            }.collect {
//       Log.d("222","~~Filter~~~~~"+it)
            }


        }



        lifecycleScope.launch {

            flowOf(1, 2, 3)
                .flatMapConcat { flowOf(it, it * 2) }
                .collect { println(it) }
        }

        lifecycleScope.launch {

            flowOf(1, 2, 3)
                .onEach { println("Emitting $it") }
                .collect { println(it) }

            lifecycleScope.launch {

                val sum = flowOf(1, 2, 3)
                    .reduce { accumulator, value -> accumulator + value }  // Sum of the values
                println(sum)
            }


            lifecycleScope.launch {
                flowOf(1, 2, 3, 4)
                    .take(3)
                    .collect { println("Take $it") }
            }

            lifecycleScope.launch {
                val flow1 = flowOf(1, 2, 3)
                val flow2 = flowOf("A", "B", "C")
                flow1.zip(flow2) { a, b -> "$a$b" }
                    .collect { println(it) }


            }
        }

        lifecycleScope.launch {
            flow {
                emit(100)
                throw RuntimeException("Error")
            }.catch { e ->
                println("Caught $e")
            }.collect { println(it) }
        }
        lifecycleScope.launch {
            flowOf(1, 2, 3).collectLatest {
                value ->
                    println("Processing $value")
                    delay(1000) // Simulate long processing
                    println("Processed $value")
                }
        }
    }


    private fun flowReceive() {

      var job=  lifecycleScope.launch {
          var flowD=flowProduce()
            flowD.collect{
                Log.d("222","~it~"+it)
            }


        }
//cancel flow
        lifecycleScope.launch {
            delay(2000)
            job.cancel()
        }

        //multiple collector
        var job3=  lifecycleScope.launch {
            var flowD=flowProduce()
            flowD.collect{
                Log.d("222","~it~second collect"+it)
            }


        }

    }

    private fun ChannelType() {


//            val channel = produce(capacity = Channel.RENDEZVOUS)
        }


     fun flowProduce()=flow<Int> {
        val list= listOf(1,2,4,5,8,9,10)
        list.forEach{
            delay(1000)
            emit(it)
        }
    }.flowOn(Dispatchers.IO)

    private fun channelReceiver() {
      lifecycleScope.launch {

          Log.d("222","~1~myValue~Recei~!!!!!"+channelSingly.receive())
          Log.d("222","~2~myValue~Recei~"+channelSingly.receive())
          Log.d("222","~~myValue~Recei~"+channelSingly.receive())
          Log.d("222","~4~myValue~Recei~"+channelSingly.receive())
          Log.d("222","~4~myValue~multi~"+channelMulti.receive())
          channelSingly.close()
          Log.d("222","~isClosedForReceive~~~~~"+channelSingly.isClosedForReceive)
          Log.d("222","~isClosedForSend~~~~~"+channelSingly.isClosedForSend)


      }

        lifecycleScope.launch {
          channelMulti.consumeEach {
              Log.d("222","~4~myValue~multi~ITTT"+it)

          }
        }
    }

    private fun channelProducer() {
        lifecycleScope.launch {
            channelSingly.send("1")
            channelSingly.send("2")
            channelSingly.send("3")


//      channelSingly.close()

//            channelMulti.close()



        }

        lifecycleScope.launch {
            launch {
                repeat(4){

                    channelMulti.send("coming soon $it")

                }
            }
        }




    }
}