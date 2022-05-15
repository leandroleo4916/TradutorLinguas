
package com.example.tradutorlinguas.activity

import com.example.tradutorlinguas.R
import com.example.tradutorlinguas.databinding.ActivityMainBinding

class ModifyIcon {

    fun iconsShow(root: ActivityMainBinding) {
        root.run {
            icPlayFrom.setImageResource(R.drawable.ic_sound)
            icSendFrom.setImageResource(R.drawable.ic_send)
        }
    }

    fun iconsHide(root: ActivityMainBinding){
        root.run {
            icSave.setImageResource(R.drawable.ic_save_gray)
            icPlayFrom.setImageResource(R.drawable.ic_sound_gray)
            icPlayTo.setImageResource(R.drawable.ic_sound_gray)
            icCopyFrom.setImageResource(R.drawable.ic_copy_gray)
            icSendFrom.setImageResource(R.drawable.ic_send_gray)
            icSwapUp.setImageResource(R.drawable.ic_swap_up_gray)
            icShare.setImageResource(R.drawable.ic_share_gray)
        }
    }

    fun iconSaveShow(root: ActivityMainBinding){
        root.run {
            icSave.setImageResource(R.drawable.ic_save)
            icPlayTo.setImageResource(R.drawable.ic_sound)
            icCopyFrom.setImageResource(R.drawable.ic_copy)
            icSwapUp.setImageResource(R.drawable.ic_swap_up)
            icShare.setImageResource(R.drawable.ic_share)
        }
    }
}