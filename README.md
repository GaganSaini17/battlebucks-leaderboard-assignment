🚀 BattleBucks – Real-Time Leaderboard Engine

📌 Overview
This project implements a real-time leaderboard system for a gaming platform.

The system simulates a backend game engine generating live score updates and a leaderboard module
that processes, ranks, and exposes reactive leaderboard state to the UI.

The focus of this implementation is:
- Real-time data handling using Coroutines + Flow
- Correct ranking logic
- Performance-aware design
- Lifecycle safety
- Smooth UI updates
- Collapsing animated header


🏗 Architecture Overview
The project follows a Clean MVVM Architecture with clear separation of responsibilities.

engine/        → Score generation (UI-agnostic)
leaderboard/   → Ranking & leaderboard state management
model/         → Domain models
ui/            → Activity, ViewModel, Adapter


🔹 Module Responsibilities
1️⃣ Score Generator (Engine Layer)
- Simulates backend score updates
- Emits Flow<ScoreUpdate>
- Random interval updates (500ms–2000ms)
- Random player updates
- Score only increases
- Runs on Dispatchers.Default

Key Class: ScoreGenerator

2️⃣ Leaderboard Manager (Domain Layer)
- Consumes score updates
- Maintains in-memory score map
- Applies ranking logic
- Same score → same rank
- Next rank skipped correctly
- Emits StateFlow<List<LeaderboardItem>>

Key Class: LeaderboardManager

3️⃣ Presentation Layer (MVVM)
ViewModel
- Coordinates engine + leaderboard
- Exposes single UiState
- Lifecycle-aware

Activity
- Collects UI state
- Updates RecyclerView via ListAdapter
- Applies animated collapsing header behavior


⚡ Real-Time Data Handling
- Kotlin Coroutines + Flow
- Non-blocking delay
- Background computation via Dispatchers.Default
- Reactive UI using StateFlow
- Single source of truth (LeaderboardUiState)


🏆 Ranking Logic
Sorting Rules:
- Sort by score (DESC)
- Same score → same rank
- Next rank skips accordingly

Ranking computation is isolated inside LeaderboardManager to maintain domain purity.


🎨 UI & Animation
Leaderboard
- RecyclerView + ListAdapter
- DiffUtil prevents flickering
- Highlight animation on score updates

Collapsing Hero Header
- CoordinatorLayout
- AppBarLayout
- CollapsingToolbarLayout
- ConstraintLayout + Guidelines for animation
- Smooth scaling and positional transitions
- Rank & score shrink but remain visible


🧠 Performance Considerations
UI Thread Safety
- No heavy computation on Main thread
- Ranking computed in background context
- RecyclerView uses DiffUtil for minimal updates

Memory Safety
- No static references
- ViewModel scope tied to lifecycle


📱 Lifecycle Behavior
Screen Rotation
- ViewModel survives configuration changes
- Score engine continues via viewModelScope

App Background
- Collection pauses due to lifecycle awareness
- Engine continues if ViewModel alive
- No memory leaks