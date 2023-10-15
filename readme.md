# Simple backup utility

## Build
```
gradle clean shadowJar
```
## Launch
```
java -jar backuper.jar /path/to/config.properties
```

## Config
.properties file containing following properties:
```
backup.source.dir=/path/to/directory/to/backup
backup.target.dir=/path/to/directory/to/save/backups/to
backup.file.prefix={prefix for the backup file name}
backup.copy.period={minimum amount of time to pass before creating next copy (seconds)}
backup.check.period={amount of time to pass before checking if next copy should be created (seconds)}
backup.file.ttl={backup files time to live}
```