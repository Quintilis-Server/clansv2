package org.quintilis.clansv2.string

import org.bukkit.ChatColor

fun String.bold(): String = "${ChatColor.BOLD}$this${ChatColor.RESET}"

fun String.italic(): String = "${ChatColor.ITALIC}$this${ChatColor.RESET}"

fun String.color(color: ChatColor): String = "$color$this${ChatColor.RESET}"