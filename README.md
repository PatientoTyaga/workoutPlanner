# WorkoutPlanner

## Description

As a gym enthusiast, I've developed WorkoutPlanner to help organize workouts effectively. This simple app allows users to arrange their workouts according to their preferences. If you prefer to target different body parts throughout the week, you can use the custom workout option. Here, each body part is listed with a variety of exercises tailored for a comprehensive workout. For example, you could focus on chest on Monday, legs on Tuesday, and so on.

Alternatively, if you prefer to shock the muscles, as Arnold Schwarzenegger puts it, you can use the randomize option. This feature includes all exercises for each body part available in the custom option but randomly selects two exercises for you to perform. This allows for a diverse workout routine, ensuring your muscles are continuously challenged.

## Features

- **Custom Workouts**: Arrange workouts by targeting specific body parts throughout the week.
- **Randomize Option**: Generate random exercise combinations for a varied and challenging workout routine.
- **Today's Workout and Completed Workouts**: Track your daily workouts and view completed exercises.

### Usage Instructions

1. Initially, the "Today's Workout" and "Completed Workout" cards are greyed out and not clickable.
2. Add workouts to the "Today's Workout" card.
3. Once a workout is added, it becomes clickable.
4. Clicking on a workout reveals a tab view of selected categories with respective exercises.
5. Complete exercises by swiping them towards the right.
6. Mark completed exercises as done when prompted.
7. Completed exercises are moved to the "Completed Workouts" page.
8. Continue workouts until all exercises are completed.
9. "Today's Workout" card dynamically updates with dates.
10. Clear completed exercises by clicking "Clear All."

## Configuration Files Needed

- AndroidManifest.xml
- build.gradle (Project-level and Module-level)
- settings.gradle
- proguard-rules.pro
- res/values/strings.xml
- res/values/colors.xml
- res/layout/*.xml
- res/menu/*.xml
- res/drawable/*.xml
