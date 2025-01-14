# WarpPlugin
Basically a copy of [MariaDB-Warp-Plugin](https://github.com/Hecker-01/MariaDB-Warp-Plugin), but is more optimized, has more features, and is more user-friendly by being able to configure a lot of things in the config.yml file.

## Setup
To set up the database, you need to run the following command in the MariaDB console.
```mariadb
CREATE DATABASE IF NOT EXISTS WarpsDatabase;
```

You can also use other database names, but you need to change the database name in the config.yml file.

```yml
database:
  ip: localhost
  port: 3306
  database-name: WarpsDatabase
  username: root
  password: mypass
file-version: 1.0
```

## MariaDB on Docker
The command i use to run MariaDB on Docker.
```bash
docker search mariadb
docker pull mariadb:10.5
docker run --name mariadbtest -e MYSQL_ROOT_PASSWORD=mypass -p 3306:3306  -d docker.io/library/mariadb:10.5
```
Stop and start the container.
```bash
docker stop mariadbtest
```
```bash
docker start mariadbtest
```