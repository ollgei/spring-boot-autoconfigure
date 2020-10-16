package com.github.ollgei.boot.autoconfigure.segment;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.style.ToStringCreator;

/**
 * desc.
 * @author ollgei
 * @since 1.0.0
 */
@ConfigurationProperties(prefix = BoundSegmentProperties.PREFIX)
public class BoundSegmentProperties {

    public static final String PREFIX = "ollgei.segment";

    private String tableName = "t_bound_segment";

    private int maxStep = 1000000;

    private long duration = 15 * 60 * 1000L;

    private double loaderFactor = 0.9;

    private Watch watch = new Watch();

    public Watch getWatch() {
        return this.watch;
    }

    public void setWatch(Watch watch) {
        this.watch = watch;
    }

    public double getLoaderFactor() {
        return loaderFactor;
    }

    public void setLoaderFactor(double loaderFactor) {
        this.loaderFactor = loaderFactor;
    }

    public int getMaxStep() {
        return maxStep;
    }

    public void setMaxStep(int maxStep) {
        this.maxStep = maxStep;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public static class Watch {

        /**
         * The number of seconds to wait (or block) for watch query, defaults to 55. Needs
         * to be less than default ConsulClient (defaults to 60). To increase ConsulClient
         * timeout create a ConsulClient bean with a custom ConsulRawClient with a custom
         * HttpClient.
         */
        private int waitTime = 55;

        /** If the watch is enabled. Defaults to true. */
        private boolean enabled = true;

        /** The value of the fixed delay for the watch in millis. Defaults to 1000. */
        private int delay = 60 * 1000;

        public Watch() {
        }

        public int getWaitTime() {
            return this.waitTime;
        }

        public void setWaitTime(int waitTime) {
            this.waitTime = waitTime;
        }

        public boolean isEnabled() {
            return this.enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public int getDelay() {
            return this.delay;
        }

        public void setDelay(int delay) {
            this.delay = delay;
        }

        @Override
        public String toString() {
            return new ToStringCreator(this).append("waitTime", this.waitTime)
                    .append("enabled", this.enabled).append("delay", this.delay)
                    .toString();
        }

    }
}
