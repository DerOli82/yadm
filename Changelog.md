# Changelog

# Version 0.6.5
- Fixes TravelSetting error https://github.com/DerOli82/yadm/issues/26
- Remove SpawnPoint interceptor and override getSpawnPoint in generic WolrdServer
- Ugly workaround to prevent loading "vanilla" WorldServer for YADM Dimensions

# Version 0.6.4
- Fix client<->server spawnpoint interception error 

# Version 0.6.3
- Intercept WorldProvider.getSpawnPoint
- Implement TPto Command
- No events inside border
- Replace javax.vecmath.Vector2d

# Version 0.6.2
- Fix CommandBlock PlayerSelector problem

# Version 0.6.1
- WorldGuard blocks explosion block damage

# Version 0.6.0
- Implement WorldBorderEvent priority
- Fixes knockback issue ( https://github.com/DerOli82/yadm/issues/22 )
- Implement WorldBorder TravelSetting
- Implement WorldGuardSetting
- Implement better dependencies download/update mechanism
- Implement Interfaces for more structure ( see https://github.com/DerOli82/yadm/blob/master/uml/SimplifiedComponentsDiagram.png )
- Implement command permissions (Operator, Owner & Player )
- Options to lock the whitelist
- Delete dimension json file if empty
- Implement teleport modifier

# Version 0.5.0
- Hopefully fixed worldtime per dimension problems
- Fix random deleted worlds
- Plausibility check on setting deserialization
- Implement Random Seed (SeedSetting with * value)
- Implement dimension owner
- Implement world border (knockback & message action) 
- Implement default safe spawn
- Implement dimension player whitelist setting
- Implement PlayerManager
- implement ManageCommandGroup & WhitelistCommand

# Version 0.4.6
- Fix some command block issues	
- Reimplement delete function

# Version 0.4.5
- Fixes Teleport with coordinates doesn't work
- Implement CommandParser Class
- First (not functional) WorldBorderSetting structure

# Version 0.4.4
- Update Forge to 1.7.10-10.13.4.1614-1.7.10
- Fixes save problem

# Version 0.4.3
- Fixes save problem
- Implement dimension and template grouping. Closes https://github.com/DerOli82/yadm/issues/12

# Version 0.4.2
- Switch from gson to minimal-json library
- Automatically download dependencies (bytebuddy & minimal-json)
- Remove WorldType from default templates (doesn't required)
- Fixes some problems in gradle.bat ( by Krakel )
- Reworked data structures
- Fixes https://github.com/DerOli82/yadm/issues/4
- Fixes https://github.com/DerOli82/yadm/issues/6

# Version 0.4.1
- Fixes https://github.com/DerOli82/yadm/issues/2
- Fixes https://github.com/DerOli82/yadm/issues/3
- Reworked chat command structure
- Added permission level

# Version 0.4.0

- Fix some Client<->Server problems
- Added basic function chat commands:
- delete
- list dimensions
- list providers
- list types
- tp
- info
- reload

# Version 0.3.1

- Versions, Versions, Versions...

# Version 0.3.0

- First functional version:
- Work with preconfigured patterns ( name, provider, type, [seed], [generatorOptions] ) stored in JSON format. 
- Creating dimensions from preconfigured patterns (default provider).
- Store dimensions information in JSON format into world data.
