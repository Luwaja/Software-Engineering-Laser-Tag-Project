# Software-Engineering-Laser-Tag-Project
Shared repository for project in Software Engineering class

Sprint 2 ✔
+ Create splash screen
+ Create player entry screen
+ Link database to application, add two players via application

Sprint 3 ✔
+ Create play action display
    - (No events yet, make sure to have the players showing up in the team windows)
+ Code up f5 key to switch to play action display and start game
+ Code up game start count-down timer

Sprint 4 🆕
+ Add network sockets
+ Add random music track selection
+ Run game using traffic generator (provided by instructor)

=============================Instructions=============================
1. Download the postgresql JDBC driver located here: https://jdbc.postgresql.org/download/ and place it the folder
2. Download the "Audio Tracks" folder specifically that holds the WAV files of the provided audio tracks here: [https://drive.google.com/drive/folders/143YStrufI2cf6j4u38x3QSyIjS0c_3ph?usp=sharing](https://drive.google.com/drive/folders/1TAvFLPZl094ZI7TIoqPCADasEc_0conr?usp=sharing)
3. Manually load the driver with the program, this is done with the following commands:
    1. javac LasaerTag.java Player.java
    2. java -cp "PATH TO YOUR FOLDER\postgres-42.5.4.jar;PATH TO YOUR FOLDER" LaserTag
3. Enter a player ID into the ID textbox and the given code name, and be sure to press the enter key for EACH player. Once filled out press start game or hit f5 for the program to insert its values in the  database.
5. NOTE: if you want a fresh experience the database reset button must be hit in heroku.
6. After the GUI switches to the play action, run the given traffic generator. Be sure you are in the correct directory, and enter the command "python3 trafficgenerator.py". You will then see the events shown on the screen as the scores are updated.
