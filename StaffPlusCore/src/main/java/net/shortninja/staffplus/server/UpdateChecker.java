package net.shortninja.staffplus.server;

import net.shortninja.staffplus.StaffPlus;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UpdateChecker {
    private static final String MODRINTH_API = "https://api.modrinth.com/v2";
    private static final String PROJECT_SLUG = "staff-plus";
    private static final String DOWNLOAD_URL = "https://modrinth.com/plugin/staff-plus/versions";

    private final StaffPlus plugin;
    private String latestVersion;
    private boolean updateAvailable;
    private boolean checkCompleted;

    public UpdateChecker(StaffPlus plugin) {
        this.plugin = plugin;
        this.updateAvailable = false;
        this.checkCompleted = false;
    }

    /**
     * Inicia la verificación de actualizaciones de forma asincrónica
     */
    public void startAsyncCheck() {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                checkForUpdates();
            } catch (Exception e) {
                plugin.getLogger().warning("Error checking for updates: " + e.getMessage());
            }
        });
    }

    /**
     * Verifica si hay nuevas versiones disponibles
     */
    private void checkForUpdates() throws Exception {
        String currentVersion = plugin.getDescription().getVersion();
        String apiUrl = MODRINTH_API + "/project/" + PROJECT_SLUG + "/versions";

        JSONArray versions = fetchVersionsFromModrinth(apiUrl);
        if (versions == null || versions.isEmpty()) {
            plugin.getLogger().warning("No versions found on Modrinth for Staff+");
            checkCompleted = true;
            return;
        }

        // Obtener la versión más reciente
        String newest = null;
        for (Object obj : versions) {
            JSONObject versionObj = (JSONObject) obj;
            String versionName = (String) versionObj.get("name");
            String versionNumber = (String) versionObj.get("version_number");

            if (versionNumber != null) {
                newest = versionNumber;
                break; // Modrinth devuelve ordenado por fecha descendente
            }
        }

        if (newest != null) {
            latestVersion = newest;
            updateAvailable = compareVersions(currentVersion, newest) < 0;

            if (updateAvailable) {
                plugin.getLogger().warning("═══════════════════════════════════════════════");
                plugin.getLogger().warning("Staff+ - Nueva versión disponible!");
                plugin.getLogger().warning("Versión actual: " + currentVersion);
                plugin.getLogger().warning("Versión más reciente: " + newest);
                plugin.getLogger().warning("Descarga desde: " + DOWNLOAD_URL);
                plugin.getLogger().warning("═══════════════════════════════════════════════");
            }
        }

        checkCompleted = true;
    }

    /**
     * Obtiene las versiones desde la API de Modrinth
     */
    private JSONArray fetchVersionsFromModrinth(String urlString) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("User-Agent", "StaffPlus-UpdateChecker/5.0.0");
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);

        if (connection.getResponseCode() != 200) {
            return null;
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        JSONParser parser = new JSONParser();
        JSONObject jsonResponse = (JSONObject) parser.parse(response.toString());
        return (JSONArray) jsonResponse.get("hits");
    }

    /**
     * Compara dos versiones en formato semver
     * Retorna: -1 si v1 < v2, 0 si son iguales, 1 si v1 > v2
     */
    private int compareVersions(String v1, String v2) {
        // Extraer números de versión (ej: "5.0.0" de "v5.0.0")
        String version1 = extractVersionNumbers(v1);
        String version2 = extractVersionNumbers(v2);

        String[] parts1 = version1.split("\\.");
        String[] parts2 = version2.split("\\.");

        int maxLength = Math.max(parts1.length, parts2.length);

        for (int i = 0; i < maxLength; i++) {
            int num1 = i < parts1.length ? parseInt(parts1[i]) : 0;
            int num2 = i < parts2.length ? parseInt(parts2[i]) : 0;

            if (num1 < num2)
                return -1;
            if (num1 > num2)
                return 1;
        }

        return 0;
    }

    /**
     * Extrae solo los números de versión de un string
     */
    private String extractVersionNumbers(String version) {
        Pattern pattern = Pattern.compile("(\\d+(?:\\.\\d+)*)");
        Matcher matcher = pattern.matcher(version);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "0.0.0";
    }

    /**
     * Intenta parsear un string a entero, retorna 0 si falla
     */
    private int parseInt(String str) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * Notifica a los jugadores con permiso de staff sobre la actualización
     * disponible
     */
    public void notifyStaffAboutUpdate() {
        if (!updateAvailable || !checkCompleted) {
            return;
        }

        String message = "§c§l[STAFF+] §r§cNueva versión disponible: §f" + latestVersion +
                "§c. Descarga desde: §f" + DOWNLOAD_URL;

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.hasPermission("staff.mode")) {
                player.sendMessage(message);
            }
        }
    }

    /**
     * Obtiene si una actualización está disponible
     */
    public boolean isUpdateAvailable() {
        return updateAvailable;
    }

    /**
     * Obtiene la versión más reciente encontrada
     */
    public String getLatestVersion() {
        return latestVersion;
    }

    /**
     * Obtiene si la verificación se ha completado
     */
    public boolean isCheckCompleted() {
        return checkCompleted;
    }
}
