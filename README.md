# Quintilis Clan plugin

This is a plugin for Minecraft 1.21.* that allows players to create, edit, and wage clan wars.

It also features a points system to show who has killed the most people in the war.

The plugin was designed for use on the "Quintilis" war server, and this is the second version of the code. Even though it's for "Quintilis," anyone can use it on their own server.

## Installation

- First you'll need [mongodb](https://www.mongodb.com/) with a valid URI, and [LuckPerms](https://luckperms.net/).
- Add the plugin to the `plugins` folder on your server and start the server to generate the `config.yml` file.
- In the `config.yml` file you edit the `database.mongodb.uri` and put the URI for the Mongo database.

## Commands

For all the commands you can type `/<name of the command>` for all the usages.

### Clan commands

#### `/clan create <name>`
* Only the player can run this command, it can run like `/clan create <name> <tag>` or only `/clan create <name>`
* The player running this command will be the de owner of the clan.

#### `/clan delete`
* Only the **clan owner** can run this command.
* Deletes the clan that the commandSender is owner.

#### `/clan list`
* Anyone can use this command.
* Lists all clans in the chat with his owners and points

#### `/clan set <type> <value>`
* Only the **clan owner** can run this command.
* Alters the value of a clan based in the `<type>`
* The type can be:
  * name: Text
  * tag: Text, max 5 letters

#### `/clan member list <clanName>`
* Anyone can use this command, it can run like `/clan member list`, it will show the members of your clan.
* Lists all members of a clan.

#### `/clan member kick <playerName>`
* Only the **clan owner** can run this command.
* Kicks the player from the clan.

#### `/clan member invite <playerName>`
* Only the **clan owner** can run this command.
* Invites a player to the clan. ***The player needs to the online to receive the invite***. 
In the future I plan to add a history to the plugin, for the offline players.

#### `/clan quit`
* Only the member can run this command, ****the player needs to be in a clan to work***.
* Quits the current clan, the owner cannot run this command, for him, it needs to run the [`/clan delete`](#clan-delete) command

### AllyCommands

***Only the owners can run these commands***

#### `/ally list`
* Lists all allies your clan has.

#### `/ally remove <clan>`
* Removes an ally

#### `/ally invite send`
* Sends an ally invite to another clan.

#### `/ally invite list`
* Lists all ally invites of the clan.

#### `/ally invite accept <clan>`
* Accept an ally invite.

#### `/ally invite reject <clan>`
* Rejects an ally invite.

### Enemy commands

***Only the owners can run these commands***

#### `/enemy list`
* Lists all enemies from your clan.

#### `/enemy remove <clan>`
* Removes an enemy.

#### `/enemy declare <clan>`
* DECLARES WAR AGAINST A CLAN.

### Invite commands

***Only members can run these commands.***

#### `/invite list`
* Lists all invitations.

#### `/invite accept <clan>`
* Accept an invitation to a clan.

#### `/invite reject <clan>`
* Reject an invitation to a clan.

## Contributing

Contributions are welcome!

Feel free to:
- Open [issues](https://github.com/Quintilis-Server/clansv2/issues) for bugs, ideas or improvements
- Create [pull requests](https://github.com/Quintilis-Server/clansv2/pulls) with fixes, features or documentation

Please follow these guidelines:
- Keep code clean and consistent
- Write clear commit messages
- If you're fixing a bug or adding a feature, include a short description

Thank you for helping improve the project!

## License

This project is licensed under the [CC BY-NC 4.0 License](https://creativecommons.org/licenses/by-nc/4.0/).

You can use, modify and share it freely — just don’t use it for commercial purposes.