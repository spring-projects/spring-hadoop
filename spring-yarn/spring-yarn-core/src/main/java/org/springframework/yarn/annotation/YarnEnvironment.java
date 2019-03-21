/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.yarn.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation which indicates that a method parameter should be bound to an environment variable.
 *
 * @author Janne Valkealahti
 *
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface YarnEnvironment {

	/**
	 * The name of the parameter to bind to.
	 *
	 * @return value
	 */
	String value() default "";

	/**
	 * Whether the environment variable is required.
	 * <p>
	 * Default is {@code true}, leading to an exception if the environment variable is
	 * missing. Switch this to {@code false} if you prefer a {@code null} in
	 * case of the environment variable missing.
	 *
	 * @return required
	 */
	boolean required() default true;

}
