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

package de.kaiserpfalzedv.security.cdi;

import javax.enterprise.util.Nonbinding;
import javax.interceptor.InterceptorBinding;
import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Specifies the list of multitenant security roles permitted to access method(s) in an
 * application.  The value of the <code>MultitentantRolesAllowed</code> annotation
 * is a list of security role names.
 * This annotation can be specified on a class or on method(s). Specifying it
 * at a class level means that it applies to all the methods in the class.
 * Specifying it on a method means that it is applicable to that method only.
 * If applied at both the class and methods level, the method value overrides
 * the class value if the two conflict.
 *
 * @author rlichti
 * @since 2020-01-02T09:07Z
 */
@Documented
@Inherited
@InterceptorBinding
@Retention(RUNTIME)
@Target({TYPE, METHOD})
public @interface MultitenantRolesAllowed {
    /**
     * List of roles that are permitted access.
     */
    @Nonbinding String[] value();
}
