package com.ashraful.learningquest.screens

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import android.view.MotionEvent
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.sin
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

/**
 * Lightweight real-time 3D pet renderer using OpenGL ES 2.0.
 * No external 3D engine or internet assets are required.
 *
 * Pets are built from lit 3D meshes (ellipsoids/cones), with touch rotation,
 * auto-rotation and tap reactions.
 */
class AnishPet3DView(context: Context) : GLSurfaceView(context) {

    private val renderer = PetRenderer()
    private var downX = 0f
    private var downY = 0f
    private var moved = false
    private var lastTouchAt = System.currentTimeMillis()

    init {
        setEGLContextClientVersion(2)
        setRenderer(renderer)
        renderMode = RENDERMODE_CONTINUOUSLY
        setZOrderOnTop(false)
    }

    fun setPet(pet: String) {
        renderer.pet = pet
    }

    fun setOnPetTap(listener: () -> Unit) {
        renderer.onTap = listener
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                downX = event.x
                downY = event.y
                moved = false
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                val dx = event.x - downX
                val dy = event.y - downY
                if (kotlin.math.abs(dx) > 3f || kotlin.math.abs(dy) > 3f) moved = true
                renderer.userYaw += dx * 0.55f
                lastTouchAt = System.currentTimeMillis()
                renderer.userPitch = (renderer.userPitch - dy * 0.25f).coerceIn(-24f, 28f)
                downX = event.x
                downY = event.y
                return true
            }
            MotionEvent.ACTION_UP -> {
                if (!moved) {
                    renderer.react()
                }
                performClick()
                return true
            }
        }
        return true
    }

    override fun performClick(): Boolean {
        super.performClick()
        return true
    }

    private inner class PetRenderer : Renderer {
        private lateinit var shader: SimpleShader
        private lateinit var sphere: Mesh
        private lateinit var cone: Mesh
        private lateinit var cube: Mesh
        private lateinit var cylinder: Mesh

        @Volatile var pet: String = "🐼"
        var userYaw = 0f
        var userPitch = 4f
        var onTap: (() -> Unit)? = null

        private var reactionUntil = 0L
        private var blinkUntil = 0L
        private var lastBlinkAt = System.currentTimeMillis()
        private var lastTime = System.nanoTime()
        private var lastInteractionAt = System.currentTimeMillis()

        override fun onSurfaceCreated(gl: javax.microedition.khronos.opengles.GL10?, config: javax.microedition.khronos.egl.EGLConfig?) {
            GLES20.glClearColor(0.92f, 0.97f, 0.94f, 1f)
            GLES20.glEnable(GLES20.GL_DEPTH_TEST)
            GLES20.glEnable(GLES20.GL_CULL_FACE)
            GLES20.glCullFace(GLES20.GL_BACK)
            shader = SimpleShader()
            sphere = makeSphere(1f, 24, 16)
            cone = makeCone(1f, 1.7f, 18)
            cube = makeCube()
            cylinder = makeCylinder(1f, 1f, 20)
            lastTime = System.nanoTime()
        }

        override fun onSurfaceChanged(gl: javax.microedition.khronos.opengles.GL10?, width: Int, height: Int) {
            GLES20.glViewport(0, 0, width, height)
        }

        override fun onDrawFrame(gl: javax.microedition.khronos.opengles.GL10?) {
            val now = System.nanoTime()
            val dt = ((now - lastTime) / 1_000_000_000f).coerceIn(0f, 0.05f)
            lastTime = now

            if (System.currentTimeMillis() - lastInteractionAt > 1400L) userYaw += dt * 9f

            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

            val w = max(width, 1)
            val h = max(height, 1)
            val aspect = w.toFloat() / h.toFloat()
            val projection = FloatArray(16)
            val view = FloatArray(16)
            val model = FloatArray(16)
            val vp = FloatArray(16)

            Matrix.setIdentityM(projection, 0)
            Matrix.perspectiveM(projection, 0, 38f, aspect, 1.2f, 30f)
            Matrix.setLookAtM(view, 0, 0f, 1.0f, 7.2f, 0f, 0.8f, 0f, 0f, 1f, 0f)
            Matrix.multiplyMM(vp, 0, projection, 0, view, 0)

            val jump = if (System.currentTimeMillis() < reactionUntil) {
                val t = ((reactionUntil - System.currentTimeMillis()) / 420f).coerceIn(0f, 1f)
                sin((1f - t) * Math.PI).toFloat() * 0.42f
            } else 0f

            Matrix.setIdentityM(model, 0)
            Matrix.translateM(model, 0, 0f, jump, 0f)
            Matrix.rotateM(model, 0, userPitch, 1f, 0f, 0f)
            Matrix.rotateM(model, 0, userYaw, 0f, 1f, 0f)
            drawEnvironment(vp, now)
            drawPet(model, vp, now)
        }

        fun react() {
            lastInteractionAt = System.currentTimeMillis()
            reactionUntil = System.currentTimeMillis() + 420
            onTap?.invoke()
        }

        private fun drawEnvironment(vp: FloatArray, now: Long) {
            val identity = FloatArray(16)
            Matrix.setIdentityM(identity, 0)

            // Soft 3D play mat under the buddy.
            cubePart(vp, 0f, -1.22f, -0.05f, 3.25f, 0.10f, 2.35f, floatArrayOf(0.72f, 0.86f, 0.68f), identity)

            // Small stones around the play area.
            val stone = floatArrayOf(0.58f, 0.66f, 0.62f)
            cubePart(vp, -2.15f, -1.02f, 0.30f, 0.30f, 0.20f, 0.42f, stone, identity, rz = -10f)
            cubePart(vp, 2.05f, -1.04f, -0.10f, 0.36f, 0.18f, 0.34f, stone, identity, rz = 14f)
            cubePart(vp, -1.85f, -1.07f, -0.70f, 0.24f, 0.15f, 0.28f, stone, identity)

            // Bamboo on both sides gives the Panda a small living habitat.
            val bamboo = floatArrayOf(0.18f, 0.55f, 0.28f)
            val bambooLight = floatArrayOf(0.30f, 0.70f, 0.35f)
            cylinderPart(vp, -2.35f, 0.20f, -0.40f, 0.16f, 2.65f, bamboo, identity)
            cylinderPart(vp, -2.05f, 0.55f, -0.20f, 0.13f, 2.10f, bambooLight, identity, rz = -7f)
            cylinderPart(vp, 2.35f, 0.18f, -0.35f, 0.16f, 2.70f, bamboo, identity)
            cylinderPart(vp, 2.05f, 0.50f, -0.10f, 0.13f, 2.15f, bambooLight, identity, rz = 7f)

            // Rounded leaves, gently moving with the idle animation.
            val leaf = floatArrayOf(0.20f, 0.62f, 0.28f)
            val leaf2 = floatArrayOf(0.28f, 0.72f, 0.32f)
            val sway = sin(now / 900_000_000.0).toFloat() * 4f
            cubePart(vp, -2.55f, 1.48f, -0.25f, 0.58f, 0.16f, 0.24f, leaf, identity, rz = -25f + sway)
            cubePart(vp, -1.90f, 1.25f, -0.18f, 0.52f, 0.14f, 0.22f, leaf2, identity, rz = 22f + sway)
            cubePart(vp, 2.55f, 1.52f, -0.25f, 0.58f, 0.16f, 0.24f, leaf, identity, rz = 25f - sway)
            cubePart(vp, 1.92f, 1.28f, -0.18f, 0.52f, 0.14f, 0.22f, leaf2, identity, rz = -22f - sway)
        }

        private fun cubePart(
            vp: FloatArray,
            x: Float, y: Float, z: Float,
            sx: Float, sy: Float, sz: Float,
            color: FloatArray,
            base: FloatArray,
            rx: Float = 0f, ry: Float = 0f, rz: Float = 0f
        ) {
            val m = FloatArray(16)
            Matrix.setIdentityM(m, 0)
            Matrix.multiplyMM(m, 0, base, 0, m, 0)
            Matrix.translateM(m, 0, x, y, z)
            Matrix.rotateM(m, 0, rx, 1f, 0f, 0f)
            Matrix.rotateM(m, 0, ry, 0f, 1f, 0f)
            Matrix.rotateM(m, 0, rz, 0f, 0f, 1f)
            Matrix.scaleM(m, 0, sx, sy, sz)
            val mvp = FloatArray(16)
            Matrix.multiplyMM(mvp, 0, vp, 0, m, 0)
            shader.draw(cube, mvp, m, color)
        }

        private fun cylinderPart(
            vp: FloatArray,
            x: Float, y: Float, z: Float,
            radius: Float, height: Float,
            color: FloatArray,
            base: FloatArray,
            rz: Float = 0f
        ) {
            val m = FloatArray(16)
            Matrix.setIdentityM(m, 0)
            Matrix.multiplyMM(m, 0, base, 0, m, 0)
            Matrix.translateM(m, 0, x, y, z)
            Matrix.rotateM(m, 0, rz, 0f, 0f, 1f)
            Matrix.scaleM(m, 0, radius, height, radius)
            val mvp = FloatArray(16)
            Matrix.multiplyMM(mvp, 0, vp, 0, m, 0)
            shader.draw(cylinder, mvp, m, color)
        }

        private fun drawPet(base: FloatArray, vp: FloatArray, now: Long) {
            val isPanda = pet == "🐼"
            val isCat = pet == "🐱"
            val isDog = pet == "🐶"
            val isFox = pet == "🦊"

            val white = floatArrayOf(0.94f, 0.94f, 0.92f)
            val black = floatArrayOf(0.035f, 0.045f, 0.05f)
            val orange = floatArrayOf(0.95f, 0.43f, 0.08f)
            val tan = floatArrayOf(0.74f, 0.49f, 0.29f)
            val foxOrange = floatArrayOf(0.92f, 0.28f, 0.06f)
            val cream = floatArrayOf(1.0f, 0.82f, 0.57f)
            val pink = floatArrayOf(0.95f, 0.38f, 0.48f)

            if (isPanda) {
                drawPanda(base, vp, now, white, black, pink)
                return
            }

            // Body
            part(base, vp, 0f, 0.05f, 0f, 1.25f, 1.55f, 0.95f, if (isPanda) white else if (isFox) foxOrange else if (isCat) orange else tan)
            // Chest patch
            if (isDog || isFox) part(base, vp, 0f, 0.05f, 0.78f, 0.58f, 0.85f, 0.18f, white)
            // Head
            part(base, vp, 0f, 1.55f, 0f, 1.08f, 0.92f, 0.98f, if (isPanda) white else if (isFox) foxOrange else if (isCat) orange else tan)

            // Ears
            if (isDog) {
                part(base, vp, -0.78f, 1.62f, 0.02f, 0.38f, 0.72f, 0.34f, tan, rx = 18f, rz = -20f)
                part(base, vp, 0.78f, 1.62f, 0.02f, 0.38f, 0.72f, 0.34f, tan, rx = 18f, rz = 20f)
            } else {
                val earColor = if (isPanda) black else if (isFox) foxOrange else orange
                partCone(base, vp, -0.62f, 2.12f, 0f, 0.38f, 0.70f, earColor, rz = -10f)
                partCone(base, vp, 0.62f, 2.12f, 0f, 0.38f, 0.70f, earColor, rz = 10f)
                if (isCat) {
                    partCone(base, vp, -0.62f, 2.12f, 0.18f, 0.20f, 0.42f, pink, rz = -10f)
                    partCone(base, vp, 0.62f, 2.12f, 0.18f, 0.20f, 0.42f, pink, rz = 10f)
                }
            }

            // Eye patches
            if (isPanda) {
                part(base, vp, -0.39f, 1.58f, 0.88f, 0.27f, 0.34f, 0.10f, black, ry = -18f, rz = 12f)
                part(base, vp, 0.39f, 1.58f, 0.88f, 0.27f, 0.34f, 0.10f, black, ry = 18f, rz = -12f)
            }

            // Eyes
            val eyeZ = 0.92f
            part(base, vp, -0.36f, 1.63f, eyeZ, 0.105f, 0.13f, 0.09f, black)
            part(base, vp, 0.36f, 1.63f, eyeZ, 0.105f, 0.13f, 0.09f, black)
            // Nose + muzzle
            part(base, vp, 0f, 1.34f, 0.96f, 0.22f, 0.18f, 0.12f, if (isFox) black else pink)
            if (!isFox) part(base, vp, 0f, 1.23f, 0.82f, 0.45f, 0.32f, 0.25f, white)

            // Tail
            val tailColor = if (isPanda) white else if (isFox) foxOrange else if (isCat) orange else tan
            part(base, vp, 1.05f, 0.48f, -0.35f, 0.32f, 1.25f, 0.32f, tailColor, rz = -35f)
            if (isFox) part(base, vp, 1.55f, 0.78f, -0.45f, 0.42f, 0.55f, 0.42f, white, rz = -35f)

            // Feet
            val footColor = if (isPanda) black else if (isFox) cream else if (isCat) orange else tan
            part(base, vp, -0.62f, -0.78f, 0.36f, 0.42f, 0.28f, 0.55f, footColor)
            part(base, vp, 0.62f, -0.78f, 0.36f, 0.42f, 0.28f, 0.55f, footColor)
        }

        private fun drawPanda(
            base: FloatArray,
            vp: FloatArray,
            now: Long,
            white: FloatArray,
            black: FloatArray,
            pink: FloatArray
        ) {
            // Gentle breathing keeps the buddy alive even when untouched.
            val breath = 1f + sin(now / 650_000_000.0).toFloat() * 0.018f
            val idleSway = sin(now / 1_350_000_000.0).toFloat() * 2.2f
            val reaction = System.currentTimeMillis() < reactionUntil
            val wave = if (reaction) sin((now / 75_000_000.0)).toFloat() * 24f else 0f

            // Soft ground shadow.
            part(base, vp, 0f, -1.08f, 0.08f, 1.25f, 0.10f, 0.62f, floatArrayOf(0.55f, 0.62f, 0.60f))

            // Rounded body and soft belly.
            part(base, vp, 0f, 0.02f, 0f, 1.10f, 1.40f * breath, 0.84f, white, rz = idleSway * 0.20f)
            part(base, vp, 0f, -0.10f, 0.79f, 0.60f, 0.82f, 0.16f, floatArrayOf(0.86f, 0.86f, 0.83f))

            // Black arms with rounded paws; the right paw waves on tap.
            part(base, vp, -0.93f, 0.25f, 0.02f, 0.31f, 0.72f, 0.35f, black, rz = -15f)
            part(base, vp, 0.93f, 0.25f, 0.02f, 0.31f, 0.72f, 0.35f, black, rz = 15f + wave)
            part(base, vp, -1.02f, -0.08f, 0.28f, 0.30f, 0.30f, 0.34f, black, rz = -12f)
            part(base, vp, 1.02f, -0.08f, 0.28f, 0.30f, 0.30f, 0.34f, black, rz = 12f + wave)
            val pawPad = floatArrayOf(0.20f, 0.22f, 0.21f)
            part(base, vp, -1.02f, -0.10f, 0.59f, 0.15f, 0.16f, 0.05f, pawPad)
            part(base, vp, 1.02f, -0.10f, 0.59f, 0.15f, 0.16f, 0.05f, pawPad)

            // Black legs and little paws.
            part(base, vp, -0.58f, -0.82f, 0.22f, 0.48f, 0.42f, 0.56f, black)
            part(base, vp, 0.58f, -0.82f, 0.22f, 0.48f, 0.42f, 0.56f, black)
            part(base, vp, -0.58f, -1.02f, 0.55f, 0.44f, 0.22f, 0.30f, black)
            part(base, vp, 0.58f, -1.02f, 0.55f, 0.44f, 0.22f, 0.30f, black)

            // Small round black tail.
            part(base, vp, 0.92f, 0.52f, -0.68f, 0.30f, 0.30f, 0.30f, black)

            // Big rounded head with a slightly shorter, friendlier profile.
            part(base, vp, 0f, 1.56f, 0f, 1.08f, 0.96f, 1.00f, white, rz = idleSway * 0.10f)
            // Ears are rounded, not cones.
            part(base, vp, -0.76f, 2.18f, -0.02f, 0.40f, 0.40f, 0.34f, black)
            part(base, vp, 0.76f, 2.18f, -0.02f, 0.40f, 0.40f, 0.34f, black)

            // Classic panda eye patches.
            part(base, vp, -0.43f, 1.62f, 0.90f, 0.32f, 0.43f, 0.13f, black, ry = -22f, rz = 10f)
            part(base, vp, 0.43f, 1.62f, 0.90f, 0.32f, 0.43f, 0.13f, black, ry = 22f, rz = -10f)

            // Bright eyes + tiny catchlights.
            if (now / 1_000_000L - lastBlinkAt > 3200L) {
                lastBlinkAt = now / 1_000_000L
                blinkUntil = lastBlinkAt + 180L
            }
            val blink = now / 1_000_000L < blinkUntil
            val eyeHeight = if (blink) 0.025f else 0.14f
            part(base, vp, -0.43f, 1.62f, 1.015f, 0.125f, eyeHeight, 0.075f, black)
            part(base, vp, 0.43f, 1.62f, 1.015f, 0.125f, eyeHeight, 0.075f, black)
            if (!blink) {
                part(base, vp, -0.40f, 1.67f, 1.085f, 0.040f, 0.052f, 0.020f, white)
                part(base, vp, 0.46f, 1.67f, 1.085f, 0.040f, 0.052f, 0.020f, white)
            }

            // Soft cheek highlights, white muzzle, black nose and smiling mouth.
            part(base, vp, -0.62f, 1.31f, 0.84f, 0.23f, 0.18f, 0.07f, floatArrayOf(0.97f, 0.97f, 0.95f))
            part(base, vp, 0.62f, 1.31f, 0.84f, 0.23f, 0.18f, 0.07f, floatArrayOf(0.97f, 0.97f, 0.95f))
            part(base, vp, -0.23f, 1.28f, 0.91f, 0.40f, 0.30f, 0.25f, white)
            part(base, vp, 0.23f, 1.28f, 0.91f, 0.40f, 0.30f, 0.25f, white)
            part(base, vp, 0f, 1.35f, 1.105f, 0.20f, 0.14f, 0.12f, black)
            part(base, vp, 0f, 1.19f, 1.075f, 0.08f, 0.16f, 0.06f, black)
            part(base, vp, -0.11f, 1.12f, 1.07f, 0.07f, 0.13f, 0.055f, black, rz = 22f)
            part(base, vp, 0.11f, 1.12f, 1.07f, 0.07f, 0.13f, 0.055f, black, rz = -22f)

            // Soft paw pads make the seated panda feel more tactile and toy-like.
            val pawPad = floatArrayOf(0.28f, 0.17f, 0.15f)
            part(base, vp, -0.58f, -1.03f, 0.58f, 0.23f, 0.16f, 0.055f, pawPad)
            part(base, vp, 0.58f, -1.03f, 0.58f, 0.23f, 0.16f, 0.055f, pawPad)
            part(base, vp, -0.73f, -1.00f, 0.53f, 0.075f, 0.075f, 0.035f, pawPad)
            part(base, vp, -0.48f, -1.00f, 0.56f, 0.075f, 0.075f, 0.035f, pawPad)
            part(base, vp, 0.73f, -1.00f, 0.53f, 0.075f, 0.075f, 0.035f, pawPad)
            part(base, vp, 0.48f, -1.00f, 0.56f, 0.075f, 0.075f, 0.035f, pawPad)

            // A subtle warm highlight on the cheeks.
            val cheek = floatArrayOf(0.98f, 0.93f, 0.91f)
            part(base, vp, -0.70f, 1.38f, 0.87f, 0.18f, 0.13f, 0.055f, cheek)
            part(base, vp, 0.70f, 1.38f, 0.87f, 0.18f, 0.13f, 0.055f, cheek)

            // Tiny pink tongue on tap.
            if (reaction) {
                part(base, vp, 0f, 1.05f, 1.095f, 0.09f, 0.12f, 0.05f, pink)
            }
        }

        private fun part(
            base: FloatArray,
            vp: FloatArray,
            x: Float, y: Float, z: Float,
            sx: Float, sy: Float, sz: Float,
            color: FloatArray,
            rx: Float = 0f, ry: Float = 0f, rz: Float = 0f
        ) {
            val m = FloatArray(16)
            Matrix.setIdentityM(m, 0)
            Matrix.multiplyMM(m, 0, base, 0, m, 0)
            Matrix.translateM(m, 0, x, y, z)
            Matrix.rotateM(m, 0, rx, 1f, 0f, 0f)
            Matrix.rotateM(m, 0, ry, 0f, 1f, 0f)
            Matrix.rotateM(m, 0, rz, 0f, 0f, 1f)
            Matrix.scaleM(m, 0, sx, sy, sz)
            val mvp = FloatArray(16)
            Matrix.multiplyMM(mvp, 0, vp, 0, m, 0)
            shader.draw(sphere, mvp, m, color)
        }

        private fun partCone(
            base: FloatArray,
            vp: FloatArray,
            x: Float, y: Float, z: Float,
            radius: Float, height: Float,
            color: FloatArray,
            rz: Float = 0f
        ) {
            val m = FloatArray(16)
            Matrix.setIdentityM(m, 0)
            Matrix.multiplyMM(m, 0, base, 0, m, 0)
            Matrix.translateM(m, 0, x, y, z)
            Matrix.rotateM(m, 0, rz, 0f, 0f, 1f)
            Matrix.scaleM(m, 0, radius, height, radius)
            val mvp = FloatArray(16)
            Matrix.multiplyMM(mvp, 0, vp, 0, m, 0)
            shader.draw(cone, mvp, m, color)
        }
    }

    private class Mesh(val vertices: FloatBuffer, val count: Int)

    private class SimpleShader {
        private val program: Int
        private val pos: Int
        private val normal: Int
        private val mvp: Int
        private val model: Int
        private val color: Int

        init {
            val vs = """
                attribute vec3 aPosition;
                attribute vec3 aNormal;
                uniform mat4 uMvp;
                uniform mat4 uModel;
                varying vec3 vNormal;
                varying vec3 vPos;
                void main() {
                    vec4 world = uModel * vec4(aPosition, 1.0);
                    vPos = world.xyz;
                    vNormal = mat3(uModel) * aNormal;
                    gl_Position = uMvp * vec4(aPosition, 1.0);
                }
            """.trimIndent()
            val fs = """
                precision mediump float;
                uniform vec3 uColor;
                varying vec3 vNormal;
                varying vec3 vPos;
                void main() {
                    vec3 n = normalize(vNormal);
                    vec3 lightDir = normalize(vec3(-0.45, 0.85, 0.65));
                    vec3 viewDir = normalize(vec3(0.0, 1.0, 7.2) - vPos);
                    vec3 halfDir = normalize(lightDir + viewDir);
                    float diffuse = max(dot(n, lightDir), 0.0);
                    float specular = pow(max(dot(n, halfDir), 0.0), 28.0);
                    float rim = pow(1.0 - max(dot(n, viewDir), 0.0), 2.2);
                    vec3 lit = uColor * (0.30 + 0.62 * diffuse) + vec3(0.10) * rim + vec3(0.34) * specular;
                    gl_FragColor = vec4(lit, 1.0);
                }
            """.trimIndent()
            program = link(load(GLES20.GL_VERTEX_SHADER, vs), load(GLES20.GL_FRAGMENT_SHADER, fs))
            pos = GLES20.glGetAttribLocation(program, "aPosition")
            normal = GLES20.glGetAttribLocation(program, "aNormal")
            mvp = GLES20.glGetUniformLocation(program, "uMvp")
            model = GLES20.glGetUniformLocation(program, "uModel")
            color = GLES20.glGetUniformLocation(program, "uColor")
        }

        fun draw(mesh: Mesh, mvpMatrix: FloatArray, modelMatrix: FloatArray, c: FloatArray) {
            GLES20.glUseProgram(program)
            mesh.vertices.position(0)
            GLES20.glEnableVertexAttribArray(pos)
            GLES20.glVertexAttribPointer(pos, 3, GLES20.GL_FLOAT, false, 24, mesh.vertices)
            mesh.vertices.position(3)
            GLES20.glEnableVertexAttribArray(normal)
            GLES20.glVertexAttribPointer(normal, 3, GLES20.GL_FLOAT, false, 24, mesh.vertices)
            GLES20.glUniformMatrix4fv(mvp, 1, false, mvpMatrix, 0)
            GLES20.glUniformMatrix4fv(model, 1, false, modelMatrix, 0)
            GLES20.glUniform3fv(color, 1, c, 0)
            GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, mesh.count)
            GLES20.glDisableVertexAttribArray(pos)
            GLES20.glDisableVertexAttribArray(normal)
        }

        private fun load(type: Int, source: String): Int {
            val s = GLES20.glCreateShader(type)
            GLES20.glShaderSource(s, source)
            GLES20.glCompileShader(s)
            return s
        }

        private fun link(v: Int, f: Int): Int {
            val p = GLES20.glCreateProgram()
            GLES20.glAttachShader(p, v)
            GLES20.glAttachShader(p, f)
            GLES20.glLinkProgram(p)
            return p
        }
    }

    private fun makeSphere(radius: Float, slices: Int, stacks: Int): Mesh {
        val data = ArrayList<Float>()
        fun add(a: Float, b: Float, c: Float) {
            data.add(a); data.add(b); data.add(c)
        }
        for (y in 0 until stacks) {
            val v0 = y.toFloat() / stacks
            val v1 = (y + 1).toFloat() / stacks
            val p0 = (v0 - 0.5f) * Math.PI
            val p1 = (v1 - 0.5f) * Math.PI
            for (x in 0 until slices) {
                val u0 = x.toFloat() / slices
                val u1 = (x + 1).toFloat() / slices
                val t0 = u0 * Math.PI * 2
                val t1 = u1 * Math.PI * 2
                val pts = arrayOf(
                    floatArrayOf(cos(p0).toFloat() * cos(t0).toFloat(), sin(p0).toFloat(), cos(p0).toFloat() * sin(t0).toFloat()),
                    floatArrayOf(cos(p1).toFloat() * cos(t0).toFloat(), sin(p1).toFloat(), cos(p1).toFloat() * sin(t0).toFloat()),
                    floatArrayOf(cos(p1).toFloat() * cos(t1).toFloat(), sin(p1).toFloat(), cos(p1).toFloat() * sin(t1).toFloat()),
                    floatArrayOf(cos(p0).toFloat() * cos(t1).toFloat(), sin(p0).toFloat(), cos(p0).toFloat() * sin(t1).toFloat())
                )
                fun tri(a: FloatArray, b: FloatArray, c: FloatArray) {
                    add(a[0] * radius, a[1] * radius, a[2] * radius); add(a[0], a[1], a[2])
                    add(b[0] * radius, b[1] * radius, b[2] * radius); add(b[0], b[1], b[2])
                    add(c[0] * radius, c[1] * radius, c[2] * radius); add(c[0], c[1], c[2])
                }
                tri(pts[0], pts[1], pts[2])
                tri(pts[0], pts[2], pts[3])
            }
        }
        return Mesh(toBuffer(data), data.size / 6)
    }

    private fun makeCone(radius: Float, height: Float, slices: Int): Mesh {
        val data = ArrayList<Float>()
        fun add(a: Float, b: Float, c: Float, nx: Float, ny: Float, nz: Float) {
            data.add(a); data.add(b); data.add(c); data.add(nx); data.add(ny); data.add(nz)
        }
        val half = height / 2f
        for (i in 0 until slices) {
            val a0 = i * Math.PI * 2 / slices
            val a1 = (i + 1) * Math.PI * 2 / slices
            val x0 = cos(a0).toFloat() * radius
            val z0 = sin(a0).toFloat() * radius
            val x1 = cos(a1).toFloat() * radius
            val z1 = sin(a1).toFloat() * radius
            val ny = radius / height
            val n0 = floatArrayOf(cos(a0).toFloat(), ny, sin(a0).toFloat())
            val n1 = floatArrayOf(cos(a1).toFloat(), ny, sin(a1).toFloat())
            add(x0, -half, z0, n0[0], n0[1], n0[2])
            add(x1, -half, z1, n1[0], n1[1], n1[2])
            add(0f, half, 0f, 0f, 1f, 0f)
        }
        return Mesh(toBuffer(data), data.size / 6)
    }

    private fun makeCylinder(radius: Float, height: Float, slices: Int): Mesh {
        val data = ArrayList<Float>()
        fun add(x: Float, y: Float, z: Float, nx: Float, ny: Float, nz: Float) {
            data.add(x); data.add(y); data.add(z)
            data.add(nx); data.add(ny); data.add(nz)
        }
        val half = height / 2f
        for (i in 0 until slices) {
            val a0 = i * Math.PI * 2 / slices
            val a1 = (i + 1) * Math.PI * 2 / slices
            val x0 = cos(a0).toFloat() * radius
            val z0 = sin(a0).toFloat() * radius
            val x1 = cos(a1).toFloat() * radius
            val z1 = sin(a1).toFloat() * radius
            val n0x = cos(a0).toFloat()
            val n0z = sin(a0).toFloat()
            val n1x = cos(a1).toFloat()
            val n1z = sin(a1).toFloat()
            add(x0, -half, z0, n0x, 0f, n0z)
            add(x1, -half, z1, n1x, 0f, n1z)
            add(x1, half, z1, n1x, 0f, n1z)
            add(x0, -half, z0, n0x, 0f, n0z)
            add(x1, half, z1, n1x, 0f, n1z)
            add(x0, half, z0, n0x, 0f, n0z)
        }
        return Mesh(toBuffer(data), data.size / 6)
    }

    private fun makeCube(): Mesh {
        val p = floatArrayOf(
            -1f,-1f,1f, 1f,-1f,1f, 1f,1f,1f, -1f,1f,1f,
            -1f,-1f,-1f, -1f,1f,-1f, 1f,1f,-1f, 1f,-1f,-1f
        )
        val faces = intArrayOf(
            0,1,2, 0,2,3, 1,7,6, 1,6,2, 7,4,5, 7,5,6,
            4,0,3, 4,3,5, 3,2,6, 3,6,5, 4,7,1, 4,1,0
        )
        val normals = arrayOf(
            floatArrayOf(0f,0f,1f), floatArrayOf(1f,0f,0f), floatArrayOf(0f,0f,-1f),
            floatArrayOf(-1f,0f,0f), floatArrayOf(0f,1f,0f), floatArrayOf(0f,-1f,0f)
        )
        val data = ArrayList<Float>()
        for (f in 0 until 6) {
            val n = normals[f]
            for (j in 0 until 6) {
                val idx = faces[f * 6 + j] * 3
                data.add(p[idx]); data.add(p[idx + 1]); data.add(p[idx + 2])
                data.add(n[0]); data.add(n[1]); data.add(n[2])
            }
        }
        return Mesh(toBuffer(data), data.size / 6)
    }

    private fun toBuffer(data: List<Float>): FloatBuffer {
        val b = ByteBuffer.allocateDirect(data.size * 4).order(ByteOrder.nativeOrder())
        val f = b.asFloatBuffer()
        data.forEach { f.put(it) }
        f.position(0)
        return f
    }
}
