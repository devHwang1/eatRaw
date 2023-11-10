package com.example.eatraw.mypagefrg

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import com.example.eatraw.R



class AlarmFragment : Fragment() {
    private lateinit var switch: SwitchCompat

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_alarm, container, false)
        switch = view.findViewById(R.id.alarm_switch)

        val sharedPreferences = requireActivity().getSharedPreferences("NotificationSettings", Context.MODE_PRIVATE)
        switch.isChecked = sharedPreferences.getBoolean("NotificationEnabled", true)

        switch.setOnCheckedChangeListener { _, isChecked ->
            sharedPreferences.edit().putBoolean("NotificationEnabled", isChecked).apply()
        }

        return view
    }
}