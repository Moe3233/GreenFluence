
# Greenfluence Android Application

## Overview
The Greenfluence app is designed to promote sustainability among university students and staff. This application features a user-friendly interface that allows users to engage with eco-friendly practices through various activities and a sustainability scoring system.

## Project Structure

### Code Structure
The Java code for the application is organized under the `Attempt2/app/src/main/java/com.example.attempt2/ui` directory. Each Java file corresponds to a specific page in the application, and the primary files are as follows:

- **LoginActivity.java**: Manages user login.
- **MainActivity.java**: The main interface of the application.
- **RegisterActivity.java**: Handles new user registrations.
- **UserSession.java**: Manages user session data.

**Note:** The above files work independently as separate pages instead of being displayed on top of the main page. 

All Java classes are well-commented and structured in a linear manner, making it easy for individuals with a basic understanding of Java to follow and comprehend.

### Resource Files
The resource files are located in the `Attempt2/app/src/main/res/` directory:

- **Layouts**: Page layouts are found in the `layout` folder.
- **Icons and Design**: Most icons and design elements are located in the `drawable` folder.

### Android Manifest
The `AndroidManifest.xml` file contains the application themes and the necessary user permissions required for the app to function correctly.

## Running the Application
To run the code and ensure the database structure is updated, you will need to modify the `google-services.json` file. If the database is no longer active, please replace it with a newer version as it is created temporarily for free from Google.

### Important Files
- **google-services.json**: Contains configurations for Firebase and Google services.
- **HomeFragment.java** (Line 144): This is where you can find the main API to be updated for the NewsAPI.

## Contributing
We welcome contributions to enhance the application. Please fork the repository and submit a pull request with your improvements.


## Contact
For any inquiries or suggestions, please reach out to u3233503@uni.canberra.edu.au. 

---
