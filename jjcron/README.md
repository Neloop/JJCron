# JJCron - Just a Java Cron

[![Build Status](https://travis-ci.org/JJCron/JJCron.svg?branch=master)](https://travis-ci.org/JJCron/JJCron)
[![License](http://img.shields.io/:license-mit-blue.svg)](http://badges.mit-license.org)
[![Wiki](https://img.shields.io/badge/docs-wiki-orange.svg)](https://github.com/JJCron/JJCron/wiki)
[![Docs](https://img.shields.io/badge/docs-javadoc-green.svg)](http://JJCron.github.io/JJCron/)

**JJCron** is cron-like program written in `Java` which can be used to periodically execute given tasks. **JJCron** can serve as standalone program and also as library used in another application. From the beginning the whole architecture was meant to be modular, easy to use and simple to understand. This is managed by delicate design of internal structures and classes.

One of the best features of **JJCron** is remote management through **[JJCronRM](https://github.com/JJCron/JJCronRM)** manager. Management includes reloading crontab, save changes to crontab, add cron task, delete cron task, pause whole execution or shutdown whole application.

## Crontab

Tasks for **JJCron** can be supplied with `crontab` configuration file which has standard format. Only restricted functionality of actual `crontab` is supported but basics should be same. In contrast to standard format of configuration seconds are added to time units which can be specified in `crontab` columns.

Basic description of format:
```
 #  ┌──────────── sec (0 - 59)
 #  │ ┌───────────── min (0 - 59) 
 #  │ │ ┌────────────── hour (0 - 23)
 #  │ │ │ ┌─────────────── day of month (1 - 31)
 #  │ │ │ │ ┌──────────────── month (1 - 12)
 #  │ │ │ │ │ ┌───────────────── day of week (1 - 7) (1 = Monday, 7 = Sunday)
 #  │ │ │ │ │ │
 #  │ │ │ │ │ │
 #  * * * * * *  command to execute
```

Also in addition to classic cron programs `Java` classes which extends specified class can be loaded as scheduled tasks. This can be done even in `crontab` using `XML` tags **class**:
```
* * * * * * <class>HelloWorldTask</class>
```

Further description of configuration can be found in wiki on [Crontab Description](https://github.com/JJCron/JJCron/wiki/Crontab-Description) page.

## Main features
- All code written in pure `
- 
- 
- 
- 
- 
- 
- ` using cutting edge `Java 8` version
- Thanks to `Java` **JJCron** is multiplatform
- Modular and lightweight design
- Easy to understand and use
- Java classes can be used as tasks and can be scheduled to execution
- Supports `RMI` remote management through **JJCronRM**

## Drawbacks
- Not all features from `crontab` standard format are implemented
- Day of week time unit is a bit non-standard with its ordering (1 == Monday)
- Due to `Java` restrictions only single program can be executed (that means no pipes, redirections or program chaining)

## More information
If you are interested enough read `JJCron` wiki pages [here](https://github.com/JJCron/JJCron/wiki) and start using it!

## Contribution

This project is open for contribution for anyone who want to make it better. All feasible pull requests will be merged.

## License
JJCron is and always will be licensed under [MIT license](LICENSE)