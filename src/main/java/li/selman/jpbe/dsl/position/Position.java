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
package li.selman.jpbe.dsl.position;

import li.selman.jpbe.dsl.DslElement;

/**
 * @author Hasan Selman Kara
 */
public interface Position extends DslElement {

    /**
     * Given an input, dynamically evaluate to an index inside for string manipulation operations.
     *
     * @param s input
     * @return the index
     * @throws NoPositionException in certain implementations
     */
    // TODO(#api-design): make return optional?
    //  As this is a hot path and in most cases a happy case,
    //  wrapping each return in an Optional might be more expensive,
    //  than a rare exception.
    int evalToPosition(String s) throws NoPositionException;

}
