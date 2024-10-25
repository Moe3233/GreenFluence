The program is written pretty straightforward. 
Each pages are using fragment on top of the main page where the only data being shared is the current user (auth). 
Each java code in the Attempt2/app/src/main/java/com.example.attempt2/ui should correspond to the pages in the application.
There are LoginActivity, MainActivity, RegisterActivity and UserSesson which are not a part of ui folder as they work independently as a new page instead of being display on top of the main page. 
These codes have enough comments and linear structure that people with adequate java coding experience should be able to understand.

The AndroidManifest.xml contains the application themes and user permissions necessary to run this application.

The layout of the pages as well as the icons and other design is inside the res folder in Attempt2/app/src/main/res/ where pages layoput is in layout folder and most of the icons are in drawable. 

To run the code and update the database structure the google-services.json should be changed with a newer version should this database cease to exist since its a temporary created free from google.
Also the main API to be changed can be found in google-services.json and in line 144 of HomeFragment for the NewsAPI. 
