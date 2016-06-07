/**
 * Copyright (C) 2015 The Gravitee team (http://gravitee.io)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.gravitee.resource.cache;

import io.gravitee.resource.api.AbstractResource;
import io.gravitee.resource.cache.configuration.CacheResourceConfiguration;
import io.gravitee.resource.cache.ehcache.EhCacheDelegate;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.config.CacheConfiguration;

import javax.inject.Inject;

/**
 * @author David BRASSELY (david at gravitee.io)
 * @author GraviteeSource Team
 */
public class CacheResource extends AbstractResource {

    @Inject
    private CacheResourceConfiguration cacheResourceConfiguration;

    private CacheManager cacheManager;
    private Cache cache;

    @Override
    protected void doStart() throws Exception {
        super.doStart();

        cacheManager = new CacheManager();

        CacheConfiguration configuration = new CacheConfiguration();
        configuration.setEternal(false);
        configuration.setTimeToIdleSeconds(cacheResourceConfiguration.getTimeToIdleSeconds());
        configuration.setTimeToLiveSeconds(cacheResourceConfiguration.getTimeToLiveSeconds());
        configuration.setMaxEntriesLocalHeap(cacheResourceConfiguration.getMaxEntriesLocalHeap());
        configuration.setName(cacheResourceConfiguration.getName());

        net.sf.ehcache.Cache ehCache = new net.sf.ehcache.Cache(configuration);
        cache = new EhCacheDelegate(ehCache);
        cacheManager.addCache(ehCache);
    }

    @Override
    protected void doStop() throws Exception {
        super.doStop();

        if (cacheManager != null) {
            cacheManager.shutdown();
        }
    }

    public Cache getCache() {
        return this.cache;
    }
}
