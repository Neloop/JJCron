# JJCron

JJCron - Just a Java Cron basic application used for croning. Can be remotely managed with JJCronRM. Built using Maven and Java 11+.

Written as semestral work for *Programming in Java* (2015/2016) course at MFF.

Composed of three parts:

- [JJCronCommon](./jjcron-common) - library for JJCron and JJCronRM which contains shared functionality of them both, most importantly interfaces for remote communication
- [JJCron](./jjcron) - cron-like program written in Java which can be used to periodically execute given tasks
- [JJCronRM](./jjcron-rm) - remote management JavaFX application for JJCron instances
