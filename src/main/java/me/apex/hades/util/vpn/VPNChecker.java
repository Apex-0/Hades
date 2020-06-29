package me.apex.hades.util.vpn;

import me.apex.hades.HadesConfig;
import me.apex.hades.HadesPlugin;
import me.apex.hades.user.User;
import me.apex.hades.util.json.JSONObject;
import org.apache.commons.io.IOUtils;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public enum VPNChecker {
    INSTANCE;

    private final HashMap<String, Boolean> ips = new HashMap<>();

    public boolean checkUser(User user) {
        if (!HadesPlugin.getInstance().getConfig().getBoolean("anti-vpn.enabled") || user.getPlayer().hasPermission(HadesConfig.BASE_PERMISSION + ".exempt.antivpn"))
            return false;
        if (ips.containsKey(user.address())) {
            return ips.get(user.address());
        } else {
            user.getExecutorService().execute(() -> {
                try {
                    checkAddress(user.address());
                } catch (Exception ex) {

                }
            });
        }
        return (ips.containsKey(user.address())) && ips.get(user.address());
    }

    private boolean checkAddress(String address) throws Exception {
        JSONObject json = new JSONObject(IOUtils.toString(new URL("https://api.iplegit.com/info?ip=" + address), StandardCharsets.UTF_8));
        if (Boolean.parseBoolean(json.get("bad").toString())) {
            ips.put(address, true);
            return true;
        }

        ips.put(address, false);
        return false;
    }
}
