/*
 * (c) Copyright 2023 40CoderPlus. All rights reserved.
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.fortycoderplus.rsocket.exchange.autoconfigure;

import lombok.Setter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.service.RSocketServiceProxyFactory;
import org.springframework.util.StringUtils;

@Setter
public class RSocketClientFactory implements FactoryBean<Object>, ApplicationContextAware {

    private Class<?> type;

    private String rsocketRequesterName;
    private String proxyFactoryName;

    private ApplicationContext applicationContext;

    @Override
    public Object getObject() {
        try {
            RSocketServiceProxyFactory proxyFactory = StringUtils.hasText(proxyFactoryName)
                    ? applicationContext.getBean(RSocketServiceProxyFactory.class, proxyFactoryName)
                    : applicationContext.getBean(RSocketServiceProxyFactory.class);
            return proxyFactory.createClient(type);
        } catch (BeansException e) {
            RSocketRequester requester = StringUtils.hasText(rsocketRequesterName)
                    ? applicationContext.getBean(RSocketRequester.class, rsocketRequesterName)
                    : applicationContext.getBean(RSocketRequester.class);
            return RSocketServiceProxyFactory.builder(requester).build().createClient(type);
        }
    }

    @Override
    public Class<?> getObjectType() {
        return type;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public void setRsocketRequester(String rsocketRequesterName) {
        this.rsocketRequesterName = rsocketRequesterName;
    }

    public void setProxyFactory(String proxyFactoryName) {
        this.proxyFactoryName = proxyFactoryName;
    }
}
