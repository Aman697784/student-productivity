# Student Productivity Planner

> An Android application for task planning, focused study, scheduling,
> exam preparation, and productivity tracking.

## 👨‍💻 Developer

**AMAN RAJ**\
Android Developer \| Kotlin

## 📖 About

**Student Productivity Planner** is a student-focused Android
application that brings academic task management, focused study
sessions, study scheduling, exam planning, productivity tracking, daily
goals, streaks, notifications, and backup/restore into one place.

The core idea is:

**Plan → Focus → Study → Achieve**

## ✨ Features

### 📝 Task Management

-   Add, edit and delete tasks
-   Mark tasks as completed
-   Priority and category/subject information
-   Search and filtering
-   Pending/completed task tracking

### 🧠 Focus Mode / Pomodoro

-   25-minute focus session
-   Start, pause and reset
-   Session completion feedback

### 📅 Study Schedule

-   Add study sessions
-   Subject, topic, day and time
-   Day-wise filtering
-   Smart/upcoming schedule
-   Start Focus from a scheduled session
-   Total study-time tracking

### 📝 Exam Planner

-   Add upcoming exams
-   Store exam details
-   Track upcoming examinations

### 📊 Productivity Analytics

-   Completed-task statistics
-   Study-hour tracking
-   Focus-session tracking
-   Productivity progress
-   Recent activity

### 🔥 Daily Goals & Streak

-   Daily productivity goals
-   Progress tracking
-   Study streaks

### 🔔 Smart Notifications

-   Study-session reminders
-   Android notification support
-   Notification permission handling

### 💾 Backup & Restore

-   Create backup files
-   Restore from backup
-   JSON backup format
-   Android document/file picker

### 🤖 AI Study Assistant

If implemented in the final version, this module can provide
personalized study plans, study suggestions and academic assistance.

### 🏆 Achievement System

If implemented in the final version, this module can provide badges for
milestones such as study streaks, focus sessions, study hours and
completed tasks.

> **Important:** Keep the AI and Achievement sections under Features
> only if they are actually implemented in your final GitHub code.
> Otherwise move them to Future Scope.

## 🛠️ Technology Stack

  Technology              Purpose
  ----------------------- --------------------
  Kotlin                  Application logic
  XML                     User interface
  Android Studio          Development
  SQLite                  Local data storage
  RecyclerView            Dynamic lists
  CardView                UI cards
  Intent                  Screen navigation
  CountDownTimer          Focus timer
  Android Notifications   Study reminders
  JSON                    Backup format

## 🏗️ Modules

``` text
Student Productivity Planner
│
├── Task Management
├── Focus / Pomodoro
├── Study Schedule
│   ├── Day-wise Schedule
│   └── Smart Schedule
├── Exam Planner
├── Productivity Analytics
├── Daily Goals & Streak
├── Smart Notifications
└── Backup & Restore
```

## 🔄 Workflow

``` text
Add Task / Exam
      ↓
Plan Study Time
      ↓
Focus Session
      ↓
Complete Tasks
      ↓
Track Progress
      ↓
Analytics & Goals
```

## 🗂️ Project Structure

``` text
app/
└── src/
    └── main/
        ├── java/com/example/studentproductivityplanner/
        │   ├── MainActivity.kt
        │   ├── AddTaskActivity.kt
        │   ├── Task.kt
        │   ├── DatabaseHelper.kt
        │   ├── TaskAdapter.kt
        │   ├── FocusActivity.kt
        │   ├── ScheduleActivity.kt
        │   ├── AddScheduleActivity.kt
        │   ├── StudySchedule.kt
        │   ├── DatabaseHelperSchedule.kt
        │   ├── ScheduleAdapter.kt
        │   ├── Exam Planner files
        │   ├── Analytics files
        │   ├── Goals/Streak files
        │   ├── Notification files
        │   └── BackupActivity.kt
        │
        └── res/
            ├── layout/
            ├── drawable/
            ├── mipmap/
            └── values/
```

The exact file list can differ in the final version.

## 💾 Data Storage

SQLite is used for local application data. Depending on the final
implementation, stored information includes tasks, schedules, exams and
productivity-related data.

Backup data is exported as JSON through Android's document picker.

## 🔐 Android Features

The application uses Android components such as Activities, Intents,
RecyclerView, SQLite, CountDownTimer, notifications and the system
document/file picker.

Only permissions required by the implemented features should be included
in the final project.

## 📊 Approximate Code Size

The project is expected to contain approximately:

**5,000--7,000 lines of Kotlin and XML combined.**

This is an estimate, not an exact measurement. The final count depends
on comments, blank lines, resources and the final implementation.

For an exact count, measure the source files in the final GitHub
repository and exclude generated/build files.

## 🚀 How to Run

### Requirements

-   Android Studio
-   Android SDK
-   Kotlin
-   Android device or emulator

### Steps

1.  Clone/download the repository.
2.  Open it in Android Studio.
3.  Allow Gradle sync to complete.
4.  Connect an Android device or start an emulator.
5.  Select the `app` configuration.
6.  Click **Run ▶**.

## 🧪 Testing Checklist

-   [ ] Add/edit/delete task
-   [ ] Complete task
-   [ ] Search/filter tasks
-   [ ] Start/pause/reset focus timer
-   [ ] Add and delete study schedule
-   [ ] Day-wise schedule filtering
-   [ ] Smart/upcoming schedule
-   [ ] Exam planner
-   [ ] Productivity analytics
-   [ ] Daily goals and streak
-   [ ] Notifications
-   [ ] Backup
-   [ ] Restore
-   [ ] App icon
-   [ ] Normal-use crash testing

## 🔮 Future Scope

-   AI-powered personalized study planning
-   Cloud synchronization
-   Student login/accounts
-   Cross-device synchronization
-   Advanced recommendation engine
-   PDF productivity reports
-   Advanced gamification and achievements
-   More detailed analytics
-   Additional customization

## 🎓 Academic Project

**Project:** Student Productivity Planner\
**Platform:** Android\
**Language:** Kotlin\
**Database:** SQLite\
**IDE:** Android Studio\
**Developer:** AMAN RAJ

This project demonstrates practical skills in Android UI development,
Kotlin programming, SQLite database management, navigation,
notifications, productivity tracking and data management.

## 📌 GitHub Description

> Student Productivity Planner --- A Kotlin-based Android app for
> managing student tasks, study schedules, focus sessions, exams,
> productivity analytics, goals, notifications, and backup/restore.

## 🏷️ Suggested GitHub Topics

`android` `kotlin` `android-studio` `sqlite` `student-productivity`
`task-manager` `pomodoro` `study-planner` `exam-planner`
`productivity-app` `mobile-app-development`

------------------------------------------------------------------------

**Made with Kotlin & Android Studio by AMAN RAJ.**

