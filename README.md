# RaspberryLife Server
This is the server of the RaspberryLife home automation project.

## Overview
See [Wiki](https://github.com/RaspberryLife/server/wiki)

## Requirements
Although the server is able to run without a database it is recommended that you install a MySQL database. 
The server will store all the data within this database.

For database access configuration refer to [Configuration](https://github.com/RaspberryLife/server/wiki/Configuration).

## Installation / Build
Download the latest release [here](https://github.com/RaspberryLife/server/releases).
Run `bin/server` or `bin/server.bat` to start the server.

If you want to build the server from source run `gradle distZip` from the command line in the main directory.

## Contributing

1. Fork it!
2. Create your feature branch: `git checkout -b my-new-feature`
3. Commit your changes: `git commit -am 'Added some feature'`
4. Push to the branch: `git push origin my-new-feature`
5. Submit a pull request

## License

* [Apache Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.html)