# Trippy
App Reminder for Trips
what is this app for ?
The user enters trip details : name, Start point , end point , date and time
and the user have choice to add notes
When the trip time comes ,an alarm starts with alert dialog with 3 options : Start trip
,Cancel trip
,Snooze
, Start send him to google maps with navigation from the start point to the end point and Floating icons appears contains the notes which the user added 
, Snooze : create onGoing notification with trip details and when the user click on it the aler dialog appears again 
You can create multiple alarms all will work greatly
and there is History activity which contains all finished and canceled trips with their notes
also you can delete the trip with swipe left or right
and you can Sync your data with Firebase , and all account has its own data

What Topic used in this app ? 
Firebase Authentication (signIN with email and password - SignIN with Google)
SQLite database
AlarmService - Notification - FloatingWindowService - RecyclerView to show the trips - ListView for notes

Photos :
LogIn Activity : 
![WhatsApp Image 2021-09-09 at 4 02 34 AM](https://user-images.githubusercontent.com/76598011/132610208-b5678668-79a8-4159-9a9e-c692581b8df7.jpeg)

Upcoming Activity:
![WhatsApp Image 2021-09-09 at 4 02 34 AM (1)](https://user-images.githubusercontent.com/76598011/132610306-3cffef6f-1c67-4420-9e82-1397a6e057e2.jpeg)

Adding Trip Activity : 
![WhatsApp Image 2021-09-09 at 4 02 34 AM (2)](https://user-images.githubusercontent.com/76598011/132610359-39b48f23-8ce4-4473-a61f-4e48f12cbbc0.jpeg)

Showing the Trips on recycler view :
![WhatsApp Image 2021-09-09 at 4 02 34 AM (3)](https://user-images.githubusercontent.com/76598011/132610394-8d7f52d4-274b-4bf9-80af-c4f19469a051.jpeg)

Adding Notes : 
![WhatsApp Image 2021-09-09 at 4 02 34 AM (4)](https://user-images.githubusercontent.com/76598011/132610454-ff7ff787-a5d4-4989-90ef-e5a0eb0ec180.jpeg)

Showing Notes after click on the image :
![WhatsApp Image 2021-09-09 at 4 02 34 AM (5)](https://user-images.githubusercontent.com/76598011/132610491-18e84109-dcf0-4a27-874c-99ca2ad37319.jpeg)

Navigation on Start button clicked and showing Floating icon :
![WhatsApp Image 2021-09-09 at 4 02 34 AM (6)](https://user-images.githubusercontent.com/76598011/132610550-83f05999-f749-47f8-b291-827ad9769f5d.jpeg)


The floating icon contains the notes with check box :
![WhatsApp Image 2021-09-09 at 4 02 34 AM (7)](https://user-images.githubusercontent.com/76598011/132610578-d4e09ec7-2ddb-45fb-b561-b17b1e8976e3.jpeg)


The trip goes to History activity after the trip is finished with RED Color :
![WhatsApp Image 2021-09-09 at 4 02 34 AM (8)](https://user-images.githubusercontent.com/76598011/132610682-fea8b1f4-013c-4258-9b82-5f69975db8c6.jpeg)

The Alert Dialog when the alarm rings for the trip : 
![WhatsApp Image 2021-09-09 at 4 02 34 AM (9)](https://user-images.githubusercontent.com/76598011/132610785-525c4793-e821-49b4-81c7-cf2a2656d21a.jpeg)
