# libmc

A lightweight minecraft client-side protocol library for Kotlin Native and JVM

# Supported targets

| Platform    | Target                | Support Tier         |
|:------------|:----------------------|:---------------------|
| **Java**    | JVM 1.8               | **Mainly Supported** |
| **Windows** | MinGW X64             | **Mainly Supported** |
| **Linux**   | Linux X64             | **Mainly Supported** |
|             | Linux ARM64           | **Not Tested**       |
| **macOS**   | macOS ARM64 (Silicon) | **Not Tested**       |

# Minecraft Protocol Library Status

## Implementation details

- [x] **Online Mode Authentication & Encryption/Decryption**: See [Embedded cryptography](docs/Embedded-cryptography.md)
- [x] **Structured `TextComponent` Parser**: TextComponent AST decoder
- [x] **Command Tree Parser**: Full binary graph decoder for brigadier nodes, argument types, and suggestions
- [ ] **Recipe Book & Recipe Data**: Recipe layout declarations and client-side recipe settings
- [ ] **Chunk & World Data**: Level Chunk Data with Light decoder (Paletted Containers, Direct/Indirect Palettes)
- [ ] **Light Engine Update**: Sky & Block light nibble array parser
- [ ] **Explosion Event Decoder**: Knockback vectors and destroyed block offsets array
- [ ] **Debug Packets Parsing**: Debug subs, block/entity states, and game performance sample events
- [ ] **Particle Parsing**
- [ ] **Slot Data Parsing**

---

## Protocol Packet Codecs Mapping

<details>
<summary>Click to expand</summary>

### 1. Status Stage

#### Clientbound

- [x] `0x00` Status Response (`ClientboundStatusResponsePacket`)
- [x] `0x01` Pong Response (`ClientboundPongResponsePacket`)

#### Serverbound

- [x] `0x00` Status Request (`ServerboundStatusRequestPacket`)
- [x] `0x01` Ping Request (`ServerboundPingRequestPacket`)

---

### 2. Handshake Stage

#### Serverbound

- [x] `0x00` Handshake (`ServerboundHandshakePacket`)

---

### 3. Login Stage

#### Clientbound

- [x] `0x00` Disconnect (`ClientboundDisconnectLoginPacket`)
- [x] `0x01` Encryption Request (`ClientboundHelloPacket`)
- [x] `0x02` Login Success (`ClientboundLoginSuccessPacket`)
- [x] `0x03` Set Compression (`ClientboundSetCompressionPacket`)
- [x] `0x04` Login Plugin Request (`ClientboundCustomQueryPacket`)
- [x] `0x05` Cookie Request (`ClientboundCookieRequestPacket`)

#### Serverbound

- [x] `0x00` Login Start (`ServerboundLoginStartPacket`)
- [x] `0x01` Encryption Response (`ServerboundKeyPacket`)
- [x] `0x02` Login Plugin Response (`ServerboundCustomQueryAnswerPacket`)
- [x] `0x03` Login Acknowledged (`ServerboundLoginAcknowledgedPacket`)
- [x] `0x04` Cookie Response (`ServerboundCookieResponsePacket`)

---

### 4. Configuration Stage

#### Clientbound

- [x] `0x00` Cookie Request (`ClientboundCookieRequestPacket`)
- [x] `0x01` Custom Payload (`ClientboundCustomPayloadPacket`)
- [x] `0x02` Disconnect (`ClientboundDisconnectConfigurationPacket`)
- [x] `0x03` Finish Configuration (`ClientboundFinishConfigurationPacket`)
- [x] `0x04` Keep Alive (`ClientboundKeepAliveConfigurationPacket`)
- [x] `0x05` Ping (`ClientboundPingConfigurationPacket`)
- [x] `0x06` Reset Chat (`ClientboundResetChatPacket`)
- [x] `0x07` Registry Data (`ClientboundRegistryDataPacket`)
- [x] `0x08` Remove Resource Pack (`ClientboundRemoveResourcePackPacket`)
- [x] `0x09` Add Resource Pack (`ClientboundAddResourcePackPacket`)
- [x] `0x0A` Store Cookie (`ClientboundStoreCookiePacket`)
- [x] `0x0B` Transfer (`ClientboundTransferPacket`)
- [x] `0x0C` Update Enabled Features (`ClientboundUpdateEnabledFeaturesPacket`)
- [x] `0x0D` Update Tags (`ClientboundUpdateTagsPacket`)
- [x] `0x0E` Select Known Packs (`ClientboundSelectKnownPacksPacket`)
- [x] `0x0F` Custom Report Details (`ClientboundCustomReportDetailsPacket`)
- [x] `0x10` Server Links (`ClientboundServerLinksPacket`)
- [x] `0x11` Clear Dialog (`ClientboundClearDialogPacket`)
- [x] `0x12` Show Dialog (`ClientboundConfigurationShowDialogPacket`)
- [x] `0x13` Code Of Conduct (`ClientboundCodeOfConductPacket`)

#### Serverbound

- [x] `0x00` Client Information (`ServerboundCookieResponsePacket`)
- [x] `0x01` Cookie Response (`ServerboundCookieResponsePacket`)
- [x] `0x02` Plugin Message (`ServerboundCustomPayloadPacket`)
- [x] `0x03` Finish Configuration Acknowledged (`ServerboundAckFinishConfigurationPacket`)
- [x] `0x04` Keep Alive Response (`ServerboundKeepAliveConfigurationPacket`)
- [x] `0x05` Pong Response (`ServerboundPongConfigurationPacket`)
- [x] `0x07` Select Known Packs (`ServerboundSelectKnownPacksPacket`)
- [x] `0x08` Custom Click Action (`ServerboundCustomClickActionPacket`)
- [x] `0x09` Accept Code Of Conduct (`ServerboundAcceptCodeOfConductPacket`)

---

### 5. Play Stage

#### Clientbound

- [x] `0x00` Delimiter (`ClientboundDelimiterPacket`)
- [x] `0x01` Spawn Entity (`ClientboundSpawnEntityPacket`)
- [x] `0x02` Entity Animation (`ClientboundEntityAnimationPacket`)
- [x] `0x03` Award Statistics (`ClientboundAwardStatisticsPacket`)
- [x] `0x04` Acknowledge Block Change (`ClientboundAcknowledgeBlockChangePacket`)
- [x] `0x05` Block Destruction (`ClientboundBlockDestructionPacket`)
- [x] `0x06` Block Entity Data (`ClientboundBlockEntityDataPacket`)
- [x] `0x07` Block Event (`ClientboundBlockEventPacket`)
- [x] `0x08` Block Update (`ClientboundBlockUpdatePacket`)
- [x] `0x09` Boss Event (`ClientboundBossEventPacket`)
- [x] `0x0A` Change Difficulty (`ClientboundChangeDifficultyPacket`)
- [x] `0x0B` Chunk Batch Finished (`ClientboundChunkBatchFinishedPacket`)
- [x] `0x0C` Chunk Batch Start (`ClientboundChunkBatchStartPacket`)
- [x] `0x0D` Chunks Biomes (`ClientboundChunksBiomesPacket`)
- [x] `0x0E` Clear Titles (`ClientboundClearTitlesPacket`)
- [x] `0x0F` Command Suggestions (`ClientboundCommandSuggestionsPacket`)
- [x] `0x10` Commands (`ClientboundCommandsPacket`)
- [x] `0x11` Container Close (`ClientboundContainerClosePacket`)
- [ ] `0x12` Container Set Content (`ClientboundContainerSetContentPacket`)
- [x] `0x13` Container Set Data (`ClientboundContainerSetDataPacket`)
- [ ] `0x14` Container Set Slot (`ClientboundContainerSetSlotPacket`)
- [x] `0x15` Cookie Request (`ClientboundCookieRequestPacket`)
- [x] `0x16` Cooldown (`ClientboundCooldownPacket`)
- [x] `0x17` Custom Chat Completions (`ClientboundCustomChatCompletionsPacket`)
- [x] `0x18` Custom Payload (`ClientboundCustomPayloadPacket`)
- [x] `0x19` Damage Event (`ClientboundDamageEventPacket`)
- [ ] `0x1A` Debug Block Value (`ClientboundDebugBlockValuePacket`)
- [ ] `0x1B` Debug Chunk Value (`ClientboundDebugChunkValuePacket`)
- [ ] `0x1C` Debug Entity Value (`ClientboundDebugEntityValuePacket`)
- [ ] `0x1D` Debug Event (`ClientboundDebugEventPacket`)
- [ ] `0x1E` Debug Sample (`ClientboundDebugSamplePacket`)
- [x] `0x1F` Delete Chat (`ClientboundDeleteChatPacket`)
- [x] `0x20` Disconnect (`ClientboundDisconnectPlayPacket`)
- [x] `0x21` Disguised Chat Message (`ClientboundDisguisedChatMessagePacket`)
- [x] `0x22` Entity Event (`ClientboundEntityEventPacket`)
- [x] `0x23` Teleport Entity (`ClientboundTeleportEntityPacket`)
- [ ] `0x24` Explode (`ClientboundExplodePacket`)
- [x] `0x25` Unload Chunk (`ClientboundUnloadChunkPacket`)
- [x] `0x26` Game Event (`ClientboundGameEventPacket`)
- [x] `0x27` Game Rule Values (`ClientboundGameRuleValuesPacket`)
- [x] `0x28` Test Highlight Position (`ClientboundTestHighlightPositionPacket`)
- [x] `0x29` Open Horse Screen (`ClientboundOpenHorseScreenPacket`)
- [x] `0x2A` Hurt Animation (`ClientboundHurtAnimationPacket`)
- [x] `0x2B` Initialize World Border (`ClientboundInitializeWorldBorderPacket`)
- [x] `0x2C` Keep Alive (`ClientboundKeepAlivePlayPacket`)
- [ ] `0x2D` Level Chunk Update With Light (`ClientboundLevelChunkUpdateWithLightPacket`)
- [x] `0x2E` Level Event (`ClientboundLevelEventPacket`)
- [ ] `0x2F` Particle (`ClientboundParticlePacket`)
- [ ] `0x30` Light Update (`ClientboundLightUpdatePacket`)
- [x] `0x31` Login Play (`ClientboundLoginPlayPacket`)
- [x] `0x32` Low Disk Space Warning (`ClientboundLowDiskSpaceWarningPacket`)
- [x] `0x33` Map Item Data (`ClientboundMapItemDataPacket`)
- [ ] `0x34` Merchant Offers (`ClientboundMerchantOffersPacket`)
- [x] `0x35` Update Entity Position (`ClientboundUpdateEntityPositionPacket`)
- [x] `0x36` Update Entity Position and Rotation (`ClientboundUpdateEntityPositionAndRotationPacket`)
- [x] `0x37` Move Minecart Along Track (`ClientboundMoveMinecartAlongTrackPacket`)
- [x] `0x38` Update Entity Rotation (`ClientboundUpdateEntityRotationPacket`)
- [x] `0x39` Move Vehicle (`ClientboundMoveVehiclePacket`)
- [x] `0x3A` Open Book (`ClientboundOpenBookPacket`)
- [x] `0x3B` Open Screen (`ClientboundOpenScreenPacket`)
- [x] `0x3C` Open Sign Editor (`ClientboundOpenSignEditorPacket`)
- [x] `0x3D` Ping (`ClientboundPingPacket`)
- [x] `0x3E` Pong Response (`ClientboundPongResponsePacket`)
- [ ] `0x3F` Place Ghost Recipe (`ClientboundPlaceGhostRecipePacket`)
- [x] `0x40` Player Abilities (`ClientboundPlayerAbilitiesPacket`)
- [x] `0x41` Player Chat Message (`ClientboundPlayerChatMessagePacket`)
- [x] `0x42` Player End Combat (`ClientboundPlayerEndCombatPacket`)
- [x] `0x43` Player Enter Combat (`ClientboundPlayerEnterCombatPacket`)
- [x] `0x44` Player Combat Death (`ClientboundPlayerCombatDeathPacket`)
- [x] `0x45` Player Info Remove (`ClientboundPlayerInfoRemovePacket`)
- [x] `0x46` Player Info Update (`ClientboundPlayerInfoUpdatePacket`)
- [x] `0x47` Player Look At (`ClientboundPlayerLookAtPacket`)
- [x] `0x48` Synchronize Player Position (`ClientboundSynchronizePlayerPositionPacket`)
- [x] `0x49` Player Rotation (`ClientboundPlayerRotationPacket`)
- [ ] `0x4A` Recipe Book Add (`ClientboundRecipeBookAddPacket`)
- [ ] `0x4B` Recipe Book Remove (`ClientboundRecipeBookRemovePacket`)
- [ ] `0x4C` Recipe Book Settings (`ClientboundRecipeBookSettingsPacket`)
- [x] `0x4D` Remove Entities (`ClientboundRemoveEntitiesPacket`)
- [x] `0x4E` Remove Entity Effect (`ClientboundRemoveEntityEffectPacket`)
- [x] `0x4F` Reset Score (`ClientboundResetScorePacket`)
- [x] `0x50` Remove Resource Pack (`ClientboundRemoveResourcePackPacket`)
- [x] `0x51` Add Resource Pack (`ClientboundAddResourcePackPacket`)
- [x] `0x52` Respawn (`ClientboundRespawnPacket`)
- [x] `0x53` Set Head Rotation (`ClientboundSetHeadRotationPacket`)
- [x] `0x54` Update Section Block (`ClientboundUpdateSectionBlockPacket`)
- [x] `0x55` Select Advancements Tab (`ClientboundSelectAdvancementsTabPacket`)
- [x] `0x56` Server Data (`ClientboundServerDataPacket`)
- [x] `0x57` Set Action Bar Text (`ClientboundSetActionBarTextPacket`)
- [x] `0x58` Set Border Center (`ClientboundSetBorderCenterPacket`)
- [x] `0x59` Set Border Leap Size (`ClientboundSetBorderLeapSizePacket`)
- [x] `0x5A` Set Border Size (`ClientboundSetBorderSizePacket`)
- [x] `0x5B` Set Border Warning Delay (`ClientboundSetBorderWarningDelayPacket`)
- [x] `0x5C` Set Border Warning Distance (`ClientboundSetBorderWarningDistancePacket`)
- [x] `0x5D` Set Camera (`ClientboundSetCameraPacket`)
- [x] `0x5E` Set Center Chunk (`ClientboundSetCenterChunkPacket`)
- [x] `0x5F` Set Render Distance (`ClientboundSetRenderDistancePacket`)
- [ ] `0x60` Set Cursor Item (`ClientboundSetCursorItemPacket`)
- [x] `0x61` Set Default Spawn Position (`ClientboundSetDefaultSpawnPositionPacket`)
- [x] `0x62` Set Display Objective (`ClientboundSetDisplayObjectivePacket`)
- [ ] `0x63` Set Entity Metadata (`ClientboundSetEntityMetadataPacket`)
- [x] `0x64` Link Entities (`ClientboundLinkEntitiesPacket`)
- [x] `0x65` Set Entity Velocity (`ClientboundSetEntityVelocityPacket`)
- [ ] `0x66` Set Equipment (`ClientboundSetEquipmentPacket`)
- [x] `0x67` Set Experience (`ClientboundSetExperiencePacket`)
- [x] `0x68` Set Health (`ClientboundSetHealthPacket`)
- [x] `0x69` Set Carried Item (`ClientboundSetCarriedItemPacket`)
- [x] `0x6A` Update Objective (`ClientboundUpdateObjectivePacket`)
- [x] `0x6B` Set Passengers (`ClientboundSetPassengersPacket`)
- [ ] `0x6C` Set Player Inventory Slot (`ClientboundSetPlayerInventorySlotPacket`)
- [ ] `0x6D` Set Player Team (`ClientboundSetPlayerTeamPacket`)
- [x] `0x6E` Update Score (`ClientboundUpdateScorePacket`)
- [x] `0x6F` Set Simulation Distance (`ClientboundSetSimulationDistancePacket`)
- [x] `0x70` Set Subtitle Text (`ClientboundSetSubtitleTextPacket`)
- [x] `0x71` Set Time (`ClientboundSetTimePacket`)
- [x] `0x72` Set Title Text (`ClientboundSetTitleTextPacket`)
- [x] `0x73` Set Title Animation Times (`ClientboundSetTitleAnimationTimesPacket`)
- [x] `0x74` Entity Sound Effect (`ClientboundEntitySoundEffectPacket`)
- [x] `0x75` Sound Effect (`ClientboundSoundEffectPacket`)
- [x] `0x76` Start Configuration (`ClientboundStartConfigurationPacket`)
- [x] `0x77` Stop Sound (`ClientboundStopSoundPacket`)
- [x] `0x78` Store Cookie (`ClientboundStoreCookiePacket`)
- [x] `0x79` System Chat Message (`ClientboundSystemChatMessagePacket`)
- [x] `0x7A` Set Tab List Header And Footer (`ClientboundSetTabListHeaderAndFooterPacket`)
- [x] `0x7B` Tag Query Response (`ClientboundTagQueryResponsePacket`)
- [x] `0x7C` Pickup Item (`ClientboundPickupItemPacket`)
- [x] `0x7D` Synchronize Vehicle Position (`ClientboundSynchronizeVehiclePositionPacket`)
- [x] `0x7E` Test Instance Block Status (`ClientboundTestInstanceBlockStatusPacket`)
- [x] `0x7F` Set Ticking State (`ClientboundSetTickingStatePacket`)
- [x] `0x80` Step Tick (`ClientboundStepTickPacket`)
- [x] `0x81` Transfer (`ClientboundTransferPacket`)
- [ ] `0x82` Update Advancements (`ClientboundUpdateAdvancementsPacket`)
- [x] `0x83` Update Attributes (`ClientboundUpdateAttributesPacket`)
- [x] `0x84` Entity Effect (`ClientboundEntityEffectPacket`)
- [ ] `0x85` Update Recipes (`ClientboundUpdateRecipesPacket`)
- [x] `0x86` Update Tags (`ClientboundUpdateTagsPacket`)
- [x] `0x87` Projectile Power (`ClientboundProjectilePowerPacket`)
- [x] `0x88` Custom Report Details (`ClientboundCustomReportDetailsPacket`)
- [x] `0x89` Server Links (`ClientboundServerLinksPacket`)
- [x] `0x8A` Waypoint (`ClientboundWaypointPacket`)
- [x] `0x8B` Clear Dialog (`ClientboundClearDialogPacket`)
- [x] `0x8C` Show Dialog (`ClientboundShowDialogPacket`)

#### Serverbound

- [x] `0x00` Accept Teleportation (`ServerboundAcceptTeleportationPacket`)
- [x] `0x01` Attack Action (`ServerboundAttackActionPacket`)
- [x] `0x02` Query Block Entity Tag (`ServerboundQueryBlockEntityTagPacket`)
- [x] `0x03` Bundle Item Selected (`ServerboundBundleItemSelectedPacket`)
- [x] `0x04` Change Difficulty (`ServerboundChangeDifficultyPacket`)
- [x] `0x05` Change Game Mode (`ServerboundChangeGameModePacket`)
- [x] `0x06` Acknowledge Chat Message (`ServerboundAcknowledgeChatMessagePacket`)
- [x] `0x07` Chat Command (`ServerboundChatCommandPacket`)
- [x] `0x08` Signed Chat Command (`ServerboundSignedChatCommandPacket`)
- [x] `0x09` Chat Message (`ServerboundChatMessagePacket`)
- [x] `0x0A` Update Chat Session (`ServerboundUpdateChatSessionPacket`)
- [x] `0x0B` Chunk Batch Received (`ServerboundChunkBatchReceivedPacket`)
- [x] `0x0C` Client Command (`ServerboundClientCommandPacket`)
- [x] `0x0D` Client Tick End (`ServerboundClientTickEndPacket`)
- [x] `0x0E` Client Information (`ServerboundClientInformationPacket`)
- [x] `0x0F` Command Suggestion Request (`ServerboundCommandSuggestionRequestPacket`)
- [x] `0x10` Configuration Acknowledged (`ServerboundConfigurationAcknowledgedPacket`)
- [x] `0x11` Container Click Button (`ServerboundContainerClickButtonPacket`)
- [x] `0x12` Container Click (`ServerboundContainerClickPacket`)
- [x] `0x13` Container Close (`ServerboundContainerClosePacket`)
- [x] `0x14` Change Container Slot State (`ServerboundChangeContainerSlotStatePacket`)
- [x] `0x15` Cookie Response (`ServerboundCookieResponsePacket`)
- [x] `0x16` Custom Payload (`ServerboundCustomPayloadPacket`)
- [ ] `0x17` Debug Subscription Request (`ServerboundDebugSubscriptionRequestPacket`)
- [x] `0x18` Edit Book (`ServerboundEditBookPacket`)
- [x] `0x19` Query Entity Tag (`ServerboundQueryEntityTagPacket`)
- [x] `0x1A` Interact (`ServerboundInteractPacket`)
- [x] `0x1B` Jigsaw Generate (`ServerboundJigsawGeneratePacket`)
- [x] `0x1C` Keep Alive (`ServerboundKeepAlivePlayPacket`)
- [x] `0x1D` Lock Difficulty (`ServerboundLockDifficultyPacket`)
- [x] `0x1E` Set Player Position (`ServerboundSetPlayerPositionPacket`)
- [x] `0x1F` Set Player Position and Rotation (`ServerboundSetPlayerPositionAndRotationPacket`)
- [x] `0x20` Set Player Rotation (`ServerboundSetPlayerRotationPacket`)
- [x] `0x21` Set Player Movement Flag (`ServerboundSetPlayerMovementFlagPacket`)
- [x] `0x22` Move Vehicle (`ServerboundMoveVehiclePacket`)
- [x] `0x23` Paddle Boat (`ServerboundPaddleBoatPacket`)
- [x] `0x24` Pick Item From Block (`ServerboundPickItemFromBlockPacket`)
- [x] `0x25` Pick Item From Entity (`ServerboundPickItemFromEntityPacket`)
- [x] `0x26` Ping Request (`ServerboundPingRequestPacket`)
- [x] `0x27` Place Recipe (`ServerboundPlaceRecipePacket`)
- [x] `0x28` Player Abilities (`ServerboundPlayerAbilitiesPacket`)
- [x] `0x29` Player Action (`ServerboundPlayerActionPacket`)
- [x] `0x2A` Player Command (`ServerboundPlayerCommandPacket`)
- [x] `0x2B` Player Input (`ServerboundPlayerInputPacket`)
- [x] `0x2C` Player Loaded (`ServerboundPlayerLoadedPacket`)
- [x] `0x2D` Pong (`ServerboundPongPlayPacket`)
- [x] `0x2E` Recipe Book Change Settings (`ServerboundRecipeBookChangeSettingsPacket`)
- [x] `0x2F` Recipe Book Seen Recipe (`ServerboundRecipeBookSeenRecipePacket`)
- [x] `0x30` Rename Item (`ServerboundRenameItemPacket`)
- [x] `0x31` Resource Pack Response (`ServerboundResourcePackResponsePacket`)
- [x] `0x32` Seen Advancements (`ServerboundSeenAdvancementsPacket`)
- [x] `0x33` Select Trade (`ServerboundSelectTradePacket`)
- [x] `0x34` Set Beacon (`ServerboundSetBeaconPacket`)
- [x] `0x35` Set Carried Item (`ServerboundSetCarriedItemPacket`)
- [x] `0x36` Set Command Block (`ServerboundSetCommandBlockPacket`)
- [x] `0x37` Set Command Minecart (`ServerboundSetCommandMinecartPacket`)
- [x] `0x38` Set Creative Mode Slot (`ServerboundSetCreativeModeSlotPacket`)
- [x] `0x39` Set Game Rule (`ServerboundSetGameRulePacket`)
- [x] `0x3A` Set Jigsaw Block (`ServerboundSetJigsawBlockPacket`)
- [x] `0x3B` Set Structure Block (`ServerboundSetStructureBlockPacket`)
- [x] `0x3C` Set Test Block (`ServerboundSetTestBlockPacket`)
- [x] `0x3D` Sign Update (`ServerboundSignUpdatePacket`)
- [x] `0x3E` Spectator Action (`ServerboundSpectatorActionPacket`)
- [x] `0x3F` Swing (`ServerboundSwingPacket`)
- [x] `0x40` Teleport To Entity (`ServerboundTeleportToEntityPacket`)
- [x] `0x41` Test Instance Block Action (`ServerboundTestInstanceBlockActionPacket`)
- [x] `0x42` Use Item On (`ServerboundUseItemOnPacket`)
- [x] `0x43` Use Item (`ServerboundUseItemPacket`)
- [x] `0x44` Custom Click Action (`ServerboundCustomClickActionPacket`)

</details>

# Open Source

Licensed under [Apache-2.0](LICENSE)