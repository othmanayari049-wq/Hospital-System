package com.qataruniversity.hms;

import com.qataruniversity.hms.cli.ConsoleMenu;
import com.qataruniversity.hms.config.SeedData;
import com.qataruniversity.hms.service.HospitalSystem;
import com.qataruniversity.hms.web.HospitalHttpServer;

public final class Application {
    private Application() { }

    public static void main(String[] args) throws Exception {
        HospitalSystem system = new HospitalSystem();
        SeedData.load(system);

        String mode = args.length == 0 ? "demo" : args[0].toLowerCase();
        switch (mode) {
            case "server" -> startServer(system);
            case "cli" -> new ConsoleMenu(system).run();
            case "demo" -> showDemo(system);
            default -> {
                System.out.println("Usage: demo | cli | server");
                showDemo(system);
            }
        }
    }

    private static void startServer(HospitalSystem system) throws Exception {
        int port = Integer.parseInt(System.getenv().getOrDefault("HMS_PORT", "8080"));
        HospitalHttpServer server = new HospitalHttpServer(system, port);
        server.start();
        System.out.println("Hospital dashboard: http://localhost:" + port);
        System.out.println("Press Ctrl+C to stop.");
    }

    private static void showDemo(HospitalSystem system) {
        System.out.println("Hospital Management System — OOP Course Project");
        System.out.println("------------------------------------------------");
        system.dashboardService.summary().forEach((key, value) -> System.out.printf("%-22s %s%n", key, value));
        System.out.println();
        System.out.println("Run with 'cli' for the interactive console or 'server' for the web dashboard.");
    }
}
