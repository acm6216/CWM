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
import me.qingshu.cwm.binding.GravityBinding
import me.qingshu.cwm.binding.InformationBinding
import me.qingshu.cwm.binding.LensBinding
import me.qingshu.cwm.binding.FilletBinding
import me.qingshu.cwm.binding.StyleBinding
import me.qingshu.cwm.binding.TemplateBinding
import me.qingshu.cwm.data.Exif
import me.qingshu.cwm.data.Icon
import me.qingshu.cwm.databinding.ParamBinding
import me.qingshu.cwm.extensions.treeObserver

class Param:BaseFragment() {

    private val binding by lazy { ParamBinding.inflate(layoutInflater) }
    private val device by lazy { DeviceBinding(binding) }
    private val gravity by lazy { GravityBinding(binding) }
    private val fillet by lazy { FilletBinding(binding) }
    private val lens by lazy { LensBinding(binding) }
    private val info by lazy { InformationBinding(binding) }
    private val cardColor by lazy { CardColorBinding(binding) }
    private val cardSize by lazy { CardSizeBinding(binding) }
    private val viewModel:PictureViewModel by activityViewModels()

    private val picturePicker =
        registerForActivityResult(ActivityResultContracts.OpenMultipleDocuments()) { uri ->
            viewModel.receivePicture(uri, requireContext().applicationContext)
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.model = viewModel
        binding.bindLifecycle()
        val behavior = BottomSheetBehavior.from(binding.lessonsSheet).apply {
            isGestureInsetBottomIgnored = true
            expandedOffset = 0
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        fun toggle(view: View) = behavior.toggle(viewModel.saveEnable.value).apply { view.invalidate() }

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
        binding.apply.setOnClick(::apply)
        binding.picker.setOnClickListener { picturePicker.launch(arrayOf("image/*")) }

        device.bind()
        info.bind()
        lens.bind()
        fillet.bind{
            viewModel.receiveFillet(it)
        }
        gravity.bind{ gravity,fromUser ->
            viewModel.receiveGravity(gravity,fromUser)
        }
        TemplateBinding(binding).bind(
            click = {
                device.setDevice(it.device)
                info.setInformation(it.information)
                lens.setLens(it.lens)
                cardSize.setCardSize(it.cardSize)
                cardColor.setCardColor(it.cardColor)
                //useLogo(it.icon)
            },
            saveCallback = { self ->
                self.save(
                    device.getDevice(),
                    info.getInformation(),
                    lens.getLens(),
                    viewModel.useCardSize,
                    viewModel.useCardColor,
                    viewModel.useLogo
                )
            }
        )
        StyleBinding(binding).bind{ style,fromUser ->
            viewModel.receiveStyle(style,fromUser)
        }
        cardColor.bind {
            viewModel.receiveCardColor(it)
        }
        cardSize.bind {
            viewModel.receiveCardSize(it)
        }

        repeatWithViewLifecycle {
            launch {
                viewModel.saveEnable.collect{
                    behavior.isDraggable = it
                }
            }
            launch {
                viewModel.styles.collect{
                    info.visible(it)
                    gravity.visible(it)
                    fillet.visible(it)
                }

            }
        }
    }

    private fun logoPicker(view: View){
        LogoPicker(viewModel.styles.value.icon) { logo ->
            useLogo(logo,view)
        }.show(childFragmentManager, javaClass.simpleName)
    }

    private fun useLogo(icon: Icon) = useLogo(icon,binding.logo)

    private fun useLogo(icon: Icon, view: View){
        viewModel.receiveLogo(icon)
        (view as ImageView).setImageResource(icon.src)
        view.setPadding(icon.compatPadding().dp)
    }

    private fun apply() {
        viewModel.receiveUserExif(
            Exif(
                device = device.getDevice(),
                lens = lens.getLens(),
                information = info.getInformation()
            )
        )
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