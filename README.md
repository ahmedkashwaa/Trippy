# Trippy
App Reminder for Trips
what is this app for ?
The user enters trip details : name, Start point , end point , date and time
and the user have choice to add notes
When the trip time comes ,an alarm starts with alert dialog with 3 options : Start trip,Cancel trip,Snooze
Start send him to google maps with navigation from the start point to the end point and Floating icons appears contains the notes which the user added 
Snooze : creat onGoing notification with trip details and when the user click on it the aler dialog appears again 
You can create multiple alarms all will work greatly
and there is History activity which contains all finished and canceled trips with their notes
also you can delete the trip with swipe left or right
and you can Sync your data with Firebase , and all account has its own data

What Topic used in this app ? 
Firebase Authentication (signIN with email and password - SignIN with Google)
SQLite database
AlarmService - Notification - FloatingWindowService - RecyclerView to show the trips - ListView for notes
