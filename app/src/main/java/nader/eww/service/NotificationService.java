package nader.eww.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import nader.eww.MainActivity;
import nader.eww.R;
import nader.eww.model.Message;

import java.util.List;

public class NotificationService {
	
	private static final String CHANNEL_ID = "chat_notifications";
	private static final int NOTIFICATION_ID = 1;
	
	private Context context;
	private NotificationManager notificationManager;
	
	public NotificationService(Context context) {
		this.context = context;
		this.notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		createNotificationChannel();
	}
	
	private void createNotificationChannel() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Chat Notifications", NotificationManager.IMPORTANCE_DEFAULT);
			notificationManager.createNotificationChannel(channel);
		}
	}
	
	public void showNotification(List<Message> messages) {
		Intent intent = new Intent(context, MainActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		
		NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
		.setSmallIcon(R.drawable.ic_notification)
		.setContentTitle("New Messages")
		.setContentText(messages.size() + " new messages")
		.setPriority(NotificationCompat.PRIORITY_DEFAULT)
		.setContentIntent(pendingIntent)
		.setAutoCancel(true);
		
		if (messages.size() > 1) {
			NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
			for (Message message : messages) {
				inboxStyle.addLine(message.content);
			}
			builder.setStyle(inboxStyle);
			} else if (messages.size() == 1) {
			builder.setContentText(messages.get(0).content);
		}
		
		notificationManager.notify(NOTIFICATION_ID, builder.build());
	}
}