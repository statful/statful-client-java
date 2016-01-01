package com.mindera.telemetron.client.api;

class DryRunBuilder {
    private final boolean dryRun;

    DryRunBuilder(boolean dryRun) {
        this.dryRun = dryRun;
    }

    boolean isDryRun() {
        return dryRun;
    }
}
