package me.apex.hades.util;

import me.apex.hades.util.boundingbox.BlockUtil;
import me.apex.hades.util.reflection.ReflectionUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PlayerUtil {

    /*
    Block Utils
     */

    public static boolean isOnGround(Player player) {
        Object box = ReflectionUtil.modifyBoundingBox(ReflectionUtil.getBoundingBox(player), 0, -0.1001, 0, 0, 0, 0);

        return ReflectionUtil.getCollidingBlocks(player, box).size() > 0;
    }

    public static boolean isInLiquid(Player player) {
        Object box = ReflectionUtil.getBoundingBox(player);

        double minX = (double) ReflectionUtil.getInvokedField(ReflectionUtil.getField(box.getClass(), "a"), box);
        double minY = (double) ReflectionUtil.getInvokedField(ReflectionUtil.getField(box.getClass(), "b"), box);
        double minZ = (double) ReflectionUtil.getInvokedField(ReflectionUtil.getField(box.getClass(), "c"), box);
        double maxX = (double) ReflectionUtil.getInvokedField(ReflectionUtil.getField(box.getClass(), "d"), box);
        double maxY = (double) ReflectionUtil.getInvokedField(ReflectionUtil.getField(box.getClass(), "e"), box);
        double maxZ = (double) ReflectionUtil.getInvokedField(ReflectionUtil.getField(box.getClass(), "f"), box);

        for (double x = minX; x < maxX; x++) {
            for (double y = minY; y < maxY; y++) {
                for (double z = minZ; z < maxZ; z++) {
                    Block block = new Location(player.getWorld(), x, y, z).getBlock();
                    if (block.isLiquid()) return true;
                }
            }
        }
        return false;
    }

    public static boolean blockNearHead(Player player) {
        double expand = 0.3;
        for (double x = -expand; x <= expand; x += expand) {
            for (double z = -expand; z <= expand; z += expand) {
                if (player.getLocation().clone().add(z, 2, x).getBlock().getType() != Material.AIR) {
                    return true;
                }
                if (player.getLocation().clone().add(z, 1.5001, x).getBlock().getType() != Material.AIR) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean nearWall(Player player) {
        double expand = 0.6;
        for (double x = -expand; x <= expand; x += expand) {
            for (double z = -expand; z <= expand; z += expand) {
                if (player.getLocation().clone().add(z, 0.1, x).getBlock().getType() != Material.AIR) {
                    return true;
                }
                if (player.getEyeLocation().clone().add(z, 0, x).getBlock().getType() != Material.AIR) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isInWeb(Player player) {
        return player.getLocation().getBlock().getType() == Material.WEB
                || player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.WEB
                || player.getEyeLocation().getBlock().getType() == Material.WEB
                || player.getEyeLocation().getBlock().getRelative(BlockFace.UP).getType() == Material.WEB;
    }

    //Credits to funkemunky :)
    public static boolean isSolid(Block block) {
        int type = block.getType().getId();

        switch (type) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 7:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
            case 29:
            case 34:
            case 33:
            case 35:
            case 36:
            case 41:
            case 42:
            case 43:
            case 44:
            case 45:
            case 46:
            case 47:
            case 48:
            case 49:
            case 52:
            case 53:
            case 54:
            case 56:
            case 57:
            case 58:
            case 60:
            case 61:
            case 62:
            case 64:
            case 65:
            case 67:
            case 71:
            case 73:
            case 74:
            case 78:
            case 79:
            case 80:
            case 81:
            case 82:
            case 84:
            case 85:
            case 86:
            case 87:
            case 88:
            case 89:
            case 91:
            case 92:
            case 93:
            case 94:
            case 95:
            case 96:
            case 97:
            case 98:
            case 99:
            case 100:
            case 101:
            case 102:
            case 103:
            case 106:
            case 107:
            case 108:
            case 109:
            case 110:
            case 111:
            case 112:
            case 113:
            case 114:
            case 116:
            case 117:
            case 118:
            case 120:
            case 121:
            case 122:
            case 123:
            case 124:
            case 125:
            case 126:
            case 127:
            case 128:
            case 129:
            case 130:
            case 133:
            case 134:
            case 135:
            case 136:
            case 137:
            case 138:
            case 139:
            case 140:
            case 144:
            case 145:
            case 146:
            case 149:
            case 150:
            case 151:
            case 152:
            case 153:
            case 154:
            case 155:
            case 156:
            case 158:
            case 159:
            case 160:
            case 161:
            case 162:
            case 163:
            case 164:
            case 165:
            case 166:
            case 167:
            case 168:
            case 169:
            case 170:
            case 171:
            case 172:
            case 173:
            case 174:
            case 178:
            case 179:
            case 180:
            case 181:
            case 182:
            case 183:
            case 184:
            case 185:
            case 186:
            case 187:
            case 188:
            case 189:
            case 190:
            case 191:
            case 192:
            case 193:
            case 194:
            case 195:
            case 196:
            case 197:
            case 198:
            case 199:
            case 200:
            case 201:
            case 202:
            case 203:
            case 204:
            case 205:
            case 206:
            case 207:
            case 208:
            case 210:
            case 211:
            case 212:
            case 213:
            case 214:
            case 215:
            case 216:
            case 218:
            case 219:
            case 220:
            case 221:
            case 222:
            case 223:
            case 224:
            case 225:
            case 226:
            case 227:
            case 228:
            case 229:
            case 230:
            case 231:
            case 232:
            case 233:
            case 234:
            case 235:
            case 236:
            case 237:
            case 238:
            case 239:
            case 240:
            case 241:
            case 242:
            case 243:
            case 244:
            case 245:
            case 246:
            case 247:
            case 248:
            case 249:
            case 250:
            case 251:
            case 252:
            case 255:
            case 397:
            case 355:
                return true;

        }
        return false;
    }

    //Credits to funkemunky :)
    public static boolean isOnClimbable(Player player) {
        return player.getLocation().getBlock().getType() == Material.LADDER || player.getLocation().getBlock().getType() == Material.VINE;
    }

    //Credits to funkemunky :)
    public static boolean hasBlocksAround(Location loc) {
        Location one = loc.clone().subtract(1, 0, 1), two = loc.clone().add(1, 1, 1);

        int minX = Math.min(one.getBlockX(), two.getBlockX()), minY = Math.min(one.getBlockY(), two.getBlockY()), minZ = Math.min(one.getBlockZ(), two.getBlockZ());
        int maxX = Math.max(one.getBlockX(), two.getBlockX()), maxY = Math.max(one.getBlockY(), two.getBlockY()), maxZ = Math.max(one.getBlockZ(), two.getBlockZ());

        for (int x = minX; x < maxX; x++) {
            for (int y = minY; y < maxY; y++) {
                for (int z = minZ; z < maxZ; z++) {
                    Location blockLoc = new Location(loc.getWorld(), x, y, z);

                    if (isSolid(blockLoc.getBlock())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    //Credits to funkemunky
    public static boolean isInStairs(Player player) {
        Object box = ReflectionUtil.modifyBoundingBox(ReflectionUtil.getBoundingBox(player), 0, -0.5, 0, 0, 0, 0);

        double minX = (double) ReflectionUtil.getInvokedField(ReflectionUtil.getField(box.getClass(), "a"), box);
        double minY = (double) ReflectionUtil.getInvokedField(ReflectionUtil.getField(box.getClass(), "b"), box);
        double minZ = (double) ReflectionUtil.getInvokedField(ReflectionUtil.getField(box.getClass(), "c"), box);
        double maxX = (double) ReflectionUtil.getInvokedField(ReflectionUtil.getField(box.getClass(), "d"), box);
        double maxY = (double) ReflectionUtil.getInvokedField(ReflectionUtil.getField(box.getClass(), "e"), box);
        double maxZ = (double) ReflectionUtil.getInvokedField(ReflectionUtil.getField(box.getClass(), "f"), box);

        for (double x = minX; x < maxX; x++) {
            for (double y = minY; y < maxY; y++) {
                for (double z = minZ; z < maxZ; z++) {
                    Block block = new Location(player.getWorld(), x, y, z).getBlock();

                    if (BlockUtil.isStair(block)
                            || BlockUtil.isSlab(block)
                            || block.getType().equals(Material.SKULL)
                            || block.getType().equals(Material.CAKE_BLOCK)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    //Credits to funkemunky
    public static int getPotionEffectLevel(Player player, PotionEffectType pet) {
        for (PotionEffect pe : player.getActivePotionEffects()) {
            if (!pe.getType().getName().equals(pet.getName())) continue;
            return pe.getAmplifier() + 1;
        }
        return 0;
    }

}
