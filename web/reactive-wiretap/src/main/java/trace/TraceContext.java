package trace;

public class TraceContext {

    TraceContext(String traceId, boolean repeatFlow) {
        this.timestamp = System.currentTimeMillis();
        this.traceId = traceId;
        this.repeatFlow = repeatFlow;
    }

    /**
     * 唯一标识一次调用
     */
    private String traceId;

    private volatile boolean repeatFlow;

    /**
     * 调用发生时间
     */
    private long timestamp;

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isRepeatFlow() {
        return repeatFlow;
    }
}
