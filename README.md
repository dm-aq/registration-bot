
[![License](https://img.shields.io/badge/license-MIT-green.svg)](https://github.com/dm-aq/registration-bot/blob/main/LICENSE)


This telegram bot application was designed for registration process in annual dance event in order to replace registration with google forms.
It asks customer a few questions to collect the following data:
1. Mobile phone number
2. Full name
3. Email
4. Gender
5. Preferred room category
6. Dance style
7. Roommates

All answers persist to bot’s database.
It is possible for user to remove draft in each step of a registration process.
A registration request can be exported to google spread sheet at the end of the registration flow.

Originally application was created with Russian locale, but now it has English one as well.

## Architectural overview
[State](src/main/kotlin/ru/registration/bot/engine/commands/flow/State.kt) is a base interface representing one interaction with the bot. In other words the bot should ask a question and handle an answer. State interface has a number of implementations for asking and handling several questions to get information we mentioned above. There are a few technical states for export request to a google spread sheet.
Full status model is presented in [StateType](src/main/kotlin/ru/registration/bot/engine/commands/flow/StateType.kt) class. This is the list of different statutes which will be persisted to the database during passing through registration flow.

## Quick start
First of all in order to use the application you have to register your own telegram bot via [@BotFather](https://telegram.me/BotFather) and get username and token for new bot

Put telegram username and token to appropriate parameters (bot.username and bot.token) in [application-local.yml](src/main/resources/application-local.yml)

Create [google service account](https://cloud.google.com/iam/docs/service-accounts) if you want to export a registration request to google spread sheets

Put credentials json to google-account-credentials.json file in the resource directory.

Fill spread sheets parameters in [application-local.yml](src/main/resources/application-local.yml). Here is an example how you can get this
spread sheet uri example:
https://docs.google.com/spreadsheets/u/0/d/02SDYgr_3COrRh8rl-fd-H46nAPLR1qgscojYzLoDmap

```yaml
services:
    sheets:
        spread-sheet-id: "02SDYgr_3COrRh8rl-fd-H46nAPLR1qgscojYzLoDmap"
        range: "<list-name>"
```

<list-name> is the name of the spread sheet list in the left bottom corner of the web page. 

Start [docker-compose.yml](docker-compose.yml) in order to launch database.

So we are ready to start the application.
Execute following command:

```bash
./gradlew bootRun --args='--spring.profiles.active=local'
```

Enjoy!