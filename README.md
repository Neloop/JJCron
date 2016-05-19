# JJCron - Just a Java Cron

[![License](http://img.shields.io/:license-mit-blue.svg)](http://badges.mit-license.org)
[![Wiki](https://img.shields.io/badge/docs-wiki-orange.svg)](https://github.com/JJCron/JJCron/wiki)

**JJCron** is cron-like program written in `Java` which can be used to periodically execute given tasks. **JJCron** can serve as standalone program and also as library used in another application. From the beginning the whole architecture was meant to be modular, easy to use and simple to understand. This is managed by delicate design of internal structures and classes.

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
- All code written in pure `Java` using cutting edge `Java 8` version
- Thanks to `Java` **JJCron** is multiplatform
- Modular design
- Easy to understand and use
- Java classes can be used as tasks and can be scheduled to execution
- Will be further improved to support `RMI` remote management

## Drawbacks
- Not all features from `crontab` standard format are implemented
- Day of week time unit is a bit non-standard with its ordering
- Due to `Java` restrictions only single program can be executed (that means no pipes, redirections or program chaining)
- Test are not written yet!

## More information
If you are interested enough read `JJCron` wiki pages [here](https://github.com/JJCron/JJCron/wiki) and start using it!

## License
[MIT license](LICENSE)