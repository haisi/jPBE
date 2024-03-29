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

import java.util.List;

/**
 * Interface for expression builders, so that the graph builder can be generically extended
 * with more expression builders.
 *
 * @author Hasan Selman Kara
 */
public interface ExpressionBuilder {

    /**
     * @param input  of the initially provided input example
     * @param substr of the initially provided output example
     * @return all expressions of the builder, so that each expression f produces: f(input) = substr
     */
    List<Expression> computeExpressions(String input, String substr);

}
