package com.shishkin.luxuriouswatchface.watchface

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import com.shishkin.luxuriouswatchface.R
import kotlin.math.roundToInt

data class RenderData(
    val context: Context,
    val backgroundImageProvider: ScaledImageProvider,
    val hourHandData: HandData,
){

    private var isInit = false

    fun init(renderDataInitProvider : () -> RenderDataInit){
        synchronized(this) {
            if (!isInit) {
                val renderDataInit = renderDataInitProvider()
                val canvas = renderDataInit.canvas

                backgroundImageProvider.init(context, canvas.width, canvas.height)
                hourHandData.init(context, renderDataInit)

                isInit = true
            }
        }
    }
}

data class RenderDataInit(
    val canvas: Canvas
)

fun createRenderData(context: Context) =
    RenderData(
        context,
        backgroundImageProvider = ScaledImageProvider(
            R.drawable.background_square,
            R.drawable.background_round
        ),
        HandData(
            imageProvider = ScaledImageProvider(
                R.drawable.hour_hand
            ),
            widthPercent = 10,
            heightPercent = 45
        ),
    )

class ScaledImageProvider(private val resourceId: Int, private val roundResourceId: Int? = null){

    private lateinit var image: Bitmap

    fun init(context: Context, width: Int, height: Int) {
        val resources = context.resources
        val backgroundResourceId =
            if (roundResourceId != null && resources.configuration.isScreenRound) {
                roundResourceId
            } else {
                resourceId
            }
        image = BitmapFactory.decodeResource(resources, backgroundResourceId)
            .let {
                Bitmap.createScaledBitmap(it, width, height, true)
            }
    }

    operator fun invoke() = image
}

data class HandData(
    val imageProvider: ScaledImageProvider,
    val widthPercent: Int,
    val heightPercent: Int
) {

    var width = 0
    var height = 0

    fun init(context: Context, renderDataInit: RenderDataInit) {
        width =  computeMeasurement(renderDataInit.canvas.width, widthPercent)
        height = computeMeasurement(renderDataInit.canvas.height, heightPercent)

        imageProvider.init(context, width, height)
    }

    private fun computeMeasurement(measurement : Int, percent: Int): Int =
        ((measurement * percent).toFloat() / 100).roundToInt()
}

//fun <T, V> lazy(argumentInitializer: () -> T, lazyFunction: (T) -> V) = LazyWithArg(argumentInitializer, lazyFunction)
//
//class LazyWithArg<T, V>(val argumentInitializer: () -> T, val lazyFunction: (T) -> V){
//
//    private val value by lazy { lazyFunction(argumentInitializer()) }
//
//    operator fun getValue(thisRef: Any?, property: KProperty<*>): V = value
//}

//class LazyProvider<T, V>(provider: () -> V){
//
//    private var providerArg: (() -> T)? = null
//
//    private val providerqwe by lazy (this::providerArg){
//        provider
//    }
//
//    operator fun invoke(arg: T): V{
//        providerArg = {arg}
//        val providerqwe = providerqwe()
//        providerArg = null
//
//        return providerqwe
//    }
//}
//
//open class Provider<V>(private val provider : () -> V){
//    private val value by lazy{
//        provider
//    }
//
//    open operator fun invoke() = value
//}
//
////open class ArgumentProvider<T, V> (arg: T, providerConverter : (T) -> () -> V): Provider<V>(providerConverter(arg))
//
//open class ArgumentProvider<T, V> (val providerConverter : (T) -> () -> V){
//
//    private val convertProvider by lazy ({100}){}
//
//    private val provider: Provider<V> by lazy{
//        provder
//    }
//
//    operator fun invoke(arg: T){
//        provider = Provider(providerConverter(arg))
//    }
//
//    open operator fun invoke() = value
//}
//
//class ScaleImageProvider(private val imageProvider :  () -> Bitmap){
//
//    lateinit var providerArg: () -> RenderData
//
//    private val provider by lazy (this::providerArg){}
//
//    operator fun invoke(width: Int, height: Int){
//        val
//    }
//
//    companion object {
//
//        fun createScaleProvider(provider: () -> Bitmap) : (Int, Int) -> Bitmap =
//            { width: Int, height: Int ->
//                Bitmap.createScaledBitmap(provider(), width, height, true)
//            }
//
//        fun convertProvider(scaleProvider : (Int, Int) -> Bitmap) : () -> Bitmap = {
//            scaleProvider(0, 0)
//        }
//    }
//}