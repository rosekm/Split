# Split

Split is a desktop app written in Java.
Purpose of this application is to manage information about customers and their assignment to salesman and branch of the company.

To run, it requires an internet connection and set up external MySQL database, where information about Customers is stored. Proper DB creation code is stored in the file ' SplitDataBaseCreateSQL'. After DB creation, before first use, below SQL command needs to be executed. 

_INSERT INTO users (name, surname, machineID, userType) VALUES(/*/,/*/,/*/,'ADMIN')

*'machineID' is machine name on which app is running.

Application is using also BIR1 - Internet Database REGON 1 for which technical documentation is accessible here: https://api.stat.gov.pl/Home/RegonApi?lang=en
