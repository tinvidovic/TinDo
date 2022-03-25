package com.loyaltiez.feature_home.presentation.fragments

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.loyaltiez.core.broadcast_receivers.AlarmReceiver
import com.loyaltiez.core.domain.model.todo.DailyToDo
import com.loyaltiez.core.domain.model.todo.ToDo
import com.loyaltiez.core.domain.model.todo.WeeklyToDo
import com.loyaltiez.core.presentation.fragments.TinDoFragment
import com.loyaltiez.core.services.AlarmService
import com.loyaltiez.feature_home.R
import com.loyaltiez.feature_home.adapters.DeleteTindoItemClickListener
import com.loyaltiez.feature_home.adapters.EditTindoItemClickListener
import com.loyaltiez.feature_home.adapters.TindoItemAdapter
import com.loyaltiez.feature_home.databinding.HomeFragmentBinding
import com.loyaltiez.feature_home.presentation.view_models.HomeViewModel
import java.util.*

class HomeFragment : TinDoFragment() {

    private val viewModel: HomeViewModel by lazy {

        // Instantiate using the Factory extension function
        ViewModelProvider(
            this,
            HomeViewModel.Factory(
                requireActivity().application,
            )
        )
            .get(HomeViewModel::class.java)
    }

    private lateinit var binding: HomeFragmentBinding

    // The recyclerview adapter
    private lateinit var adapter: TindoItemAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Create the binding variable
        binding = DataBindingUtil
            .inflate(
                inflater,
                R.layout.home_fragment,
                container,
                false
            )

        // Set the lifecycle owner so that live data can be observed
        binding.lifecycleOwner = viewLifecycleOwner

        // Set the viewModel for the binding variable
        binding.viewModel = viewModel

        setAdapters()
        setObservers()

        createNotificationChannel()


        return binding.root
    }

    private lateinit var alarmService: AlarmService
    private lateinit var pendingIntent: PendingIntent


    @SuppressLint("UnspecifiedImmutableFlag")
    private fun clearAlarm(toDo: ToDo) {

        alarmService = AlarmService(requireContext())
        val intent = Intent(requireActivity(), AlarmReceiver::class.java)

        pendingIntent = PendingIntent.getBroadcast(
            requireContext(),
            toDo.id!!,
            intent,
            PendingIntent.FLAG_NO_CREATE
        )

        alarmService.removeAlarm(pendingIntent, toDo.id!!)
    }

    @Suppress("DEPRECATION")
    @SuppressLint("MissingPermission", "UnspecifiedImmutableFlag")
    private fun setAlarm(toDo: ToDo, type: String) {

        val dateTimeInMillis = toDo.date?.time

        val calendar: Calendar = Calendar.getInstance().apply {
            if (dateTimeInMillis != null) {
                timeInMillis = dateTimeInMillis
            }
            set(Calendar.HOUR_OF_DAY, toDo.time.hours)
            set(Calendar.MINUTE, toDo.time.minutes)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        val alarmService = AlarmService(requireContext())

        val intent = Intent(requireActivity(), AlarmReceiver::class.java)

        intent.putExtra("title", toDo.title)
        intent.putExtra("description", toDo.description)
        intent.putExtra("time", toDo.getTimeString())
        intent.putExtra("date", toDo.getDateString())
        intent.putExtra("id", toDo.id)
        intent.putExtra("type", type)

        // Check if the alarm already exists
        val alarmExists = PendingIntent.getBroadcast(
            requireContext(),
            toDo.id!!,
            intent,
            PendingIntent.FLAG_NO_CREATE
        ) != null

        if (!alarmExists) {

            // If it doesnt create a new pending intent with the todoid as the request code
            pendingIntent = PendingIntent.getBroadcast(
                requireContext(),
                toDo.id!!,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )

            // Set the alarm using the alarm service
            alarmService.setAlarm(
                calendar.timeInMillis,
                pendingIntent,
                toDo.id!!
            )
        }
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("ID1", name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun setAdapters() {

        adapter = TindoItemAdapter(
            requireActivity().application,
            EditTindoItemClickListener { tindo ->
                viewModel.onEditTindoClicked(tindo)
            },
            DeleteTindoItemClickListener { tindo ->
                viewModel.onDeleteTindoClicked(tindo)
                clearAlarm(tindo) // Delete the associated alarm
            }
        )

        binding.recyclerViewTodos.adapter = adapter
    }

    private fun setObservers() {

        setNavigationObservers()

        viewModel.toDos.observe(
            viewLifecycleOwner
        ) { it ->

            val list = mutableListOf<ToDo>()

            for (todo in it) {
                if (todo.date == null) {
                    val dailyToDo = DailyToDo(
                        todo.userEmail,
                        todo.title,
                        todo.description,
                        todo.color,
                        todo.time,
                        todo.id
                    )
                    list.add(dailyToDo)
                    setAlarm(dailyToDo, DailyToDo.getTypeString())
                } else {
                    val weeklyToDo = WeeklyToDo(
                        todo.userEmail,
                        todo.title,
                        todo.description,
                        todo.color,
                        todo.time,
                        todo.date,
                        todo.id
                    )
                    list.add(weeklyToDo)
                    setAlarm(weeklyToDo, WeeklyToDo.getTypeString())
                }
            }
            adapter.submitList(list)
        }
    }


    private fun setNavigationObservers() {

        observeFragmentToActivityNavigationFlag(
            viewModel.navigateToCreateToDo,
            { HomeFragmentDirections.actionHomeFragmentToCreateTodoActivity() },
            viewModel::onNavigateToCreateToDoComplete,
            false
        )

        viewModel.navigateToEditToDo.observe(
            viewLifecycleOwner
        ) {
            if (it != null) {

                val navController = findNavController()

                navController.navigate(
                    HomeFragmentDirections.actionHomeFragmentToEditTodoActivity(it)
                )

                viewModel.onNavigateToEditToDoComplete()
            }
        }
    }

}