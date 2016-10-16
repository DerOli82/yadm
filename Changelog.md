# Changelog

# Version 0.4.3
Fixes save problem
Implement dimension and template grouping. Closes https://github.com/DerOli82/yadm/issues/12

# Version 0.4.2
Switch from gson to minimal-json library
Automatically download dependencies (bytebuddy & minimal-json)
Remove WorldType from default templates (doesn't required)
Fixes some problems in gradle.bat ( by Krakel )
Reworked data structures
Fixes https://github.com/DerOli82/yadm/issues/4
Fixes https://github.com/DerOli82/yadm/issues/6

# Version 0.4.1
Fixes https://github.com/DerOli82/yadm/issues/2
Fixes https://github.com/DerOli82/yadm/issues/3
Rewoked chat command structure
Added permission level

# Version 0.4.0

Fix some Client<->Server problems
Added basic function chat commands:
- delete
- list dimensions
- list providers
- list types
- tp
- info
- reload

# Version 0.3.1

Versions, Versions, Versions...

# Version 0.3.0

First functional version:
- Work with preconfigured patterns ( name, provider, type, [seed], [generatorOptions] ) stored in JSON format. 
- Creating dimensions from preconfigured patterns (default provider).
- Store dimensions information in JSON format into world data.