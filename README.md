# JJCron - Just a Java Cron

[![CI](https://github.com/Neloop/JJCron/actions/workflows/ci.yml/badge.svg)](https://github.com/Neloop/JJCron/actions/workflows/ci.yml)
[![License](http://img.shields.io/:license-mit-blue.svg)](http://badges.mit-license.org)
[![Wiki](https://img.shields.io/badge/docs-wiki-orange.svg)](https://github.com/Neloop/JJCron/wiki)

JJCron - Just a Java Cron basic application used for croning. Can be remotely managed with JJCronRM. Originally built
using Ant and Java 8, later on migrated to Maven and Java 11+.

Written as semestral work for *Programming in Java* (2015/2016) course at MFF.

Composed of three parts:

- [JJCronCommon](./jjcron-common) - library for JJCron and JJCronRM which contains shared functionality of them both,
  most importantly interfaces for remote communication
- [JJCron](./jjcron) - cron-like program written in Java which can be used to periodically execute given tasks
- [JJCronRM](./jjcron-rm) - remote management JavaFX application for JJCron instances
