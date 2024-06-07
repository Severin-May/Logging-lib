# Logger Library
### Usage:
To use this library you need to have a configuration file 
available at the following path "src/main/resources/log-config.json".
The format of the config file is: 
```json
{
  "consoleEnabled": true,
  "consoleMessageFormat" : {
    "showDate": true,
    "showMessage": true,
    "showLevel": true
  },
  "consoleLogLevel": "DEBUG",
  "logIntoFileEnabled": true,
  "logFileCapacity": 200,
  "fileLogLevel": "DEBUG",
  "fileMessageFormat" : {
    "showDate": true,
    "showMessage": true,
    "showLevel": true
  }
}
```
Note that the json objects' values can be left empty in which
case the default values will be used.

Singleton design pattern was used for the implementation. 

The log output files can be found at "src/main/resources/log-outputs".
At the beginning of each execution all log files under "log-outputs" folder
get deleted. Therefore, it is user's responsibility to save the generated
log files if necessary.

### Configurations reference:
- consoleEnabled - enable logging into console
- consoleMessageFormat.showDate - logs date (console) if set true
- consoleMessageFormat.showMessage - logs message (console) if set true
- consoleMessageFormat.showLevel - logs level (console) if set true
- consoleLogLevel - choose from [DEBUG, INFO, WARNING, ERROR] (capital letters)
- logIntoFileEnabled - enable logging into file
- logFileCapacity - maximum number of lines per log file
- fileLogLevel - choose from [DEBUG, INFO, WARNING, ERROR] (capital letters)
- fileMessageFormat - (see consoleMessageFormat)