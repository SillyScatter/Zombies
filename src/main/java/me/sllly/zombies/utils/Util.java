package me.sllly.zombies.utils;

import com.octanepvp.splityosis.configsystem.configsystem.actionsystem.ActionData;
import com.octanepvp.splityosis.configsystem.configsystem.actionsystem.Actions;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.DecimalFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {

    public static final String LOG_PREFIX = "&8[&2Zombies&8]"; //Only affects messages sent to console

    public static void sendMessage(CommandSender to, String message){
        to.sendMessage(colorize(message));
    }

    public static void sendPrefixedMessage(Player to, Sound sound, String message){
        if (sound != null)
            to.playSound(to.getLocation(), sound, 1f, 1f);
        if (message != null)
            to.sendMessage(colorize(LOG_PREFIX + "&f "+message));
    }

    public static void sendMessage(CommandSender to, List<String> message){
        message.forEach(s -> {
            sendMessage(to, s);
        });
    }

    public static void sendPrefixedMessage(CommandSender to, String message){
        sendMessage(to, LOG_PREFIX + "&f "+message);
    }

    public static void broadcast(String message){
        for (Player onlinePlayer : Bukkit.getOnlinePlayers())
            sendMessage(onlinePlayer, message);
        log(message);
    }

    public static void broadcast(List<String> message){
        for (Player onlinePlayer : Bukkit.getOnlinePlayers())
            sendMessage(onlinePlayer, message);
        log(message);
    }

    public static void log(String message){
        sendMessage(Bukkit.getServer().getConsoleSender(), LOG_PREFIX+" "+message);
    }


    public static void log(List<String> message){
        List<String> msg = new ArrayList<>(message);
        if (!msg.isEmpty())
            msg.set(0, LOG_PREFIX+" "+msg.get(0));
        sendMessage(Bukkit.getServer().getConsoleSender(), msg);
    }

    private static final Pattern HEX_PATTERN = Pattern.compile("&(#\\w{6})");
    public static String colorize(String str) {
        Matcher matcher = HEX_PATTERN.matcher(net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', str));
        StringBuffer buffer = new StringBuffer();

        while (matcher.find())
            matcher.appendReplacement(buffer, net.md_5.bungee.api.ChatColor.of(matcher.group(1)).toString());

        return ChatColor.translateAlternateColorCodes('&', matcher.appendTail(buffer).toString());
    }

    public static List<String> colorize(List<String> lst){
        if (lst == null) return null;
        List<String> newList = new ArrayList<>();
        lst.forEach(s -> {
            newList.add(colorize(s));
        });
        return newList;
    }

    public static ItemStack createItemStack(Material material, int amount, String name, List<String> lore){
        ItemStack itemStack = new ItemStack(material, amount);
        ItemMeta meta = itemStack.getItemMeta();
        if (name != null)
            meta.setDisplayName(colorize(name));
        meta.setLore(colorize(lore));
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public static ItemStack createItemStack(Material material, int amount, String name, String... lore){
        return createItemStack(material, amount, name, Arrays.asList(lore));
    }

    public static ItemStack createItemStack(Material material, int amount){
        return createItemStack(material, amount, null);
    }

    public static List<String> replaceList(List<String> lst, String from, String to){
        if (lst == null) return null;
        List<String> newList = new ArrayList<>();
        lst.forEach(s -> {
            newList.add(s.replace(from, to));
        });
        return newList;
    }

    // 123456
    // 123,456
    // 123456.123
    // 123,456,12
    // 0.00123456
    // 0.0012

    public static String formatNumber(Number number) {
        if (number.intValue() == number.doubleValue())
            return String.format("%,d", number.intValue());
        return new DecimalFormat("#,##0.00").format(number);
    }

    public static String signifyNumber(double number) {
        if (number >= 1) return formatNumber(number);
        String str = new DecimalFormat("#.####################").format(number);
        char[] chars = str.toCharArray();
        if (chars.length < 4) return str;
        int sig = 0;

        for (int i = 2; i < chars.length; i++) {
            char c = chars[i];
            if (sig == 0){
                if (c == '0') continue;
                sig++;
                continue;
            }

            if (c == '0')
                return str.substring(0, i);
            return str.substring(0, i+1);
        }
        return str;
    }

    public static String replace(String str, Map<String, String> replacements){
        if (replacements == null)
            return str;

        for (Map.Entry<String, String> entry : replacements.entrySet())
            str = str.replace(entry.getKey(), entry.getValue());
        return str;
    }

    public static Actions getDefaultActions(Sound sound, String... msg){
        List<ActionData> actionDataList = new ArrayList<>();
        if (msg != null)
            actionDataList.add(new ActionData("MESSAGE", Arrays.asList(msg)));
        if (sound != null)
            actionDataList.add(new ActionData("SOUND", Arrays.asList(sound.name())));
        return new Actions(actionDataList);
    }

    public static Actions getActionBarActions(String msg){
        List<ActionData> actionDataList = new ArrayList<>();
        if (msg != null)
            actionDataList.add(new ActionData("ACTIONBAR", Arrays.asList(msg)));
        return new Actions(actionDataList);
    }

    public static Actions getDefaultTitleActions(Sound sound, String title, String subtitle, String... msg){
        List<ActionData> actionDataList = new ArrayList<>();
        if (msg != null)
            actionDataList.add(new ActionData("MESSAGE", Arrays.asList(msg)));
        if (sound != null)
            actionDataList.add(new ActionData("SOUND", Arrays.asList(sound.name())));
        if (title != null && subtitle != null)
            actionDataList.add(new ActionData("TITLE", Arrays.asList(title, subtitle, "20", "20", "20")));
        return new Actions(actionDataList);
    }

    public static ItemStack replaceItemStack(ItemStack itemStack, String from, String to){
        return replaceItemStack(itemStack, Collections.singletonMap(from, to));
    }

    public static ItemStack replaceItemStack(ItemStack itemStack, Map<String, String> fromToMap){
        if (fromToMap == null) return itemStack.clone();
        ItemStack item = itemStack.clone();
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return item;

        String name = meta.getDisplayName();
        if (name != null)
            for (Map.Entry<String, String> stringStringEntry : fromToMap.entrySet())
                name = name.replace(stringStringEntry.getKey(), stringStringEntry.getValue());

        List<String> newLore = new ArrayList<>();
        List<String> lore = meta.getLore();
        if (lore != null)
            for (String s : lore) {
                String line = s;
                for (Map.Entry<String, String> stringStringEntry : fromToMap.entrySet())
                    line = line.replace(stringStringEntry.getKey(), stringStringEntry.getValue());
                newLore.add(line);
            }
        meta.setDisplayName(name);
        meta.setLore(newLore);
        item.setItemMeta(meta);
        return item;
    }

    public static void giveItemsToPlayer(Player player, ItemStack itemStack, int amount) {
        int maxStackSize = itemStack.getMaxStackSize();
        int remainingAmount = amount;

        while (remainingAmount > 0) {
            int giveAmount = Math.min(remainingAmount, maxStackSize);
            ItemStack giveItem = itemStack.clone();
            giveItem.setAmount(giveAmount);

            HashMap<Integer, ItemStack> leftovers = player.getInventory().addItem(giveItem);
            if (!leftovers.isEmpty()) {
                // If there are leftovers, drop them on the floor
                World world = player.getWorld();
                Location playerLocation = player.getLocation();
                for (ItemStack leftover : leftovers.values()) {
                    world.dropItemNaturally(playerLocation, leftover);
                }
            }

            remainingAmount -= giveAmount;
        }
    }

    public static String createBar(int completed, int total, String completedChar, String emptyChar, String maxedOutChar){
        StringBuilder builder = new StringBuilder();
        if (completed == total){
            for (int i = 0; i < total; i++)
                builder.append(maxedOutChar);
            return builder.toString();
        }

        for (int i = 0; i < completed; i++)
            builder.append(completedChar);

        for (int i = 0; i < total - completed; i++)
            builder.append(emptyChar);
        return builder.toString();
    }

    public static String intToRoman(int num) {
        if (num < 1 || num > 3999) {
            throw new IllegalArgumentException("Input must be between 1 and 3999");
        }

        String[] romanSymbols = {"I", "IV", "V", "IX", "X", "XL", "L", "XC", "C", "CD", "D", "CM", "M"};
        int[] values = {1, 4, 5, 9, 10, 40, 50, 90, 100, 400, 500, 900, 1000};
        StringBuilder roman = new StringBuilder();

        int i = values.length - 1;
        while (num > 0) {
            if (num - values[i] >= 0) {
                roman.append(romanSymbols[i]);
                num -= values[i];
            } else {
                i--;
            }
        }

        return roman.toString();
    }

    public static int getRandomInt(int min, int max) {
        return (int) ((Math.random() * (max + 1 - min)) + min);
    }

    public static String formatTimeDigital(long milliseconds) {
        int totalSeconds = (int) (milliseconds / 1000);
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;

        String m;
        if (minutes == 0) m = "00";
        else if (minutes < 10) m = "0" + minutes;
        else m = String.valueOf(minutes);

        String s;
        if (seconds == 0) s = "00";
        else if (seconds < 10) s = "0" + seconds;
        else s = String.valueOf(seconds);

        return m + ":" + s;
    }

    public static String formatTime(long timeInMillis) {
        final int secondsInAMinute = 60;
        final int secondsInAnHour = 3600;
        final int secondsInADay = 86400;

        long totalSeconds = timeInMillis / 1000;
        long days = totalSeconds / secondsInADay;
        totalSeconds %= secondsInADay;
        long hours = totalSeconds / secondsInAnHour;
        totalSeconds %= secondsInAnHour;
        long minutes = totalSeconds / secondsInAMinute;
        long seconds = totalSeconds % secondsInAMinute;

        StringBuilder sb = new StringBuilder();
        if (days > 0) {
            sb.append(days).append(" Days, ");
        }
        if (hours > 0) {
            sb.append(hours).append(" Hours, ");
        }
        if (minutes > 0) {
            sb.append(minutes).append(" Minutes, ");
        }
        sb.append(seconds).append(" Seconds");

        return sb.toString();
    }

    public static <T> T getRandomEntry(Set<T> set) {
        // Convert HashSet to a List
        List<T> list = new ArrayList<>(set);

        // Generate a random index
        Random random = new Random();
        int randomIndex = random.nextInt(list.size());

        // Return the random element
        return list.get(randomIndex);
    }

}