# About MinigameBox

The MinigameBox is a small project that aims to create an easy way to run a diverse set of
minigames on your server. It lets you create and edit game arenas in your world, allowing players to play games like
spleef and similar games. The focus is to maintain and expand a high level of customizablity and constantly expand the set
of minigames.

Note: This plugin doesn't protect the arena outside if minigames, control pvp (but that might change), or create a huge
build for the arena. The plugin just creates the barebones functionality.

If questions arise or you want to give some feedback, contact me on GitHub or Discord @upscairs.


### Status Quo

The number of the minigames you can play at the moment is, well, two - Spleef and TntRun. My plans are to increase that number
obviously, but I want to build a solid foundation first. A foundation that allows me and everyone else who wants to 
work with it to have it as easy as possible.

### The future

I started this plugin as a hobby, because I like to work on it and I will keep working on it until I have no ideas 
anymore (that's quite unlikely) or I don't have any fun or time in doing it anymore. I don't see a reason why that
would happen in the near future, but I thought it would be fair to let you know ;)

But let's talk about features I have planned:

- More options in the edit and queue gui
- More customizability in guis
- A pvp toggle
- Leaderboards

# Guide

The default command, everything is handled with is ``/minigame``.
## Arena creation and moderation
Can be accessed by Ops and everyone with the permission ``minigamebox.manage``.
### Creation
- Arena creation via ``/minigame create <GameTyp> <Name>``.
- You get asked for 3 positions, the first two are the arena boundary corners, where games take place. The third is an outside position, players get teleported to, when they get thrown out of the arena for whatever reason. (Game end, leaving and similar). You input the Location via ``/minigame setpos``, while standing on the position of your choice.
- If the input was successful, the arena blocks get placed. (Note: Those blocks are unprotected from players not ingame. Please take measures to protect those blocks from other players, like a protection plugin)
- After creation you can edit other parameters of the arena -> See Editing
### Editing
- Edit arenas via ``/minigame edit <Name>`` and click on the setting you want to edit.
- True/False-Settings change when clicked on.
- More complex inputs (Blocks/Numbers) are given via ``/minigame edit-input <Value>``.
- You get messaged in chat about the success.
### Game Moderation
- Games are abled to get started/stopped manually.
- This happens via command: ``/minigame start <Name>``, ``/minigame stop <Name>`` or the icon in the bottom-right corner of the edit menu of any arena.
- A manual start ignores player numbers, which means, games can get started with less players than intended.
- A force-stop aborts the current game on the spot, doesn't reward players and changes the setting ``Autostart next game`` to ``false``.
### Refresh
- ``/minigame refresh <Name>`` reloads the arenas contents (not possible during games).
### Delete
- ``/minigame delete <Name>`` deletes an arena and all their contents.
## Player Features
### Join/Leave
- Joining a queue via ``/minigame join <Name>``.
- Leave a queue/game via ``/minigame leave <Name>``.
### Queue
- The queue with all players listed can be viewed via ``/minigame queue <Name>``.
### List
- A list of all visible arenas can be seen with ``/minigame list`` or just ``/minigame``.
## Further customizations
- Settings for arena broadcast and gui interactions can be edited in the file ``/plugins/MinigameBox/settings.yml``.
- ``listClickAction`` can have the values: ``tp``, ``queue`` or ``none`` (future possibilities will be "info" and "leaderboard").
- Chat messages (and Gui texts) can be edited in the file ``/plugins/MinigameBox/messages-config.yml``.