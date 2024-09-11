package com.shishkin.luxuriouswatchface.watchface

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Rect
import android.util.Log
import android.view.SurfaceHolder
import androidx.wear.watchface.CanvasType
import androidx.wear.watchface.ComplicationSlotsManager
import androidx.wear.watchface.DrawMode
import androidx.wear.watchface.Renderer
import androidx.wear.watchface.WatchFace
import androidx.wear.watchface.WatchFaceService
import androidx.wear.watchface.WatchFaceType
import androidx.wear.watchface.WatchState
import androidx.wear.watchface.style.CurrentUserStyleRepository
import androidx.wear.watchface.style.UserStyleSchema
import com.shishkin.luxuriouswatchface.data.usersstyles.SettingsSchema
import com.shishkin.luxuriouswatchface.di.AppEntryPoint
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.ZonedDateTime
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

private const val FRAME_PERIOD_MS_TICKING = 16L

class LuxuriousWatchFace : WatchFaceService() {

    private val schema by lazy {
        EntryPointAccessors.fromApplication(applicationContext, AppEntryPoint::class.java)
            .createSettingsSchema()
    }

    override suspend fun createWatchFace(
        surfaceHolder: SurfaceHolder,
        watchState: WatchState,
        complicationSlotsManager: ComplicationSlotsManager,
        currentUserStyleRepository: CurrentUserStyleRepository
    ) = WatchFace(
        watchFaceType = WatchFaceType.ANALOG,
        renderer = AnalogWatchCanvasRenderer(
            surfaceHolder = surfaceHolder,
            currentUserStyleRepository = currentUserStyleRepository,
            watchState = watchState,
            canvasType = CanvasType.HARDWARE,
            applicationContext,
            schema
        )
    )

    override fun createUserStyleSchema(): UserStyleSchema =
        schema.createUserStyleSchema(resources)

    class AnalogWatchCanvasRenderer(
        surfaceHolder: SurfaceHolder,
        currentUserStyleRepository: CurrentUserStyleRepository,
        watchState: WatchState,
        canvasType: Int,
        applicationContext: Context,
        schema: SettingsSchema
    ) : Renderer.CanvasRenderer2<AnalogWatchCanvasRenderer.SharedAssets>(
        surfaceHolder,
        currentUserStyleRepository,
        watchState,
        canvasType,
        FRAME_PERIOD_MS_TICKING,
        clearWithBackgroundTintBeforeRenderingHighlightLayer = false
    ) {

        private val scope: CoroutineScope =
            CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

        private val settingsHolder = EntryPointAccessors
            .fromApplication(applicationContext, AppEntryPoint::class.java)
            .createSettingsHolder()

        init {
            settingsHolder.subscribeToStyle(currentUserStyleRepository.userStyle, scope)
        }

        private lateinit var renderData: RenderData

        init {
            scope.launch {
                settingsHolder.settings.collectLatest { v ->
                    renderData = createRenderData(applicationContext, v, schema)
                }
            }
        }

        override fun onDestroy() {
            scope.cancel("AnalogWatchCanvasRenderer scope clear() request")
            super.onDestroy()
        }

        class SharedAssets : Renderer.SharedAssets {
            override fun onDestroy() {
            }
        }

        override suspend fun createSharedAssets(): SharedAssets = SharedAssets()

        override fun renderHighlightLayer(
            canvas: Canvas,
            bounds: Rect,
            zonedDateTime: ZonedDateTime,
            sharedAssets: SharedAssets
        ) {
            canvas.drawColor(renderParameters.highlightLayer!!.backgroundTint)
        }

        override fun render(
            canvas: Canvas,
            bounds: Rect,
            zonedDateTime: ZonedDateTime,
            sharedAssets: SharedAssets
        ) {
            renderData.init { RenderDataInit(canvas) }

            drawBackground(canvas)
            drawHands(canvas, zonedDateTime, renderParameters.drawMode)
            drawTexts(canvas)
        }

        private fun drawBackground(canvas: Canvas){
                canvas.drawBitmap(
                renderData.backgroundImageProvider(),
                    0F,
                    0F,
                    null
                )
        }

        private fun drawHands(canvas: Canvas, zonedDateTime: ZonedDateTime, drawMode: DrawMode) {
            val centerX = canvas.width / 2f
            val centerY = canvas.height / 2f

            val generalData = HandsData(
                canvas = canvas,
                centerPoint = Point(centerX, centerY),
            )

            drawHand(generalData, zonedDateTime.minute, Color.RED)
            drawHand(generalData, zonedDateTime.hour % 12 * 60 + zonedDateTime.minute, renderData.minuteHandColor, true)

            drawHand(generalData, renderData.hourHandData, zonedDateTime.hour % 12 * 60 + zonedDateTime.minute, true)

            if (drawMode != DrawMode.AMBIENT) {
                drawHand(generalData, zonedDateTime.second, Color.YELLOW)
            }
        }

        data class HandsData(
            val canvas: Canvas,
            val centerPoint: Point,
        )

        data class Point(
            val x: Float,
            val y: Float
        )

        private fun drawHand(data: HandsData, timeValue: Int, color: Int, itsHourHand: Boolean = false){

            val handEnd = computeHandEnd(data, timeValue, itsHourHand)

            data.canvas.drawLine(
                data.centerPoint.x,
                data.centerPoint.y,
                handEnd.x,
                handEnd.y,
                Paint().apply {
                    this.color = color
                    strokeWidth = 10f
                    isAntiAlias = true
                    style = Paint.Style.STROKE
                    strokeCap = Paint.Cap.ROUND
                }
            )
        }

        private fun drawHand(generalData: HandsData, handData : HandData, timeValue: Int, itsHourHand: Boolean = false){
            val anglePieces = if(itsHourHand) 180 else 15
            val handAngle: Float = (timeValue * (90 / anglePieces.toFloat()))
            val width = handData.width
            val height = handData.height

            generalData.canvas.drawBitmap(
                handData.imageProvider(),
                Matrix().apply {
                    setRotate(
                        handAngle,
                        (width / 2).toFloat(),
                        (height).toFloat()
                    )
                    postTranslate((generalData.canvas.width/2 - width / 2).toFloat(), (generalData.canvas.height / 2  - height).toFloat())
                },
                null
            )
        }

        private fun computeHandEnd(data: HandsData, timeValue: Int, itsHourHand: Boolean = false): Point{
            val anglePieces = if(itsHourHand) 180 else 15
            val handAngle: Double = (timeValue * (90 / anglePieces.toDouble()))

            val x = sin(handAngle * PI / 180 ) * data.centerPoint.x + data.centerPoint.x
            val y = data.centerPoint.y - cos(handAngle * PI / 180 )  * data.centerPoint.y

            return Point(x.toFloat(), y.toFloat())
        }

        private fun drawTexts(canvas: Canvas){
            drawText(canvas, renderData.topTextData)
        }

        private fun drawText(canvas: Canvas, textData: TextData){

            val text = textData.text
            val paint = Paint().apply {
                color = textData.color
                    typeface = renderData.topTextData.font
                this.textSize =  textData.textSize
            }

            Log.e("qwe", "drawTexts textSize = ${textData.textSize} canvas.width = ${canvas.width}  canvas.height = ${canvas.height} yCenterOffset = ${textData.yCenterOffset}")

            canvas.drawText (text,
                (canvas.width - paint.measureText(text)) / 2f,
                canvas.height / 2f + textData.yCenterOffset,
                paint
            )
        }


    }
}