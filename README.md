Documentation: https://docs.neoforged.net/docs

# TODO

- Network
  - [x] add new pipe entities to the network
  - [x] sync data to the network
  - [x] create an item routing algorithm
  - [ ] Optimize processing
  - [ ] rebuild network on unload
  - [ ] recalculation on pipe break
  - [ ] recalculation on machine break
  - [x] remove network when empty
  - [ ] remove network on world close

- TileEntity
  - [x] creation on machine placement
  - [x] creation on pipe next to machine placement
  - [x] make block entity respect disabled sides
  - [x] proper deletion of the existing tile entity
  - [ ] proper serialization of filters
  - [x] sign out of network on unload/deletion
  - [ ] GUI fix filter slots
  - [x] GUI add extract/insert button
  - [ ] GUI sync with network

- Block
  - [ ] Make block respect disabled sides
  - [ ] Update connections on disconnect through pipe entity

# BUGS: 
- fix Block connections
- disable insert+extract should delete tile entity if no connections are present
- block model should only update when closing gui (connection gets removed but user can still re-enable connection → desync)
- fix world reload disconnect tile entity extract only connection for tile entity  
- fix insert/extract being the wrong way around
- fix extract limit for item extraction