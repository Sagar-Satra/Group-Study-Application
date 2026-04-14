# INFO 6205: Program Structure and Algorithms - Spring 2026 - Group Project Topic 

# Group Study Application
## Project Overview
- We made a JavaFX-based (maven build) collaborative study room application for students that gamifies the studying experience using Pokemon evolution concept. 
- Users can create or join virtual study rooms, track their progress, collect Pokemon trophies, and compete on global and local room leaderboards. 
- This application basically motivates students to study more and simultaneously hook them up to play game.
- Students can focus on studying together while being visible to others for accountability. 
- The application features a Pokemon-themed gamification system where users are assigned a random Pokemon that evolve as they study more, creating an engaging and motivating study experience.
> Core Idea: Combine real-time group accountability with gamification 
to encourage consistent and focused studying.

## Team Members
- **Sagar Jayantilal Satra**: satra.sa@northeastern.edu
- **Aditya Yendhe**: yendhe.a@northeastern.edu
- **Yuxuan Chen**: chen.yuxuan9@northeastern.edu


## Project setup instructions
### Technology Stack
- **Language**: Required Version 17. Java 21 may work but Java 17 is the configured version
- **UI Framework**: JavaFX 21
- **Build Tool**: Apache Maven 3.9.14
- **Design Pattern**: Service Oriented Architecture

### Step 1: Prerequisites to download and install - (Required)
- **Java Development Kit (JDK)**: Required Version 17. Java 21 may work but Java 17 is the configured version in pom.xml
- **Maven**: Version 3.6 or higher (required)
- **IDE**: Eclipse

- check installed version
```bash
java -version
mvn -version
```

### Step 2: Project Download or clone the repository
**Option A: Download and Extract ZIP from Canvas Submission**
- Download the project ZIP from the canvas submission
- Extract it into a new folder "Group-Study-Application"

**Option B: Clone from GitHub**
Project: https://github.com/Sagar-Satra/Group-Study-Application.git
Make a new folder: Group-Study-Application and open terminal into that folder
```bash
git clone https://github.com/Sagar-Satra/Group-Study-Application.git
```

### 3. Import Project into IDE
**For Eclipse IDE:**
1. Open Eclipse
2. Select `File` -> `Import` -> Select `Existing Maven Projects`
3. Browse to the project folder "Group-Study-Application"
4. Click `Finish`
5. Right click the project or pom.xml file
6. Select Maven -> Update Project -> Click Ok
7. Wait for Maven to download dependencies and sync

**For IntelliJ IDEA: in case Eclipse does not work**
1. Open IntelliJ IDEA
2. Select `File` ->  `Open`
3. Navigate to the project folder and select it
4. IntelliJ will automatically detect it as a Maven project
5. Wait for Maven to download dependencies


#### 4. Build the Project
**Option A: Using Eclipse**
- Right click the project or pom.xml
- Click "Run As" -> click **Maven clean**
- Click "Run As" -> click **Maven install**

**Option B: Using Terminal**
Run this inside the project folder
```bash
mvn clean install 
```

OR

Open terminal inside Eclipse IDE -> Right click project -> Locate "Show in Local Terminal" option
```bash
mvn clean install 
```

#### 5. Run the Application
**Option A: Using Eclipse**
1. Right-click on **project** or **pom.xml** or **Main.java**
2. Select Run As -> Maven Build
2. In Goals field, enter: **clean javafx:run**
3. Click **Run**


**Option B: Using Maven**
Run this inside the project folder - Preferred Command
```bash
mvn clean javafx:run
```

**Option C: Using Command Line - Alternative**
This may also work:
```bash
mvn exec:java -Dexec.mainClass="com.groupstudy.Main"
```

## Troubleshooting

### Common Issues

**Issue 1: JavaFX Runtime Components Missing**
```
Error: JavaFX runtime components are missing
```
**Solution:**
- Ensure JavaFX dependencies are in `pom.xml`
- Make sure Java 17 and Maven is installed
```bash
java -version
mvn -version
```
- In Eclipse: right click on project and run:
   - Maven -> Update Project
- Rebuilt the project:
- Run `mvn clean install`
- Run using preferred command: 
```bash
mvn clean javafx:run
```

**Issue 2: Pokemon Images Not Loading**
```
Could not load Pokemon image: /images/pokemon.png
```
**Solution:**
- Verify images are in `src/main/resources/images/`
- Check image paths in `PokemonService.java` have `/images/` prefix
- Rebuild project: `mvn clean compile`
- Run using preferred command: 
```bash
mvn clean javafx:run
```



## Individual Contributions to the application
- **Sagar Jayantilal Satra**: Pokemon evolution system, Room UI, trophy collection logic and UI, notification system, leaderboard, ADT(List, Queue, Bag, Sorting) and implementation(ArrayList, Queue Implementation, Resizable Array Bag, Merge Sort)
- **Aditya Yendhe**: User management, room management, Profile UI, Search UI, Lobby UI, authentication screen, Linked List implementation
- **Yuxuan Chen**: Time tracking, Hashing ADT, HashMap implementation, room status management, user status management, Room search logic, room creation logic


## Key Features
### 1. Study Room Management
- Create public or private (password-protected) study rooms
- Set custom session duration and room capacity
- Real-time room status tracking (active, ended)
- Automatic room cleanup after session ends

### 2. Pokemon Evolution System
- Random Pokemon assignment when joining a room
- Total Pokemon collected is tracked per user (each assignment counts as +1)
- 15 different Pokemon families with 3 evolution stages each
- Pokemon evolves automatically based on study time:
  - Stage 1 → Stage 2: 30 minutes (accelerated to seconds for demo)
  - Stage 2 → Stage 3: 60 minutes total (accelerated for demo)
- Visual progress bar showing evolution progress
- Real-time Pokemon display with image updates for all participants in the room

### 3. Trophy Collection
- Earn trophies when Pokemon reaches evolution stage 2 and stage 3
- Trophy collection stored in a Bag data structure because user can have same pokemon multiple times
- View all collected trophies in dedicated Trophy Collection UI
- Trophies track Pokemon name, stage, and study time

### 4. Leaderboard System
- **Global Leaderboard**: Ranks all users by total trophy count
- **Room Leaderboard**: Ranks participants in current session by trophy count
- Real-time leaderboard updates
- Notification when reaching #1 position
- Leaderboard is sorted based on Trophy count. In case there is a tie between users, studied time is compared to rank the users.

### 5. Notification System
- Queue-based notification system (FIFO)
- Notifications for:
  - Pokemon evolution
  - Trophy earned
  - Reaching top of leaderboard
  - Session end
- Auto-dismiss notifications
- All notifications cleared when leaving room

### 6. User Status Tracking
- **Global Status**: Online, Offline, In Room
- **Room Status**: Studying, Break, Left Early, Session Ended
- Idle detection (5 seconds idle = studying)
- Demo users simulate real-time status changes (Studying ↔ Break) for demonstration purposes
- Simulation does not affect the current user
- Manual break toggle in the room and keeping tracking

### 7. Multi-User Support
- See other participants in the same room
- View other users' Pokemon as small icons
- Real-time participant list updates

### 8. Demo Simulation System
- Preloaded demo users automatically switch between Studying and Break states
- Break duration is extended to simulate realistic behavior
- Higher probability of breaks for better demo visualization
- Only demo users are affected (current user is not impacted)
- Ensures real-time UI updates without manual refresh

## Technical Architecture

### Data Structures Implemented

| Data Structure | Implementation | Usage |
|----------------|----------------|-------|
| **ArrayList** | Custom ADT | Pokemon collection, leaderboard entries |
| **HashMap** | Custom ADT | Room storage, user-status mapping, user-time tracking |
| **HashMap** | Custom ADT | Room participants (deduplication) |
| **Queue** | Custom ADT | Notification system (FIFO) |
| **Bag** | ResizableArrayBag | Trophy collection (allows duplicates) |
| **LinkedList** | Custom ADT | User action history |
| **Sorting** | MergeSort | Leaderboard ranking |



## How to Use the application

### Getting Started

1. **Launch the Application**
   - Run the application using one of the methods above
   - The Lobby screen will appear showing available study rooms

2. **Join a Study Room**
   - Click on any room card to enter
   - You'll be assigned a random Pokemon (Stage 1)
   - Your study session begins automatically

3. **Study and Evolve**
   - Stay idle for 5+ seconds to be marked as "Studying"
   - Pokemon evolves every 30 minutes of study time
   - Progress bar shows evolution status
   - Stage 1 (0-30 min) -> Stage 2 (30-60 min) -> Stage 3 (60+ min)

4. **Collect Trophies**
   - When Pokemon reaches Stage 3, you earn a trophy
   - Click "🏆 My Trophies" to view your collection
   - Trophies are saved permanently

5. **View Leaderboards**
   - **Global Leaderboard**: Click 🏆 button in Lobby to see all users ranked by trophies
   - **Room Leaderboard**: Click "🏆 Check Room Leaderboard" inside a study room to see current session rankings

6. **Take Breaks**
   - Click "Take Break" to pause your study timer
   - Click "Resume" to continue studying
   - Pokemon only evolves during active study time

7. **Leave Room**
   - Click "<- Leave" to exit the room early
   - Your progress is saved
   - Other users continue studying

### Creating Study Rooms

1. Click the ➕ button in the Lobby
2. Set room title, capacity, and duration
3. Choose public or private (with password)
4. Room appears in the lobby for others to join

### Notifications

You'll receive popup notifications for:
- Pokemon evolution (Stage 1 → 2, Stage 2 → 3)
- Trophy earned (when reaching Stage 3)
- Reaching #1 on room leaderboard
- Study session ending

## Demo Flow
### Demo Setup

The system is pre-populated using a DataInitializer service:

- Demo users are automatically created with default password: `demo123`
- Study rooms (both public and private) are pre-created
- Demo users are automatically assigned to rooms and will auto exit the room when account been logged in
- Each user is assigned a random Pokémon upon joining a room
- Study progress, Pokémon evolution, and leaderboard data are partially simulated

> Notes:
> - User behavior and Pokémon progression are randomized for demonstration purposes  
> - Time is accelerated (seconds represent minutes)  
> - UI icons may render differently across operating systems (Windows vs macOS)
> - Admin can join the room even if the capacity is full

<table>
<tr>
<td valign="top">

### 👥 Precreated Demo Users

| Username | Password |
|---------|----------|
| John | demo123 |
| Eve | demo123 |
| Frank | demo123 |
| Joe | demo123 |
| Bob | demo123 |
| Adele | demo123 |
| Lily | demo123 |
| Mike | demo123 |
| Sophia | demo123 |
| Chris | demo123 |
| David | demo123 |
| Emma | demo123 |

</td>

<td valign="top">

### 🏠 Precreated Demo Rooms

| Room Title | Type | Admin | Capacity | Password |
|-----------|------|------|----------|----------|
| Quick Study Sprint | Public | Lily | 5 | — |
| Private Focus Group | Private | Chris | 4 | study123 |
| Math Study Group | Public | Emma | 6 | — |
| CS Algorithm Practice | Public | John | 8 | — |
| Late Night Grind | Private | Eve | 3 | team456 |

</td>
</tr>
</table>

### Sample User Journey

<img width="761" height="657" alt="Screenshot 2026-04-14 at 12 56 58" src="https://github.com/user-attachments/assets/0789ee99-5e96-4f32-949a-a368113f0f17" />


## Development Notes

### Key Components

### Custom Data Structures
All core data structures are implemented from scratch (no Java Collections Framework):
- **ArrayList**: Dynamic array with automatic resizing
- **LinkedList**: Keeping track of user activity status in each room
- **HashMap**: Hash table with chaining for collision resolution
- **Queue**: Circular array-based queue
- **Bag**: Unordered collection allowing duplicates
- **Sorting**: Using of merge sorting


## Data Structure Requirements Met
- **HashSet**: Room participant management (deduplication)
- **HashMap**: User-status mapping, room storage
- **LinkedList**: User action history
- **ArrayList**: Pokemon list, leaderboard entries
- **Queue**: Notification system (FIFO)
- **Bag**: Trophy collection (unordered, allows duplicates)
- **Sorting**: MergeSort for leaderboard ranking
- **Hashing**: Password authentication, room IDs

### Data Structures Used
1. Custom ArrayList implementation
2. Custom HashMap implementation with chaining
3. Custom Queue implementation (circular array)
4. Custom Bag implementation (resizable array)
5. MergeSort algorithm for leaderboard
6. LinkedList for action history

### Object-Oriented Design
- Abstract Data Types (ADT) with interfaces
- Concrete implementations
- Service layer for business logic
- Service Oriented Architecture
- Enums for status management

### JavaFX Features
- BorderPane, VBox, HBox layouts
- Timeline animations for real-time updates
- Custom UI components (extends VBox, BorderPane)
- FXML integration for complex views
- Stage management for multiple windows
- Event handling and navigation

## UI Overview

### Lobby View
- Lists all available study rooms
- Shows room capacity and remaining time
- Navigation buttons: Profile, Leaderboard, Create Room, Search

### Study Room View
- Large Pokemon display with evolution progress
- Room timer countdown
- Participant list showing other users' Pokemon
- Control buttons: Take Break, Check Leaderboard, Leave

### Leaderboard View
- Table with rankings (Rank, Username, Trophies, Study Time)
- Current user's rank highlighted below the table
- Supports both global and room-specific views
- Leaderboard is sorted based on Trophy count. In case there is a tie between users, studied time is compared to rank the users

### Trophy Collection
- Grid layout of collected Pokemon trophies
- Shows Pokemon image, name, stage, and study time
- Empty state for new users

## Future Enhancements
- AI integration for QnA
- MySQL database integration for data persistence
- Study streak tracking and visualization
- Friends system with sending request and connecting using Sockets
- Study analytics
- Chat functionality within study rooms - Video chat or text based chat

## License

This project is created as part of the Program Structures and Algorithm course (INFO 6205) at Northeastern University.

## Acknowledgments
- Northeastern University - PSA Course - Professor Jones
- JavaFX Documentation
- Pokemon imagery (educational use only)

---

**Built with Passion by Team Sagar, Yuxuan, Aditya**
