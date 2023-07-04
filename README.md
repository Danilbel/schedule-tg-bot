# schedule-tg-bot:1.0.0

## About the bot

Bot to track the schedule of KPI pairs in Telegram.
The bot works by getting data from the API of the KPI schedule (https://schedule.kpi.ua/api).

## Technologies used

This project was developed with:

* **Java 17**
* **Spring Boot 2.7.12**
* **Spring Web**
* **Telegrambots 6.7.0**
* **Lombok 1.18.22**
* **GSON 2.10.1**
* **Gradle**
* **Docker**

### Build the project

- Clone the repository
```bash
git clone https://github.com/Danilbel/schedule-tg-bot.git
```

- Enter the project directory
```bash
cd schedule-tg-bot
```

- Write in the `parser.properties` file the data for the timetable API parser to work
```properties
## Semester start date in the format yyyy-MM-dd, for example: 2021-09-01
parser.start.semester.date=
## Semester start week, first or second
parser.start.semester.week=
## Group ID from https://schedule.kpi.ua/
## For example for ТР-11: ce5e9c74-a1ec-49f4-a67c-631e85949c49 
parser.schedule.group.id=
```

- Write in the `bot.properties` file the data of your telegram bot
```properties
## Telegram bot username
bot.username=
## Telegram bot token
bot.token=
```

- Build the project
```bash
./gradlew build
```

After successfully building the project, the `schedule-tg-bot-1.0.0.jar` file will be generated in the `build/libs` directory.

### Build the docker image

```bash
docker build -t schedule-tg-bot:1.0.0 .
```

### Run the docker image

```bash
docker run -d --name schedule-tg-bot schedule-tg-bot:1.0.0
```