
# Location Tracker App

The goal of Location Tracker app is to show current location on map, and track user location in background and store the location data in Database to view in map.

## Android Studio Environment

For development, the latest version of Android Studio is required. Project was developed in Android Studio 3.x

Project developed with following specifications:
- compilesdkversion 34
- targetsdkversion 34
- minsdkversion 21
- JDK version used JDK 8


## Android Studio

Android Studio (Recommended)
- (Project was built on Androi studio Bumblee version)

- First, clone the repo:
    git clone: https://github.com/Ravivarma14/LocationTracker.git
- Open Android Studio and select ```File->Open ``` and navigate to the root directory of the Location Tracker project.
- In the "Open File or Project" dialog, Select the directory of cloned repo (downloaded project).
- Click 'OK' to open the the project in Android Studio.
- A Gradle sync should start, but you can force a sync and build the 'app' module as needed from ```File->Sync project with Gradle files```.

- To Run the App Select ```Run -> Run 'app'``` (or Debug 'app') from the menu bar
- Select the device you wish to run the app on and click ```OK```
## Dependencies to Add

```implementation 'com.google.android.gms:play-services-maps:18.20'```
- This dependency used to configure Google Maps template automatically and adds a basic map to a new Android Studio project.
```implementation 'com.google.android.gms:play-services-location:21.1.0' ```

- This dependency used to add location support and related libraries to Android Studio project.

```implementation 'androidx.room:room-runtime:2.4.0'```
- This dependency provides Room Database Library.

 ```annotationProcessor 'androidx.room:room-compiler:2.4.0'```
 - This dependency provides Room Database Library annotations.
## Permissions to Add in Manifest


- Permission to Access INTERNET sevices. 

``<uses-permission android:name="android.permission.INTERNET" />``


- Permissions to Access Location sevices. 

    ``<uses-permission android:name="android.permission ACCESS_FINE_LOCATION" />``

    ``<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />``

    
- Permissions to Access Frorground and Notification sevices. 
    
   `` <uses-permission android:name="android.permission.FOREGROUND_SERVICE" />``
     
   `` <uses-permission android:name="android.permission.POST_NOTIFICATIONS" />``

## Libraries Used

### Room DataBase

- Room is a database layer on top of an SQLite database.
-  Room takes care of mundane tasks that you used to handle with an SQLiteOpenHelper . 
- Room uses the DAO to issue queries to its database. By default, to avoid poor UI performance, Room doesn't allow you to issue queries on the main thread.

#### Entity: 
Entity is a modal class (```LocatoinModel.java```) that is annotated with @Entity. This class is having variables that will be our columns and the class is our table named as ```LocationModel```.

#### Database: 
It is an abstract class (```LocationDB```) where we will be storing all our database entries which we can call Entities and returns database instance to database from method ```getDatabase```.

#### DAO: 
The full form of DAO is a Database access object which is an interface class (```LocationDAO```) with the help of it we can perform CRUD operations in our database.


### Locaion Manager

- This class provides access to the system location services. 
- These services allow applications to obtain periodic updates of the device's geographical location, or to be notified when the device enters the proximity of a given geographical location.
- Locaion Manager has to register with listener to get notified with location changes and related implementations.

#### LocationListener
- Used for receiving notifications when the device location has changed. These methods are called when the listener has been registered with the LocationManager.

### SupportMapFragment

- It is a Map component, This fragment is the way to place a map in an application. 
- It's a wrapper around a view of a map to automatically. As a fragment it can handle the necessary life cycle needs.
- Being a fragment, this component can be added to an activity's layout file simply with the XML code.
- A ```GoogleMap``` must be acquired using ```getMapAsync(OnMapReadyCallback)``` on ``SupportMapFragment``. 
- This class automatically initializes the maps system and the view with ``OnMapReadyCallback`` will provides instance to ``GoogleMap``.

### Service

- A Service is an application component that can perform long-running operations in the background. 
- It does not provide a user interface. Once started, a service might continue running for some time, even after the user switches to another application.

#### NotificationManager

- Class to notify the user of events that happen. This is how you tell the user that something has happened in the background.

#### Notification Builder
- Builder class for Notification objects. 
- Provides a convenient way to set the various fields of a Notification and generate content views using the platform's notification layout template.
## Libraries Used

### Room DataBase

- Room is a database layer on top of an SQLite database.
-  Room takes care of mundane tasks that you used to handle with an SQLiteOpenHelper . 
- Room uses the DAO to issue queries to its database. By default, to avoid poor UI performance, Room doesn't allow you to issue queries on the main thread.

#### Entity: 
Entity is a modal class (```LocatoinModel.java```) that is annotated with @Entity. This class is having variables that will be our columns and the class is our table named as ```LocationModel```.

#### Database: 
It is an abstract class (```LocationDB```) where we will be storing all our database entries which we can call Entities and returns database instance to database from method ```getDatabase```.

#### DAO: 
The full form of DAO is a Database access object which is an interface class (```LocationDAO```) with the help of it we can perform CRUD operations in our database.


### Locaion Manager

- This class provides access to the system location services. 
- These services allow applications to obtain periodic updates of the device's geographical location, or to be notified when the device enters the proximity of a given geographical location.
- Locaion Manager has to register with listener to get notified with location changes and related implementations.

#### LocationListener
- Used for receiving notifications when the device location has changed. These methods are called when the listener has been registered with the LocationManager.

### SupportMapFragment

- It is a Map component, This fragment is the way to place a map in an application. 
- It's a wrapper around a view of a map to automatically. As a fragment it can handle the necessary life cycle needs.
- Being a fragment, this component can be added to an activity's layout file simply with the XML code.
- A ```GoogleMap``` must be acquired using ```getMapAsync(OnMapReadyCallback)``` on ``SupportMapFragment``. 
- This class automatically initializes the maps system and the view with ``OnMapReadyCallback`` will provides instance to ``GoogleMap``.

### Service

- A Service is an application component that can perform long-running operations in the background. 
- It does not provide a user interface. Once started, a service might continue running for some time, even after the user switches to another application.

#### NotificationManager

- Class to notify the user of events that happen. This is how you tell the user that something has happened in the background.

#### Notification Builder
- Builder class for Notification objects. 
- Provides a convenient way to set the various fields of a Notification and generate content views using the platform's notification layout template.
## App Architecture

### Activities

#### SplashScreenActivity

- This Activity shows Splash Screen for 1 second. Then navigates to ``MainActivity``.

#### MainActivity 
- MainActivity asks for required permissions such as Location.
- MainActivity contains ``Map Fragment`` to show current location.
- Contains a Button to ``Start Tracking`` and ``stop Tracking`` which starts a service (we can see Notification with current location lattitude and langitude) or stop the existing service. 
- Contains a Button ``Location History`` which starts ``HistoryActivity`` to show list of saved locations in Database.

#### HistoryActivity
- This Activity contain ``Recycler View`` to show the list of locations stored in Room Database.
- ``Onclick`` of ``Recycler view Item`` will navigate to ``MapActivity`` to show the selected location on map.
- ``OnSwipe`` of ``Recycler view Item`` to Left side will delete the swiped locaiton record from Database.
- This Activity contains ``options menu`` had three operations.
    #### Delete All Locations
    - This option will delete all the locaiton records from table.
    #### Show PolyLine
    - Onclick of this option will navigate to ``MapActivity`` to draw PolyLines with the stored locaiton records from Database.
    #### Settings
    - Onclick of this option will navigate to ``SettingsActivity``.

#### MapActivity
- This Activity contain ``map fragment`` that takes instance of ``MapFragment`` to show locaiton marker on ``Map`` or to draw PolyLines on ``Map``.

#### SettingsActivity
- SettingsActivity has one setting for ``Location Interval`` to update Location for every Interval time in minutes set, will store this value in ``SharedPreferences``.

-  contain setting for ``Location Accuracy`` to set Accuracy for ``Location Manager``, will store this value in ``SharedPreferences``.

### Classes and Interfaces
#### MapFragment
- Java class extends Fragment to show ``Google Map`` in a fragment with ``SupportMapFragment``.
- This will show ``Location Marker`` for a locaiton object on ``Map``.
- Also will draw and show ``PolyLines`` on ``Map`` with provided locaiton records from Database.

#### MyForegroundService
- MyForegroundService extends service, this will create and update ``Notification`` with Tracking Location from ``MainActivity``.

#### LocationListAdapter
- This is a Adapter extends ``RecyclerView.Adapter``. 
- ``LocationListAdapter`` creates Adapter with locaiton records from Database.

#### LocationDB, LocationDAO, LocationModel, LocationEntry
- ``LocationDB`` is class will extends ``Room Database`` and returns instance of Database.
- ``LocationDAO`` is an interface contains all the operations performed on Database or Table.
- ``LocationModel`` is class of Table contains columns information to create table.
- ``LocationEntry`` represents Entry or Row data in Table, this will used to create Adapter and show Location Marker.
## Features

- Location Tracker app will used to Track current Location and store the tracking location in Database to view as List and in Map.
- With the Interval setting and Accuracy setting, will update and store the location corresponding with the settings.
- Provides feature to show the PolyLines with the stored tracking data as Locations.
- Will update Notification while Tracking with current location updated every Interval of time set.

### Notes
- Check Location and Notification permissions manually from settings if not allowed, may app will not work as expected.
