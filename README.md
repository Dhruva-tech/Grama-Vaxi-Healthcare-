# Grama-Vaxi (Healthcare)

Grama-Vaxi is an Android application designed to provide healthcare management and support, with a specific focus on animal health and community alerts. It leverages modern Android technologies to provide a robust and user-friendly experience.
## Screenshots
<img width="335" height="753" alt="Screenshot 2026-05-15 170640" src="https://github.com/user-attachments/assets/5bdd3dd3-e3f6-47eb-82f9-65f570a31112" />
<img width="337" height="751" alt="Screenshot 2026-05-15 170727" src="https://github.com/user-attachments/assets/829a9a8e-5d46-4129-b2d9-9da9308510a7" />




## Features

- **Animal Management**: Track and manage animal health records, including adding new animals and viewing detailed profiles.
- **Disease Reporting**: Easily report animal diseases to relevant authorities or community members.
- **AI-Powered Chat**: Integrated Gemini AI to assist with healthcare queries and information.
- **Health Calendar**: Keep track of vaccinations, check-ups, and important health events.
- **Real-time Alerts & Inbox**: Receive important notifications and messages regarding local health updates.
- **Profile Management**: Maintain user profiles and registration details.

## Tech Stack

- **UI**: [Jetpack Compose](https://developer.android.com/jetpack/compose) for a modern, declarative UI.
- **Architecture**: MVVM (Model-View-ViewModel) for clean separation of concerns.
- **Database**: [Room](https://developer.android.com/training/data-storage/room) for local data persistence.
- **Networking**: [Retrofit](https://square.github.io/retrofit/) with GSON for API communication.
- **Background Tasks**: [WorkManager](https://developer.android.com/topic/libraries/architecture/workmanager) for reliable background processing.
- **Navigation**: [Compose Navigation](https://developer.android.com/jetpack/compose/navigation) for seamless screen transitions.
- **AI Integration**: [Google Generative AI SDK](https://ai.google.dev/) (Gemini) for intelligent chat capabilities.
- **Image Loading**: [Coil](https://coil-kt.github.io/coil/) for performant image loading.

## Getting Started

### Prerequisites

- Android Studio Ladybug (or newer)
- JDK 11
- Android SDK 35 (Compile SDK)
- Minimum SDK: Android 7.0 (API level 24)

### Installation

1. Clone the repository:
   ```bash
   git clone <repository-url>
   ```
2. Open the project in Android Studio.
3. Sync the project with Gradle files.
4. Run the app on an emulator or a physical device.

## Project Structure

- `ui/`: Contains Compose screens, components, and theme definitions.
- `data/`: Data layer handling Room database, repositories, and models.
- `viewmodel/`: Business logic and UI state management.
- `network/`: Retrofit service definitions and API clients.
- `worker/`: Background worker implementations.
- `navigation/`: Navigation graph and route definitions.

## License

[Add License Type, e.g., MIT License]
