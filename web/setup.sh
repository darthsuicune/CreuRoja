#!/bin/bash

function writeEnvironment {
	mkdir -p config;
	echo "#Definition for $1 environment" >> config/database.yml
	echo "$1:" >> config/database.yml
	if [ $2 = "mysql" ]; then
		echo "  adapter: mysql2" >> config/database.yml
		echo "  encoding: utf8" >> config/database.yml
		echo "  database: $3" >> config/database.yml
		echo "  username: $4" >> config/database.yml
		echo "  password: $5" >> config/database.yml
		echo "  socket: /tmp/mysql.sock" >> config/database.yml
	else
		echo "  adapter: sqlite3" >> config/database.yml
		echo "  database: db/$1.sqlite3" >> config/database.yml
	fi
	echo "  pool: 5" >> config/database.yml
	echo "  timeout: 5000" >> config/database.yml
	echo "" >> config/database.yml
}

function setupConnection {
	writeEnvironment "development" "sqlite3"
	writeEnvironment "test" "sqlite3"
	writeEnvironment "production" "mysql" $1 $2 $3
}

function setupData {
	rake db:migrate;
	echo "user = User.new" > setup.rb;
	echo "user.name = \"Admin\""  >> setup.rb;
	echo "user.surname = \"User\"" >> setup.rb;
	echo "user.email = \"$1\"" >> setup.rb;
	echo "user.password = \"$2\"" >> setup.rb;
	echo "user.password_confirmation = \"$2\"" >> setup.rb;
	echo "user.role = \"admin\"" >> setup.rb;
	echo "user.save" >> setup.rb;
	bundle exec rails runner "eval(File.read 'setup.rb')";
	rm setup.rb
}

function setupDb {
	setupConnection $1 $2 $3;
	setupData $4 $5;
}

## Receives $1 as the environment for installation
function setEnvironmentVariables {
	PWD=`pwd`
	GEM_FOLDER=$PWD/gems
	if [ ! -d $GEM_FOLDER ]; then
		mkdir -p $GEM_FOLDER
	fi;
	echo "Exporting Gems to $GEM_FOLDER"
	export GEM_PATH=$GEM_FOLDER
	export GEM_HOME=$GEM_FOLDER
	echo "export GEM_PATH=$GEM_FOLDER" >> .bashrc
	echo "export GEM_HOME=$GEM_FOLDER" >> .bashrc

	export RAILS_ENV $1
}

#RETURN CODES:
#	0: Success
#	1: Incorrect parameter count
#	2: bundle failed, make sure all packages are in place.


if [ "$#" -ne 5 ]; then
	echo "Usage: $0 <database name> <database user> <database password> <admin email> <admin password> <environment>";
	exit 1;
fi;

setEnvironmentVariables $6;

bundle install

if [ $? -ne 0 ]
then
	echo "Bundle failed, please, make sure sqlite3, libsqlite3-dev, libnotify4, libmysql-ruby and libmysqlclient-dev are installed and try again";
	exit 2;
fi;

DB_NAME="$1";
DB_USER="$2";
DB_PASS="$3";
ADMIN_EMAIL="$4";
ADMIN_PASS="$5";

setupDb $DB_NAME $DB_USER $DB_PASS $ADMIN_EMAIL $ADMIN_PASS;



