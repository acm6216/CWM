package me.qingshu.cwm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.setPadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.card.MaterialCardView
import me.qingshu.cwm.binding.CardColorBinding
import me.qingshu.cwm.binding.CardSizeBinding
import me.qingshu.cwm.binding.DeviceBinding
import me.qingshu.cwm.binding.LensBinding
import me.qingshu.cwm.binding.InformationBinding
import me.qingshu.cwm.data.UserExif
import me.qingshu.cwm.databinding.ParamBinding

class Param:Fragment() {

    private val binding by lazy { ParamBinding.inflate(layoutInflater) }
    private val device by lazy { DeviceBinding(binding) }
    private val lens by lazy { LensBinding(binding) }
    private val info by lazy { InformationBinding(binding) }
    private val cardColor by lazy { CardColorBinding(binding) }
    private val cardSize by lazy { CardSizeBinding(binding) }
    private val picture:PictureViewModel by activityViewModels()

    private val picturePicker = registerForActivityResult(ActivityResultContracts.OpenMultipleDocuments()) { uri ->
        picture.receivePicture(uri,requireContext().applicationContext)
    }

    private lateinit var behavior: BottomSheetBehavior<MaterialCardView>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        behavior = BottomSheetBehavior.from(binding.lessonsSheet)
        behavior.isGestureInsetBottomIgnored = true
        behavior.expandedOffset = 0
        behavior.state = BottomSheetBehavior.STATE_HIDDEN
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

        binding.toolbar.apply {
            setNavigationOnClickListener(::toggle)
            setOnClickListener(::toggle)
        }
        binding.logo.apply {
            setOnClickListener {
                LogoPicker { logo ->
                    picture.receiveLogo(logo)
                    setImageResource(logo.src)
                    setPadding(logo.compatPadding().dp)
                }.show(childFragmentManager, javaClass.simpleName)
            }
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
    }

    private fun apply(view: View) {
        picture.receiveUserExif(
            UserExif(
                device = device.getDevice(),
                lens = lens.getLens(),
                information = info.getInformation()
            )
        )
        view.invalidate()
    }

    private fun toggle(view: View) = behavior.toggle().apply { view.invalidate() }

    private fun <T:View> BottomSheetBehavior<T>.toggle(){
        state = if(state==BottomSheetBehavior.STATE_EXPANDED) BottomSheetBehavior.STATE_COLLAPSED
        else BottomSheetBehavior.STATE_EXPANDED
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