# FitSphere 

**FitSphere** is a modern fitness tracking app built with **Jetpack Compose**, offering a seamless experience for logging workouts, tracking daily diet, and managing user profiles. It supports both **email/password** and **Google Sign-In** using Firebase Authentication.

---

##  Features

-  **User Authentication**
  - Email/password login & registration
  - Google Sign-In integration

-  **Weather Display**
  - Display realtime weather info using api

-  **Workout Module**
  - Start and track workouts
  - Save workout records with timestamps
  - Display workout history

-  **Diet Tracker**
  - Select food items and log calorie intake
  - Choose meal type (breakfast, lunch, dinner)
  - Save and view diet history with local **Room database**
  - Delete saved diet entries

-  **User Profile**
  - View and update profile info

-  **Map Support**
  - Integrated with **Mapbox** for map-related features

---

##  Built With

| Category     | Tech Stack                              |
|--------------|-----------------------------------------|
| UI           | Jetpack Compose                         |
| State        | ViewModel                               |
| Database     | Room (local) + Firebase Firestore       |
| Auth         | Firebase Authentication (Email, Google) |
| Map          | Mapbox                                  |
| Image        | Coil (image loading)                    |
| Routing      | Navigation Compose                      |
| Others       | Gradle + Kotlin DSL                     |

---

##  Getting Started

### Clone the repo

```bash
git clone https://github.com/SejarTang/FitSphere.git
