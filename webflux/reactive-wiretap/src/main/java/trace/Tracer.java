package trace;

import com.alibaba.ttl.TransmittableThreadLocal;
import lombok.extern.slf4j.Slf4j;

/**
 * 由于上下文信息是从entrance插件开启{@code Tracer.start()}，必须在entrance插件进行关闭({@code Tracer.end()})，否则会出现上下文错乱问题
 * </p>
 */
@Slf4j
public class Tracer {

    private static ThreadLocal<TraceContext> ttlContext = new TransmittableThreadLocal<TraceContext>();


    /**
     * 开启追踪一次调用，非线程安全
     *
     * @param traceId 调用唯一
     * @return 调用上下文
     */
    public static TraceContext start(String traceId) {
        boolean repeatFlow = false;
        if (!TraceGenerator.isValid(traceId)) {
            traceId = TraceGenerator.generate();
        } else {
            repeatFlow = true;
        }

        TraceContext context = new TraceContext(traceId, repeatFlow);
        if (log.isDebugEnabled()) {
            log.debug("[Tracer] start trace success,traceId={},timestamp={}", context.getTraceId(), context.getTimestamp());
        }
        Tracer.getContextCarrie().set(context);
        return context;
    }

    /**
     * 获取当前上下文
     *
     * @return TraceContext
     */
    public static TraceContext getContext() {
        return Tracer.getContextCarrie().get();
    }

    /**
     * 获取当前上下文的追踪ID，未开启追踪情况下返回空
     *
     * @return 调用追踪ID
     */
    public static String getTraceId() {
        return Tracer.getContextCarrie().get() == null ? null : Tracer.getContextCarrie().get().getTraceId();
    }

    public static boolean isRepeatFlow() {
        return Tracer.getContextCarrie().get() == null ? true : Tracer.getContextCarrie().get().isRepeatFlow();
    }

    /**
     * 结束追踪一次调用，清理上下文
     */
    public static void end() {
        final TraceContext context = getContext();
        if (context != null && log.isDebugEnabled()) {
            log.debug("[Tracer] stop  trace success,traceId={},cost={}ms", context.getTraceId(), System.currentTimeMillis() - context.getTimestamp());
        }
        getContextCarrie().remove();
    }

    /**
     * 根据用户是否开启ttl选择合适的载体
     */
    private static ThreadLocal<TraceContext> getContextCarrie() {
        return ttlContext;
    }
}
