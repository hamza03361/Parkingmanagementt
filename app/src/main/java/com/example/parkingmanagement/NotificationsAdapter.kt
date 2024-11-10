package com.example.parkingmanagement

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

data class Notification(val iconResId: Int, val title: String, val time: String)


class NotificationsAdapter(private val notifications: MutableList<Notification>) :
    RecyclerView.Adapter<NotificationsAdapter.NotificationViewHolder>() {

    class NotificationViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val iconImageView: ImageView = view.findViewById(R.id.notificationIcon)
        val titleTextView: TextView = view.findViewById(R.id.notificationTitle)
        val timeTextView: TextView = view.findViewById(R.id.notificationTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_notification, parent, false)
        return NotificationViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val notification = notifications[position]
        holder.iconImageView.setImageResource(notification.iconResId)
        holder.titleTextView.text = notification.title
        holder.timeTextView.text = notification.time
    }

    override fun getItemCount(): Int = notifications.size

    // Add a method to remove a notification at a specific position
    fun removeNotificationAt(position: Int) {
        notifications.removeAt(position)
        notifyItemRemoved(position)
    }
}
