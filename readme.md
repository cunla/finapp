Personal Finance Management Server
===============================

Configuration
----------------
Under `src/main/resources/config` you can find several spring-boot profiles. Edit a profile file or create your own before compiling the webapp.

Use `mvn package` to compile the server.

Run `java -jar target/finapp.war --spring.profiles.active={profile}`

Troubleshooting
---------------
When running on Ubuntu machines, make sure to install the package haveged
`apt-get install haveged -y`

waffle
------
If you want to contribute to this project write to style.daniel@gmail.com

[![Stories in Ready](https://badge.waffle.io/cunla/finapp.png?label=ready&title=Ready)](http://waffle.io/cunla/finapp)
