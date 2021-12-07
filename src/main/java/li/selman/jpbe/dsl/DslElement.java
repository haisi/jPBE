/*
 * (c) Copyright 2021 Hasan Selman Kara. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package li.selman.jpbe.dsl;

/**
 * @author Hasan Selman Kara
 */
public interface DslElement {

    /**
     * The weight can be used to find an optimal program in a set of valid programs.
     * "Optimal" is subjective and can go into different directions.
     * Some examples include:
     * <li>
     *     <ul>Weight by how expensive a DSL element is to execute.</ul>
     *     <ul>
     *         Weight by how likely a DSL element is to be part of a generic full program.
     *         A ConstantPosition generally is less generic.
     *         Thus, the user has to specify their intent more often to finish their wrangling task.
     *     </ul>
     * </li>
     *
     * @return the weight of the DSL element
     */
    int getDslWeight();

}
