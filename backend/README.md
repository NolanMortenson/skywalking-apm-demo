# Installation & Setup

## Install PostgreSQL

`brew install postgresql`

`brew services start postgresql`

Check that postgres is running with `brew services`

## Create `forecast-app` database

Enter the PostgreSQL terminal: `psql -d postgres`

Create the database: `CREATE DATABASE "forecast-app";`

