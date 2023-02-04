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

package com.fortycoderplus.rsocket.exchange.sample.infrastructure;

import static io.rsocket.metadata.WellKnownMimeType.APPLICATION_CBOR;
import static org.springframework.util.MimeType.valueOf;

import com.fortycoderplus.rsocket.exchange.autoconfigure.EnableRSocketClients;
import java.time.Duration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.codec.cbor.Jackson2CborDecoder;
import org.springframework.http.codec.cbor.Jackson2CborEncoder;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.RSocketStrategies;
import org.springframework.messaging.rsocket.service.RSocketServiceProxyFactory;
import reactor.util.retry.Retry;

@Profile("client")
@Configuration
@AutoConfigureAfter(RSocketExchangeConfigure.class)
@EnableConfigurationProperties(RSocketExchangeClientProperties.class)
@EnableRSocketClients(basePackages = "com.fortycoderplus.rsocket.exchange.sample.client")
public class RSocketExchangeClientConfigure {

    @ConditionalOnMissingBean
    @Bean
    public RSocketStrategies rsocketStrategies() {
        return RSocketStrategies.builder()
                .encoders(encoders -> encoders.add(new Jackson2CborEncoder()))
                .decoders(decoders -> decoders.add(new Jackson2CborDecoder()))
                .build();
    }

    @Bean
    public RSocketRequester rsocketRequester(
            RSocketExchangeClientProperties properties, RSocketStrategies rsocketStrategies) {
        return RSocketRequester.builder()
                .rsocketStrategies(rsocketStrategies)
                .rsocketConnector(connector -> connector.reconnect(Retry.fixedDelay(2, Duration.ofSeconds(2))))
                .dataMimeType(valueOf(APPLICATION_CBOR.getString()))
                .tcp(properties.server().host(), properties.server().port());
    }

    @Bean
    public RSocketServiceProxyFactory proxyFactory(RSocketRequester rsocketRequester) {
        return RSocketServiceProxyFactory.builder(rsocketRequester).build();
    }
}
