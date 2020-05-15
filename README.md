# Location Tracker Mobile Application:
A Foreground serviece that contnously Logs location details.

Language: Kotlin 
•	Kotlin is designed to interoperate fully with Java, and the JVM version of its standard library depends on the Java Class Library, but type inference allows its syntax to be more concise.
•	https://kotlinlang.org/docs/reference/android-overview.html
App Description:
This application focuses on continuously capturing the GPS location broadcast signals emitted by Android. To achieve this, we create an application with basic UI features to turn on/off Foreground service (where the actual data capturing happens).
Once the application is installed and as you navigate through app. You should see a play button, on clicking the play button it gives a toast message (“Location Tracker activated”) make sure the toast message shown is same, if you get a different toast please click on the button again (will be fixed later). 

Once the service starts running you should be able to see a notification message displayed on phone. This notification is cannot be cleared as this is key for our service to run without getting killed by OS.

Data Stored:
Currently we store the latitude, longitude and timestamp on every broadcast signal. 
Data fetched is stored in a Text files under the filesDir of the application with naming convention as “location_data_timestamp.txt”
 Note: The files generated with in the application cannot be viewed by directly trying to access from phone file manager settings. To access these files, one should have root access to the device.
•	If root access is unavailable, to see the data getting stored you can try to build application and run throw Android Studio, once a file gets stored code is written to fetch the file data and log the details. 

Current Configuration:
•	For every 5 mins the data is pushed to a file and stored. 
•	Fetching GPS data is done for every 5 seconds.
Both these configurations are modifiable. 

The link for the source code is github.
Please feel free to check the code, Kotlin is provides greater readability and lesser code.

Uncalled/Future Code files:
These are the code files to push the data to server as soon as the files get saved. Currently these are not called and are not tested completely yet.
•	FlieUploadApi
•	MyResponse.kt
•	ServiceGenerator.kt
You can ignore these files.
