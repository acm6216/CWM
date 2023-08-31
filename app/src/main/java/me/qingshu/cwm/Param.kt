package me.qingshu.cwm

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.addCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.setPadding
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.launch
import me.qingshu.cwm.binding.CardColorBinding
import me.qingshu.cwm.binding.CardSizeBinding
import me.qingshu.cwm.binding.DeviceBinding
import me.qingshu.cwm.binding.InformationBinding
import me.qingshu.cwm.binding.LensBinding
import me.qingshu.cwm.data.Exif
import me.qingshu.cwm.databinding.ParamBinding
import me.qingshu.cwm.extensions.treeObserver

class Param:BaseFragment() {

    private val binding by lazy { ParamBinding.inflate(layoutInflater) }
    private val device by lazy { DeviceBinding(binding) }
    private val lens by lazy { LensBinding(binding) }
    private val info by lazy { InformationBinding(binding) }
    private val cardColor by lazy { CardColorBinding(binding) }
    private val cardSize by lazy { CardSizeBinding(binding) }
    private val picture:PictureViewModel by activityViewModels()

    private val picturePicker =
        registerForActivityResult(ActivityResultContracts.OpenMultipleDocuments()) { uri ->
            picture.receivePicture(uri, requireContext().applicationContext)
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.model = picture
        binding.bindLifecycle()
        val behavior = BottomSheetBehavior.from(binding.lessonsSheet).apply {
            isGestureInsetBottomIgnored = true
            expandedOffset = 0
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        fun toggle(view: View) = behavior.toggle(picture.saveEnable.value).apply { view.invalidate() }

        val backCallback = requireActivity()
            .onBackPressedDispatcher.addCallback(viewLifecycleOwner, false) {
                behavior.close()
            }
        behavior.addBottomSheetCallback(
            onStateChanged = {_,newState->
                backCallback.isEnabled = newState == BottomSheetBehavior.STATE_EXPANDED
            },
            onSlide = {_,offset ->
                binding.toolbar.navigationIcon?.level = (10000f*offset).toInt()
            }
        )
        binding.lessonsSheet.treeObserver {
            val displayMetrics = resources.displayMetrics
            val height = displayMetrics.heightPixels
            it.layoutParams.height = (height/10f*6).toInt()
        }

        binding.toolbar.apply {
            setNavigationOnClickListener(::toggle)
            setOnClickListener(::toggle)
        }
        binding.logo.setOnClickListener(::logoPicker)

        binding.root.setOnTouchListener{ _,_ ->
            behavior.close()
            false
        }
        binding.apply.setOnClickListener(::apply)
        binding.picker.setOnClickListener { picturePicker.launch(arrayOf("image/*")) }

        device.bind()
        info.bind()
        lens.bind()
        cardColor.bind {
            picture.receiveCardColor(it)
        }
        cardSize.bind {
            picture.receiveCardSize(it)
        }

        repeatWithViewLifecycle {
            launch {
                picture.saveEnable.collect{
                    behavior.isDraggable = it
                }
            }
        }
    }

    private fun logoPicker(view: View){
        LogoPicker { logo ->
            picture.receiveLogo(logo)
            (view as ImageView).setImageResource(logo.src)
            view.setPadding(logo.compatPadding().dp)
        }.show(childFragmentManager, javaClass.simpleName)
    }

    private fun apply(view: View) {
        picture.receiveUserExif(
            Exif(
                device = device.getDevice(),
                lens = lens.getLens(),
                information = info.getInformation()
            )
        )
        view.invalidate()
    }

    private fun <T:View> BottomSheetBehavior<T>.toggle(isEnable:Boolean){
        if(isEnable) {
            state = if (state == BottomSheetBehavior.STATE_EXPANDED) BottomSheetBehavior.STATE_COLLAPSED
                else BottomSheetBehavior.STATE_EXPANDED
        }
    }

    private fun <T:View> BottomSheetBehavior<T>.close(){
        state = BottomSheetBehavior.STATE_COLLAPSED
    }

    private fun <T:View> BottomSheetBehavior<T>.addBottomSheetCallback(onStateChanged:((View, Int)->Unit), onSlide:((View, Float)->Unit)? = null){
        addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback(){
            override fun onStateChanged(bottomSheet: View, newState: Int) { onStateChanged.invoke(bottomSheet,newState)}
            override fun onSlide(bottomSheet: View, slideOffset: Float) { onSlide?.invoke(bottomSheet,slideOffset) }
        })
    }

    private inline val Int.dp: Int get() = run { toFloat().dp }
    private inline val Float.dp: Int get() = run {
        val scale: Float =resources.displayMetrics.density
        (this * scale + 0.5f).toInt()
    }
}