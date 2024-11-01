package com.shishkin.luxuriouswatchface.watchface

import android.content.Context
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Rect
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

        private var renderData = createRenderData(applicationContext, schema)

        init {
            scope.launch {
                settingsHolder.settings.collectLatest { v ->
                    if(v != null) renderData = createRenderData(applicationContext, schema, v)
                }
            }
        }

        override fun onDestroy() {
            scope.cancel("${javaClass.name} scope clear() request")
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
            renderData.init(RenderData.RenderDataInit(canvas))

            drawBackground(canvas)
            drawHands(canvas, zonedDateTime, renderParameters.drawMode)
            drawTexts(canvas)
        }

        private fun drawBackground(canvas: Canvas){
            canvas.drawBitmap(renderData.backgroundImageProvider())
        }

        private fun drawHands(canvas: Canvas, zonedDateTime: ZonedDateTime, drawMode: DrawMode) {
            val generalData = HandsData(
                canvas = canvas,
                centerPoint = Point(canvas.width / 2f, canvas.height / 2f),
            )

            drawHand(generalData, renderData.hourHandData, zonedDateTime.hour % 12 * 60 + zonedDateTime.minute, true)
            drawHand(generalData, renderData.minuteHandData, zonedDateTime.minute)

            if (drawMode != DrawMode.AMBIENT) {
                drawHand(generalData, renderData.secondHandData, zonedDateTime.second)
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

        private fun drawHand(generalData: HandsData, handData : RenderData.HandData, timeValue: Int, itsHourHand: Boolean = false){
            val anglePieces = if(itsHourHand) 180 else 15
            val handAngle: Float = (timeValue * (90 / anglePieces.toFloat()))
            val width = handData.width
            val height = handData.height
            val heightPadding = handData.heightPadding

            generalData.canvas.drawBitmap(
                handData.imageProvider(),
                Matrix().apply {
                    setRotate(
                        handAngle,
                        (width / 2).toFloat(),
                        (height - heightPadding).toFloat()
                    )
                    postTranslate((generalData.canvas.width/2 - width / 2).toFloat(), (generalData.canvas.height / 2  - height + heightPadding).toFloat())
                },
                null
            )
        }

        private fun drawTexts(canvas: Canvas){
            drawText(canvas, renderData.topTextData)
        }

        private fun drawText(canvas: Canvas, textData: RenderData.TextData) =
            Paint().apply {
                color = textData.color
                typeface = renderData.topTextData.font
                this.textSize = textData.textSize
            }.also { paint ->
                canvas.drawText(
                    textData.text,
                    (canvas.width - paint.measureText(textData.text)) / 2f,
                    canvas.height / 2f + textData.yCenterOffset,
                    paint
                )
            }

    }
}