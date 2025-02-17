/*
 *
 *  * Copyright (c) 2020-2021, Lykan (jiashuomeng@gmail.com).
 *  * <p>
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  * <p>
 *  *     http://www.apache.org/licenses/LICENSE-2.0
 *  * <p>
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */
package cn.kstry.framework.core.resource.service;

import cn.kstry.framework.core.role.Role;

import javax.annotation.Nonnull;

/**
 * 角色匹配资源
 *
 * @author lykan
 */
public interface RoleMatchResource extends ServiceNodeIdentity {

    /**
     * 根据 角色匹配当前资源是否可以被使用
     *
     * @param role 角色
     * @return 是否匹配成功。true：匹配成功
     */
    boolean match(@Nonnull Role role);
}
