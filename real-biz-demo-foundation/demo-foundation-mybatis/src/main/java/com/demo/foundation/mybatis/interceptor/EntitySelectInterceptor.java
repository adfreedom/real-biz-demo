package com.demo.foundation.mybatis.interceptor;

import com.demo.foundation.mybatis.entities.CreationAuditedEntity;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * 实体查询拦截器：SELECT 返回的 CreationAuditedEntity 自动建立监控快照，
 * 使后续 isModified() 能识别变更、差量保存识别为更新。仓储层无需再手动 markLoaded。
 */
@Intercepts({@Signature(
        type = Executor.class,
        method = "query",
        args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}
)})
public class EntitySelectInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object result = invocation.proceed();
        if (result instanceof List) {
            List<?> list = (List<?>) result;
            for (Object entity : list) {
                if (entity instanceof CreationAuditedEntity) {
                    ((CreationAuditedEntity) entity).monitor();
                }
            }
        }
        return result;
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }
}
