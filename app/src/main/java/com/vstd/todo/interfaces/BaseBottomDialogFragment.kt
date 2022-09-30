package com.vstd.todo.interfaces

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.vstd.todo.R

abstract class BaseBottomDialogFragment : BottomSheetDialogFragment() {
    override fun getTheme(): Int {
        return R.style.BotSheetDialogTheme
    }

    @SuppressLint("RestrictedApi", "VisibleForTests")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (dialog as BottomSheetDialog).behavior.disableShapeAnimations()
    }
}