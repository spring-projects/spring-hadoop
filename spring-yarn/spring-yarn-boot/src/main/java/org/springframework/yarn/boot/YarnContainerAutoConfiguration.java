/*
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.yarn.boot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.yarn.YarnSystemConstants;
import org.springframework.yarn.boot.condition.ConditionalOnYarnContainer;
import org.springframework.yarn.boot.support.ContainerLauncherRunner;
import org.springframework.yarn.boot.support.SpringYarnContainerProperties;
import org.springframework.yarn.boot.support.SpringYarnEnvProperties;
import org.springframework.yarn.boot.support.SpringYarnProperties;
import org.springframework.yarn.config.annotation.EnableYarn;
import org.springframework.yarn.config.annotation.EnableYarn.Enable;
import org.springframework.yarn.config.annotation.SpringYarnConfigurerAdapter;
import org.springframework.yarn.config.annotation.builders.YarnConfigConfigure;
import org.springframework.yarn.config.annotation.builders.YarnContainerConfigure;
import org.springframework.yarn.container.YarnContainer;

/**
 * {@link EnableAutoConfiguration Auto-configuration} for Spring Hadoop Yarn container.
 *
 * @author Janne Valkealahti
 *
 */
@Configuration
@ConditionalOnYarnContainer
@ConditionalOnClass(EnableYarn.class)
@ConditionalOnMissingBean(YarnContainer.class)
public class YarnContainerAutoConfiguration {

	@Configuration
	@EnableConfigurationProperties({SpringYarnProperties.class, SpringYarnContainerProperties.class})
	public static class RunnerConfig {

		@Autowired
		private SpringYarnContainerProperties sycp;

		@Bean
		@ConditionalOnMissingBean(ContainerLauncherRunner.class)
		@ConditionalOnBean(YarnContainer.class)
		public ContainerLauncherRunner containerLauncherRunner() {
			ContainerLauncherRunner runner = new ContainerLauncherRunner();
			runner.setWaitLatch(sycp.isWaitLatch());
			return runner;
		}
	}

	@Configuration
	@EnableConfigurationProperties({SpringYarnProperties.class, SpringYarnContainerProperties.class, SpringYarnEnvProperties.class})
	@EnableYarn(enable=Enable.CONTAINER)
	public static class SpringYarnConfig extends SpringYarnConfigurerAdapter {

		@Autowired
		private SpringYarnProperties syp;

		@Autowired
		private SpringYarnContainerProperties sycp;

		@Autowired(required=false)
		@Qualifier(YarnSystemConstants.DEFAULT_ID_CONTAINER_CLASS)
		private Class<? extends YarnContainer> yarnContainerClass;

		@Override
		public void configure(YarnConfigConfigure config) throws Exception {
			config
				.fileSystemUri(syp.getFsUri());
		}

		@Override
		public void configure(YarnContainerConfigure container) throws Exception {
			if (StringUtils.hasText(sycp.getContainerClazz())) {
				container
					.containerClass(sycp.getContainerClazz());
			} else if (yarnContainerClass != null){
				container
					.containerClass(yarnContainerClass);
			}
		}
	}

}
