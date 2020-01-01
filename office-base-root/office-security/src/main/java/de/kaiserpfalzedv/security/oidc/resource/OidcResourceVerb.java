/*
 * Copyright Kaiserpfalz EDV-Service, Roland T. Lichti , 2020. All rights reserved.
 *
 *  This file is part of Kaiserpfalz EDV-Service Office.
 *
 *  This is free software: you can redistribute it and/or modify it under the terms of
 *  the GNU Lesser General Public License as published by the Free Software
 *  Foundation, either version 3 of the License.
 *
 *  This file is distributed in the hope that it will be useful, but WITHOUT ANY
 *  WARRANTY; without even the implied warranty of MERCHANTABILITY or
 *  FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 *  License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License along
 *  with this file. If not, see <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */

package de.kaiserpfalzedv.security.oidc.resource;

import javax.interceptor.InterceptorBinding;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * The verb for the resource to protect. Normally the provided annotations {@link OidcResourceVerbView},
 * {@link OidcResourceVerbEdit} and {@link OidcResourceVerbAdmin} should be sufficient. But your milage may vary.
 *
 * @author rlichti
 * @since 2020-01-01T19:04Z
 */
@Inherited
@InterceptorBinding
@Retention(RUNTIME)
@Target({TYPE, METHOD, PARAMETER})
public @interface OidcResourceVerb {
    String getId() default "";

    String getName() default "";

    String getDisplayname() default "";

    String getIconUri() default "";
}
